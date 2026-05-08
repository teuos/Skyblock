package net.teuos.skyblock.managers;


import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.properties.SlimeProperties;
import com.infernalsuite.asp.api.world.properties.SlimePropertyMap;
import com.sk89q.worldguard.WorldGuard;
import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.libs.CSVLibs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;


import java.io.File;
import java.io.IOException;

public class IslandManager {

    private final File csvFile;
    private final SlimeLoader loader;
    private final AdvancedSlimePaperAPI api;
    private final WorldGuard worldGuard;
    private final IslandPermissionsManager permissionsManager;
    private final CSVLibs csvLibs;
    private final IslandLevelManager islandLevelManager;
    private final Skyblock plugin;

    public IslandManager(File csvFile, SlimeLoader loader, WorldGuard worldGuard, IslandPermissionsManager permissionsManager, CSVLibs csvLibs, IslandLevelManager islandLevelManager, Skyblock plugin) {
        this.csvFile = csvFile;
        this.loader = loader;
        this.worldGuard = worldGuard;
        this.api = AdvancedSlimePaperAPI.instance();
        this.permissionsManager = permissionsManager;
        this.csvLibs = csvLibs;
        this.islandLevelManager = islandLevelManager;
        this.plugin = plugin;
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

            loadIsland(islandName);

            World world = Bukkit.getWorld(islandName);

            csvLibs.createRecord(islandName,plugin.getConfig().getInt("island.default-generator-level"),plugin.getConfig().getInt("island.default-generator-level"));

            permissionsManager.applyDefaultFlags(world, islandName);

            return true;


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean deleteIsland(String islandName) {


        try {

            World world = Bukkit.getWorld(islandName);

            if (world != null) {
                for (Player player : world.getPlayers()) {
                    player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                }

                try {
                    Bukkit.unloadWorld(islandName, false);
                } catch (Exception ignored) {
                }
            }

            loader.deleteWorld(islandName);

            csvLibs.deleteRecord(islandName);

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


    public void updateWorldBorder(String worldName) throws IOException {

        try {
            if (Bukkit.getWorld(worldName) != null) {
                World world = Bukkit.getWorld(worldName);
                world.getWorldBorder().setSize(islandLevelManager.getBorderSize(worldName));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean loadIsland(String worldName)throws IOException {
        try {
            if (Bukkit.getWorld(worldName) == null) {
                SlimeWorld slimeWorld = api.readWorld(loader, worldName, false, new SlimePropertyMap());
                api.loadWorld(slimeWorld, true);
                updateWorldBorder(worldName);
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

}
