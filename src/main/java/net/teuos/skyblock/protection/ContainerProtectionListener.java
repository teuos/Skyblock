package net.teuos.skyblock.protection;

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

        controller.handle(p, ProtectionController.ProtectionType.INVENTORY, event);

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityContainerInteract(PlayerInteractEntityEvent event) {

        ProtectionController.ProtectionType type;

        switch (event.getRightClicked().getType()) {
            case ITEM_FRAME -> type = ProtectionController.ProtectionType.ITEM_FRAME;
            case ARMOR_STAND -> type = ProtectionController.ProtectionType.ARMOR_STAND;
            default -> type = ProtectionController.ProtectionType.INTERACT_ENTITY;
        }

        controller.handle(event.getPlayer(), type, event);

    }

}
