import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("java-library")
    alias(libs.plugins.palantirGitVersion)
}

dependencies {
    api(libs.logging.api)
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly(libs.logging.runtime)
    runtimeOnly(libs.logging.runtime)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    maxHeapSize = "1G"
    maxParallelForks=1
}

val createVersionProperties by tasks.registering(WriteProperties::class) {
    val filePath = sourceSets.main.map {
        it.output.resourcesDir!!.resolve("mylie/engine/version.properties")
    }
    destinationFile = filePath

    property("version", project.version.toString())
    val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by project.extra
    val details = versionDetails()
    property("lastTag",details.lastTag ?: "unknown")
    property("commitDistance", details.commitDistance)
    property("gitHash",details.gitHash?:"unknown")
    property("gitHashFull",details.gitHashFull?:"unknown") // full 40-character Git commit hash
    property("branchName",details.branchName ?: "local") // is null if the repository in detached HEAD mode
    property("isCleanTag", details.isCleanTag)
    property("buildTime", SimpleDateFormat("dd-MM-yyyy hh:mm").format(Date()))
}

tasks.classes {
    dependsOn(createVersionProperties)
}