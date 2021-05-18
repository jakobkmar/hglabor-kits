package net.axay.kotlinkitapi.api

import org.bukkit.entity.Player

class KitContext<P : KitProperties>(
    val properties: P,
    val player: Player,
)
