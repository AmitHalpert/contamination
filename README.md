# contamination
![Gameicon](https://i.imgur.com/5YK952r.png)
**contamination** is a 2D-shooter-platformer game with simple gameplay mechanics.  
This is a Java-based game that uses [LibGDX](https://libgdx.com) as its game framework.

# Installation

- All releases for contamination can be found [here](https://github.com/AmitHalpert/contamination/releases)
- Last build status: https://github.com/AmitHalpert/contamination/actions

## Build and runtime dependencies
- **Important** update to the latest graphics driver
- Gradle (From the package manager or the one provided in this repository)
- JDK 8-15

## How to run?
In order to run the game you must execute the commands below:
```bash
git clone https://github.com/AmitHalpert/contamination.git
cd contamination
./gradlew desktop:run
```
For nix users (with flakes), simply run the command below:
```bash
nix run github:AmitHalpert/contamination
```
