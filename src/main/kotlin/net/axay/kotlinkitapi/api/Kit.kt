package net.axay.kotlinkitapi.api

import net.axay.kotlinkitapi.builder.KitBuilder
import org.bukkit.event.Listener

open class Kit<P : KitProperties> internal constructor(
    propertiesCallback: () -> P,
    val items: Collection<KitItem>,
    val kitPlayerEvents: Collection<Listener>,
) {
    val properties by lazy { propertiesCallback.invoke() }

    companion object {
        inline operator fun <P : KitProperties> invoke(
            noinline properties: () -> P,
            builder: KitBuilder<P>.() -> Unit
        ) = KitBuilder(properties).apply(builder).internalBuilder.build()
    }
}
