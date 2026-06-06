package net.teuos.skyblock.gui.impl;

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

import java.util.List;


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

        for (int i = 0; i < inventorySize; i++) {
            this.addButton(i, this.createNullButton(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
        }


        this.removeButton(13);
        this.addButton(13, createIsTeleportButton(Material.GRASS_BLOCK));

        super.decorate(player);
    }

    private InventoryButton createIsTeleportButton(Material material){
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "Teleport to Island");
            meta.setLore(List.of(
                    "§e- Click to teleport"
            ));
            item.setItemMeta(meta);
            return item;

        }).consumer(event -> {
           Player player = (Player) event.getWhoClicked();
           islandManager.teleportIsland(player.getUniqueId().toString(), player);
           player.closeInventory();
        });
    }

    private InventoryButton createIsDeleteButton(Material material){
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("Delete Island");
            meta.setLore(List.of(
                    "§e- Click to delete island"
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
            islandManager.deleteIsland(player.getUniqueId().toString());
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
