@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.kotlinkitapi.builder

import net.axay.kotlinkitapi.api.Kit

class KitBuilder(
    val name: String,
) {
    fun build() = Kit(name)
}
