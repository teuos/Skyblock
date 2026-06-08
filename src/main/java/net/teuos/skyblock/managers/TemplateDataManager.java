package net.teuos.skyblock.managers;

import net.teuos.skyblock.Skyblock;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

public class TemplateDataManager {

    private final Skyblock plugin;
    private final File templateFile;
    private FileConfiguration templatesConfig;

    public TemplateDataManager(Skyblock plugin) {

        this.plugin = plugin;
        templateFile = new File(plugin.getDataFolder(), "templates.yml");

        if (!templateFile.exists()) {
            try {
                templateFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create templates.yml", e);
            }
        }

        templatesConfig = YamlConfiguration.loadConfiguration(templateFile);

    }

    public void reload() {
        templatesConfig = YamlConfiguration.loadConfiguration(templateFile);
    }

    public void save() {
        try {
            templatesConfig.save(templateFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save templates.yml", e);
        }
    }

    public void createRecord(String templateName, Material itemType){
        String path = "templates." + templateName;
        templatesConfig.set(path + ".guiItem", itemType.name());
        save();
    }

    public void deleteRecord(String templateName){
        templatesConfig.set("templates." + templateName, null);
        save();
    }

    public Material getItemType(String templateName){
        String path = "templates." + templateName;

        String materialName = templatesConfig.getString(path + ".guiItem");

        if (materialName==null) {
            return Material.GRASS_BLOCK;
        }

        Material material = Material.getMaterial(materialName);

        return material != null ? material : Material.GRASS_BLOCK;
    }

    public int getSize(){
        if (templatesConfig.getConfigurationSection("templates") == null) {
            return 0;
        }

        Set<String> keys = templatesConfig
                .getConfigurationSection("templates")
                .getKeys(false);

        return keys.size();
    }

    public Configuration getConfig(){
        return templatesConfig;
    }


}
