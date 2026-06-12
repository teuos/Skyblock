package net.teuos.skyblock.protection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.Cancellable;
import net.teuos.skyblock.managers.IslandDataManager;
import org.bukkit.event.Event;
import org.bukkit.entity.Player;

public class ProtectionController {

    private final IslandDataManager islandDataManager;

    public ProtectionController(IslandDataManager islandDataManager) {

        this.islandDataManager = islandDataManager;
    }

    public enum ProtectionType {

        BLOCK_BREAK("skyblock.protect.block.break"),
        BLOCK_PLACE("skyblock.protect.block.place"),
        BLOCK_TRAMPLE("skyblock.protect.block.trample"),
        BLOCK_SPREAD("skyblock.protect.block.spread"),
        BLOCK_GROW("skyblock.protect.block.grow"),
        FLUID_FLOW("skyblock.protect.fluid.flow"),

        DOOR("skyblock.protect.interact.door"),
        TRAPDOOR("skyblock.protect.interact.trapdoor"),
        FENCE_GATE("skyblock.protect.interact.fencegate"),
        BUTTON("skyblock.protect.interact.button"),
        LEVER("skyblock.protect.interact.lever"),
        BED("skyblock.protect.interact.bed"),

        CHEST("skyblock.protect.container.chest"),
        FURNACE("skyblock.protect.container.furnace"),
        HOPPER("skyblock.protect.container.hopper"),
        INVENTORY("skyblock.protect.container.inventory"),
        DROP("skyblock.protect.container.drop"),

        ITEM_FRAME("skyblock.protect.entity.itemframe"),
        ARMOR_STAND("skyblock.protect.entity.armorstand"),

        INTERACT_PLAYER("skyblock.protect.interact.player"),
        INTERACT_ENTITY("skyblock.protect.interact.entity");

        private final String permission;

        ProtectionType(String permission) {
            this.permission = permission;
        }

        public String permission() {
            return permission;
        }
    }

    public boolean handle(Player player, ProtectionType type, Event event) {

        if (!islandDataManager.islandExists(player.getWorld().getName())){
            return true;
        }

        if (player.hasPermission("skyblock.protect.island.bypass")) {
            return true;
        }

        if (player.hasPermission(type.permission())) {
            return true;
        }

        if (event instanceof Cancellable c) {
            c.setCancelled(true);
            player.sendMessage(Component.text("Hey!", NamedTextColor.RED)
                    .decoration(TextDecoration.BOLD, true)
                    .append(Component.text(" you can't do that here!", NamedTextColor.GRAY)
                            .decoration(TextDecoration.BOLD, false)));
        }

        return false;
    }



}
