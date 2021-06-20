package net.axay.kotlinkitapi.implementation

import net.axay.kotlinkitapi.api.Kit
import net.axay.kotlinkitapi.cooldown.CooldownProperties
import net.axay.kotlinkitapi.cooldown.applyCooldown
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


class MagmaProperties : CooldownProperties(15 * 1000) {
    val likelihood by int(33)
    val effectDuration by int(2)
    val extraDamage by double(2.0)
    val radius by double(5.0)
    val height by int(8)

    val velocityEnabled by boolean(true)

    val velocity by double(0.5)
}

val Magma = Kit("Magma", ::MagmaProperties) {

    clickableItem(ItemStack(Material.MAGMA_BLOCK)) {
        applyCooldown(it) {
            val radius = kit.properties.radius
            spawnFireCircle(it.player, radius, kit.properties.height)
            it.player.world.getNearbyEntities(it.player.location, radius, radius, radius)
                .filter { entity -> entity is Player && entity.uniqueId != it.player.uniqueId }
                .forEach { entity ->
                    entity.fireTicks = kit.properties.effectDuration * 20
                    if (kit.properties.velocityEnabled) entity.velocity =
                        entity.velocity.apply { y = kit.properties.velocity }
                    if (Random.nextInt(1, 101) <= kit.properties.likelihood)
                        (entity as Player).damage(kit.properties.extraDamage, it.player)
                }
        }
    }

    kitPlayerEvent<BlockPlaceEvent>(
        { it.player }
    ) { it, player ->
        val kitItem = kit.internal.items[0]?.stack
        if (
            player.inventory.itemInMainHand == kitItem ||
            player.inventory.itemInOffHand == kitItem
        ) it.isCancelled = true
    }

}

private fun spawnFireCircle(player: Player, radius: Double, height: Int) {
    val location: Location = player.location
    val world: World = player.world
    for (i in 0..radius.toInt()) {
        world.spawnParticle(Particle.FLAME, location.clone().add(i.toDouble(), 0.0, 0.0), 0, 0.0, 0.0, 0.0, 8.0)
        world.spawnParticle(Particle.FLAME, location.clone().add(i.toDouble() * -1, 0.0, 0.0), 0, 0.0, 0.0, 0.0, 8.0)
        world.spawnParticle(Particle.FLAME, location.clone().add(0.0, 0.0, i.toDouble()), 0, 0.0, 0.0, 0.0, 8.0)
        world.spawnParticle(Particle.FLAME, location.clone().add(0.0, 0.0, i.toDouble() * -1.0), 0, 0.0, 0.0, 0.0, 8.0)
    }
    for (i in 0 until height) {
        var y = 0.0
        while (y < Math.PI * 2) {
            val x: Double = radius * cos(y)
            val z: Double = radius * sin(y)
            location.world
                ?.spawnParticle(Particle.FLAME, location.clone().add(x, i.toDouble() / 4, z), 0, 0.0, 0.0, 0.0, 5.0)
            y += .1
        }
    }
}