plugins {
    id("java-library")
}

dependencies {
    implementation("io.github.classgraph:classgraph:4.8.179")
    implementation(project(":core"))
}