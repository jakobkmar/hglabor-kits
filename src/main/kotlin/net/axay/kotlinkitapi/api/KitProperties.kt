package net.axay.kotlinkitapi.api

import kotlinx.serialization.json.*
import net.axay.kotlinkitapi.config.Config
import kotlin.reflect.KProperty

abstract class KitProperties(val kit: Kit) {
    abstract class KitProperty<T> {
        abstract val defaultValue: T

        private var kitName: String? = null
        private var propertyName: String? = null

        private var value: T? = null

        operator fun getValue(thisRef: Any, property: KProperty<*>): T {
            if (kitName == null) kitName = (thisRef as KitProperties).kit.name
            if (propertyName == null) propertyName = property.name

            val currentValue = value

            return if (currentValue == null) {
                val jsonElement = Config.kits[kitName!!]?.jsonObject?.get(propertyName!!)
                val newValue = if (jsonElement != null) {
                    parseJsonElement(jsonElement) ?: kotlin.run {
                        println("Could not parse given property $propertyName of kit $kitName")
                        defaultValue
                    }
                } else defaultValue
                return newValue.also { value = it }
            } else currentValue
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
            value = newValue
        }

        protected abstract fun parseJsonElement(jsonElement: JsonElement): T?
    }

    class BooleanProperty(override val defaultValue: Boolean) : KitProperty<Boolean>() {
        override fun parseJsonElement(jsonElement: JsonElement) = jsonElement.jsonPrimitive.booleanOrNull
    }

    class IntProperty(override val defaultValue: Int) : KitProperty<Int>() {
        override fun parseJsonElement(jsonElement: JsonElement) = jsonElement.jsonPrimitive.intOrNull
    }

    class LongProperty(override val defaultValue: Long) : KitProperty<Long>() {
        override fun parseJsonElement(jsonElement: JsonElement) = jsonElement.jsonPrimitive.longOrNull
    }

    class FloatProperty(override val defaultValue: Float) : KitProperty<Float>() {
        override fun parseJsonElement(jsonElement: JsonElement) = jsonElement.jsonPrimitive.floatOrNull
    }

    class DoubleProperty(override val defaultValue: Double) : KitProperty<Double>() {
        override fun parseJsonElement(jsonElement: JsonElement) = jsonElement.jsonPrimitive.doubleOrNull
    }

    class StringProperty(override val defaultValue: String) : KitProperty<String>() {
        override fun parseJsonElement(jsonElement: JsonElement) = jsonElement.jsonPrimitive.let {
            if (it.isString) it.toString() else null
        }
    }
}
