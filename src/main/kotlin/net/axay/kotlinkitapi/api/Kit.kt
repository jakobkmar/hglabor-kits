package net.axay.kotlinkitapi.api

import net.axay.kotlinkitapi.builder.KitBuilder
import org.bukkit.event.Listener

open class Kit internal constructor(
    propertiesCallback: () -> KitProperties,
    val items: Collection<KitItem>,
    val kitPlayerEvents: Collection<Listener>,
) {
    val properties by lazy { propertiesCallback.invoke() }

    companion object {
        inline operator fun invoke(
            noinline properties: () -> KitProperties,
            builder: KitBuilder.() -> Unit
        ) = KitBuilder(properties).apply(builder).internalBuilder.build()
    }
}
