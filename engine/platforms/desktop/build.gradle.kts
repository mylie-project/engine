plugins {
    id("java-library")
}

dependencies {
    api(project(":core"))
    runtimeOnly(libs.logging.runtime)
}