@file:Suppress("unused")

package net.axay.kotlinkitapi.registry

import net.axay.kotlinkitapi.api.Kit
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

object PlayerKits {
    var removeOnLeave = true

    private val kits = HashMap<UUID, MutableSet<Kit<*>>>()

    init {
        listen<PlayerQuitEvent> {
            if (removeOnLeave)
                kits -= it.player.uniqueId
        }
    }

    fun Player.addKit(kit: Kit<*>) {
        kits.getOrPut(uniqueId) { HashSet() }.add(kit)
        kit.internal.givePlayer(this)
    }

    fun Player.hasKit(kit: Kit<*>): Boolean {
        return kits[uniqueId]?.contains(kit) == true
    }
}
