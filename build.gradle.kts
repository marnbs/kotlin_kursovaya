import org.gradle.api.tasks.JavaExec
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(kotlin("stdlib"))
}

application {
    mainClass.set("org.example.MainKt")
    mainClassName = "org.example.MainKt"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    if (System.getProperty("os.name").startsWith("Windows", ignoreCase = true)) {
        jvmArgs("-Dfile.encoding=IBM866")
    }
}
