package net.teuos.skyblock.protection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractProtectionListener implements Listener {

    private final ProtectionController controller;

    public InteractProtectionListener(ProtectionController controller) {
        this.controller = controller;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {

        if (event.getClickedBlock() == null) return;
        Player player = event.getPlayer();
        Material material = event.getClickedBlock().getType();
        String materialName = material.name();
        ProtectionController.ProtectionType type;


        if (materialName.endsWith("_DOOR")) {
            type = ProtectionController.ProtectionType.DOOR;

        } else if (materialName.endsWith("_TRAPDOOR")) {
            type = ProtectionController.ProtectionType.TRAPDOOR;

        } else if (materialName.endsWith("_FENCE_GATE")) {
            type = ProtectionController.ProtectionType.FENCE_GATE;

        } else if (materialName.endsWith("_BUTTON")) {
            type = ProtectionController.ProtectionType.BUTTON;

        } else if (material == Material.LEVER) {
            type = ProtectionController.ProtectionType.LEVER;

        } else {
            type = ProtectionController.ProtectionType.INTERACT_PLAYER;
        }

        controller.handle(player, type, event);


    }

}
