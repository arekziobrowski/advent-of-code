plugins {
    kotlin("jvm") version "2.2.0"
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

kotlin {
    jvmToolchain(24)
}

dependencies {
    implementation("io.ksmt:ksmt-core:0.5.26")
    implementation("io.ksmt:ksmt-z3:0.5.26")
}