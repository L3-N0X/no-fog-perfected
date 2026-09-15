pluginManagement {
	repositories {
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		maven {
			name = "NeoForge"
			url = uri("https://maven.neoforged.net/releases/")
		}
		mavenCentral()
		gradlePluginPortal()
		maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
	}

 plugins {
		id("net.neoforged.moddev") version "2.0.147"
	}
}

plugins {
	id("dev.kikugie.stonecutter") version "0.9.1-beta.5"
}

rootProject.name = "no-fog-perfected"

stonecutter {
	create(rootProject) {
		version("1.21.11-fabric", "1.21.11")
		version("1.21.11-neoforge", "1.21.11")
		version("26.1.2-fabric", "26.1.2")
		version("26.1.2-neoforge", "26.1.2")
		version("26.2-fabric", "26.2")
		version("26.2-neoforge", "26.2")
		version("26.3-fabric", "26.3")
		// TODO(26.3): NeoForge has no 26.3 build yet - uncomment once it is published
		//   and fill in the "26.3" branch of `neoforgeVersions` in build.gradle.kts.
		// version("26.3-neoforge", "26.3")
	}
}
