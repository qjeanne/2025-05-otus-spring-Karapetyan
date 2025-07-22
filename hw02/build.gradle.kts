import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.0"
    id("checkstyle")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.otus.hw"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.2.3")

    implementation("com.opencsv:opencsv:5.9") {
        exclude(group = "commons-collections", module = "commons-collections")
    }

    implementation(kotlin("stdlib"))

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.10.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow-jvm:1.2.5")
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