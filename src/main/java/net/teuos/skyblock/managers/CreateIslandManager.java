package net.teuos.skyblock.managers;


import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.SlimeWorldInstance;
import com.infernalsuite.asp.api.world.properties.SlimeProperties;
import com.infernalsuite.asp.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;


import java.io.File;
import java.io.IOException;

public class CreateIslandManager {

    private final File csvFile;
    private final SlimeLoader loader;
    private final AdvancedSlimePaperAPI api;



    public CreateIslandManager(File csvFile, SlimeLoader loader) {
        this.csvFile = csvFile;
        this.loader = loader;
        this.api = AdvancedSlimePaperAPI.instance();
    }

    public boolean createIsland(String islandName) {

        try {

            SlimePropertyMap props = new SlimePropertyMap();

            props.setValue(SlimeProperties.SPAWN_X, 0);
            props.setValue(SlimeProperties.SPAWN_Y, 60);
            props.setValue(SlimeProperties.SPAWN_Z, 0);

            SlimeWorld template = api.readWorld(
                    loader,
                    "skyblock_template",
                    false,
                    props
            );

            SlimeWorld island = template.clone(islandName, loader);

            api.saveWorld(island);

            api.loadWorld(island, true);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean deleteIsland(String islandName) {


        try {
            try {
                Bukkit.unloadWorld(islandName, false);
            } catch (Exception ignored) {
            }

            loader.deleteWorld(islandName);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createTemplate(org.bukkit.entity.Player player){
        try {

            SlimePropertyMap props = new SlimePropertyMap();

            props.setValue(SlimeProperties.SPAWN_X, 0);
            props.setValue(SlimeProperties.SPAWN_Y, 60);
            props.setValue(SlimeProperties.SPAWN_Z, 0);

            SlimeWorld template = api.createEmptyWorld(
                    "skyblock_template",
                    false,
                    props,
                    loader
            );

            api.saveWorld(template);

            api.loadWorld(template, true);

            player.sendMessage(ChatColor.GREEN + "Created Island Template");

        }
        catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Failed to create template.");
            e.printStackTrace();
        }
    }

    public boolean teleportIsland(String worldName)throws IOException {
        try {
            if (Bukkit.getWorld(worldName) == null) {
                SlimeWorld world = api.readWorld(loader, worldName, false, new SlimePropertyMap());
                api.loadWorld(world, true);
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

}
