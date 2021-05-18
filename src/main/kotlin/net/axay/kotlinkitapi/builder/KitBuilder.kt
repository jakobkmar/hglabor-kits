@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.kotlinkitapi.builder

import net.axay.kotlinkitapi.api.ClickableKitItem
import net.axay.kotlinkitapi.api.HoldableKitItem
import net.axay.kotlinkitapi.api.Kit
import net.axay.kotlinkitapi.api.KitItem
import org.bukkit.inventory.ItemStack

class KitBuilder(
    val name: String,
) {
    private val items = ArrayList<KitItem>()

    fun build() = Kit(name, items)

    fun clickableItem(stack: ItemStack, cooldown: Long, onClick: () -> Unit) {
        items += ClickableKitItem(stack, cooldown, onClick)
    }

    fun holdableItem(stack: ItemStack, period: Long, onHold: () -> Unit) {
        items += HoldableKitItem(stack, period, onHold)
    }
}
