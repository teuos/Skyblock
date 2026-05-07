package net.teuos.skyblock;

import net.teuos.skyblock.commands.Island;
import net.teuos.skyblock.managers.IslandLevelManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public final class Skyblock extends JavaPlugin {

    private File islandLevelFile;

    @Override
    public void onEnable() {
        // Plugin startup logic


        System.out.println("Skyblock is enabled");


        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        islandLevelFile = new File(getDataFolder(), "island_levels.csv");

        IslandLevelManager levelManager = new IslandLevelManager(islandLevelFile);


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
        getServer().getPluginManager().registerEvents(new CobbleGen(levelManager), this);

        getCommand("island").setExecutor(new Island(levelManager));


    }

    @Override
    public void onDisable() {
        System.out.println("Skyblock is disabled");
    }
}
