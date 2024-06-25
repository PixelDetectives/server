plugins {
    java
    application
}
application {
    mainClass = "site.pixeldetective.server.Main"
}

group = "site.pixeldetective.server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20210307")
    implementation("mysql:mysql-connector-java:8.0.29")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

