package net.teuos.skyblock.protection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ContainerProtectionListener implements Listener {

    private final ProtectionController controller;

    public ContainerProtectionListener(ProtectionController controller) {
        this.controller = controller;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChestOpen(InventoryOpenEvent event) {

        if (!(event.getPlayer() instanceof Player p)) return;

        if (!(controller.handle(p, ProtectionController.ProtectionType.INVENTORY, event))){
            event.getPlayer().sendMessage(Component.text("Hey!", NamedTextColor.RED)
                    .decoration(TextDecoration.BOLD, true)
                    .append(Component.text(" you can't open containers here!", NamedTextColor.GRAY)
                            .decoration(TextDecoration.BOLD, false)));
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityContainerInteract(PlayerInteractEntityEvent event) {

        ProtectionController.ProtectionType type;

        switch (event.getRightClicked().getType()) {
            case ITEM_FRAME -> type = ProtectionController.ProtectionType.ITEM_FRAME;
            case ARMOR_STAND -> type = ProtectionController.ProtectionType.ARMOR_STAND;
            default -> type = ProtectionController.ProtectionType.INTERACT_ENTITY;
        }

        if (!(controller.handle(event.getPlayer(), type, event))){
            event.getPlayer().sendMessage(Component.text("Hey!", NamedTextColor.RED)
                    .decoration(TextDecoration.BOLD, true)
                    .append(Component.text(" you can't interact with that here!", NamedTextColor.GRAY)
                            .decoration(TextDecoration.BOLD, false)));
        }

    }

}
