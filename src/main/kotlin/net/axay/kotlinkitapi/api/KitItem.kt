package net.axay.kotlinkitapi.api

import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

sealed class KitItem {
    abstract val stack: ItemStack
}

class SimpleKitItem(override val stack: ItemStack) : KitItem()

class ClickableKitItem(
    override val stack: ItemStack,
    val onClick: (PlayerInteractEvent) -> Unit,
) : KitItem()

class ClickableEntityKitItem(
    override val stack: ItemStack,
    val onClick: (PlayerInteractAtEntityEvent) -> Unit,
) : KitItem()

class HoldableKitItem(
    override val stack: ItemStack,
    val period: Long,
    val onHold: (Player) -> Unit,
) : KitItem()
