package net.teuos.skyblock.managers;

import net.teuos.skyblock.libs.CSVInteract;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class IslandLevelManager {


    private final File csvFile;
    private final CSVInteract csvInteract;

    public IslandLevelManager(File csvFile, CSVInteract csvInteract) {
        this.csvFile = csvFile;
        this.csvInteract = csvInteract;
    }

    public int increaseGenLevel(String worldName)throws IOException{

        List<String> lines = Files.readAllLines(this.csvFile.toPath(), StandardCharsets.UTF_8);
        List<String> updatedLines = new ArrayList<>();

        updatedLines.add(lines.get(0));

        boolean worldFound = false;
        int newLevel = 1;

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] split = line.split(",");

            String storedWorld = split[0];
            int level = Integer.parseInt(split[1]);
            int borderLevel = Integer.parseInt(split[2]);
            if (storedWorld.equalsIgnoreCase(worldName)) {
                level++;
                newLevel = level;
                updatedLines.add(storedWorld + "," + Integer.toString(level) + "," + borderLevel);
                worldFound = true;

            } else {
                updatedLines.add(line);
            }

        }

        if (!worldFound) {
            updatedLines.add(worldName + ",1,30");
        }

        Files.write(csvFile.toPath(), updatedLines);
        return newLevel;

    }



    public int increaseBorderLevel(String worldName) throws IOException{
        List<String> lines = Files.readAllLines(this.csvFile.toPath(), StandardCharsets.UTF_8);
        List<String> updatedLines = new ArrayList<>();

        updatedLines.add(lines.get(0));

        int defaultSize = 30; // Replace this with a config option
        int increaseAmount = 20; // Replace this with a config option
        boolean worldFound = false;
        int newLevel = defaultSize + increaseAmount;
        int borderSize;

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] split = line.split(",");

            String storedWorld = split[0];
            int genLevel = Integer.parseInt(split[1]);
            int borderLevel = Integer.parseInt(split[2]);
            if (storedWorld.equalsIgnoreCase(worldName)) {
                borderLevel++;
                borderSize = defaultSize + (increaseAmount * borderLevel);
                newLevel = borderLevel;
                updatedLines.add(storedWorld + "," + genLevel + "," + Integer.toString(borderLevel));
                worldFound = true;

                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    world.getWorldBorder().setSize(borderSize);
                }

            } else {
                updatedLines.add(line);
            }

        }

        if (!worldFound) {
            updatedLines.add(worldName + ",0," + Integer.toString(defaultSize));
        }

        Files.write(csvFile.toPath(), updatedLines);

        return newLevel;

    }


    public double getBorderSize(String worldName) throws IOException{
        World world = Bukkit.getWorld(worldName);
        int defaultSize = 30; // Replace this with a config option
        int increaseAmount = 20; // Replace this with a config option
        if (world != null) {
            int borderLevel = csvInteract.getBorderLevel(worldName);
            return defaultSize + (increaseAmount * borderLevel);
        }
        return defaultSize;
    }




}
