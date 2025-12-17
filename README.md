# MACtouchMC

**MACtouchMC** provides context-aware Touch Bar integration for Minecraft on macOS. It displays relevant controls and information on your MacBook's Touch Bar depending on your in-game context (Menu, In-Game, Debug).

![Icon](src/main/resources/assets/mactouchmc/icon.png)

## Features
- **Context Awareness**: Automatically switches layouts between Main Menu, In-Game, and Debug screens.
- **In-Game Controls**: Toggle HUD, Take Screenshots, and access useful internal toggles.
- **Debug Tools**: Quick access to F3 debug features like reloading chunks, showing hitboxes, copying location, and more.
- **Apple Silicon Native**: Optimized for modern Macs (M1/M2/M3).

## Credits
**Author**: [ROOCKYdev](https://github.com/ROOCKYdev)

This project is a fork/revival of **MCTouchBar**, originally created by **MaximumFX**.
- Original Repository: [MaximumFX/MCTouchBar](https://github.com/MaximumFX/MCTouchBar)
- Original Author: [MaximumFX](https://github.com/MaximumFX)

## Installation
1. Install [Fabric Loader](https://fabricmc.net/) for Minecraft 1.21+.
2. Download the latest `mactouchmc-x.x.x.jar` from Releases.
3. Place the jar in your `.minecraft/mods` folder.
4. Launch Minecraft!

## Building from Source
```bash
./gradlew build
```
The output jar will be in `build/libs`.

## License
MIT License.
