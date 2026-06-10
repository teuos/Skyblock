package net.teuos.skyblock.gui.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import java.util.function.Consumer;

public class ConfirmSaleGUI extends InventoryGUI {

    private final Consumer<Boolean> resultHandler;

    public ConfirmSaleGUI(Consumer<Boolean> resultHandler) {
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
            meta.displayName(Component.text("Cancel", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("- Click to cancel sale", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
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
            meta.displayName(Component.text(" "));
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
            meta.displayName(Component.text("Sell", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Warning: This action can not be undone!", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false),
                    Component.text("- Click to sell", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
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
