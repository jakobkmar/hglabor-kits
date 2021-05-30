# KotlinKitAPI

## Dependency

Add the following dependencies:

```kotlin
implementation("net.axay:kotlin-kit-api:$version")
```

The KotlinKitAPI also depends on [spigot-language-kotlin](https://github.com/bluefireoly/spigot-language-kotlin)
and [KSpigot](https://github.com/bluefireoly/KSpigot).

Make sure to add `spigot-language-kotlin` as a "compileOnly" dependency and provide its plugin at runtime. Also, do not
forget to read the setup guide of KSpigot.

## Usage

### Kit properties

A kit will likely need some variable properties, which can be changed in the config or via commands. Let us create a
class representing these properties:

```kotlin
class NinjaProperties : KitProperties() {
    val maxDistance by int(30)
}
```

A lot of kits do also need a cooldown of some kind, therefore there is a special `CooldownProperties` class, let us use
that one for our Ninja kit instead.

```kotlin
class NinjaProperties : CooldownProperties(30 * 1000) {
    val maxDistance by int(30)
}
```

Now we do have a `cooldown` property automatically, which opens up the possibility to use some nice extensions
functions.

### Kit logic

#### Create the kit

A kit can be created by calling the `invoke()` operator function of the `Kit` companion object:

```kotlin
val Ninja = Kit("Ninja", ::NinjaProperties) {
    // inside the kit body
}
```

#### Manage state

It is best practice handling the state inside the kit body, therefore let us create a list to keep track of whom the kit
player damaged last.

```kotlin
val Ninja = Kit("Ninja", ::NinjaProperties) {
    val lastDamaged = OnlinePlayerMap<Player>()
}
```

The following code snippets will all be inside the kit body.

#### Kit events

The KotlinKitAPI can automatically check if a player (of a `PlayerEvent` has the kit).

```kotlin
kitPlayerEvent<PlayerToggleSneakEvent> {
    it.player.sendMessage("You definitely have the ${kit.key} kit!")
}
```

If the event is not a `PlayerEvent`, you have to provide some logic for getting the player for whom the check should be
performed.

With this knowledge, let us create an event to find out who the kit player damages:

```kotlin
kitPlayerEvent<EntityDamageByEntityEvent>(
    // the playerGetter:
    { it.damager as? Player }
) { it, player ->
    lastDamaged[player] = it.entity as? Player ?: return@kitPlayerEvent
}
```

Note: You can return null in that `playerGetter`, this will mean that there is no kit player and therefore the kit event
won't be executed.

#### Cooldown

You can apply the cooldown your `CooldownProperties` very easily if you are inside the kit body, by using the `applyCooldown` function and passing the `PlayerEvent` or the `Player` to it.

Let us actually implement the teleport functionality of the Ninja kit, with our cooldown applied:

```kotlin
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
```
