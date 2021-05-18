package net.axay.kotlinkitapi.api

import org.bukkit.inventory.ItemStack

abstract class KitItem {
    abstract val stack: ItemStack
}

class ClickableKitItem(
    override val stack: ItemStack,
    val cooldown: Long,
    val onClick: () -> Unit,
) : KitItem()

class HoldableKitItem(
    override val stack: ItemStack,
    val period: Long,
    val onHold: () -> Unit,
) : KitItem()
