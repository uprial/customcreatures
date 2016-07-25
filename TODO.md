# High priority
- Use metadata to store initial maximal health only for instances of
'Player' class.
- Use static method to create a creatures' config.
- Hide access to 'handlers' field in config.
- Localize workarounds related to schedulers. Detect a real need to 
schedule a task with maximal accuracy and use only one periodic 
scheduler.
- Release v1.0.1.

# Next versions
- Backport bug-fixes (reload of debug configuration, fair config
reload) to other plugins.
- Implement specific properties of LivingEntities: powered creeper;
horse's jump strength, domestication, style, variant, armor and saddle;
saddled pig; angry bunny; baby zombie.
- Unique flag: don't apply two handlers with the same value of this
flag. This flag should replace filter's probability with a system of
 weights.