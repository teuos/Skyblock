package net.teuos.skyblock.protection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockProtectionListener implements Listener {

    private final ProtectionController controller;

    public BlockProtectionListener(ProtectionController controller) {
        this.controller = controller;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        controller.handle(event.getPlayer(), ProtectionController.ProtectionType.BLOCK_BREAK, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        controller.handle(event.getPlayer(), ProtectionController.ProtectionType.BLOCK_PLACE, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTrample(PlayerInteractEvent event) {

        if(event.getAction() != Action.PHYSICAL) return;

        controller.handle(event.getPlayer(), ProtectionController.ProtectionType.BLOCK_TRAMPLE, event);

    }

}
