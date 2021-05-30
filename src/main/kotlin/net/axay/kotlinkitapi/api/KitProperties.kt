@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.axay.kotlinkitapi.api

import com.typesafe.config.ConfigException
import io.github.config4k.extract
import net.axay.kotlinkitapi.config.ConfigManager
import net.axay.kspigot.extensions.bukkit.warn
import net.axay.kspigot.extensions.server
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.reflect.KProperty

abstract class KitProperties {
    lateinit var kitname: String
        internal set

    abstract class KitProperty<T> {
        abstract val defaultValue: T

        protected var value: T? = null

        abstract operator fun getValue(thisRef: Any, property: KProperty<*>): T

        operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
            value = newValue
        }
    }

    inline fun <reified T : Any> any(default: T) = object : KitProperty<T>() {
        override val defaultValue = default

        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            val currentValue = value
            if (currentValue != null) return currentValue

            val newValue = try {
                try {
                    ConfigManager.kits.getConfig(kitname).extract<T>(property.name)
                } catch (missing: ConfigException.Missing) {
                    defaultValue
                }
            } catch (exc: Exception) {
                server.consoleSender.warn("The property '${property.name}' of kit '$kitname' is given, but it could not be parsed! Using default value as a fallback.")
                null
            } ?: defaultValue
            return newValue.also { value = it }
        }
    }


    fun boolean(default: Boolean) = any(default)
    fun int(default: Int) = any(default)
    fun long(default: Long) = any(default)
    fun float(default: Float) = any(default)
    fun double(default: Double) = any(default)
    fun string(default: String) = any(default)
    fun sound(default: Sound) = any(default)
    fun particle(default: Particle) = any(default)
}
