# CP Run

A 2D endless runner built in Java with JavaFX, inspired by Cookie Run. Pick a cookie, pick a world, then jump and slide past obstacles while collecting jellies, coins and health items. The longer you survive, the faster the world scrolls.

Built as a team of two for the Programming Methodology course (2025) at Chulalongkorn University.

## Highlights

- **5 playable cookies** with different HP pools and abilities (passive and active)
- **5 worlds** with their own art, music and difficulty curve
- **Event-driven architecture**: a lightweight `EventBus` decouples game logic from UI and audio
- **Object pooling** (generic `ObjectPool`, `ParticlePool`) to reuse objects instead of allocating every frame
- **Unit-tested** core systems with JUnit 5 and TestFX
- Class diagram included: [`FianlUML.png`](FianlUML.png)

## Gameplay

The cookie runs automatically. Your job is to:

- Jump over ground obstacles (spikes, blocks, candy walls)
- Slide under air obstacles (bats, cloud spikes, fireballs)
- Collect coins and jellies for score
- Pick up health items to restore HP
- Use your cookie's ability at the right moment

HP drains slowly over time. The run ends when HP reaches zero.

## Controls

| Action  | Keys              |
|---------|-------------------|
| Jump    | Space or Up Arrow |
| Slide   | Down Arrow or S   |
| Ability | E                 |
| Pause   | P                 |

Mouse also works: click the upper half of the screen to jump, the lower half to slide.

## Cookies

All five cookies are unlocked from the start.

| Cookie            | Tier | HP  | Ability                                              |
|-------------------|------|-----|------------------------------------------------------|
| Blueberry Cookie  | S    | 110 | Magnetic: pulls nearby coins and jellies (passive)   |
| Hero Cookie       | S    | 115 | Jump Boost: jumps higher (passive)                   |
| Pirate Cookie     | A    | 105 | Ghost: phase through obstacles for 3 s, 5 s cooldown |
| Zombie Cookie     | B    | 150 | None, but the highest HP in the roster               |
| Brave Cookie      | C    | 110 | None, the baseline cookie                            |

## Worlds

Each world increases base speed and obstacle density:

| # | World         | Difficulty |
|---|---------------|------------|
| 1 | Forest        | Easy       |
| 2 | Palace        | Medium     |
| 3 | Theatre       | Normal     |
| 4 | City of Magic | Hard       |
| 5 | CP Grave      | Extreme    |

Difficulty also ramps up within a single run the longer you survive (`DifficultyManager`).

## Getting Started

**Requirements:** JDK 21 or newer. JavaFX 24 is downloaded automatically by the `org.openjfx.javafxplugin` Gradle plugin, so no separate JavaFX SDK is needed.

```bash
# macOS / Linux
./gradlew run      # run the game
./gradlew test     # run unit tests
./gradlew build    # build everything

# Windows
gradlew.bat run
```

You can also open the project in IntelliJ IDEA and run `application.CookieRunApp`.

## Project Structure

```
src/
├── main/java/
│   ├── application/   Entry point (CookieRunApp)
│   ├── audio/         SoundManager (music and sound effects)
│   ├── core/          Physics, entities, abilities, object pool, stages
│   ├── game/          GameController, managers, cookies, obstacles, collectibles, config
│   ├── gamelogic/     EventBus and game events, DifficultyManager
│   ├── gui/           Pages, components, graphics (scrolling layers, particles)
│   └── utils/         Interfaces: Animatable, Renderable, Updatable, Collidable, Spawnable
├── main/resources/    Sprites, backgrounds, audio, fonts
└── test/java/         JUnit 5 + TestFX tests
```

## Architecture

- **Game loop**: `GameStage` runs a JavaFX `AnimationTimer`. Delta time is capped at 0.1 s so lag spikes don't cause huge jumps.
- **GameController** owns score, coins and game-over state, and delegates each system to a manager.
- **Managers** (`CookieManager`, `ObstacleManager`, `CollectibleManager`, `HealthManager`, `AbilityManager`) each handle their own update and collision pass, and clean up their object lists with a single `removeIf` per frame.
- **EventBus** is a small publish/subscribe system for `COIN_COLLECTED`, `HEALTH_CHANGED`, `OBSTACLE_HIT` and `GAME_OVER`. UI and audio react to events instead of being called directly.
- **Config classes** in `game/config/` (`GameConfig`, `PhysicsConfig`, `ObstacleConfig`, `CollectibleConfig`) keep tunable constants in one place.

### Tuning reference

| What                           | Where                          | Default             |
|--------------------------------|--------------------------------|---------------------|
| Window size, target FPS        | `GameConfig`                   | 800 × 450, 60 FPS   |
| Gravity, jump velocity, ground | `PhysicsConfig`                | Ground at y = 290   |
| HP drain rate                  | `CookieManager.update`         | about 2 HP / second |
| Health item heal amount        | `HealthManager.checkCollision` | +25 HP              |
| Per-world speed and spawn rate | `StageList`                    | See file            |

## Testing

Tests live in `src/test/java/` and cover physics, abilities, object pooling, stages, collectibles, the event bus and difficulty scaling.

```bash
./gradlew test
```

JavaFX-dependent tests use TestFX with the Glass robot.

## Credits

Fan project made for learning purposes only. Cookie Run characters, art and font belong to Devsisters. This project is not affiliated with or endorsed by Devsisters.
