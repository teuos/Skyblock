package net.teuos.skyblock;

import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.loaders.file.FileLoader;
import net.teuos.skyblock.commands.IslandCommands;
import net.teuos.skyblock.commands.SkyblockCommands;
import net.teuos.skyblock.managers.CreateIslandManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public final class Skyblock extends JavaPlugin {

    private SlimeLoader worldLoader;
    private final AdvancedSlimePaperAPI api = AdvancedSlimePaperAPI.instance();

    private File islandLevelFile;

    @Override
    public void onEnable() {
        // Plugin startup logic

        File islandsFolder = new File(getDataFolder(), "islands");


        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        if(!islandsFolder.exists()){
            islandsFolder.mkdir();
        }

        worldLoader = new FileLoader(islandsFolder);

        islandLevelFile = new File(getDataFolder(), "island_levels.csv");

        IslandLevelManager levelManager = new IslandLevelManager(islandLevelFile);

        CreateIslandManager islandManager = new CreateIslandManager(islandLevelFile, worldLoader);


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
        getServer().getPluginManager().registerEvents(new CobbleGen(levelManager), this);

        getCommand("island").setExecutor(new IslandCommands(levelManager, islandManager));
        getCommand("sb").setExecutor(new SkyblockCommands(levelManager, islandManager));
        getCommand("skyblock").setExecutor(new SkyblockCommands(levelManager, islandManager));


    }

    @Override
    public void onDisable() {
        System.out.println("Skyblock is disabled");
    }
}
