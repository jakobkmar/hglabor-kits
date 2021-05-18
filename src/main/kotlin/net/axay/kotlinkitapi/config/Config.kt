package net.axay.kotlinkitapi.config

import kotlinx.serialization.json.JsonObject
import net.axay.kspigot.config.PluginFile
import net.axay.kspigot.config.kSpigotJsonConfig

object Config {
    val kitsDelegate = kSpigotJsonConfig(PluginFile("json")) { JsonObject(emptyMap()) }
    val kits by kitsDelegate
}
