@file:Suppress("unused")

package net.axay.kotlinkitapi.registry

import net.axay.kotlinkitapi.api.ClickableEntityKitItem
import net.axay.kotlinkitapi.api.ClickableKitItem
import net.axay.kotlinkitapi.api.Kit
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.events.interactItem
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.persistence.PersistentDataType.INTEGER
import org.bukkit.persistence.PersistentDataType.STRING
import java.util.*

object PlayerKits {
    var removeOnLeave = true

    private val kits = HashMap<UUID, MutableSet<Kit<*>>>()

    init {
        listen<PlayerQuitEvent> {
            if (removeOnLeave)
                kits -= it.player.uniqueId
        }

        listen<PlayerInteractEvent> { event ->
            val item = event.item ?: return@listen
            val playerKits = kits[event.player.uniqueId]
            if (playerKits != null) {
                val itemData = item.itemMeta?.persistentDataContainer ?: return@listen
                val kitKey = itemData[Kit.kitItemKitKey, STRING] ?: return@listen
                val kitItem = playerKits.find { it.key == kitKey }?.internal?.items
                    ?.get(itemData[Kit.kitItemIdKey, INTEGER] ?: return@listen)

                if (kitItem != null) {
                    if (kitItem is ClickableKitItem)
                        kitItem.onClick.invoke(event)
                }
            }
        }

        listen<PlayerInteractAtEntityEvent> { event ->
            val item = event.interactItem ?: return@listen
            val playerKits = kits[event.player.uniqueId]
            if (playerKits != null) {
                val itemData = item.itemMeta?.persistentDataContainer ?: return@listen
                val kitKey = itemData[Kit.kitItemKitKey, STRING] ?: return@listen
                val kitItem = playerKits.find { it.key == kitKey }?.internal?.items
                    ?.get(itemData[Kit.kitItemIdKey, INTEGER] ?: return@listen)

                if (kitItem != null) {
                    if (kitItem is ClickableEntityKitItem)
                        kitItem.onClick.invoke(event)
                }
            }
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
