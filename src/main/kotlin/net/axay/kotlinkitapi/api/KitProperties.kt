@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.axay.kotlinkitapi.api

import io.github.config4k.extract
import net.axay.kotlinkitapi.config.ConfigManager
import net.axay.kspigot.extensions.bukkit.warn
import net.axay.kspigot.extensions.server
import kotlin.reflect.KProperty

abstract class KitProperties(val kitname: String) {
    abstract class KitProperty<T> {
        abstract val defaultValue: T

        protected var kitName: String? = null
        protected var propertyName: String? = null

        protected var value: T? = null

        abstract operator fun getValue(thisRef: Any, property: KProperty<*>): T

        operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
            value = newValue
        }
    }

    inline fun <reified T : Any> any(default: T) = object : KitProperty<T>() {
        override val defaultValue = default

        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            if (kitName == null)
                kitName = (thisRef as? KitProperties)?.kitname
                    ?: error("Using a kit property outside of a KitProperties class")
            if (propertyName == null)
                propertyName = property.name

            val currentValue = value
            if (currentValue != null) return currentValue

            val newValue = try {
                ConfigManager.kits.getConfig(kitName!!).extract<T>(propertyName!!)
            } catch (exc: Exception) {
                server.consoleSender.warn("The property '$propertyName' of kit '$kitName' is given, but it could not be parsed! Using default value as a fallback.")
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
}
