# High priority
- Split ConfigReader into several classes.
- Explicitly limit all integer and float point values.
- Analyze all the code via all the possible instruments in Idea.
- Create default set of handlers:
(gold, iron, diamond) * (zombie, skeleton)
- Find way to identify a neutral entities and make some of them fat.
- Find way to identify potentially aggressive entities
and make some of them rapid.
- Release v1.0.1.

# Low priority
- Filter by logical groups of entities.
- Implement specific properties of LivingEntities: powered creeper;
horse's jump strength, domestication, style, variant, armor and saddle;
saddled pig; angry bunny; baby zombie.
- Unique flag: don't apply two handlers with the same value of this
flag. This flag should replace filter's probability with a system of
 weights.