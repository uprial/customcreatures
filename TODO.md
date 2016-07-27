# High priority
- Release v1.0.1.

# Next versions
- Backport bug-fixes (reload of debug configuration, fair config
reload) to other plugins.
- Modify Creeper's fuse ticks and its jump height.
- Restrict entities' despawn (setRemoveWhenFarAway). Investigate
a possibility to move such specific entities and limit their number.
- Add a possibility to modify custom attributes like follow-distance.
Teach Skeletons to shoot much further via abstract configuration
parameters.
- Implement specific properties of LivingEntities: powered creeper;
horse's jump strength, domestication, style, variant, armor and saddle;
saddled pig; angry bunny; baby zombie.
- Unique flag: don't apply two handlers with the same value of this
flag. This flag should replace filter's probability with a system of
 weights.