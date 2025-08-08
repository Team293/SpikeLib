import groovy.json.JsonSlurper
import java.io.File


plugins {
    id("java")
    id("edu.wpi.first.GradleRIO") version "2025.3.2"
    id("maven-publish")
}

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
    withJavadocJar()
}

group = "com.spike293"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://frcmaven.wpi.edu/artifactory/release/")
    maven("https://3015rangerrobotics.github.io/pathplannerlib/repo")
    maven("https://maven.ctr-electronics.com/release/")
    maven("https://frcmaven.wpi.edu/artifactory/littletonrobotics-mvn-release/")
}

dependencies {
    compileOnly("edu.wpi.first.wpilibj:wpilibj-java:+")
    compileOnly("edu.wpi.first.wpiutil:wpiutil-java:+")
    compileOnly("edu.wpi.first.wpimath:wpimath-java:+")
    compileOnly("edu.wpi.first.wpiunits:wpiunits-java:+")
    compileOnly("edu.wpi.first.ntcore:ntcore-java:+")
    compileOnly("edu.wpi.first.cscore:cscore-java:+")
    compileOnly("edu.wpi.first.wpilibNewCommands:wpilibNewCommands-java:+")
    compileOnly("edu.wpi.first.cameraserver:cameraserver-java:+")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0-rc1")

    compileOnly("com.ctre.phoenix6:wpiapi-java:+")
    compileOnly("com.pathplanner.lib:PathplannerLib-java:2025.2.2")
    compileOnly("org.littletonrobotics.akit:akit-java:4.1.2")
    implementation("us.hebi.quickbuf:quickbuf-runtime:1.4")

    val akitJson = JsonSlurper().parseText(
        File(projectDir, "vendordeps/AdvantageKit.json").readText()
    ) as Map<*, *>

    annotationProcessor("org.littletonrobotics.akit:akit-autolog:${akitJson["version"]}")
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileJava {
    options.compilerArgs.add("-XDstringConcat=inline")
}

tasks.jar {
    archiveBaseName.set("spike-lib")

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<JavaExec>("replayWatch") {
    mainClass.set("org.littletonrobotics.junction.ReplayWatch")
    classpath = sourceSets["main"].runtimeClasspath
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.spike293"
            artifactId = "spike-lib"
            version = "1.0.0"
        }
    }
    repositories {
        mavenLocal()
    }
}