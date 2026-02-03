import io.papermc.paperweight.userdev.ReobfArtifactConfiguration

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta12"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
}

group = "eg.mqzen"

tasks.compileJava {
    options.encoding = "UTF-8"
}

paperweight {
    reobfArtifactConfiguration.set(ReobfArtifactConfiguration.MOJANG_PRODUCTION)
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "central-snapshots"
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    }

    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }

    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        name = "alessiodpRepoSnapshots"
        url = uri("https://repo.alessiodp.com/snapshots")
    }
}

dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    implementation(project(":cardinal-api"))

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
    compileOnly("me.clip:placeholderapi:2.11.6")

    compileOnly("org.jetbrains:annotations:26.0.2")

    compileOnly("studio.mevera:imperat-core:2.4.2")
    compileOnly("studio.mevera:imperat-bukkit:2.4.2")

    compileOnly("org.mongodb:mongodb-driver-sync:5.3.1")
    compileOnly("com.zaxxer:HikariCP:6.3.0")

    compileOnly("dev.dejvokep:boosted-yaml:1.3.6")
    compileOnly("dev.dejvokep:boosted-yaml-spigot:1.5")
    compileOnly("tools.jackson.core:jackson-databind:3.0.0")

    compileOnly("com.github.Mqzn:Lotus:1.6.0")

    //TODO add dependency runtime using libby for other dependencies
    implementation("com.alessiodp.libby:libby-paper:2.0.0-SNAPSHOT")
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.2")
}


val targetJavaVersion = 21
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"

    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release.set(targetJavaVersion)
    }
}
tasks.shadowJar {
    exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
    //minimize();
    archiveClassifier.set("")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    destinationDirectory = project.rootProject.properties["outputDir"]?.let { file(it) }?.let { file(it) }
    archiveFileName.set("Cardinal-${project.version}.jar")

    relocate("com.github.benmanes", "eg.mqzen.cardinal.libs.caffeine")
    relocate("com.alessiodp.libby", "eg.mqzen.cardinal.libs.com.alessiodp.libby");
    relocate("studio.mevera.imperat", "eg.mqzen.cardinal.libs.commands")
    relocate("dev.dejvokep.boostedyaml", "eg.mqzen.cardinal.libs.config")
    relocate("org.objectweb.asm", "eg.mqzen.cardinal.libs.asm")
    relocate("com.mongodb", "eg.mqzen.cardinal.libs.mongo")
    relocate("org.bson", "eg.mqzen.cardinal.libs.bson")
    relocate("com.zaxxer.hikari", "eg.mqzen.cardinal.libs.hikari")
    //relocate for lotus
    relocate("io.github.mqzen", "eg.mqzen.cardinal.libs.menus")
}

tasks.processResources {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}