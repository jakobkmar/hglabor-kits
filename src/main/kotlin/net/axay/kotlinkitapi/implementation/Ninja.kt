package net.axay.kotlinkitapi.implementation

import net.axay.kotlinkitapi.api.Kit
import net.axay.kotlinkitapi.cooldown.CooldownProperties
import net.axay.kotlinkitapi.cooldown.applyCooldown
import net.axay.kspigot.utils.OnlinePlayerMap
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerToggleSneakEvent

class NinjaProperties : CooldownProperties(30 * 1000) {
    val maxDistance by int(30)
}

val Ninja = Kit("Ninja", ::NinjaProperties) {
    val lastDamaged = OnlinePlayerMap<Player>()
    kitPlayerEvent<PlayerToggleSneakEvent> {
        applyCooldown(it) {
            val toPlayer = lastDamaged[it.player]
            if (toPlayer == null || !toPlayer.isOnline) {
                cancelCooldown()
            } else {
                if (it.player.location.distance(toPlayer.location) <= kit.properties.maxDistance)
                    it.player.teleport(toPlayer)
            }
        }
    }
    kitPlayerEvent<EntityDamageByEntityEvent>({ it.damager as? Player }) { it, player ->
        lastDamaged[player] = it.entity as? Player ?: return@kitPlayerEvent
    }
}
