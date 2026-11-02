package net.teuos.skyblock.managers;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.objects.RegionType;
import net.teuos.skyblock.objects.ResourceRegion;
import org.apache.maven.model.Resource;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class RegionDataManager {


    private final Skyblock plugin;
    private final File regionFile;
    private FileConfiguration regionsConfig;
    private final Map<String, ResourceRegion> regions = new HashMap<>();

    public RegionDataManager(Skyblock plugin) {

        this.plugin = plugin;
        regionFile = new File(plugin.getDataFolder(), "regions.yml");

        if (!regionFile.exists()) {
            try {
                regionFile.createNewFile();
            } catch (IOException e){
                plugin.getLogger().log(Level.SEVERE, "Could not create region file", e);
            }
        }

        reload();

    }

    public void reload() {
        regionsConfig = YamlConfiguration.loadConfiguration(regionFile);
        regions.clear();

        ConfigurationSection section = regionsConfig.getConfigurationSection("regions");
        if (section == null) {
            return;
        }

        for (String name : section.getKeys(false)) {
            String path = "regions." + name;

            RegionType type;
            Material subtype;
            try {
                type = RegionType.valueOf(regionsConfig.getString(path + ".type", ""));
                subtype = Material.valueOf(regionsConfig.getString(path + ".subtype", ""));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Skipping region " + name + ": invalid type or subtype");
                continue;
            }

            regions.put(name, new ResourceRegion(name, type, subtype,
                    regionsConfig.getString(path + ".world"),
                    regionsConfig.getInt(path + ".min.x"),
                    regionsConfig.getInt(path + ".min.y"),
                    regionsConfig.getInt(path + ".min.z"),
                    regionsConfig.getInt(path + ".max.x"),
                    regionsConfig.getInt(path + ".max.y"),
                    regionsConfig.getInt(path + ".max.z")));
        }

    }

    public void save() {
        try {
            regionsConfig.save(regionFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save regions.yml", e);
        }
    }

    public void createRecord(ResourceRegion region) {
        String path = "regions." + region.getName();
        regionsConfig.set(path + ".type", region.getType().name());
        regionsConfig.set(path + ".subtype", region.getSubtype().name());
        regionsConfig.set(path + ".world", region.getWorld());
        regionsConfig.set(path + ".min.x", region.getMinX());
        regionsConfig.set(path + ".min.y", region.getMinY());
        regionsConfig.set(path + ".min.z", region.getMinZ());
        regionsConfig.set(path + ".max.x", region.getMaxX());
        regionsConfig.set(path + ".max.y", region.getMaxY());
        regionsConfig.set(path + ".max.z", region.getMaxZ());
        save();
        regions.put(region.getName(), region);
    }

    public void deleteRecord(String name) {
        regionsConfig.set("regions." + name, null);
        save();
        regions.remove(name);
    }

    public ResourceRegion getRegion(String name) {
        return regions.get(name);
    }

    public boolean exists(String name) {
        return regions.containsKey(name);
    }

    public ResourceRegion getRegionAt(Location location) {
        for (ResourceRegion region : regions.values()) {
            if (region.contains(location)) {
                return region;
            }
        }
        return null;
    }

    public Collection<ResourceRegion> getRegions() {
        return regions.values();
    }

}
