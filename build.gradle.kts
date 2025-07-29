plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "3.1.1"
}

group = "net.justapie"
version = "1.0.0"

repositories {
    mavenCentral()
}

val junitVersion = "5.13.3"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("net.justapie.majutsu")
    mainClass.set("net.justapie.majutsu.Main")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.controlsfx:controlsfx:11.2.2")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0") {
      exclude(group = "org.openjfx")
    }
    implementation("net.synedra:validatorfx:0.6.1") {
      exclude(group = "org.openjfx")
    }
    implementation("org.kordamp.ikonli:ikonli-javafx:12.4.0")
    implementation("org.xerial:sqlite-jdbc:3.50.2.0")
    implementation("io.github.cdimascio:dotenv-java:3.2.0")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("de.mkammerer:argon2-jvm:2.12")
    implementation("net.java.dev.jna:jna:5.17.0")
    implementation("me.fthomys:snowflake-lib:1.0.0")
    implementation("io.github.mkpaz:atlantafx-base:2.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "Majutsu"
    }
}

tasks.build {
    dependsOn(tasks.jpackage)
}

// maven cai lon 