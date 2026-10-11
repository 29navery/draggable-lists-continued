# Draggable Lists Continued

A Fabric continuation of [MrMelon54's Draggable Lists](https://github.com/MrMelon54/draggable_lists), maintained by nicky_lul.

Drag resource packs, datapacks, and saved multiplayer servers into a new order. Includes a floating drag preview, edge scrolling, optional Shift-to-drag, and options to hide the vanilla reorder arrows. Fixed-position packs, list headers, and LAN servers stay protected.

## Downloads and installation

Download the jar for **your exact Minecraft version** from [the downloads folder](https://github.com/29navery/draggable-lsits-continued/tree/main/releases/1.1.0). Put it in your instance's `mods` directory with [Fabric Loader](https://fabricmc.net/use/installer/) and [Cloth Config](https://modrinth.com/mod/cloth-config). [Mod Menu](https://modrinth.com/mod/modmenu) is optional and opens the configuration screen. Architectury and Fabric API are not required by this mod.

| Minecraft | Java | Cloth Config | Mod Menu (optional) |
| --- | --- | --- | --- |
| 1.21.9 | 21+ | 20.0.149 | 16.0.1 |
| 1.21.10 | 21+ | 20.0.149 | 16.0.1 |
| 1.21.11 | 21+ | 21.11.152 | 17.0.1 |
| 26.1 | 25+ | 26.1.154 | 18.0.2 |
| 26.1.1 | 25+ | 26.1.154 | 18.0.2 |
| 26.1.2 | 25+ | 26.1.154 | 18.0.2 |
| 26.2 | 25+ | 26.2.155 | 20.0.3 |
| 26.3 | 25+ | 26.3.159 | 21.0.0 |

Use Fabric Loader 0.19.5 or later. Each jar declares an exact game version to prevent accidental installation on an incompatible release.

## Building

Install JDK 25 to run Gradle, then select a supported game version:

```sh
./gradlew build mixinSmoke -Pminecraft_version=26.3
```

Artifacts appear under `build/<minecraft-version>/libs/`. Use the main jar in Minecraft; the `-sources.jar` is for developers. Older targets are compiled for Java 21. `versions.json` pins the game-specific dependency versions, and source generation adapts the GUI and input APIs without modifying the shared sources.

The GitHub Actions matrix builds all eight targets, applies every required mixin to real game bytecode, and uploads downloadable build artifacts for each successful target. This bytecode smoke check does not replace testing mouse interaction in a running Minecraft client.

## Credits and provenance

- Original mod and source: [MrMelon54](https://github.com/MrMelon54/draggable_lists), based on upstream commit `c6fb52e7f0ebf0b112d55612c49799b5243f5204` (v1.0.15).
- Continuation and version ports: nicky_lul.
- Original logo: [CarbonGhost](https://github.com/CarbonGhost).
- Original license retained in [LICENSE.md](LICENSE.md).

The existing destination repository is a continuation repository, rather than a GitHub-managed fork. The original Quilt and NeoForge source directories are retained for reference; this continuation's current build targets Fabric.
