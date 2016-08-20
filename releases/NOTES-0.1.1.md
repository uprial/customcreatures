## New features
* A new parameter `<handler>.attributes.attack-damage-multiplier`
allows to change damage that will be dealt by entity or its projectile
* A new parameter `<handler>.attributes.base-armor` allows to change
entity's base armor before all the other modifiers
* A new parameter `<handler>.attributes.follow-range` allows to
change range at which an entity will follow others; this parameter
changes significantly the behaviour of projectiles, affected by gravity
## Changes
* Moved the configuration of max. health from `<handler>.max-health`
to `<handler>.attributes.max-health-multiplier`,  marked the old
attribute as a deprecated one
## Performance improvements
* Process only valid entities