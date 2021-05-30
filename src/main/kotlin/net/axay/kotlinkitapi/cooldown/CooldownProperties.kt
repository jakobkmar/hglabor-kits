package net.axay.kotlinkitapi.cooldown

import net.axay.kotlinkitapi.api.KitProperties
import net.axay.kotlinkitapi.builder.KitBuilder
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

abstract class CooldownProperties(default: Long) : KitProperties() {
    val cooldown by long(default)

    @Suppress("LeakingThis")
    val cooldownInstance = Cooldown(this::cooldown)
}

inline fun <P : CooldownProperties> KitBuilder<P>.applyCooldown(player: Player, block: CooldownScope.() -> Unit) =
    player.applyCooldown(kit.properties.cooldownInstance, block)

inline fun <P : CooldownProperties> KitBuilder<P>.applyCooldown(event: PlayerEvent, block: CooldownScope.() -> Unit) =
    event.applyCooldown(kit.properties.cooldownInstance, block)
