@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.axay.kotlinkitapi.builder

import net.axay.kotlinkitapi.api.*
import net.axay.kotlinkitapi.cooldown.Cooldown
import net.axay.kotlinkitapi.cooldown.CooldownManager
import net.axay.kotlinkitapi.cooldown.CooldownScope
import net.axay.kotlinkitapi.registry.PlayerKits.hasKit
import net.axay.kotlinkitapi.utils.ExperimentalKitApi
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class KitBuilder<P : KitProperties>(val kit: Kit<P>) {
    private var currentItemId = 0

    fun simpleItem(stack: ItemStack) {
        kit.internal.items[currentItemId++] = SimpleKitItem(stack)
    }

    fun clickableItem(stack: ItemStack, onClick: (PlayerInteractEvent) -> Unit) {
        kit.internal.items[currentItemId++] = ClickableKitItem(stack, onClick)
    }

    @ExperimentalKitApi
    fun holdableItem(stack: ItemStack, period: Long, onHold: (Player) -> Unit) {
        kit.internal.items[currentItemId++] = HoldableKitItem(stack, period, onHold)
    }

    inline fun <reified T : PlayerEvent> kitPlayerEvent(crossinline callback: (event: T) -> Unit) {
        kit.internal.kitPlayerEvents += listen<T> {
            if (it.player.hasKit(kit))
                callback.invoke(it)
        }
    }

    inline fun <reified T : Event> kitPlayerEvent(
        crossinline playerGetter: (T) -> Player?,
        crossinline callback: (event: T, player: Player) -> Unit,
    ) {
        kit.internal.kitPlayerEvents += listen<T> {
            val player = playerGetter(it) ?: return@listen
            if (player.hasKit(kit))
                callback(it, player)
        }
    }

    inline fun Player.applyCooldown(cooldown: Cooldown, block: CooldownScope.() -> Unit) {
        if (!CooldownManager.hasCooldown(cooldown, this)) {
            if (CooldownScope().apply(block).shouldApply) {
                CooldownManager.addCooldown(cooldown, this)
            }
        }
    }

    inline fun PlayerEvent.applyCooldown(cooldown: Cooldown, block: CooldownScope.() -> Unit) =
        player.applyCooldown(cooldown, block)
}
