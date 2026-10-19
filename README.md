# ZombiesVsPlants

A CMSC132 Java game inspired by Plants vs. Zombies, with the player controlling the zombies. Plan a formation, spend moonlight to deploy units, and break through the plants to reach Dave's Brain.

The game uses Java AWT, the provided `Game`, `Polygon`, and `Point` starter classes, and pixel-style characters drawn in code. No external libraries or image downloads are required.

## Run the game

Requires **JDK 17 or newer** and a desktop environment.

### In an IDE

Open the project, use `src/` as the source folder, and run the `main` method in [`ZombiesVsPlantsGame.java`](src/game/ZombiesVsPlantsGame.java).

### From PowerShell

Run these commands from the project root:

```powershell
New-Item -ItemType Directory -Force -Path bin | Out-Null
$sourceFiles = @(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName)
javac -d bin @sourceFiles
java -cp bin game.ZombiesVsPlantsGame
```

## How to play

Each of the **three levels** has **three waves**.

1. During the 30-second planning phase, choose zombies and place them on the two-column, five-lane deployment grid. A transparent preview shows where the selected zombie will go.
2. Press Space to start early, or let the countdown finish. At least one zombie must be placed before a wave can start.
3. Plants select a fixed offensive, neutral, or defensive formation based on your zombies' combined offense and defense scores. Layouts vary by level and wave.
4. Your zombies move right automatically and attack plants blocking their lane. Shooters fire peas that damage zombies on collision.
5. A living zombie reaching the brain wins the wave. If all deployed zombies die, you lose. Combat has no time limit.
6. Press Enter after a wave victory to plan the next wave. Winning the third wave completes the level; press Enter to continue. Losing offers a restart from Level 1.

## Controls

| Key | Action |
| --- | --- |
| `1` | Select BasicZombie |
| `2` | Select FastZombie |
| `3` | Select TankZombie |
| `WASD` or arrow keys | Move the deployment selection |
| `Enter` during planning | Place the selected zombie |
| `Backspace` during planning | Remove the zombie in the selected cell and refund its cost |
| `Space` during planning | Start the wave early |
| `Enter` on a result screen | Continue or restart, as shown on screen |

Click the game board if it does not have keyboard focus.

## Moonlight

- Each level starts with **250 moonlight**.
- Each subsequent wave adds **250** to the remaining balance, including kill rewards.
- Killing a plant earns **25 moonlight**.
- Placement and refunds are available during planning. Occupied cells and insufficient funds prevent placement.
- A new level or a restart resets the balance to **250**.

| Zombie | Cost | Role |
| --- | ---: | --- |
| BasicZombie | 50 | Balanced stats |
| FastZombie | 75 | Faster movement and attacks; lower health |
| TankZombie | 125 | Higher health and damage |

## Source layout

| Location | Purpose |
| --- | --- |
| `src/game/ZombiesVsPlantsGame.java` | Game state, input, planning, combat, and progression |
| `src/game/Battlefield.java` | Board and goal artwork |
| `src/game/DeploymentMenu.java` | Moonlight display and zombie cards |
| `src/game/DeploymentCursor.java` | Grid selection and cell highlight |
| `src/game/PlantLayoutSelector.java` | Composition analysis and fixed layouts |
| `src/game/units/` | Shared unit classes, peas, pixel rendering, and combat interface |
| `src/game/units/zombies/` | Zombie types and costs |
| `src/game/units/plants/` | Plant types and layout strategies |
| `bin/` | Generated Java class files; ignored by Git |

Edit the Java files under `src/`. The IDE or compiler generates the files under `bin/`.

## Current status

Implemented: planning and ghost previews, placement/refunds, moonlight carryover, all nine waves, fixed layouts for all three strategies, automatic movement, attack cooldowns, and visible peas with collision damage and cleanup.

Remaining work includes forward-only shooter targeting, death animations with rotation and shrinking, health feedback, balance playtesting, and final Javadocs/UML and assignment-rubric checks.
