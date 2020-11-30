/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.facet

import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.module.Module
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.io.FileUtil
import org.jetbrains.kotlin.caching.FlatArgsInfo
import org.jetbrains.kotlin.caching.FlatCompilerArgumentsBucket
import org.jetbrains.kotlin.cli.common.arguments.CommonCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.K2JSCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.K2MetadataCompilerArguments
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.idea.core.isAndroidModule
import org.jetbrains.kotlin.idea.facet.KotlinFacet
import org.jetbrains.kotlin.idea.facet.hasExternalSdkConfiguration
import org.jetbrains.kotlin.idea.facet.joinPluginOptions
import org.jetbrains.kotlin.idea.framework.KotlinSdkType
import org.jetbrains.kotlin.idea.util.getProjectJdkTableSafe
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.platform.js.isJs
import org.jetbrains.kotlin.platform.jvm.isJvm
import org.jetbrains.kotlin.platform.konan.isNative
import kotlin.reflect.KProperty1

val commonUIExposedFields = listOf(
    CommonCompilerArguments::languageVersion,
    CommonCompilerArguments::apiVersion,
    CommonCompilerArguments::suppressWarnings,
    CommonCompilerArguments::coroutinesState
)
val commonUIHiddenFields = listOf(
    CommonCompilerArguments::pluginClasspaths,
    CommonCompilerArguments::pluginOptions,
    CommonCompilerArguments::multiPlatform
)
val commonPrimaryFields = commonUIExposedFields + commonUIHiddenFields

val jvmSpecificUIExposedFields = listOf(
    K2JVMCompilerArguments::jvmTarget,
    K2JVMCompilerArguments::destination,
    K2JVMCompilerArguments::classpath
)
val jvmSpecificUIHiddenFields = listOf(
    K2JVMCompilerArguments::friendPaths
)
val jvmUIExposedFields = commonUIExposedFields + jvmSpecificUIExposedFields
val jvmPrimaryFields = commonPrimaryFields + jvmSpecificUIExposedFields + jvmSpecificUIHiddenFields

val jsSpecificUIExposedFields = listOf(
    K2JSCompilerArguments::sourceMap,
    K2JSCompilerArguments::sourceMapPrefix,
    K2JSCompilerArguments::sourceMapEmbedSources,
    K2JSCompilerArguments::outputPrefix,
    K2JSCompilerArguments::outputPostfix,
    K2JSCompilerArguments::moduleKind
)
val jsUIExposedFields = commonUIExposedFields + jsSpecificUIExposedFields
val jsPrimaryFields = commonPrimaryFields + jsSpecificUIExposedFields
val metadataSpecificUIExposedFields = listOf(
    K2MetadataCompilerArguments::destination,
    K2MetadataCompilerArguments::classpath
)
val metadataUIExposedFields = commonUIExposedFields + metadataSpecificUIExposedFields
val metadataPrimaryFields = commonPrimaryFields + metadataSpecificUIExposedFields

private val TargetPlatform.primaryFields: List<KProperty1<out CommonCompilerArguments, *>>
    get() = when {
        isJvm() -> jvmPrimaryFields
        isJs() -> jsPrimaryFields
        isNative() -> commonPrimaryFields
        else -> metadataPrimaryFields
    }


private val TargetPlatform.ignoredFields: List<KProperty1<out CommonCompilerArguments, *>>
    get() = when {
        isJvm() -> listOf(K2JVMCompilerArguments::noJdk, K2JVMCompilerArguments::jdkHome)
        else -> emptyList()
    }

fun configureFacetByFlatArgsInfo(kotlinFacet: KotlinFacet, flatArgsInfo: FlatArgsInfo, modelsProvider: IdeModifiableModelsProvider?) {
    with(kotlinFacet.configuration.settings) {
        val compilerArgumentsBucket = this.compilerArgumentsBucket ?: return

        val currentBucket = flatArgsInfo.currentCompilerArgumentsBucket
        val defaultBucket = flatArgsInfo.defaultCompilerArgumentsBucket

        defaultBucket.convertPathsToSystemIndependent()
        with(compilerArgumentsBucket) {
            setMultipleArgument(
                CommonCompilerArguments::pluginOptions,
                joinPluginOptions(
                    extractMultipleArgumentValue(CommonCompilerArguments::pluginOptions),
                    currentBucket.extractMultipleArgumentValue(CommonCompilerArguments::pluginOptions)
                )
            )
            convertPathsToSystemIndependent()
        }

        val targetPlatform = this.targetPlatform!!
        if (modelsProvider != null)
            kotlinFacet.module.configureSdkIfPossible(targetPlatform, compilerArgumentsBucket, modelsProvider)

        val primaryFields = targetPlatform.primaryFields
        val ignoredFields = targetPlatform.ignoredFields

        fun exposeAsAdditionalSingleArgument(property: KProperty1<CommonCompilerArguments, String?>) = property !in primaryFields
                && compilerArgumentsBucket.extractSingleArgumentValue(property) != defaultBucket.extractSingleArgumentValue(property)

        fun exposeAsAdditionalMultipleArgument(property: KProperty1<CommonCompilerArguments, Array<String>?>) = property !in primaryFields
                && !compilerArgumentsBucket.extractMultipleArgumentValue(property)
            .contentEquals(defaultBucket.extractMultipleArgumentValue(property))

        fun exposeAsAdditionalFlagArgument(property: KProperty1<CommonCompilerArguments, Boolean>) = property !in primaryFields
                && compilerArgumentsBucket.extractFlagArgumentValue(property) != defaultBucket.extractFlagArgumentValue(property)

    }
}

private fun Module.configureSdkIfPossible(
    targetPlatform: TargetPlatform,
    bucket: FlatCompilerArgumentsBucket,
    modelsProvider: IdeModifiableModelsProvider
) {
    // SDK for Android module is already configured by Android plugin at this point
    if (isAndroidModule(modelsProvider) || hasNonOverriddenExternalSdkConfiguration(targetPlatform, bucket)) return

    val projectSdk = ProjectRootManager.getInstance(project).projectSdk
    KotlinSdkType.setUpIfNeeded()
    val allSdks = getProjectJdkTableSafe().allJdks
    val sdk = if (targetPlatform.isJvm()) {
        val jdkHome = bucket.extractSingleArgumentValue(K2JVMCompilerArguments::jdkHome)
        when {
            jdkHome != null -> allSdks.firstOrNull { it.sdkType is JavaSdk && FileUtil.comparePaths(it.homePath, jdkHome) == 0 }
            projectSdk != null && projectSdk.sdkType is JavaSdk -> projectSdk
            else -> allSdks.firstOrNull { it.sdkType is JavaSdk }
        }
    } else {
        allSdks.firstOrNull { it.sdkType is KotlinSdkType }
            ?: modelsProvider
                .modifiableModuleModel
                .modules
                .asSequence()
                .mapNotNull { modelsProvider.getModifiableRootModel(it).sdk }
                .firstOrNull { it.sdkType is KotlinSdkType }
    }

    val rootModel = modelsProvider.getModifiableRootModel(this)
    if (sdk == null || sdk == projectSdk) {
        rootModel.inheritSdk()
    } else {
        rootModel.sdk = sdk
    }
}

private fun Module.hasNonOverriddenExternalSdkConfiguration(targetPlatform: TargetPlatform, bucket: FlatCompilerArgumentsBucket): Boolean =
    hasExternalSdkConfiguration && (!targetPlatform.isJvm() || bucket.extractSingleArgumentValue(K2JVMCompilerArguments::jdkHome) == null)
