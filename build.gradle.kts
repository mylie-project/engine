plugins {
    id("com.diffplug.spotless") version "7.0.0.BETA4"
    id("com.google.osdetector") version "1.7.3"
}

val engineVersion="0.0.1"
val engineGroup = "mylie-project"

repositories {
    mavenCentral()
}

subprojects{
    apply(plugin = "com.google.osdetector")
    group=engineGroup
    version=engineVersion
    repositories {
        mavenCentral()
    }

    afterEvaluate {
        if (project.hasProperty("java-library")||project.hasProperty("java")) {
            apply(plugin = "com.diffplug.spotless")
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
                    eclipse()
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