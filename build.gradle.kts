plugins {
    alias(libs.plugins.spotless)
}

val engineVersion="0.0.1"
val engineGroup = "mylie-project"

subprojects{

    group=engineGroup
    version=engineVersion
    repositories {
        mavenCentral()
    }

    afterEvaluate {
        if (project.hasProperty("java-library")||project.hasProperty("java")) {

            apply(plugin = libs.plugins.spotless.get().pluginId)
            dependencies {
                val implementation by configurations
                val compileOnly by configurations
                val annotationProcessor by configurations
                compileOnly(libs.lombok)
                annotationProcessor(libs.lombok)
            }

            spotless{
                java{
                    removeUnusedImports()
                    importOrder()
                    eclipse().configFile(rootProject.file("javaFormat.xml"))
                    formatAnnotations()
                    trimTrailingWhitespace()
                    endWithNewline()
                }
            }

        }
        tasks.withType<JavaCompile>().configureEach {
            options.compilerArgs.add("--enable-preview")
        }
        tasks.withType<JavaExec>().configureEach {
            jvmArgs("--enable-preview")
        }
    }
}