package net.axay.kotlinkitapi.api

import net.axay.kotlinkitapi.builder.KitBuilder

open class Kit internal constructor(
    val name: String,
) {
    companion object {
        operator fun invoke(name: String, builder: KitBuilder.() -> Unit) = KitBuilder(name).apply(builder).build()
    }
}
