plugins {
    id("java")
}

group = "ru.tolstov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}

tasks.test {
    useJUnitPlatform()
}