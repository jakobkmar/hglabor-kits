package net.axay.kotlinkitapi.utils

import org.bukkit.Material

val Material.isSoup: Boolean
    get() = this == Material.MUSHROOM_STEW || this == Material.RABBIT_STEW || this == Material.SUSPICIOUS_STEW
