plugins {
    application
    kotlin("jvm").version("2.0.0")
    kotlin("plugin.serialization").version("2.0.0")
}

application {
    mainClass.set("link.kotlin.scripts.Application")
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.9.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.0")

    implementation("ch.qos.logback:logback-classic:1.5.8")

    implementation("com.rometools:rome:2.1.0")
    implementation("com.github.dfabulich:sitemapgen4j:1.1.2")
    implementation("org.jsoup:jsoup:1.18.1")

    implementation(kotlin("scripting-common"))
    implementation(kotlin("scripting-jvm"))
    implementation(kotlin("scripting-jvm-host"))

    implementation("org.commonmark:commonmark:0.23.0")
    implementation("org.commonmark:commonmark-ext-gfm-tables:0.23.0")

    implementation("io.ktor:ktor-client-apache:2.3.12")
    implementation("io.ktor:ktor-client-jackson:2.3.12")


    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3") // Use 'implementation' instead of 'runtimeOnly'y
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.2")
    // https://mvnrepository.com/artifact/io.mockk/mockk
    testImplementation("io.mockk:mockk:1.13.13")
    // https://mvnrepository.com/artifact/io.strikt/strikt-core
    implementation("io.strikt:strikt-core:0.35.1")
    // https://mvnrepository.com/artifact/io.kotest/kotest-core
    implementation("io.kotest:kotest-core:4.2.0.RC2")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-test
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    implementation("org.jetbrains.kotlin:kotlin-maven-plugin:2.1.0-Beta2")
    // https://mvnrepository.com/artifact/io.kotest/kotest-assertions-core-jvm
    testImplementation("io.kotest:kotest-assertions-core-jvm:6.0.0.M1")
// https://mvnrepository.com/artifact/io.kotest/kotest-property
    runtimeOnly("io.kotest:kotest-property:6.0.0.M1")
// https://mvnrepository.com/artifact/io.kotest.extensions/kotest-property-arbs
    implementation("io.kotest.extensions:kotest-property-arbs:2.1.2")
    // https://mvnrepository.com/artifact/org.mockito/mockito-core
    testImplementation("org.mockito:mockito-core:5.14.2")




}
