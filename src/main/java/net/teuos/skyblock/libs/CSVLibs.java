package net.teuos.skyblock.libs;

import net.teuos.skyblock.Skyblock;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CSVLibs {


    private final File csvFile;
    private final Skyblock plugin;

    public CSVLibs(File csvFile, Skyblock plugin) {
        this.csvFile = csvFile;
        this.plugin = plugin;
    }

    public void createRecord(String islandName, int generatorLevel, int borderLevel) {

        try {
            List<String> lines = Files.readAllLines(this.csvFile.toPath(), StandardCharsets.UTF_8);
            List<String> updatedLines = new ArrayList<>(lines);
            updatedLines.add(islandName + "," + generatorLevel + "," + borderLevel);
            Files.write(csvFile.toPath(), updatedLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecord(String islandName) {
        try{
            List<String> lines = Files.readAllLines(this.csvFile.toPath(), StandardCharsets.UTF_8);
            List<String> updatedLines = new ArrayList<>();
            updatedLines.add(lines.get(0));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] split = line.split(",");
                String storedWorld = split[0];
                if (!storedWorld.equals(islandName)) {
                    updatedLines.add(line);
                }
            }
            Files.write(
                    csvFile.toPath(),
                    updatedLines,
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean IslandExists(String islandName){
        try {
            List<String> lines = Files.readAllLines(this.csvFile.toPath(), StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] split = line.split(",");
                String storedWorld = split[0];
                if (storedWorld.equals(islandName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public int getBorderLevel(String worldName) throws IOException {
        List<String> lines = Files.readAllLines(this.csvFile.toPath(), StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] split = line.split(",");
            String storedWorld = split[0];
            int level = Integer.parseInt(split[2]);
            if (storedWorld.equalsIgnoreCase(worldName)) {
                return level;
            }
        }
        return 0;
    }


    public int updateLevel(String islandName, int level, String type) throws IOException {
        List<String> lines = Files.readAllLines(this.csvFile.toPath(), StandardCharsets.UTF_8);
        List<String> updatedLines = new ArrayList<>();

        int pos = -1;
        String line = lines.getFirst();
        String[] split = line.split(",");
        for (int i = 0 ; i < split.length; i++) {
            if (split[i].equals(type)) {
                pos = i;
            }
        }

        updatedLines.add(lines.get(0));

        if (pos != -1) {
            for (int i = 1; i < lines.size(); i++) {
                line = lines.get(i);
                split = line.split(",");
                String storedWorld = split[0];
                if (storedWorld.equals(islandName)) {
                    split[pos] = String.valueOf(level);
                    updatedLines.add(split[0] + "," + split[1] + "," + split[2]);
                } else {
                    updatedLines.add(line);
                }
            }
            Files.write(csvFile.toPath(), updatedLines);
        } else {
            return -1;
        }
        return level;
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
