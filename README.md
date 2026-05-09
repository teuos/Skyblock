# A simple Skyblock plugin for advanced slime paper

## Commands
### /island:
- create - Allows a user to create an island
- delete - Allows a user to delete an island
- teleport - Allows a user to teleport to their island
- level:
  - generator - Will level up a users generators
  - border - Will level up a users world border
- visit - Allows a user to visit another users island
- trust - Allows a user to trust another user in their island
- untrust - Allows a user to untrust another user in their island

### /skyblock:
- template:
  - create - Allows an admin to create the template island
  - delete - Allows an admin to delete the template island
  - teleport - Allows an admin to teleport the template island

## Permissions:
  - skyblock.island.help:
    - description: Allows display of help menu for the skyblock command
    - default: true
  - skyblock.island.create:
    - description: Allows creating islands
    - default: true
  - skyblock.island.delete:
    - description: Allows deleting islands
    - default: true
  - skyblock.island.visit:
    - description: Allows visiting islands
    - default: true
  - skyblock.island.teleport:
    - description: Allows teleporting to own island
    - default: true
  - skyblock.island.trust:
    - description: Allows trusting others on their island
    - default: true
  - skyblock.island.untrust:
    - description: Allows removing trust of others on their island
    - default: true
  - skyblock.island.upgrade:
    - description: Allows the ability to level up their island
    - default: true
  - skyblock.island.upgrade.generator:
    - description: Allows the ability to level up their generator
    - default: true
  - skyblock.island.upgrade.border:
    - description: Allows the ability to level up their world border
    - default: true
  - skyblock.command.reload:
    - description: Allows reloading the plugin
    - default: op
  - skyblock.command.template.create:
    - description: Allows creation of the template island
    - default: op
  - skyblock.command.template.delete:
    - description: Allows deletion of the template island
    - default: op
  - skyblock.command.template.teleport:
    - description: Allows teleportation to the template island
    - default: op
  - skyblock.admin:
    - description: Full admin access
    - default: op


## Dependencies
This plugin requires being on an AdvancedSlimePaper server jar.<br>
https://infernalsuite.com/download/asp<br>
#### Required Plugins:
World Guard - https://dev.bukkit.org/projects/worldguard/files 7.0.15 or newer<br>
Advanced Slime Paper - https://infernalsuite.com/download/asp/



## Notes

This plugin is mostly designed for the minecraft server furrymc.net<br>
I do not plan on marketing this plugin to other servers, however if you wish to use it feel free!