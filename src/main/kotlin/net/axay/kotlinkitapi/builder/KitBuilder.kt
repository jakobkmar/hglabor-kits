@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.kotlinkitapi.builder

import net.axay.kotlinkitapi.api.*
import net.axay.kspigot.event.listen
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

class KitBuilder(val properties: () -> KitProperties) {
    inner class Internal {
        val items = ArrayList<KitItem>()

        val kitPlayerEvents = ArrayList<Listener>()

        fun build() = Kit(properties, items, kitPlayerEvents)
    }

    val internalBuilder = this.Internal()

    fun clickableItem(stack: ItemStack, cooldown: Long, onClick: () -> Unit) {
        internalBuilder.items += ClickableKitItem(stack, cooldown, onClick)
    }

    fun holdableItem(stack: ItemStack, period: Long, onHold: () -> Unit) {
        internalBuilder.items += HoldableKitItem(stack, period, onHold)
    }

    inline fun <reified T : PlayerEvent> kitPlayerEvent(crossinline callback: (T) -> Unit) {
        internalBuilder.kitPlayerEvents += listen<T>(register = false) {
            // TODO if player kit == this kit
            callback.invoke(it)
        }
    }
}
