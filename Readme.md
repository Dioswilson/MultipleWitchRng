# Rng multiple witch hut finder

This application lets you find mansion areas for witch rng on witch huts, allowing you to have multiple on the same perimeter.

## How to use

- Execute the jar file (Download it from releases) normally, no parameters needed
- Insert seed and witch huts positions (chunks coords), if you don't want 4 WH just leave it empty
- Insert the maximum amount of players that can load new chunks
- Insert player coordinates, it does not need to be precise, player can move inside the chunk.
- Select the max amount of advancers (dispensers, hoppers, etc.) you are willing to use
- Select the CPU usage profile you want to use (More CPU also means more RAM). Use high or very high only if you are not planning to use a lot of advancers.
- "Generate Litematic" tab is made to input known values and compare them or just generate the litematic

### Extra information
- Max players is the amount of non spectators players that were logged in simultaneously with different chunkmaps since the server restart
- You need to avoid randomticks
- Program does not check if all with huts are in range of the player, if they are not in range it will never find an output.
- Not planning to implement other way of building the perimeter. Litematica is the only option. https://masa.dy.fi/mcmods/client_mods/ to download it
- It does not matter the order you put witch huts, heightmap result will always be in the same order (Depending on the hash)
- Chunk loading grid must be before mob spawning (TT is ok)
- DO NOT raise heightmap where litematic placed bedrock or a witch hut
- `Spawns` value represents (with a small error) the amount of spawns every gametick. Contact me if not