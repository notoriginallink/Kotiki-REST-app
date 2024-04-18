plugins {
    id("java")
}

group = "ru.tolstov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    implementation(":kotiki-app:service:")
}

tasks.test {
    useJUnitPlatform()
}