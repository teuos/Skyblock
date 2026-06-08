package net.teuos.skyblock.gui.impl;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.teuos.skyblock.gui.InventoryButton;
import net.teuos.skyblock.gui.InventoryGUI;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;


public class MainGUI extends InventoryGUI {

    private final IslandManager islandManager;

    public MainGUI(IslandManager islandManager) {
        this.islandManager = islandManager;
    }


    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9*5, "Skyblock Menu");
    }

    @Override
    public void decorate(Player player){

        int inventorySize = this.getInventory().getSize();

        for (int i = 0; i < inventorySize - 1; i++) {
            this.addButton(i, this.createNullButton(Material.BLACK_STAINED_GLASS_PANE));
        }

        this.addButton(44, createCloseButton());

        if (islandManager.islandExists(player.getUniqueId().toString())) {
            this.removeButton(11);
            this.addButton(11, createIsAccessButton(Material.TRIAL_KEY));

            this.removeButton(15);
            this.addButton(15, createVisitMenuButton(Material.ENDER_EYE));

            this.removeButton(22);
            this.addButton(22, createIsTeleportButton("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmE0YzRhZGZmYWJiNjM4ZTNlNDFhOTM5YjRkZjE1ZWYzODA4MGMxNmMzNzkwODExOTExZGFkZjYyNjEzZDYifX19"));

            this.removeButton(29);
            this.addButton(29, createIsUpgradesMenuButton(Material.EXPERIENCE_BOTTLE));

            this.removeButton(33);
            this.addButton(33, createIsSettingsMenuButton(Material.COMPARATOR));

            super.decorate(player);
        } else {
            this.removeButton(20);
            this.addButton(20, createIsCreateButton("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmE0YzRhZGZmYWJiNjM4ZTNlNDFhOTM5YjRkZjE1ZWYzODA4MGMxNmMzNzkwODExOTExZGFkZjYyNjEzZDYifX19"));

            this.removeButton(24);
            this.addButton(24, createVisitMenuButton(Material.ENDER_EYE));
            super.decorate(player);
        }


    }


    private InventoryButton createCloseButton() {
        return new InventoryButton().creator(player -> {
           ItemStack item = new ItemStack(Material.BARRIER);
           ItemMeta meta = item.getItemMeta();
           meta.displayName(Component.text("Close", NamedTextColor.RED));
           meta.lore(List.of(
                   Component.text("- Click to close menu", NamedTextColor.YELLOW)
           ));
           item.setItemMeta(meta);
           return item;
        }).consumer(event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
        });
    }


    private InventoryButton createIsUpgradesMenuButton(Material material) {
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Upgrades").color(TextColor.color(0x14DD42)).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Island upgrades menu", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false),
                    Component.text("- Click to open", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
        });
    }

    private InventoryButton createIsAccessButton(Material material) {
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Access").color(TextColor.color(0xDDD814)).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
               Component.text("Island access options", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false),
               Component.text("- Click to open", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
        });
    }

    private InventoryButton createIsSettingsMenuButton(Material material) {
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Settings").color(TextColor.color(0x5CBCF3)).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Island settings", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false),
                    Component.text("- Click to open", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
        });
    }

    private InventoryButton createVisitMenuButton(Material material) {
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Visit").color(TextColor.color(0x2DE5B1)).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Island visit menu", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false),
                    Component.text("- Click to open", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
        });
    }

    private InventoryButton createIsTeleportButton(String base64Texture){
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            if (meta == null){
                return item;
            }
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(
                    new ProfileProperty(
                            "textures",
                            base64Texture
                    )
            );
            meta.setPlayerProfile(profile);
            meta.displayName(Component.text("Teleport").color(TextColor.color(0xF7D148)).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Teleport to your island", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false),
                    Component.text("- Click to teleport", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;

        }).consumer(event -> {
           Player player = (Player) event.getWhoClicked();
           islandManager.teleportIsland(player.getUniqueId().toString(), player);
           player.closeInventory();
        });
    }


    private InventoryButton createIsCreateButton(String base64Texture){
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            if (meta == null){
                return item;
            }
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(
                    new ProfileProperty(
                            "textures",
                            base64Texture
                    )
            );
            meta.setPlayerProfile(profile);
            meta.displayName(Component.text("Create island").color(TextColor.color(0xF7D148)).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Island create menu", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false),
                    Component.text("- Click to open", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;

        }).consumer(event -> {
            Player player = (Player) event.getWhoClicked();
            islandManager.teleportIsland(player.getUniqueId().toString(), player);
            player.closeInventory();
        });
    }

    private InventoryButton createNullButton(Material material){
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(" ");
            meta.setHideTooltip(true);
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
        });
    }
}
