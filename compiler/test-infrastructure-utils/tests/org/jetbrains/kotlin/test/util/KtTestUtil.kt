/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.test.util

//object KtTestUtil {
//    @JvmStatic
//    @Throws(IOException::class)
//    fun tmpDir(name: String?): File {
//        return normalizeFile(FileUtil.createTempDirectory(name!!, "", false))
//    }
//
//    @JvmStatic
//    @Throws(IOException::class)
//    fun tmpDirForTest(testClassName: String, testName: String): File {
//        return normalizeFile(FileUtil.createTempDirectory(testClassName, testName, false))
//    }
//
//    @Throws(IOException::class)
//    fun tmpDirForReusableFolder(name: String?): File {
//        return normalizeFile(
//            FileUtil.createTempDirectory(
//                File(System.getProperty("java.io.tmpdir")),
//                name!!, "", true
//            )
//        )
//    }
//
//    @Throws(IOException::class)
//    fun normalizeFile(file: File): File {
//        // Get canonical file to be sure that it's the same as inside the compiler,
//        // for example, on Windows, if a canonical path contains any space from FileUtil.createTempDirectory we will get
//        // a File with short names (8.3) in its path and it will break some normalization passes in tests.
//        return file.canonicalFile
//    }
//}
