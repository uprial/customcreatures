# New features
- Deeply test exponential distribution, specifically on two integer values.
- Change binary flags like "infinity" to random.
- Maximize thorns for items.
- Increase jump and speed effects after the player spawn, a little blindness
- Limit number of currently living entities by type
- Modify Creeper's fuse ticks and its jump height.
- Add a filter based on entity's location: location in the world,
under sun
- Restrict entities' despawn (setRemoveWhenFarAway). Investigate
a possibility to move such specific entities and limit their number.
- Implement specific properties of LivingEntities: powered creeper;
horse's jump strength, domestication, style, variant, armor and saddle;
saddled pig; angry bunny; baby zombie.
- Unique flag: don't apply two handlers with the same value of this
flag. This flag should replace filter's probability with a system of
 weights.
