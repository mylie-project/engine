plugins {
    id("java")
    id("application")
}

var mainClassName="mylie.demos.desktop.DesktopDemoLauncher"

dependencies {
    implementation(project(":core"))
    implementation(project(":platform.desktop"))
    implementation(project(":examples.demos"))
    implementation(libs.logging.runtime)
    runtimeOnly(libs.logging.runtime)
}

tasks.register<JavaExec>("runDemos") {
    group = "application"
    classpath=sourceSets.main.get().runtimeClasspath
    mainClass=mainClassName
}

tasks.register<Exec>("runDemosRenderDoc") {
    group = "application"
    val javaExecTask = tasks.named<JavaExec>("run").get()
    val javaHome = javaExecTask.javaLauncher.get().metadata.installationPath.asFile.absolutePath

    commandLine = listOf(
        "C://Program Files/RenderDoc/renderdoccmd",
        "capture",
        "--wait-for-exit",
        "--working-dir", ".",
        "$javaHome/bin/java",
        "--enable-preview",
        "-classpath", sourceSets.main.get().runtimeClasspath.asPath,
        mainClassName,

        )
}

tasks.register<Exec>("runDemosNsight") {
    group = "application"
    val javaExecTask = tasks.named<JavaExec>("run").get()
    val javaHome = javaExecTask.javaLauncher.get().metadata.installationPath.asFile.absolutePath

    commandLine = listOf(
        "C:\\Program Files\\NVIDIA Corporation\\Nsight Graphics 2024.3.0\\host\\windows-desktop-nomad-x64\\ngfx.exe",
        "--activity=Frame Debugger",
        "--wait-hotkey",
        "--dir", ".",
        "--exe",
        "$javaHome/bin/java",
        "--args",
        "-classpath "+sourceSets.main.get().runtimeClasspath.asPath+" "+mainClassName,
    )
}
