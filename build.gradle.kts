/*
 * Copyright (C) 2025  Tete
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import org.gradle.kotlin.dsl.kotlin
import kotlin.RuntimeException;

plugins {
    `java-library`
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish` //apply false
}

version = project.property("version") as String

group = project.property("maven_group") as String

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("com.google.code.gson:gson:2.11.0")
}

java {
    val targetJavaVersion = 21
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}

gradlePlugin {
    plugins {
        create("bobbuilder") {
            id = "com.trs.bobbuilder"
            implementationClass = "com.trs.bobbuilder.BobBuilderPlugin"
        }
    }
}




publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${System.getenv("GITHUB_USERNAME")}/${project.property("archives_base_name") as String}")
            credentials {
                username = System.getenv("GITHUB_USERNAME") ?: throw RuntimeException("Github username environment variable is null")
                password = System.getenv("GITHUB_MAVEN_TOKEN") ?: throw RuntimeException("Github Maven token environment variable is null")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            groupId = "com.trs"
            artifactId = project.property("archives_base_name") as String
            version = project.version as String

            from(components["java"])

            pom {
                name = "BobBuilder"
                description = "A plugin I developed to help with jar marking and other utility's"
                url = "http://www.github.com/tetex7/bobbuilder"
            }
        }
    }
}

