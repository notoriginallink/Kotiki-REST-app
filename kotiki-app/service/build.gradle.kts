plugins {
    id("java")
}

group = "ru.tolstov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":kotiki-app:dao:"))
    implementation(project(mapOf("path" to ":kotiki-app:service:models")))

    // https://mvnrepository.com/artifact/com.h2database/h2
    testImplementation("com.h2database:h2:2.2.224")

    // https://mvnrepository.com/artifact/org.dbunit/dbunit
    testImplementation("org.dbunit:dbunit:2.7.3")
}

tasks.test {
    useJUnitPlatform()
}