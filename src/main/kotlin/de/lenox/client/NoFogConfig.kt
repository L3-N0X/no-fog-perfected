package de.lenox.client

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.minecraft.client.Minecraft
import java.io.File

object NoFogConfig {
    private val GSON: Gson = GsonBuilder().setPrettyPrinting().create()
    private var configFile: File? = null

    var overworldFog: Boolean = false
    var netherFog: Boolean = false
    var endFog: Boolean = false
    var waterFog: Boolean = true
    var lavaFog: Boolean = true
    var powderSnowFog: Boolean = true
    var blindnessFog: Boolean = true
    var darknessFog: Boolean = true
    var waterFogOffset: Float = 0f
    var lavaFogOffset: Float = 0f
    var powderSnowFogOffset: Float = 0f
    var overworldFogOffset: Float = 0f
    var netherFogOffset: Float = 0f
    var endFogOffset: Float = 0f

    fun init() {
        val mc = Minecraft.getInstance()
        configFile = File(mc.gameDirectory, "config/no-fog.json")
        load()
    }

    private fun load() {
        val file = configFile ?: return
        if (!file.exists()) {
            save()
            return
        }
        try {
            val json = file.readText()
            val data = GSON.fromJson(json, ConfigData::class.java) ?: return
            overworldFog = data.overworldFog
            netherFog = data.netherFog
            endFog = data.endFog
            waterFog = data.waterFog
            lavaFog = data.lavaFog
            powderSnowFog = data.powderSnowFog
            blindnessFog = data.blindnessFog
            darknessFog = data.darknessFog
            waterFogOffset = data.waterFogOffset
            lavaFogOffset = data.lavaFogOffset
            powderSnowFogOffset = data.powderSnowFogOffset
            overworldFogOffset = data.overworldFogOffset
            netherFogOffset = data.netherFogOffset
            endFogOffset = data.endFogOffset
        } catch (e: Exception) {
            NoFogMod.LOGGER.warn("Failed to load no-fog config, using defaults", e)
        }
    }

    fun save() {
        val file = configFile ?: return
        try {
            file.parentFile?.mkdirs()
            val data = ConfigData(
                overworldFog = overworldFog,
                netherFog = netherFog,
                endFog = endFog,
                waterFog = waterFog,
                lavaFog = lavaFog,
                powderSnowFog = powderSnowFog,
                blindnessFog = blindnessFog,
                darknessFog = darknessFog,
                waterFogOffset = waterFogOffset,
                lavaFogOffset = lavaFogOffset,
                powderSnowFogOffset = powderSnowFogOffset,
                overworldFogOffset = overworldFogOffset,
                netherFogOffset = netherFogOffset,
                endFogOffset = endFogOffset
            )
            file.writeText(GSON.toJson(data))
        } catch (e: Exception) {
            NoFogMod.LOGGER.error("Failed to save no-fog config", e)
        }
    }

    private data class ConfigData(
        val overworldFog: Boolean = false,
        val netherFog: Boolean = false,
        val endFog: Boolean = false,
        val waterFog: Boolean = true,
        val lavaFog: Boolean = true,
        val powderSnowFog: Boolean = true,
        val blindnessFog: Boolean = true,
        val darknessFog: Boolean = true,
        val waterFogOffset: Float = 0f,
        val lavaFogOffset: Float = 0f,
        val powderSnowFogOffset: Float = 0f,
        val overworldFogOffset: Float = 0f,
        val netherFogOffset: Float = 0f,
        val endFogOffset: Float = 0f
    )
}
