plugins {
    `maven-publish`
    kotlin("jvm") version "1.6.20-RC"
    kotlin("plugin.serialization") version "1.4.0"

}

group = "com.virginiaprivacy"
version = "1.2.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.1.0")
    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    implementation(kotlin("reflect"))
}



tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.kotlinSourcesJar {
    archiveClassifier.set("sources")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            group = "com.virginiaprivacy"
            artifactId = "rvalerts"
            version = "1.2.2"
            from(components["java"])
            artifact(tasks.kotlinSourcesJar)
        }
    }
}
