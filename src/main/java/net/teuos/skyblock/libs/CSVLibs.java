package net.teuos.skyblock.libs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CSVInteract {


    private final File csvFile;

    public CSVInteract(File csvFile) {
        this.csvFile = csvFile;

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
