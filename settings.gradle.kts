rootProject.name = "mylie.engine"

dependencyResolutionManagement{
    versionCatalogs{
        create("libs"){
            version("lombok","1.18.36")
            version("logback","1.5.13")
            version("slf4j","2.0.16")

            library("lombok","org.projectlombok","lombok").versionRef("lombok")
            library("logging.api","org.slf4j","slf4j-api").versionRef("slf4j")
            library("logging.runtime","ch.qos.logback","logback-classic").versionRef("logback")
        }
    }
}

include("core")
project(":core").projectDir=file("engine/core")