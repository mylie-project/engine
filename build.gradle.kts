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

val tmp = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        arrayOf("Linux", "SunOS", "Unit").any { name.startsWith(it) } ->
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
            else if (arch.startsWith("ppc"))
                "natives-linux-ppc64le"
            else if (arch.startsWith("riscv"))
                "natives-linux-riscv64"
            else
                "natives-linux"
        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) }     ->
            "natives-macos"
        arrayOf("Windows").any { name.startsWith(it) }                ->
            "natives-windows"
        else                                                                            ->
            throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}
val osNameArch by extra(tmp)