package net.teuos.skyblock.gui.impl;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.teuos.skyblock.gui.InventoryButton;
import net.teuos.skyblock.gui.InventoryGUI;
import net.teuos.skyblock.managers.IslandDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ManagePlayerPermissionsGUI extends InventoryGUI {

    private final Consumer<String> level;
    private final OfflinePlayer targetPlayer;
    private final String targetName;
    private final IslandDataManager islandDataManager;
    private final String islandName;


    public ManagePlayerPermissionsGUI(Consumer<String> level, UUID targetUUID, String islandName, IslandDataManager islandDataManager) {
        this.level = level;
        this.islandName = islandName;
        this.targetPlayer = Bukkit.getOfflinePlayer(targetUUID);
        this.islandDataManager = islandDataManager;
        this.targetName = islandDataManager.getTrustedPlayerName(islandName, targetUUID);
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9*3, Component.text("Manage trust level", NamedTextColor.AQUA));
    }

    @Override
    public void decorate(Player player){

        for (int i = 0 ; i < 9*3 ; i++) {
            background(i);
        }

        this.removeButton(4);
        this.addButton(4, new InventoryButton().creator(p -> {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            if (meta == null){
                return item;
            }
            meta.setOwningPlayer(targetPlayer);
            meta.displayName(Component.text(targetName, NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
        }));

        this.removeButton(10);
        this.addButton(10, new InventoryButton().creator(p -> {
            ItemStack item = new ItemStack(Material.CRAFTER);
            ItemMeta meta = item.getItemMeta();
            if (meta == null){
                return item;
            }
            meta.displayName(Component.text("Manager", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Can modify settings, access, and upgrades", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("+ Everything in builder", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
            level.accept("manager");
        }));

        this.removeButton(11);
        this.addButton(11, new InventoryButton().creator(p -> {
            ItemStack item = new ItemStack(Material.IRON_PICKAXE);
            ItemMeta meta = item.getItemMeta();
            if (meta == null){
                return item;
            }
            meta.displayName(Component.text("Builder", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Can place and break blocks", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("+ Everything in container", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
            level.accept("builder");
        }));

        this.removeButton(12);
        this.addButton(12, new InventoryButton().creator(p -> {
            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta meta = item.getItemMeta();
            if (meta == null){
                return item;
            }
            meta.displayName(Component.text("Container", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Can open and take items from containers", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("+ Everything in interact", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
            level.accept("container");
        }));

        this.removeButton(14);
        this.addButton(14, new InventoryButton().creator(p -> {
            ItemStack item = new ItemStack(Material.LEVER);
            ItemMeta meta = item.getItemMeta();
            if (meta == null){
                return item;
            }
            meta.displayName(Component.text("Interact", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Can click on buttons and levers, can open and close doors ect..", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("+ Everything in visitor", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
            level.accept("interact");
        }));

        this.removeButton(15);
        this.addButton(15, new InventoryButton().creator(p -> {
            ItemStack item = new ItemStack(Material.OAK_DOOR);
            ItemMeta meta = item.getItemMeta();
            if (meta == null){
                return item;
            }
            meta.displayName(Component.text("Visitor", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Can visit this island when it is private", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("Has no effect when the island is set to public", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
            level.accept("visitor");
        }));

        this.removeButton(16);
        this.addButton(16, new InventoryButton().creator(p -> {
            ItemStack item = new ItemStack(Material.BARRIER);
            ItemMeta meta = item.getItemMeta();
            if (meta == null){
                return item;
            }
            meta.displayName(Component.text("Remove trust", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Remove this players trust", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
            level.accept("remove");
        }));

        this.removeButton(26);
        this.addButton(26, new InventoryButton().creator(p -> {
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            if (meta == null){
                return item;
            }
            meta.displayName(Component.text("Close", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
        }));

        super.decorate(player);
    }

    private void background(int slot) {
        this.addButton(slot, new InventoryButton().creator(p -> {
            ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.setHideTooltip(true);
            meta.displayName(Component.text(" "));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
        }));
    }



}
