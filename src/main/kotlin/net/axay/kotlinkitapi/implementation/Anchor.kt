package net.axay.kotlinkitapi.implementation

import net.axay.kotlinkitapi.api.Kit
import net.axay.kotlinkitapi.api.KitProperties
import net.axay.kspigot.runnables.task
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.util.Vector

class AnchorProperties : KitProperties() {
    val sound by sound(Sound.BLOCK_ANVIL_PLACE)
    val volume by float(0.2f)
}

val Anchor = Kit("Anchor", ::AnchorProperties) {
    kitPlayerEvent<EntityDamageByEntityEvent>({ it.damager as? Player }) { it, damager ->
        val target = (it.entity as? LivingEntity) ?: return@kitPlayerEvent
        task(delay = 1L, howOften = 1L) {
            target.velocity = Vector(0, 0, 0)
        }
        damager.playSound(target.location, this.kit.properties.sound, this.kit.properties.volume, 1f)
    }
}
