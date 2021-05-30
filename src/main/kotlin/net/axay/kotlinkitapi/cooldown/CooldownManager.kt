package net.axay.kotlinkitapi.cooldown

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import java.util.*

object CooldownManager {
    private val cooldownCoroutineScope = CoroutineScope(Dispatchers.Default)

    private val cooldownMap = HashMap<Cooldown, MutableSet<UUID>>()

    fun addCooldown(cooldown: Cooldown, player: Player) {
        val uuid = player.uniqueId
        val cooldownCollection = cooldownMap.getOrPut(cooldown) {
            Collections.synchronizedSet(HashSet())
        }
        cooldownCollection += uuid
        cooldownCoroutineScope.launch {
            delay(cooldown.property.get())
            cooldownCollection -= uuid
        }
    }

    fun hasCooldown(cooldown: Cooldown, player: Player) =
        cooldownMap[cooldown]?.contains(player.uniqueId) == true
}
