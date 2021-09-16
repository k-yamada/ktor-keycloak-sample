import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
    application
}

group = "github.kyamada"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.21")
    implementation("io.ktor:ktor-server-netty:1.6.3")
    implementation("io.ktor:ktor-html-builder:1.6.3")
    implementation("io.ktor:ktor-auth:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("ServerKt")
}
