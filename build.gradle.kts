plugins {
    java
    application
    eclipse
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
    implementation ("org.java-websocket:Java-WebSocket:1.5.2")
    implementation ("org.slf4j:slf4j-simple:1.7.32")
    implementation ("com.auth0:java-jwt:3.18.1")

//    testImplementation(platform("org.junit:junit-bom:5.9.1"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

