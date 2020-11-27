import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

allprojects {
    tasks.withType<KotlinCompile<*>> {
        kotlinOptions {
            allWarningsAsErrors = true
        }
    }
}
