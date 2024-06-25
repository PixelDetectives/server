plugins {
    java
}

group = "site.pixeldetective"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JSON-Java 라이브러리 추가
    implementation("org.json:json:20210307")
    // MySQL Connector 라이브러리 추가
    implementation("mysql:mysql-connector-java:8.0.29")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}