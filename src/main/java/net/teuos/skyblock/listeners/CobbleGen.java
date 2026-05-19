package net.teuos.skyblock.listeners;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.io.IOException;

public class CobbleGen implements Listener {

    private final IslandLevelManager levelManager;
    private final IslandDataManager islandDataManager;
    private final Skyblock plugin;

    public enum GeneratorTier {
        COAL("generate-coal-level"),
        COPPER("generate-copper-level"),
        IRON("generate-iron-level"),
        GOLD("generate-gold-level"),
        REDSTONE("generate-redstone-level"),
        LAPIS("generate-lapis-level"),
        EMERALD("generate-emerald-level"),
        DIAMOND("generate-diamond-level"),
        NETHERITE("generate-ancient-debris-level");

        private final String path;

        private int requiredLevel;

        GeneratorTier(String path) {
            this.path = path;
        }

        public static void loadAll(FileConfiguration config) {

            for (GeneratorTier tier : GeneratorTier.values()) {
                tier.requiredLevel = config.getInt("island."+tier.path, -1);
            }
        }

        public int getRequiredLevel() {
            return requiredLevel;
        }

        private boolean isEnabled(){
            return requiredLevel != -1;
        }

        public static GeneratorTier getGeneratorTier(int level) {

            GeneratorTier current = null;
            for (GeneratorTier tier : GeneratorTier.values()) {
                if (!tier.isEnabled()){
                    break;
                }

                if (level >= tier.getRequiredLevel()) {
                    current = tier;
                } else {
                    break;
                }
            }

            return current;
        }
    }




    public CobbleGen(IslandLevelManager levelManager, IslandDataManager islandDataManager, Skyblock plugin) {
        this.levelManager = levelManager;
        this.islandDataManager = islandDataManager;
        this.plugin = plugin;
        GeneratorTier.loadAll(plugin.getConfig());

    }

    public int randInt(){
        return (int)(Math.random()*101);
    }

    public boolean tryGenerate(BlockFormEvent event, Material material, String ore) {
        if (randInt() <= plugin.getConfig().getInt("island.generate-"+ore+"-percentage")){
            event.getNewState().setType(material);
            return true;
        }
        return false;
    }


    public void generateStone(BlockFormEvent event) {

        int genLevel = islandDataManager.getGenLevel(event.getNewState().getLocation().getWorld().getName());
        int deepslateLevel = plugin.getConfig().getInt("island.generate-deepslate-level");

        if (genLevel >= deepslateLevel && deepslateLevel >= 0) {
            if (randInt() <= plugin.getConfig().getInt("island.deepslate-generation-percentage", 30)) {
                if (plugin.getConfig().getBoolean("island.generate-stone", true)) {
                    event.getNewState().setType(Material.DEEPSLATE);
                } else {
                    event.getNewState().setType(Material.COBBLED_DEEPSLATE);
                }

            } else {
                if (plugin.getConfig().getBoolean("island.generate-stone", true)) {
                    event.getNewState().setType(Material.STONE);
                } else {
                    event.getNewState().setType(Material.COBBLESTONE);
                }
            }
        } else {
            if (plugin.getConfig().getBoolean("island.generate-stone", true)) {
                event.getNewState().setType(Material.STONE);
            } else {
                event.getNewState().setType(Material.COBBLESTONE);
            }
        }

    }

    @EventHandler
    public void onBlockFormEvent(BlockFormEvent event) {
        if (event.getNewState().getType() == Material.COBBLESTONE && islandDataManager.islandExists(event.getBlock().getWorld().getName())) {

            int genLevel = islandDataManager.getGenLevel(event.getNewState().getLocation().getWorld().getName());

            GeneratorTier islandTier = GeneratorTier.getGeneratorTier(genLevel);

            if (islandTier == null) {
                generateStone(event);
                return;
            }


            switch (islandTier) {

                case NETHERITE:
                    if (tryGenerate(event, Material.ANCIENT_DEBRIS, "ancient-debris")) return;

                case DIAMOND:
                    if (tryGenerate(event, Material.DIAMOND_ORE, "diamond")) return;

                case EMERALD:
                    if (tryGenerate(event, Material.EMERALD_ORE, "emerald")) return;

                case LAPIS:
                    if (tryGenerate(event, Material.LAPIS_ORE, "lapis")) return;

                case REDSTONE:
                    if (tryGenerate(event, Material.REDSTONE_ORE, "redstone")) return;

                case GOLD:
                    if (tryGenerate(event, Material.GOLD_ORE, "gold")) return;

                case IRON:
                    if (tryGenerate(event, Material.IRON_ORE, "iron")) return;

                case COPPER:
                    if (tryGenerate(event, Material.COPPER_ORE, "copper")) return;

                case COAL:
                    if (tryGenerate(event, Material.COAL_ORE, "coal")) return;
                    break;
            }

            generateStone(event);

        }

    }
}


