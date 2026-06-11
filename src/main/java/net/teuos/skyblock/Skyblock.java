package net.teuos.skyblock;

import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.loaders.file.FileLoader;
import com.sk89q.worldguard.WorldGuard;
import net.teuos.skyblock.commands.IslandCommands;
import net.teuos.skyblock.commands.SkyblockCommands;
import net.teuos.skyblock.libs.CustomHeadLibs;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.listeners.CobbleGen;
import net.teuos.skyblock.listeners.GUIListener;
import net.teuos.skyblock.listeners.TeleportListeners;
import net.teuos.skyblock.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;


import java.io.File;

public final class Skyblock extends JavaPlugin {


    private static Economy economy = null;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();

        if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            getLogger().severe("WorldGuard not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }



        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File islandsFolder = new File(getDataFolder(), "islands");

        if(!islandsFolder.exists()){
            islandsFolder.mkdir();
        }

        File templatesFolder = new File(getDataFolder(), "templates");

        if(!templatesFolder.exists()){
            templatesFolder.mkdir();
        }


        SlimeLoader worldLoader = new FileLoader(islandsFolder);
        SlimeLoader templateLoader = new FileLoader(templatesFolder);
        WorldGuard worldGuardApi = WorldGuard.getInstance();

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }



        IslandDataManager islandDataManager =
                new IslandDataManager(this);

        TemplateDataManager templateDataManager =
                new TemplateDataManager(this);

        MessageLibs messageLibs = new MessageLibs(this);

        CustomHeadLibs customHeadLibs = new CustomHeadLibs();


        IslandPermissionsManager permissionsManager = new IslandPermissionsManager(islandDataManager, this);

        IslandLevelManager levelManager = new IslandLevelManager(islandDataManager, this);

        IslandManager islandManager = new IslandManager(worldLoader, templateLoader, permissionsManager, islandDataManager, templateDataManager, levelManager, this);

        EcoManager ecoManager = new EcoManager(this, economy, islandDataManager);

        SellManager sellManager = new SellManager(this);

        GUIManager guiManager = new GUIManager();
        GUIListener guiListener = new GUIListener(guiManager);
        Bukkit.getPluginManager().registerEvents(guiListener, this);

        islandManager.startIslandUnloadTask();

        // Register CobbleGen
        getServer().getPluginManager().registerEvents(new CobbleGen(levelManager, islandDataManager, this), this);

        // Register IslandLeave
        getServer().getPluginManager().registerEvents(new TeleportListeners(islandDataManager), this);

        // Register island commands
        IslandCommands islandCommands = new IslandCommands(levelManager, islandManager, permissionsManager, islandDataManager, messageLibs, ecoManager, this, templatesFolder, guiManager, templateDataManager, sellManager);
        getCommand("is").setExecutor(islandCommands);
        getCommand("island").setExecutor(islandCommands);
        getCommand("skyblock").setExecutor(islandCommands);
        getCommand("sb").setExecutor(islandCommands);

        // Register skyblock commands
        SkyblockCommands skyblockCommands = new SkyblockCommands(levelManager, islandManager, permissionsManager, this, messageLibs, islandDataManager, templatesFolder, templateDataManager);
        getCommand("sba").setExecutor(skyblockCommands);
        getCommand("skyblockadmin").setExecutor(skyblockCommands);

        this.getLogger().info(String.format("[Skyblock] - Enabled %s!", getDescription().getName()));

    }

    @Override
    public void onDisable() {
        System.out.println("Skyblock is disabled");
    }
}
