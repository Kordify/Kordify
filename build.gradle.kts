plugins {
    kotlin("jvm") version "1.9.22"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
}

group = "world.anhgelus.kordify"
version = "0.4.0"
application.mainClass = "${group}.MainKt"

val jdaVersion = "5.0.0-beta.20"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("org.yaml:snakeyaml:2.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true

    // Set this to the version of java you want to use,
    // the minimum required for JDA is 1.8
    sourceCompatibility = "17"
}

tasks.withType<ProcessResources> {
    val props = mapOf(Pair("version", version), Pair("jda-version", jdaVersion))
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("info.yml") {
        this.expand(props)
    }
}
