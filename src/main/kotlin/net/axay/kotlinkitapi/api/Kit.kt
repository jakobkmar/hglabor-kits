@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")

package net.axay.kotlinkitapi.api

import net.axay.kotlinkitapi.builder.KitBuilder
import org.bukkit.event.Listener

open class Kit<P : KitProperties> private constructor(
    /**
     * The unique identifier of this kit.
     */
    val key: String,
    /**
     * The properties of this kit.
     */
    val properties: P,
) {
    class Internal internal constructor() {
        companion object {
            /**
             * Do not use this function, it is only there to expose
             * the private constructor publicly for inlining from a safe
             * place.
             */
            fun <P : KitProperties> createKit(key: String, properties: P) = Kit(key, properties)
        }

        val items = ArrayList<KitItem>()
        val kitPlayerEvents = HashSet<Listener>()
    }

    val internal = Internal()

    init {
        properties.kitname = key
    }

    companion object {
        /**
         * Creates a new lazy kit delegate.
         *
         * Usage:
         * ```kt
         * val MyKit = Kit("MyKit", ::MyKitProperties) { }
         * // or for instant access
         * val MyKit by Kit("MyKit", ::MyKitProperties) { }
         * ```
         *
         * @param key the unique key of this kit
         * @param properties the properties callback, for creating a properties instance
         * for this kit
         * @param builder the [KitBuilder]
         */
        inline operator fun <P : KitProperties> invoke(
            key: Any,
            crossinline properties: () -> P,
            crossinline builder: KitBuilder<P>.() -> Unit,
        ) = lazy {
            Internal.createKit(
                key.toString().replaceFirstChar { it.lowercase() },
                properties.invoke()
            ).apply {
                KitBuilder(this).apply(builder)
            }
        }
    }
}
