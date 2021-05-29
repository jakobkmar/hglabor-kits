package net.axay.kotlinkitapi.api

import net.axay.kotlinkitapi.builder.KitBuilder
import org.bukkit.event.Listener

open class Kit<P : KitProperties> internal constructor(
    key: String,
    propertiesCallback: () -> P,
    val items: Collection<KitItem>,
    val kitPlayerEvents: Collection<Listener>,
) {
    val properties by lazy {
        propertiesCallback.invoke().apply { kitname = key }
    }

    companion object {
        inline operator fun <P : KitProperties> invoke(
            key: Any,
            noinline properties: () -> P,
            builder: KitBuilder<P>.() -> Unit
        ) = KitBuilder(
            key.toString().replaceFirstChar { it.lowercase() },
            properties
        ).apply(builder).internalBuilder.build()
    }
}
