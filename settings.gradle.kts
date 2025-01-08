rootProject.name = "mylie.engine"

dependencyResolutionManagement{
    versionCatalogs{
        create("libs"){
            version("lombok","1.18.34")
            version("logback","1.5.13")
            version("slf4j","2.0.16")
            version("joml","1.10.7")
            version("lwjgl3","3.3.6")
            version("imgui","1.87.7")

            library("lombok","org.projectlombok","lombok").versionRef("lombok")
            library("logging.api","org.slf4j","slf4j-api").versionRef("slf4j")
            library("logging.runtime","ch.qos.logback","logback-classic").versionRef("logback")
            library("joml","org.joml","joml").versionRef("joml")
            library("lwjgl3.imgui","io.github.spair","imgui-java-app").versionRef("imgui")
            library("lwjgl3.core","org.lwjgl","lwjgl").versionRef("lwjgl3")
            library("lwjgl3.glfw","org.lwjgl","lwjgl-glfw").versionRef("lwjgl3")
            library("lwjgl3.opengl","org.lwjgl","lwjgl-opengl").versionRef("lwjgl3")
            library("lwjgl3.stb","org.lwjgl","lwjgl-stb").versionRef("lwjgl3")
            library("lwjgl3.assimp","org.lwjgl","lwjgl-assimp").versionRef("lwjgl3")
            library("lwjgl3.assimp.natives","org.lwjgl","lwjgl-assimp").versionRef("lwjgl3")
            library("lwjgl3.core.natives","org.lwjgl","lwjgl").versionRef("lwjgl3")
            library("lwjgl3.glfw.natives","org.lwjgl","lwjgl-glfw").versionRef("lwjgl3")
            library("lwjgl3.opengl.natives","org.lwjgl","lwjgl-opengl").versionRef("lwjgl3")
            library("lwjgl3.stb.natives","org.lwjgl","lwjgl-stb").versionRef("lwjgl3")

        }
    }
}

include("core")
project(":core").projectDir=file("engine/core")

include("platform.desktop")
project(":platform.desktop").projectDir=file("engine/platforms/desktop")

include("examples.demos")
project(":examples.demos").projectDir=file("examples/demos")

include("examples.desktop")
project(":examples.desktop").projectDir=file("examples/desktop")
