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
    implementation(project(":kotiki-app:service:"))
    implementation(project(":kotiki-app:service:models"))
}

allprojects {
    apply(plugin = "java")

    dependencies{
        // lombok
        compileOnly("org.projectlombok:lombok:1.18.32")
        annotationProcessor("org.projectlombok:lombok:1.18.32")
        testCompileOnly("org.projectlombok:lombok:1.18.32")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

        // hibernate
        implementation(platform("org.hibernate.orm:hibernate-platform:6.4.4.Final"))
        implementation("org.hibernate.orm:hibernate-core")
        implementation("jakarta.transaction:jakarta.transaction-api")

        // https://mvnrepository.com/artifact/org.postgresql/postgresql
        implementation("org.postgresql:postgresql:42.7.3")

        // JUnit
        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")

        // https://mvnrepository.com/artifact/org.mockito/mockito-core
        testImplementation("org.mockito:mockito-core:5.11.0")
    }
}

tasks.test {
    useJUnitPlatform()
}