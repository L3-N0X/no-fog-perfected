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
		id("net.neoforged.moddev") version "2.0.141"
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
	}
}
