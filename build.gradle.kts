import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.jpa") version "1.5.31"
    kotlin("plugin.allopen") version "1.5.31"
    id("org.openjfx.javafxplugin") version "0.0.10"
}

group = "me.lanik"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("javax.activation:activation:1.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.0")
    implementation ("no.tornado:tornadofx:1.7.20")
    implementation ("org.hibernate:hibernate-core:5.3.7.Final")
    implementation ("javax.xml.bind:jaxb-api:2.3.0")
    runtimeOnly("org.hsqldb:hsqldb:2.6.1")
}

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}