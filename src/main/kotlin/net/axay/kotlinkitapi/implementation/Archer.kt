package net.axay.kotlinkitapi.implementation

import net.axay.kotlinkitapi.api.Kit
import net.axay.kotlinkitapi.api.KitProperties
import org.bukkit.Material
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*
import kotlin.math.roundToInt

class ArcherProperties : KitProperties() {
    val effectDuration by int(80)
    val effectMultiplier by int(0)
    val arrowAmount by int(6)
    val probability by int(50)
}

val Archer = Kit("Archer", ::ArcherProperties) {
    val potionEffectTypes = listOf(
        PotionEffectType.BLINDNESS,
        PotionEffectType.SLOW_FALLING,
        PotionEffectType.LEVITATION,
        PotionEffectType.REGENERATION,
        PotionEffectType.SLOW_DIGGING,
        PotionEffectType.HUNGER,
        PotionEffectType.CONFUSION,
        PotionEffectType.GLOWING,
        PotionEffectType.WITHER,
        PotionEffectType.POISON,
        PotionEffectType.INCREASE_DAMAGE
    )

    simpleItem(ItemStack(Material.BOW))

    kitPlayerEvent<ProjectileHitEvent>({ it.entity.shooter as? Player }) { it, shooter ->
        val hitEntity = (it.hitEntity as? LivingEntity) ?: return@kitPlayerEvent
        shooter.inventory.addItem(ItemStack(Material.ARROW, this.kit.properties.arrowAmount))
        shooter.sendMessage("${hitEntity.name} is on ${(hitEntity.health * 100.0).roundToInt() / 100.0}")
    }

    kitPlayerEvent<EntityShootBowEvent>({ it.entity as? Player }) { it, _ ->
        if ((0..100).random() > this.kit.properties.probability) return@kitPlayerEvent
        if (it.projectile !is Arrow) return@kitPlayerEvent
        val arrow = it.projectile as Arrow
        arrow.addCustomEffect(
            PotionEffect(
                potionEffectTypes.random(),
                this.kit.properties.effectDuration * 20,
                this.kit.properties.effectMultiplier
            ), false
        )
        arrow.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
    }
}
