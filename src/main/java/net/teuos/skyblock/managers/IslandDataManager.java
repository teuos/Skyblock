package net.teuos.skyblock.managers;

import net.teuos.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IslandDataManager {

    private final Skyblock plugin;
    private final File islandsFile;
    private FileConfiguration islandsConfig;

    public IslandDataManager(Skyblock plugin) {
        this.plugin = plugin;

        islandsFile = new File(plugin.getDataFolder(), "islands.yml");

        if (!islandsFile.exists()) {
            try {
                islandsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        islandsConfig = YamlConfiguration.loadConfiguration(islandsFile);

    }

    public void reload() {
        islandsConfig = YamlConfiguration.loadConfiguration(islandsFile);
    }

    public void save() {
        try {
            islandsConfig.save(islandsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createRecord(String islandName) {

        String path = "islands." + islandName;

        islandsConfig.set(path + ".last-active", 0);
        islandsConfig.set(path + ".levels.generator-level", plugin.getConfig().getInt("levels.generator-level"));
        islandsConfig.set(path + ".levels.border-level", plugin.getConfig().getInt("levels.border-level"));
        islandsConfig.set(path + ".blocked-players", plugin.getConfig().getString("blocked-players"));

        save();

    }


    public void deleteRecord(String islandName) {
        islandsConfig.set("islands." + islandName, null);
        save();
    }

    public boolean islandExists(String islandName) {

        return islandsConfig.contains("islands." + islandName);
    }

    public int getBorderLevel(String islandName) {

        return islandsConfig.getInt(
                "islands." + islandName + ".levels.border-level",
                0
        );
    }

    public int getGenLevel(String islandName) {

        return islandsConfig.getInt(
                "islands." + islandName + ".levels.generator-level",
                0
        );
    }

    public long getLastActive(String islandName) {

        return islandsConfig.getLong(
                "islands." + islandName + ".last-active",
                0
        );
    }

    public List<String> getBlockedPlayers(String islandName) {
        return islandsConfig.getStringList("islands." + islandName + ".blocked-players");
    }

    public boolean getBlockedStatus(String islandName, Player player) {
        List<String> blockedPlayers = getBlockedPlayers(islandName);
        return blockedPlayers.contains(player.getUniqueId().toString());

    }


    public void addBlockedPlayer(String islandName, Player player) {
        List<String> blockedPlayers = getBlockedPlayers(islandName);
        if (!blockedPlayers.contains(player.getUniqueId().toString())) {
            blockedPlayers.add(player.getUniqueId().toString());
        }
        islandsConfig.set(
                "islands." + islandName + ".blocked-players", blockedPlayers
        );
        save();
    }

    public void removeBlockedPlayer(String islandName, Player player) {
        List<String> blockedPlayers = getBlockedPlayers(islandName);
        blockedPlayers.remove(player.getUniqueId().toString());
        islandsConfig.set(
                "islands." + islandName + ".blocked-players", blockedPlayers
        );
        save();
    }

    public void updateBorderLevel(String islandName, int level) {

        islandsConfig.set(
                "islands." + islandName + ".levels.border-level",
                level
        );

        save();
    }

    public void updateGeneratorLevel(String islandName, int level) {

        islandsConfig.set(
                "islands." + islandName + ".levels.generator-level",
                level
        );

        save();
    }

    public void updateLastActive(String islandName, long time) {

        islandsConfig.set(
                "islands." + islandName + ".last-active",
                time
        );

        save();
    }


    public List<String> getAllIslands() {

        if (!islandsConfig.contains("islands")) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                islandsConfig.getConfigurationSection("islands")
                        .getKeys(false)
        );
    }
}
