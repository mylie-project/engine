import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("java-library")
    id("com.palantir.git-version") version ("3.1.0")
}

dependencies {
    api(libs.logging.api)
    testImplementation(platform("org.junit:junit-bom:5.11.2"))
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
    property("lastTag",details.lastTag)
    property("commitDistance",details.commitDistance)
    property("gitHash",details.gitHash)
    property("gitHashFull",details.gitHashFull) // full 40-character Git commit hash
    property("branchName",details.branchName) // is null if the repository in detached HEAD mode
    property("isCleanTag",details.isCleanTag)
    property("buildTime", SimpleDateFormat("dd-MM-yyyy hh:mm").format(Date()))
}

tasks.classes {
    dependsOn(createVersionProperties)
}