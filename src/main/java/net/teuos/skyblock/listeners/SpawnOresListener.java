package net.teuos.skyblock.listeners;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.managers.RegionDataManager;
import net.teuos.skyblock.objects.RegionType;
import net.teuos.skyblock.objects.ResourceRegion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SpawnOresListener implements Listener {


    private final RegionDataManager regionDataManager;
    private final Skyblock plugin;


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

        // Block is mid-respawn, don't let it be broken
        if (brokenType == Material.BEDROCK || brokenType == Material.COBBLESTONE) {
            event.setCancelled(true);
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

        block.setType(Material.BEDROCK);

        long bedrockToCobble = plugin.getConfig().getLong("delays.ores.bedrock-to-cobble", 5) * 20L;
        long cobbleToOre = plugin.getConfig().getLong("delays.ores.cobble-to-ore", 10) * 20L;
        int oreChance = plugin.getConfig().getInt("ore-farm-odds." + ore.name().toLowerCase(), 50);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            block.setType(Material.COBBLESTONE);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (Math.random() * 100 < oreChance) {
                    block.setType(ore);
                } else {
                    block.setType(Material.STONE);
                }
            }, cobbleToOre);

        }, bedrockToCobble);

    }

}
