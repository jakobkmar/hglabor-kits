@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")

package net.axay.kotlinkitapi.api

import net.axay.kotlinkitapi.builder.KitBuilder
import org.bukkit.event.Listener

open class Kit<P : KitProperties> internal constructor(
    val key: String,
    val properties: P,
    val items: Collection<KitItem>,
    val kitPlayerEvents: Collection<Listener>,
) {
    init {
        properties.kitname = key
    }

    companion object {
        inline operator fun <P : KitProperties> invoke(
            key: Any,
            crossinline properties: () -> P,
            crossinline builder: KitBuilder<P>.() -> Unit,
        ) = lazy {
            KitBuilder(
                key.toString().replaceFirstChar { it.lowercase() },
                properties.invoke()
            ).apply(builder).internalBuilder.build()
        }
    }
}
