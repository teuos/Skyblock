package net.teuos.skyblock;

import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.loaders.file.FileLoader;
import com.sk89q.worldguard.WorldGuard;
import net.teuos.skyblock.commands.IslandCommands;
import net.teuos.skyblock.commands.SkyblockCommands;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.listeners.CobbleGen;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import net.teuos.skyblock.managers.IslandPermissionsManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Skyblock extends JavaPlugin {



    private SlimeLoader worldLoader;
    private WorldGuard worldGuardApi;

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


        IslandDataManager islandDataManager =
                new IslandDataManager(this);

        MessageLibs messageLibs = new MessageLibs(this);

        IslandPermissionsManager permissionsManager = new IslandPermissionsManager(worldLoader, worldGuardApi, this);

        IslandLevelManager levelManager = new IslandLevelManager(islandDataManager, this);

        IslandManager islandManager = new IslandManager(worldLoader, worldGuardApi, permissionsManager, islandDataManager, levelManager, this);

        islandManager.startIslandUnloadTask();

        System.out.println("Skyblock is enabled");


        // Register CobbleGen
        getServer().getPluginManager().registerEvents(new CobbleGen(levelManager, islandDataManager), this);

        // Register island commands
        IslandCommands islandCommands = new IslandCommands(levelManager, islandManager, permissionsManager, islandDataManager, messageLibs);
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
