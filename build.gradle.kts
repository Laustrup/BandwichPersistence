import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    `java-library`
    `maven-publish`
    id("org.springframework.boot") version "3.4.4"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    api(libs.org.springframework.boot.spring.boot.starter.web)
    api(libs.org.springframework.boot.spring.boot.starter.security)
    api(libs.org.springframework.boot.spring.boot.starter.jdbc)
    api(libs.com.zaxxer.hikaricp)
    api(libs.org.projectlombok.lombok)
    runtimeOnly(libs.com.mysql.mysql.connector.j)
    runtimeOnly(libs.com.h2database.h2)
    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
    compileOnly(libs.org.springframework.boot.spring.boot.starter.tomcat)
    compileOnly(libs.org.projectlombok.lombok)
    annotationProcessor(libs.org.projectlombok.lombok)
    testCompileOnly(libs.org.projectlombok.lombok)
    testAnnotationProcessor(libs.org.projectlombok.lombok)
}

val groupInstance = "laustrup"

group = groupInstance
version = "0.0.1-SNAPSHOT"
description = "BandwichPersistence"
java.sourceCompatibility = JavaVersion.VERSION_21

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    maxHeapSize = "1G"

    testLogging {
        events("passed")
    }
}

tasks.named<JavaExec>("bootRun") {
    standardInput = System.`in`
}

tasks.named<BootRun>("bootRun") {
    mainClass.set(StringBuilder()
        .append(groupInstance).append('.')
        .append("bandwichpersistence").append('.')
        .append("BandwichPersistenceApplication")
        .toString()
    )
}