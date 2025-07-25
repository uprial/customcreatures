![CustomCreatures Logo](images/customcreatures-logo.png)

## Compatibility

Tested on Spigot-1.21.3, 1.21.5, 1.21.6.

## Introduction

A Minecraft (Bukkit) plugin that allows to fully customize cloths and tools, enhancements and effects, drop and attributes at entity spawn.

## Features

* Synergies perfectly with [TakeAim](https://github.com/uprial/takeaim) and [MasochisticSurvival](https://legacy.curseforge.com/minecraft/bukkit-plugins/masochisticsurvival/) plugins
* 5% of animals naturally spawn with up to 400% health increase.
* 5% of hostile monsters spawn with an increased movement speed up to 100%.
* Once killed, a player respawns with an apple in hand and a small jump and movement bonus for a minute.
* 4% (*4) of zombie-like mobs spawn with a tiny jump and movement bonus, fire resistance, in GOLDEN equipment(*1) with random protection and random thorns, a slightly-enchanted golden axe (*1). Drops a gold ignot. (*3)
* 2% (*4) of zombie-like mobs spawn with a tiny jump and movement bonus, fire resistance, in IRON equipment(*1) with random protection and random durability, a well-enchanted sword (*1). Drops an iron block. (*3)
* 1% (*4) of skeleton-like mobs spawn with 5 minutes glowing aura, a medium jump and movement bonus, fire resistance, in DIAMOND equipment(*1) with maybe top protection and thorns, random durability and random support enhancements, a maybe top-enchanted bow (*2). Drops a diamond. (*3)
* 0.5% (*4) of zombie-like mobs spawn with 5 minutes glowing aura, a medium jump and movement bonus, fire resistance, in CHAINMAIL equipment(*1) with maybe top protection and thorns, random durability and random support enhancements, a maybe top-enchanted sword (*2). Drops lapiz lazuli and redstone. (*3)
* 5% of Pillagers and Piglings have crossbows (*2) with maybe top piercing, random durability, random quick charge, multishot.
* 5% of Drowned have tridents (*2) with maybe top impaling, random durability, channeling.
* 5% of Horses have a tamed zombie friend with improved knockback resistance, maximum health, movement speed, and jump strength.
* 5% of Rabbits are the killer bunnies scaled visually with a large health and movement bonus.
* 100% of Withers have thorns.
* 5% of creepers are babies: smaller, much faster, more explosive.
* 1% (*4) of Vexes are spawned together with illusioners, 10% have a diamond maybe top-enchanted sword (*2)
* 100% of Evokers, Illusioners and Ravagers have a large jump and movement bonus, fire resistance and regeneration, in NETHERITE equipment(*1) with maybe top protection and thorns, random support enhancements, specifically Illusioners with a maybe top-enchanted bow (*2). May drop an enchanted golden apple, a netherite scrap, a book of mending, a totem of undying, and specifically Illusioners - a diamond pickaxe. (*3)
* 25% of Vindicators spawn with a medium jump and movement bonus, fire resistance, a slightly-enchanted diamond axe (*1).
* 5% of Piglins spawn an angry Hoglin with increased movement speed and health.

(*1) The default drop chance is 8.5%, and each level of looting adds 1% to the drop chance.

(*2) "Maybe top" items have twice less default drop chance (4.25%), looting still adds 1% to the drop chance.

(*3) Each level of looting adds 1 to the max possible drop amount.

(*4) Each 5,000 damage dealt by the closest player increases probability of hard monsters spawn to 100%.

![CustomCreatures Promo](images/customcreatures-promo.png)

#### You can configure:
* A filter of types, spawn reasons and probabilities
* Entities' max health, effects, and equipment
* Templates of effects and enchantments
* Random distributions for the majority of numeric values
* Entities' following range
* Drop items and drop exp

#### You can solve the following problems:
* Increase the velocity of specific entities, thus make some enemies more dangerous naturally
* Equip entities to protect them from environmental influence
* Make items droppable to award a killer

## Commands

`customcreatures reload` - reload config from disk
`customcreatures apply <handler>` - apply a handler to currently living entities

## Permissions

* Access to 'reload' command:
`customcreatures.reload` (default: op)

* Access to 'apply' command:
`customcreatures.apply` (default: op)

## Configuration
[Default configuration file](src/main/resources/config.yml)

I assume the typical server admin config lifecycle for this plugin involves having their own custom configuration, which you keep in a dedicated part of the config file and migrate from version to version.

The first section of 500 commented lines shows the config structure you can use for your handlers. Each potential handler config option is used in the real config below, so you may check it for examples.

For example, you see "jump-strength" in the structure, where its type and mechanics are described:

```
#     jump-strength: <float-value:0.0..2.0> ### https://minecraft.fandom.com/wiki/Horse#Jump_strength
#                                           ### Horse's jump strength ranges from 0.4–1.0, with an average of 0.7.
(...)
```

Then you search its real usage

```
cool-horse:
(...)
  entity-specific-attributes:
(...)
      jump-strength: 2.0
```

## Author
I will be happy to add some features or fix bugs. My mail: uprial@gmail.com.

## Useful links
* [Project on GitHub](https://github.com/uprial/customcreatures/)
* [Project on Bukkit Dev](http://dev.bukkit.org/bukkit-plugins/customcreatures/)
* [Project on Spigot](https://www.spigotmc.org/resources/customcreatures.68711/)
* [TODO list](TODO.md)

## Related projects
* CustomBazookas: [Bukkit Dev](https://legacy.curseforge.com/minecraft/bukkit-plugins/custombazookas/) [GitHub](https://github.com/uprial/custombazookas), [Spigot](https://www.spigotmc.org/resources/custombazookas.124997/)
* CustomNukes: [Bukkit Dev](http://dev.bukkit.org/bukkit-plugins/customnukes/), [GitHub](https://github.com/uprial/customnukes), [Spigot](https://www.spigotmc.org/resources/customnukes.68710/)
* CustomRecipes: [Bukkit Dev](https://dev.bukkit.org/projects/custom-recipes), [GitHub](https://github.com/uprial/customrecipes/), [Spigot](https://www.spigotmc.org/resources/customrecipes.89435/)
* CustomVillage: [Bukkit Dev](http://dev.bukkit.org/bukkit-plugins/customvillage/), [GitHub](https://github.com/uprial/customvillage/), [Spigot](https://www.spigotmc.org/resources/customvillage.69170/)
* MasochisticSurvival: [Bukkit Dev](https://legacy.curseforge.com/minecraft/bukkit-plugins/masochisticsurvival/), [GitHub](https://github.com/uprial/masochisticsurvival/), [Spigot](https://www.spigotmc.org/resources/masochisticsurvival.124943/)
* TakeAim: [Bukkit Dev](https://dev.bukkit.org/projects/takeaim), [GitHub](https://github.com/uprial/takeaim), [Spigot](https://www.spigotmc.org/resources/takeaim.68713/)
