package net.teuos.skyblock.gui.impl;

import net.teuos.skyblock.gui.InventoryButton;
import net.teuos.skyblock.gui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class ConfirmIsDeleteGUI extends InventoryGUI {

    private final Consumer<Boolean> resultHandler;

    public ConfirmIsDeleteGUI(Consumer<Boolean> resultHandler) {
        this.resultHandler = resultHandler;
    }


    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9, "Confirm");
    }

    @Override
    public void decorate(Player player){
        for (int i = 0; i < 4; i++) {
            this.addButton(i, this.createDenyButton(Material.RED_STAINED_GLASS_PANE));
        }

        this.addButton(4, createNullButton(Material.LIGHT_GRAY_STAINED_GLASS_PANE));

        for (int i = 5; i < 9; i++) {
            this.addButton(i, createConfirmButton(Material.GREEN_STAINED_GLASS_PANE));
        }

        super.decorate(player);
    }


    private InventoryButton createDenyButton(Material material){
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Cancel");
            meta.setLore(List.of(
                    "§a- Click to cancel!"
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
            resultHandler.accept(false);
            event.getWhoClicked().closeInventory();
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

    private InventoryButton createConfirmButton(Material material){
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Delete");
            meta.setLore(List.of(
                    "§CWARNING: This action",
                    "§CCan not be undone.",
                    "§C- Click to delete"
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
            resultHandler.accept(true);
            event.getWhoClicked().closeInventory();
        });
    }

}
