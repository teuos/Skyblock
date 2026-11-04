package net.teuos.skyblock.listeners;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.managers.RegionDataManager;
import net.teuos.skyblock.objects.RegionType;
import net.teuos.skyblock.objects.ResourceRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class SpawnOresListener implements Listener {


    private final RegionDataManager regionDataManager;
    private final Skyblock plugin;

    // The currently scheduled regen step for each block that is regenerating
    private final Map<Location, BukkitTask> regenTasks = new HashMap<>();


    public SpawnOresListener(RegionDataManager regionDataManager, Skyblock plugin) {
        this.regionDataManager = regionDataManager;
        this.plugin = plugin;
    }


    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        Block block = event.getBlock();
        ResourceRegion region = regionDataManager.getRegionAt(block.getLocation());

        if (region == null || region.getType() != RegionType.ORE) {
            return;
        }

        Material ore = region.getSubtype();
        Material brokenType = block.getType();

        // Bedrock is mid-respawn, don't let it be broken
        if (brokenType == Material.BEDROCK) {
            event.setCancelled(true);
            return;
        }

        // Mining the cobblestone gives nothing and restarts the regen from bedrock
        if (brokenType == Material.COBBLESTONE) {
            event.setCancelled(true);
            startRegen(block, ore);
            return;
        }

        if (brokenType != ore && brokenType != Material.STONE) {
            return;
        }

        Player player = event.getPlayer();

        // Give the drops/xp ourselves so the block doesn't turn into air
        event.setCancelled(true);
        player.give(block.getDrops(player.getInventory().getItemInMainHand(), player));
        player.giveExp(event.getExpToDrop());

        startRegen(block, ore);
    }


    private void startRegen(Block block, Material ore) {

        Location location = block.getLocation();

        // Cancel any regen already in progress for this block so old timers can't finish it early
        BukkitTask existing = regenTasks.remove(location);
        if (existing != null) {
            existing.cancel();
        }

        long bedrockToCobble = plugin.getConfig().getLong("delays.ores.bedrock-to-cobble", 5) * 20L;
        long cobbleToOre = plugin.getConfig().getLong("delays.ores.cobble-to-ore", 10) * 20L;
        int oreChance = plugin.getConfig().getInt("ore-farm-odds." + ore.name().toLowerCase(), 50);

        block.setType(Material.BEDROCK);

        BukkitTask toCobble = Bukkit.getScheduler().runTaskLater(plugin, () -> {

            block.setType(Material.COBBLESTONE);

            BukkitTask toOre = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (Math.random() * 100 < oreChance) {
                    block.setType(ore);
                } else {
                    block.setType(Material.STONE);
                }
                regenTasks.remove(location);
            }, cobbleToOre);

            regenTasks.put(location, toOre);

        }, bedrockToCobble);

        regenTasks.put(location, toCobble);
    }


    /**
     * Cancels every regen in progress and sets those blocks back to stone.
     * Call this from onDisable so blocks don't get stuck as bedrock/cobblestone.
     */
    public void restoreAll() {
        for (Map.Entry<Location, BukkitTask> entry : regenTasks.entrySet()) {
            entry.getValue().cancel();

            Location location = entry.getKey();
            if (location.getWorld() != null) {
                location.getBlock().setType(Material.STONE);
            }
        }
        regenTasks.clear();
    }

}
