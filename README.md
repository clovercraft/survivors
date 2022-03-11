# Clovercraft Survivors
Ready to put your survival skills to the test? Clovercraft Survivors takes the SMP experience to a new level with hidden roles, bonus abilities, limited lives, and custom world structures. Dive in to a world full of new challenges, work together to climb above the odds, and compete for the crown of Survivor.

## Features
- All features configurable
- Permissions based commands (default OP only)
- Random life count for all players joining the server between 2 and 6 lives.
- PvP restrictions for players above the hostility threshold
- Custom abilities for the Hunter, Vampire, and Spartan hidden roles
- Automated timers for limited sessions, hidden role selection, and world border shrink.
- Custom advancements & recipe modifications
- In-game GUI scoreboard to see the status of all players.

## Setup
This mod needs to be installed with a fresh Minecraft server for world generation. Install the mod like any other Fabric mod, and start up the server. If you would like to override any of the default world options for Survivors, you can do so by following the steps below.

1. Stop your server
2. edit the `survivors.json` config file to reflect your desired settings.
3. If you are changing settings related to structures and world generation, you will need to delete your `/world` directory, or move it to a different location to allow the server to regenerate the world.
4. Start the server

### Default Options
```json
{
  "world_border": 1500,
  "session_time": 180,
  "worldgen": {
    "force_endportal": true,
    "spawn_enchanter": true
  },
  "lives": {
    "randomized": true,
    "min": 2,
    "max": 6
  },
  "roles": {
    "hunter": 1,
    "vampire": 1,
    "spartan": 1
  },
  "rules": {
    "hostile_lives_count": 1,
    "allow_passive_pvp": false,
    "allow_passive_pvp_hunter": false,
    "allow_passive_pvp_vampire": false,
    "allow_agro_pvp_hunter": true,
    "allow_agro_pvp_vampire": true
  }
}
```
