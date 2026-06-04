package net.teuos.skyblock.managers;


import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.properties.SlimeProperties;
import com.infernalsuite.asp.api.world.properties.SlimePropertyMap;
import net.teuos.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;


import java.io.IOException;
import java.util.logging.Level;

public class IslandManager {

    private final SlimeLoader loader;
    private final SlimeLoader templateLoader;
    private final AdvancedSlimePaperAPI slimeApi;
    private final IslandPermissionsManager permissionsManager;
    private final IslandDataManager islandDataManager;
    private final IslandLevelManager islandLevelManager;
    private final Skyblock plugin;

    public IslandManager(SlimeLoader loader, SlimeLoader templateLoader, IslandPermissionsManager permissionsManager, IslandDataManager islandDataManager, IslandLevelManager islandLevelManager, Skyblock plugin) {
        this.loader = loader;
        this.templateLoader = templateLoader;
        this.slimeApi = AdvancedSlimePaperAPI.instance();
        this.permissionsManager = permissionsManager;
        this.islandDataManager = islandDataManager;
        this.islandLevelManager = islandLevelManager;
        this.plugin = plugin;
    }

    public boolean createIsland(String islandName, String templateName) {

        try {

            SlimePropertyMap props = new SlimePropertyMap();

            props.setValue(SlimeProperties.SPAWN_X, 0);
            props.setValue(SlimeProperties.SPAWN_Y, 60);
            props.setValue(SlimeProperties.SPAWN_Z, 0);

            SlimeWorld template = slimeApi.readWorld(
                    templateLoader,
                    templateName,
                    false,
                    props
            );

            SlimeWorld island = template.clone(islandName, loader);

            slimeApi.saveWorld(island);

            loadIsland(islandName);

            World world = Bukkit.getWorld(islandName);

            islandDataManager.createRecord(islandName);

            permissionsManager.applyDefaultFlags(world, islandName);

            return true;


        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create island " + islandName, e);
            return false;
        }

    }

    public boolean deleteIsland(String islandName) {


        try {

            World world = Bukkit.getWorld(islandName);

            if (world != null) {
                for (Player player : world.getPlayers()) {
                    player.teleport(Bukkit.getWorld(plugin.getConfig().getString("spawn.spawn-world-name", "world")).getSpawnLocation());
                }

                try {
                    Bukkit.unloadWorld(islandName, false);
                } catch (Exception ignored) {
                    plugin.getLogger().log(Level.WARNING, "Failed to unload island " + islandName);
                }
            }

            loader.deleteWorld(islandName);

            islandDataManager.deleteRecord(islandName);

            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to delete island " + islandName, e);
            return false;
        }
    }

    public boolean islandExists(String islandName) {
        try {
            return loader.worldExists(islandName);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to check island " + islandName, e);
            return false;
        }
    }


    public boolean templateExists(String templateName) {
        try {
            return templateLoader.worldExists(templateName);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to check template " + templateName, e);
            return false;
        }
    }


    public void createTemplate(org.bukkit.entity.Player player, String templateName){

        if (islandExists(templateName)) {
            player.sendMessage(ChatColor.RED + "An island named " + templateName + " already exists!");
            return;
        }

        try {

            SlimePropertyMap props = new SlimePropertyMap();

            props.setValue(SlimeProperties.SPAWN_X, 0);
            props.setValue(SlimeProperties.SPAWN_Y, 60);
            props.setValue(SlimeProperties.SPAWN_Z, 0);

            SlimeWorld template = slimeApi.createEmptyWorld(
                    templateName,
                    false,
                    props,
                    templateLoader
            );

            slimeApi.saveWorld(template);

            loadTemplate(templateName);

            permissionsManager.applyTemplateFlags(Bukkit.getWorld(templateName));

            player.sendMessage(ChatColor.GREEN + "Created Island Template");

        }
        catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Failed to create template.");
            plugin.getLogger().log(Level.SEVERE, "Failed to create template", e);
        }
    }

    public int deleteTemplate(String templateName) {

        if (!templateExists(templateName)) {
            return 1;
        }

        try {
            World world = Bukkit.getWorld(templateName);

            if (world != null) {
                for (Player player : world.getPlayers()) {
                    player.teleport(Bukkit.getWorld(plugin.getConfig().getString("spawn.spawn-world-name", "world")).getSpawnLocation());
                }

                if (!Bukkit.unloadWorld(world, false)) {
                    return 2;
                }
            }

            templateLoader.deleteWorld(templateName);
            return 0;

        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to delete template", e);
            return 2;
        }


    }

    public void updateWorldBorder(String worldName) throws IOException {

        try {
            if (Bukkit.getWorld(worldName) != null) {
                World world = Bukkit.getWorld(worldName);
                world.getWorldBorder().setSize(islandLevelManager.getBorderSize(worldName));
            }
        } catch (Exception e){
            plugin.getLogger().log(Level.SEVERE, "Failed to update world border", e);
        }

    }


    public void unloadIsland(String islandName) {
        Bukkit.unloadWorld(islandName, true);
    }


    public void startActivityChecker() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            long now = System.currentTimeMillis();

            for (Player player : Bukkit.getOnlinePlayers()) {

                String worldName = player.getWorld().getName();

                if (islandDataManager.islandExists(worldName)) {

                    islandDataManager.updateLastActive(
                            worldName,
                            now
                    );
                }
            }

        }, 20L * 60L, 20L * 60L);
    }


    public void startIslandUnloadTask() {

        if (plugin.getConfig().getLong("island-unload-task") < 0) {
            return;
        }

        startActivityChecker();

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            long now = System.currentTimeMillis();

            for (String islandID : islandDataManager.getAllIslands()) {

                World world = Bukkit.getWorld(islandID);

                if (world == null) {
                    continue;
                }

                if (!world.getPlayers().isEmpty()) {
                    islandDataManager.updateLastActive(
                            islandID,
                            now
                    );
                    continue;
                }

                long lastActive = islandDataManager.getLastActive(islandID);

                long inactiveTime = System.currentTimeMillis() - lastActive;

                long timer = plugin.getConfig().getLong("islands.unload-delay") * 1000;

                if (inactiveTime >= timer) {

                    unloadIsland(islandID);

                    plugin.getLogger().info(
                            "Unloaded inactive island" + islandID
                    );

                }
            }
        }, 20L * 60L, 20L * 60);
    }


    public boolean loadIsland(String worldName)throws IOException {
        try {
            if (Bukkit.getWorld(worldName) == null) {
                SlimeWorld slimeWorld = slimeApi.readWorld(loader, worldName, false, new SlimePropertyMap());
                slimeApi.loadWorld(slimeWorld, true);
                updateWorldBorder(worldName);
            }
            return true;
        } catch (Exception e){
            plugin.getLogger().log(Level.SEVERE, "Failed to load island " + worldName, e);
            return false;
        }

    }

    public boolean loadTemplate(String worldName) throws IOException {
        try {
            if (Bukkit.getWorld(worldName) == null) {
                SlimeWorld slimeWorld = slimeApi.readWorld(templateLoader, worldName, false, new SlimePropertyMap());
                slimeApi.loadWorld(slimeWorld, true);
                World world = Bukkit.getWorld(worldName);
                world.getWorldBorder().setSize(plugin.getConfig().getInt("island.default-border-size", 30));
            }
            return true;
        } catch (Exception e){
            plugin.getLogger().log(Level.SEVERE, "Failed to load template " + worldName, e);
            return false;
        }

    }

}
