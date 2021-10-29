# Modular Forcefield System (MFFS)

![Compatible with Minecraft 1.12](https://img.shields.io/badge/minecraft-1.12-green) ![Over 3,850,000 Downloads on CurseForge!](https://img.shields.io/badge/downloads-3,851,506-blue)
> Modular Forcefield System, often just referred to as MFFS, is a Mod for Minecraft Java 1.12 and before that adds a modular system for generating Force fields within game, at the cost of Redstone Flux (from Thermal Expansion). Beyond simply force fields, this mod also adds a wealth of base security, including the Area Defense station that is capable of fending off NPCs, mobs and other players - and stripping them of prohibited items!

![Screenshot of MFFS Capacitor](https://media.forgecdn.net/attachments/7/127/2013-08-10_11.48.55.png)

## Installation
### Source installation information for modders

This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

### Standalone source installation


**Step 1:** Open your command-line and browse to the folder where you extracted the zip file.

**Step 2:** Once you have a command window up in the folder that the downloaded material was placed, type:

Windows: 
```bash
gradlew setupDecompWorkspace
```
Linux/Mac OS: 
```bash
./gradlew setupDecompWorkspace
```

**Step 3:** After all that finished, you're left with a choice.
For eclipse, run "gradlew eclipse" (./gradlew eclipse if you are on Mac/Linux)

#### IntelliJ

If you prefer to use IntelliJ, steps are a little different.
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Once it's finished you must close IntelliJ and run the following command:

"gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)

**Step 4:** The final step is to open Eclipse and switch your workspace to /eclipse/ (if you use IDEA, it should automatically start on your project)

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not effect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.

**Tip:**
If you do not care about seeing Minecraft's source code you can replace "setupDecompWorkspace" with one of the following:
"setupDevWorkspace": Will patch, deobfusicated, and gather required assets to run minecraft, but will not generated human readable source code.
"setupCIWorkspace": Same as Dev but will not download any assets. This is useful in build servers as it is the fastest because it does the least work.

**Tip:**
When using Decomp workspace, the Minecraft source code is NOT added to your workspace in a editable way. Minecraft is treated like a normal Library. Sources are there for documentation and research purposes and usually can be accessed under the 'referenced libraries' section of your IDE.

### CurseForge installation

[View the CurseForge page for MFFS](https://www.curseforge.com/minecraft/mc-mods/modular-forcefield-system)

## Support

Please list an [Issue on GitHub](https://github.com/KatrinaAS/modularforcefieldsystem/issues) if you are having difficulties with MFFS, or why not join our [Friendly Discord Community](https://discord.gg/wdK3GfN37m)?

## Credits

* Original Mod Developers: Thunderdark & Matchlight  
* Programming: KatrinaAS & Minalien  
* Art: Imalune
* README: ![aimeesunflower](https://img.shields.io/badge/github-aimeesunflower-fbc9c9)
