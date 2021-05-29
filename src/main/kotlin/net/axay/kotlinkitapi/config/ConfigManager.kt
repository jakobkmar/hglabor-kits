package net.axay.kotlinkitapi.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import net.axay.kspigot.config.PluginFile

object ConfigManager {
    val kits: Config by lazy {
        val configString = PluginFile("kits.conf").readText()
        ConfigFactory.parseString(configString)
    }
}
