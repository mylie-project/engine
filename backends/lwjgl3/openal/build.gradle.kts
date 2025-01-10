plugins {
    id("java-library")
}

val osNameArch : String  by rootProject.extra

dependencies {
    api(project(":core"))
    api(libs.lwjgl3.openal)
    api(libs.lwjgl3.core)
    runtimeOnly(variantOf(libs.lwjgl3.core.natives) { classifier(osNameArch) })
    runtimeOnly(variantOf(libs.lwjgl3.openal.natives) { classifier(osNameArch) })
}