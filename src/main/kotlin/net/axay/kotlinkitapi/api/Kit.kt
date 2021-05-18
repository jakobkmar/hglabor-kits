package net.axay.kotlinkitapi.api

import net.axay.kotlinkitapi.builder.KitBuilder
import org.bukkit.event.Listener

open class Kit internal constructor(
    val name: String,
    val items: Collection<KitItem>,
    val kitPlayerEvents: Collection<Listener>,
) {
    companion object {
        inline operator fun invoke(name: String, builder: KitBuilder.() -> Unit) =
            KitBuilder(name).apply(builder).internalBuilder.build()
    }
}
