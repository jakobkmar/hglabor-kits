package net.axay.kotlinkitapi.implementation

import net.axay.kotlinkitapi.api.Kit
import net.axay.kotlinkitapi.api.KitProperties
import net.axay.kotlinkitapi.utils.isSoup
import net.axay.kspigot.extensions.broadcast
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.math.min

class AutomaticProperties : KitProperties() {
    val soupHealAmount by double(5.0)
}

val Automatic = Kit("Automatic", ::AutomaticProperties) {
    kitPlayerEvent<EntityDamageEvent>({ it.entity as? Player }) { _, player ->
        val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
        if (player.health >= maxHealth - this.kit.properties.soupHealAmount) return@kitPlayerEvent
        for (i in 0..9) {
            val item = player.inventory.getItem(i) ?: continue
            if (!item.type.isSoup) continue
            player.health = min(
                player.health + this.kit.properties.soupHealAmount,
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
            )
            item.amount = 0
            break
        }
    }
}
