package net.teuos.skyblock.managers;


import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.exceptions.CorruptedWorldException;
import com.infernalsuite.asp.api.exceptions.NewerFormatException;
import com.infernalsuite.asp.api.exceptions.UnknownWorldException;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.properties.SlimeProperties;
import com.infernalsuite.asp.api.world.properties.SlimePropertyMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.objects.SpawnPoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    private final TemplateDataManager templateDataManager;

    public IslandManager(SlimeLoader loader, SlimeLoader templateLoader, IslandPermissionsManager permissionsManager, IslandDataManager islandDataManager, TemplateDataManager templateDataManager, IslandLevelManager islandLevelManager, Skyblock plugin) {
        this.loader = loader;
        this.templateLoader = templateLoader;
        this.slimeApi = AdvancedSlimePaperAPI.instance();
        this.permissionsManager = permissionsManager;
        this.islandDataManager = islandDataManager;
        this.islandLevelManager = islandLevelManager;
        this.plugin = plugin;
        this.templateDataManager = templateDataManager;
    }

    public boolean createIsland(String islandName, String templateName) {

        try {

            SlimePropertyMap props = new SlimePropertyMap();

            props.setValue(SlimeProperties.SPAWN_X, (int) templateDataManager.getSpawnPoint(templateName).x());
            props.setValue(SlimeProperties.SPAWN_Y, (int) templateDataManager.getSpawnPoint(templateName).y());
            props.setValue(SlimeProperties.SPAWN_Z, (int) templateDataManager.getSpawnPoint(templateName).z());

            SlimeWorld template = slimeApi.readWorld(
                    templateLoader,
                    templateName,
                    false,
                    props
            );

            SlimeWorld island = template.clone(islandName, loader);

            slimeApi.saveWorld(island);

            loadIsland(islandName);

            islandDataManager.createRecord(islandName);

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


    public void createTemplate(Player player, String templateName){

        if (islandExists(templateName)) {
            player.sendMessage(ChatColor.RED + "An island named " + templateName + " already exists!");
            return;
        }

        Material heldItem = player.getInventory().getItemInMainHand().getType();

        if (heldItem.isAir()){
            player.sendMessage(Component.text("Please hold an item to be used as the GUI icon!", NamedTextColor.RED));
            return;
        }

        try {
            templateDataManager.createRecord(templateName, heldItem);

            SlimePropertyMap props = new SlimePropertyMap();

            SpawnPoint spawn = templateDataManager.getSpawnPoint(templateName);

            props.setValue(SlimeProperties.SPAWN_X, ((int) spawn.x()));
            props.setValue(SlimeProperties.SPAWN_Y, ((int) spawn.y()));
            props.setValue(SlimeProperties.SPAWN_Z, ((int) spawn.z()));

            SlimeWorld template = slimeApi.createEmptyWorld(
                    templateName,
                    false,
                    props,
                    templateLoader
            );

            slimeApi.saveWorld(template);

            loadTemplate(templateName);

            World world = Bukkit.getWorld(templateName);

            if (world != null) {
                permissionsManager.applyTemplateFlags(world);
            }

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

            templateDataManager.deleteRecord(templateName);

            templateLoader.deleteWorld(templateName);
            return 0;

        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to delete template", e);
            return 2;
        }


    }

    public void updateTemplateSpawn(String templateName, SpawnPoint location){

        if (location == null || !templateExists(templateName)) {
            return;
        }

        templateDataManager.setSpawnPoint(templateName, location);
        try {
            SlimeWorld slimeWorld = slimeApi.readWorld(templateLoader, templateName, false, new SlimePropertyMap());
            slimeWorld.getPropertyMap().setValue(SlimeProperties.SPAWN_X, ((int) location.x()));
            slimeWorld.getPropertyMap().setValue(SlimeProperties.SPAWN_Y, ((int) location.y()));
            slimeWorld.getPropertyMap().setValue(SlimeProperties.SPAWN_Z, ((int) location.z()));
            World world = Bukkit.getWorld(templateName);
            if (world != null) {
                world.setSpawnLocation((int) location.x(), (int) location.y(), (int) location.z());
            }
            slimeApi.saveWorld(slimeWorld);
        } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException e) {
            throw new RuntimeException(e);
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

    public int teleportIsland(String islandName, Player player) {
        if (!islandDataManager.islandExists(islandName)){
            return 1;
        }

        try {
            this.loadIsland(islandName);
            World target = Bukkit.getWorld(islandName);
            player.teleport(target.getSpawnLocation());
            return 0;
        } catch (IOException e) {
            return 2;
        }
    }

}
