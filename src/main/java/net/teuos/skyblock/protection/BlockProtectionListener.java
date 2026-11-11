package net.teuos.skyblock.protection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
        if (!(controller.handle(event.getPlayer(), ProtectionController.ProtectionType.BLOCK_BREAK, event))){
            event.getPlayer().sendMessage(Component.text("Hey!", NamedTextColor.RED)
                    .decoration(TextDecoration.BOLD, true)
                    .append(Component.text(" you can't place blocks here!", NamedTextColor.GRAY)
                            .decoration(TextDecoration.BOLD, false)));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!(controller.handle(event.getPlayer(), ProtectionController.ProtectionType.BLOCK_PLACE, event))){
            event.getPlayer().sendMessage(Component.text("Hey!", NamedTextColor.RED)
                    .decoration(TextDecoration.BOLD, true)
                    .append(Component.text(" you can't place blocks here!", NamedTextColor.GRAY)
                            .decoration(TextDecoration.BOLD, false)));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTrample(PlayerInteractEvent event) {

        if(event.getAction() != Action.PHYSICAL) return;

        if (!(controller.handle(event.getPlayer(), ProtectionController.ProtectionType.BLOCK_TRAMPLE, event))){
            event.getPlayer().sendMessage(Component.text("Hey!", NamedTextColor.RED)
                    .decoration(TextDecoration.BOLD, true)
                    .append(Component.text(" you can't do that here!", NamedTextColor.GRAY)
                            .decoration(TextDecoration.BOLD, false)));

        }

    }

}
