package net.teuos.skyblock.managers;

import net.teuos.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;

public class IslandLevelManager {



    private final IslandDataManager islandDataManager;
    private final Skyblock plugin;

    public IslandLevelManager(IslandDataManager islandDataManager, Skyblock plugin) {
        this.islandDataManager = islandDataManager;
        this.plugin = plugin;
    }

    public int increaseGenLevel(String worldName)throws IOException{

        if (islandDataManager.islandExists(worldName)) {
            islandDataManager.updateGeneratorLevel(worldName, islandDataManager.getGenLevel(worldName) + 1);
            return islandDataManager.getGenLevel(worldName);
        } else {
            return -1;
        }
    }

    public int increaseBorderLevel(String worldName) throws IOException{

        if (islandDataManager.islandExists(worldName)) {
            islandDataManager.updateBorderLevel(worldName, islandDataManager.getBorderLevel(worldName) + 1);
            return islandDataManager.getBorderLevel(worldName);
        } else {
            return -1;
        }

    }

    public double getBorderSize(String worldName) throws IOException{
        World world = Bukkit.getWorld(worldName);
        int defaultSize = plugin.getConfig().getInt("island.default-border-size");
        int increaseAmount = plugin.getConfig().getInt("island.increase-border-amount");
        if (world != null) {
            int borderLevel = islandDataManager.getBorderLevel(worldName);
            return defaultSize + (increaseAmount * borderLevel);
        }
        return defaultSize;
    }




}
