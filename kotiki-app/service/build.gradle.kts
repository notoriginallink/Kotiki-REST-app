plugins {
    id("java")
}

group = "ru.tolstov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(mapOf("path" to ":kotiki-app:service:models")))
    implementation(project(mapOf("path" to ":kotiki-app:dao")))
}

tasks.test {
    useJUnitPlatform()
}


tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}