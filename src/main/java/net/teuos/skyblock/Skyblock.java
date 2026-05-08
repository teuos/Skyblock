package net.teuos.skyblock;

import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.loaders.file.FileLoader;
import com.sk89q.worldguard.WorldGuard;
import net.teuos.skyblock.commands.IslandCommands;
import net.teuos.skyblock.commands.SkyblockCommands;
import net.teuos.skyblock.libs.CSVLibs;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.listeners.CobbleGen;
import net.teuos.skyblock.managers.IslandManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import net.teuos.skyblock.managers.IslandPermissionsManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public final class Skyblock extends JavaPlugin {



    private SlimeLoader worldLoader;
    private WorldGuard worldGuardApi;

    private File islandLevelFile;

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();

        if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            getLogger().severe("WorldGuard not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        File islandsFolder = new File(getDataFolder(), "islands");


        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        if(!islandsFolder.exists()){
            islandsFolder.mkdir();
        }

        worldLoader = new FileLoader(islandsFolder);
        worldGuardApi = WorldGuard.getInstance();


        islandLevelFile = new File(getDataFolder(), "island_levels.csv");

        CSVLibs csvLibs = new CSVLibs(islandLevelFile, this);

        MessageLibs messageLibs = new MessageLibs(this);

        IslandPermissionsManager permissionsManager = new IslandPermissionsManager(worldLoader, worldGuardApi, this);

        IslandLevelManager levelManager = new IslandLevelManager(csvLibs, this);

        IslandManager islandManager = new IslandManager(islandLevelFile, worldLoader, worldGuardApi, permissionsManager, csvLibs, levelManager, this);


        System.out.println("Skyblock is enabled");


        try {
            if (!islandLevelFile.exists()) {
                islandLevelFile.createNewFile();

                Files.write(
                        islandLevelFile.toPath(),
                        List.of("world,cobblegen,border")
                );

                System.out.println("Created island_levels.csv");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Register CobbleGen
        getServer().getPluginManager().registerEvents(new CobbleGen(levelManager, csvLibs), this);

        // Register island commands
        IslandCommands islandCommands = new IslandCommands(levelManager, islandManager, permissionsManager, csvLibs, messageLibs);
        getCommand("is").setExecutor(islandCommands);
        getCommand("island").setExecutor(islandCommands);

        // Register skyblock commands
        SkyblockCommands skyblockCommands = new SkyblockCommands(levelManager, islandManager, permissionsManager, this, messageLibs);
        getCommand("sb").setExecutor(skyblockCommands);
        getCommand("skyblock").setExecutor(skyblockCommands);


    }

    @Override
    public void onDisable() {
        System.out.println("Skyblock is disabled");
    }
}
