package net.teuos.skyblock.managers;


import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.world.properties.SlimePropertyMap;

import java.io.File;

public class CreateIslandManager {

    private final File csvFile;

    AdvancedSlimePaperAPI api = AdvancedSlimePaperAPI.instance();

    SlimePropertyMap properties = new SlimePropertyMap();


    public CreateIslandManager(File csvFile) {
        this.csvFile = csvFile;
    }

    public void createIsland(String islandName) {




    }

}
