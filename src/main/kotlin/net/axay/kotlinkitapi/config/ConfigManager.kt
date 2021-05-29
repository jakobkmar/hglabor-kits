package net.axay.kotlinkitapi.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import net.axay.kspigot.config.PluginFile
import net.axay.kspigot.languageextensions.kotlinextensions.createIfNotExists

object ConfigManager {
    val kits: Config by lazy {
        val configString = PluginFile("kits.conf")
            .apply { createIfNotExists() }
            .readText()
        ConfigFactory.parseString(configString)
    }
}
