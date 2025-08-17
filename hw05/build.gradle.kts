import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.0"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.0"
    id("checkstyle")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.otus.hw"
version = "1.0"

object Versions {
    const val springShell = "3.3.2"

    const val kotestJunit = "5.8.1"
    const val kotestArrow = "1.2.5"
}

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("com.h2database:h2")

    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.shell:spring-shell-starter:${Versions.springShell}")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("io.kotest:kotest-runner-junit5:${Versions.kotestJunit}")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow-jvm:${Versions.kotestArrow}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

checkstyle {
    toolVersion = "10.15.0"
    config = resources.text.fromUri("https://raw.githubusercontent.com/OtusTeam/Spring/master/checkstyle.xml")
}

tasks {
    test {
        useJUnitPlatform()
    }

    named<ShadowJar>("shadowJar") {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")
        archiveVersion.set(project.version.toString())
    }
}

kotlin {
    jvmToolchain(17)
}