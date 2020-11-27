import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

val tasksWithWarnings: List<String> by rootProject.extra

allprojects {
    tasks.withType<KotlinCompile<*>> {
        if (path !in tasksWithWarnings) {
            kotlinOptions {
                allWarningsAsErrors = true
            }
        }
    }
}
