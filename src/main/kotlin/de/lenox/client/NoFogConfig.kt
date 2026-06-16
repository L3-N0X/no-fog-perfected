package de.lenox.client

object NoFogConfig {
    var overworldFog: Boolean = false
    var netherFog: Boolean = false
    var endFog: Boolean = false
    var waterFog: Boolean = true
    var lavaFog: Boolean = true
    var powderSnowFog: Boolean = true
    var blindnessFog: Boolean = true
    var darknessFog: Boolean = true

    fun save() {
        // TODO: Implement config persistence
    }
}
