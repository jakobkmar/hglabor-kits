@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.kotlinkitapi.builder

import net.axay.kotlinkitapi.api.*
import net.axay.kotlinkitapi.cooldown.Cooldown
import net.axay.kotlinkitapi.cooldown.CooldownManager
import net.axay.kotlinkitapi.cooldown.CooldownScope
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

class KitBuilder<P : KitProperties>(val kit: Kit<P>) {
    fun clickableItem(stack: ItemStack, cooldown: Long, onClick: (KitContext<P>) -> Unit) {
        kit.internal.items += ClickableKitItem(stack, cooldown, onClick)
    }

    fun holdableItem(stack: ItemStack, period: Long, onHold: (KitContext<P>) -> Unit) {
        kit.internal.items += HoldableKitItem(stack, period, onHold)
    }

    inline fun <reified T : PlayerEvent> kitPlayerEvent(crossinline callback: (T) -> Unit) {
        kit.internal.kitPlayerEvents += listen<T>(register = false) {
            // TODO if player kit == this kit
            callback.invoke(it)
        }
    }

    fun Player.applyCooldown(cooldown: Cooldown, block: CooldownScope.() -> Unit) {
        if (!CooldownManager.hasCooldown(cooldown, this)) {
            if (CooldownScope().apply(block).shouldApply) {
                CooldownManager.addCooldown(cooldown, this)
            }
        }
    }

    fun PlayerEvent.applyCooldown(cooldown: Cooldown, block: CooldownScope.() -> Unit) =
        player.applyCooldown(cooldown, block)
}
