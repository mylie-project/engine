plugins {
    alias(libs.plugins.spotless)
    jacoco
    id ("org.sonarqube").version("6.0.0.5145")
}

val engineVersion="0.0.1"
val engineGroup = "mylie-project"

sonar {
    properties {
        property("sonar.projectKey", "mylie-project_engine")
        property("sonar.organization", "mylie-project")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

tasks.withType(JacocoReport::class.java).all {
    dependsOn(tasks.findByName("test"))
    reports {
        xml.required.set(true)
    }
}

tasks.withType<Test>().configureEach {
    finalizedBy(tasks.withType<JacocoReport>())
}


subprojects{

    group=engineGroup
    version=engineVersion
    repositories {
        mavenCentral()
    }

    tasks.withType(JacocoReport::class.java).all {
        reports {
            xml.required.set(true)
        }
    }

    afterEvaluate {
        if (project.hasProperty("java-library")||project.hasProperty("java")) {

            apply(plugin = libs.plugins.spotless.get().pluginId)
            apply(plugin = "jacoco")

            tasks.withType(JacocoReport::class.java).all {
                dependsOn(tasks.findByName("test"))
                reports {
                    xml.required.set(true)
                }
            }

            tasks.withType<Test>().configureEach {
                finalizedBy(tasks.withType<JacocoReport>())
            }

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