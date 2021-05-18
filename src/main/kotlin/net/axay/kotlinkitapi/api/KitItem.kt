package net.axay.kotlinkitapi.api

import org.bukkit.inventory.ItemStack

abstract class KitItem {
    abstract val stack: ItemStack
}

class ClickableKitItem<P : KitProperties>(
    override val stack: ItemStack,
    val cooldown: Long,
    val onClick: (KitContext<P>) -> Unit,
) : KitItem()

class HoldableKitItem<P : KitProperties>(
    override val stack: ItemStack,
    val period: Long,
    val onHold: (KitContext<P>) -> Unit,
) : KitItem()
