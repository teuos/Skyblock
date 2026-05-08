package net.teuos.skyblock.managers;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.libs.CSVLibs;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class IslandLevelManager {



    private final CSVLibs csvLibs;
    private final Skyblock plugin;

    public IslandLevelManager(CSVLibs csvLibs, Skyblock plugin) {
        this.csvLibs = csvLibs;
        this.plugin = plugin;
    }

    public int increaseGenLevel(String worldName)throws IOException{

        if (csvLibs.IslandExists(worldName)) {
            return csvLibs.updateLevel(worldName, csvLibs.getGenLevel(worldName) + 1, "cobblegen");
        } else {
            return -1;
        }
    }

    public int increaseBorderLevel(String worldName) throws IOException{

        if (csvLibs.IslandExists(worldName)) {
            return csvLibs.updateLevel(worldName, csvLibs.getBorderLevel(worldName) + 1, "border");
        } else {
            return -1;
        }

    }

    public double getBorderSize(String worldName) throws IOException{
        World world = Bukkit.getWorld(worldName);
        int defaultSize = plugin.getConfig().getInt("island.default-border-size");
        int increaseAmount = plugin.getConfig().getInt("island.increase-border-amount");
        if (world != null) {
            int borderLevel = csvLibs.getBorderLevel(worldName);
            return defaultSize + (increaseAmount * borderLevel);
        }
        return defaultSize;
    }




}
