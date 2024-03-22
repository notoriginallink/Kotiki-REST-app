plugins {
    id("java")
}

group = "ru.tolstov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lab1-banks:lab1-application"))
    implementation(project(":lab1-banks:lab1-application:infrastructure"))
    implementation(project(":lab1-banks:lab1-application:contracts"))
    implementation(project(":lab1-banks:lab1-application:models"))
    implementation(project(":lab1-banks:lab1-data"))
    implementation(project(":lab1-banks:lab1-presentation"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}