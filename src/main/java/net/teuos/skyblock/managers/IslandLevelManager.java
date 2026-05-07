package net.teuos.skyblock.managers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class IslandLevelManager {


    private final File csvFile;

    public IslandLevelManager(File csvFile) {
        this.csvFile = csvFile;
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

            if (storedWorld.equalsIgnoreCase(worldName)) {
                level++;
                newLevel = level;
                updatedLines.add(storedWorld + "," + Integer.toString(level));
                worldFound = true;

            } else {
                updatedLines.add(line);
            }

        }

        if (!worldFound) {
            updatedLines.add(worldName + ",1");
        }

        Files.write(csvFile.toPath(), updatedLines);
        return newLevel;

    }

    public int getGenLevel(String worldName)throws IOException{
        List<String> lines = Files.readAllLines(this.csvFile.toPath(), StandardCharsets.UTF_8);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] split = line.split(",");
            String storedWorld = split[0];
            int level = Integer.parseInt(split[1]);
            if (storedWorld.equalsIgnoreCase(worldName)) {
                return level;
            }
        }
        return 0;
    }

}
