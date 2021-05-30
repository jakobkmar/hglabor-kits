@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")

package net.axay.kotlinkitapi.api

import net.axay.kotlinkitapi.builder.KitBuilder
import net.axay.kspigot.extensions.pluginKey
import net.axay.kspigot.items.meta
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.persistence.PersistentDataType.INTEGER
import org.bukkit.persistence.PersistentDataType.STRING

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
    inner class Internal internal constructor() {
        val items = HashMap<Int, KitItem>()
        val kitPlayerEvents = HashSet<Listener>()

        fun givePlayer(player: Player) {
            for ((id: Int, item: KitItem) in items) {
                val kitItemStack = item.stack.apply {
                    meta {
                        persistentDataContainer[kitItemKitKey, STRING] = key
                        persistentDataContainer[kitItemIdKey, INTEGER] = id
                    }
                }
                if (!player.inventory.contains(kitItemStack))
                    player.inventory.addItem(kitItemStack)
            }
        }
    }

    /**
     * Gives access to internal values which have to be
     * public to make inlining possible.
     */
    val internal = this.Internal()

    init {
        properties.kitname = key
    }

    companion object {
        val kitItemKitKey = pluginKey("kitItemKit")
        val kitItemIdKey = pluginKey("kitItemId")

        /**
         * Do not use this function, it is only there to expose
         * the private constructor publicly for inlining from a safe
         * place.
         */
        fun <P : KitProperties> createRawKit(key: String, properties: P) =
            Kit(key, properties)

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
            createRawKit(
                key.toString().replaceFirstChar { it.lowercase() },
                properties.invoke()
            ).apply {
                KitBuilder(this).apply(builder)
            }
        }
    }
}
