# Contributing to MACtouchMC

First off, thanks for taking the time to contribute! 🎉

## ⚠️ Hardware Requirements

This mod relies on **JTouchBar** and macOS-specific natives.

* **Best Case:** You have an Intel MacBook Pro with a Touch Bar.
* **Okay Case:** You have an Apple Silicon Mac (M1/M2/M3). You can verify the code builds, but you cannot test the physical bar.
* **Worst Case:** You are on Windows/Linux. **Please do not submit PRs affecting `Bars.java`** unless you are 100% sure, as you cannot test them.

## Development Setup

1. **Java 21:** Ensure your JDK is set to 21 (required for MC 1.21+).
2. **Fabric:** We use the latest Fabric Loader.
3. **Checkstyle:** Please run `./gradlew check` before pushing to ensure no formatting errors.

## The "Soft Dependency" Rule

If adding integration for another mod (e.g., Simple Voice Chat, ReplayMod):

* **NEVER** hard-depend on it in `fabric.mod.json`.
* Always wrap your code in a check: `if (FabricLoader.getInstance().isModLoaded("modid")) { ... }`.
* Keep integration logic in a separate package: `com.roocky.mactouch.integrations`.

## Pull Request Process

1. Update the `README.md` with details of changes to the interface.
2. If you add a new **Icon**, include the `.png` in `src/main/resources/assets/mactouchmc/textures/gui`.
3. **Squash your commits** into a single clean commit before submitting.
