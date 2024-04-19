plugins {
    id("java")
}

group = "ru.tolstov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(mapOf("path" to ":kotiki-app:service")))
    implementation(project(mapOf("path" to ":kotiki-app:service:models")))
}

tasks.test {
    useJUnitPlatform()
}