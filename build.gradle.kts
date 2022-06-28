import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    kotlin("plugin.allopen") version "1.6.21"
    kotlin("kapt") version "1.3.61"
}

group = "team.waggly"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8
val qeurydslVersion = "5.0.0" // <= 추가 설정


repositories {
    mavenCentral()
}

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation ("org.springframework.boot:spring-boot-starter-validation")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    // Jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("commons-codec:commons-codec:1.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")
    implementation("com.auth0:java-jwt:3.13.0")

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:$qeurydslVersion")
    kapt("com.querydsl:querydsl-apt:$qeurydslVersion:jpa")
    kapt("org.springframework.boot:spring-boot-configuration-processor:2.6.7")

    // S3
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.0.1.RELEASE")

    //Email send
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Tika
    implementation("org.apache.tika:tika-parsers:1.18")
}



tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
