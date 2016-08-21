## New features
* A new parameter `<handler>.attributes.attack-damage-multiplier`
allows to change damage that will be dealt by entity or its projectile
* A new parameter `<handler>.attributes.base-armor` allows to change
entity's a base armor before all the other modifiers
* A new parameter `<handler>.attributes.follow-range` allows to
change a range at which an entity will follow others; this parameter
changes significantly the behaviour of projectiles, affected by gravity
* A new parameter `<handler>.attributes.knockback-resistance`
allows to change entity's resistance to knockback
* A new parameter `<handler>.attributes.max-health`
allows to change entity's max. health in absolute values
* A new parameter `<handler>.attributes.movement-speed-multiplier`
allows to change entity's base movement speed
* Allow a probability lower than one percent

## Changes
* Moved the configuration of max. health from `<handler>.max-health`
to `<handler>.attributes.max-health-multiplier`,  marked the old
attribute as a deprecated one

## Performance improvements
* Process only valid entities
