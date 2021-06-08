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
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class KitBuilder<P : KitProperties>(val kit: Kit<P>) {
    private var currentItemId = 0

    /**
     * This just gives the [stack] to the player if
     * he has the kit.
     */
    fun simpleItem(stack: ItemStack) {
        kit.internal.items[currentItemId++] = SimpleKitItem(stack)
    }

    /**
     * Gives the [stack] to the player if he has the kit
     * and executed the [onClick] callback when the player
     * interacts using the item.
     */
    fun clickableItem(stack: ItemStack, onClick: (PlayerInteractEvent) -> Unit) {
        kit.internal.items[currentItemId++] = ClickableKitItem(stack, onClick)
    }

    /**
     * Gives the [stack] to the player if he has the kit
     * and executed the [onClick] callback when the player
     * interacts at an entity with the item.
     */
    fun clickOnEntityItem(stack: ItemStack, onClick: (PlayerInteractAtEntityEvent) -> Unit) {
        kit.internal.items[currentItemId++] = ClickableEntityKitItem(stack, onClick)
    }

    /**
     * Gives the [stack] to the player if he has the kit.
     *
     * Repeatedly executed the [onHold] block (in the given [period]) when
     * the player is holding the [stack].
     */
    @ExperimentalKitApi
    fun holdableItem(stack: ItemStack, period: Long, onHold: (Player) -> Unit) {
        kit.internal.items[currentItemId++] = HoldableKitItem(stack, period, onHold)
    }

    /**
     * Executes the given [callback] if the player of the
     * [PlayerEvent] has this kit.
     */
    inline fun <reified T : PlayerEvent> kitPlayerEvent(crossinline callback: (event: T) -> Unit) {
        kit.internal.kitPlayerEvents += listen<T> {
            if (it.player.hasKit(kit))
                callback.invoke(it)
        }
    }

    /**
     * Executes the given [callback] if the player of the
     * [playerGetter] has this kit.
     */
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

    /**
     * Executes the given [block] if the player does not currently
     * have a cooldown for this action.
     *
     * Inside if the [CooldownScope], you can use [CooldownScope.cancelCooldown]
     * if you wish to not apply the cooldown regardless of the [block]
     * being executed.
     */
    inline fun Player.applyCooldown(cooldown: Cooldown, block: CooldownScope.() -> Unit) {
        if (!CooldownManager.hasCooldown(cooldown, this)) {
            if (CooldownScope().apply(block).shouldApply) {
                CooldownManager.addCooldown(cooldown, this)
            }
        }
    }

    /**
     * @see [Player.applyCooldown]
     */
    inline fun PlayerEvent.applyCooldown(cooldown: Cooldown, block: CooldownScope.() -> Unit) =
        player.applyCooldown(cooldown, block)
}
