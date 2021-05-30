package net.axay.kotlinkitapi.implementation

import net.axay.kotlinkitapi.api.Kit
import net.axay.kotlinkitapi.cooldown.CooldownProperties
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import kotlin.random.Random

class MonkProperties : CooldownProperties(15 * 1000) {
}

val Monk = Kit("Monk", ::MonkProperties) {
    //TODO armor dingsen
    mitDemKitItemGegnerAnKlicken(ItemStack(Material.BLAZE_ROD), onClick = {
        if (it.rightClicked is Player) {
            val inventory = (it.rightClicked as Player).inventory
            switchItem(inventory, inventory.heldItemSlot, Random.nextInt(inventory.size))
        }
    })
}

private fun switchItem(inventory: PlayerInventory, firstSlot: Int, secondSlot: Int) {
    val firstItem: ItemStack = getItem(inventory, firstSlot)
    val secondItem: ItemStack = getItem(inventory, secondSlot)
    inventory.setItem(firstSlot, secondItem)
    inventory.setItem(secondSlot, firstItem)
}

private fun getItem(inventory: PlayerInventory, slot: Int): ItemStack {
    val item = inventory.getItem(slot)
    return item ?: ItemStack(Material.AIR)
}
