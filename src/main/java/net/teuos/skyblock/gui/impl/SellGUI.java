package net.teuos.skyblock.gui.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.gui.InventoryButton;
import net.teuos.skyblock.gui.InventoryGUI;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.EcoManager;
import net.teuos.skyblock.managers.GUIManager;
import net.teuos.skyblock.managers.SellManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class SellGUI extends InventoryGUI {

    private final EcoManager ecoManager;
    private final Skyblock plugin;
    private final SellManager sellManager;
    private final GUIManager guiManager;
    Boolean confirmingSale = false;

    public SellGUI(Skyblock plugin, EcoManager ecoManager, SellManager sellManager, GUIManager guiManager) {
        this.ecoManager = ecoManager;
        this.plugin = plugin;
        this.sellManager = sellManager;
        this.guiManager = guiManager;
    }


    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9*6, Component.text("Sell inventory", NamedTextColor.GOLD));
    }

    @Override
    public void decorate(Player player){
        this.addButton(45, cancelButton());
        for (int i = 46; i < 53; i++) {
            this.addButton(i, createNullButton(Material.BLACK_STAINED_GLASS_PANE));
        }
        this.addButton(53, sellButton());
        super.decorate(player);
    }

    @Override
    public void onClick(InventoryClickEvent event){
        Inventory clicked = event.getClickedInventory();

        if (clicked == null) return;

        if (clicked.equals(event.getWhoClicked().getInventory())) {
            event.setCancelled(false);
            return;
        }

        int slot = event.getRawSlot();

        if (slot < 45){

            InventoryButton button = buttonMap.get(slot);
            if (button != null) {
                button.getEventConsumer().accept(event);
            } else {
                event.setCancelled(false);
            }
            return;
        }

        event.setCancelled(true);

        InventoryButton button = buttonMap.get(slot);
        if (button != null) {
            button.getEventConsumer().accept(event);
        }

    }


    @Override
    public void onClose(InventoryCloseEvent event) {

        if (confirmingSale) {
            return;
        }

        handelClose(
                (Player) event.getPlayer(),
                event.getInventory()
        );
    }

    private void handleSale(Player player, Inventory inv){
        double total = 0;
        int amountSold = 0;
        for (int slot = 0; slot < 45; slot++) {
            ItemStack item = inv.getItem(slot);

            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            if (sellManager.isSellable(item.getType())) {

                total += sellManager.getPrice(item.getType()) * item.getAmount();
                amountSold += item.getAmount();


                inv.setItem(slot, null);
            } else {
                HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);

                leftovers.values().forEach(drop -> {
                    player.getWorld().dropItemNaturally(player.getLocation(), drop);
                });

                inv.setItem(slot, null);
            }
        }
        player.closeInventory();
        ecoManager.deposit(player, total);
        new MessageLibs(plugin).sendMessage(player, ChatColor.GREEN + "You have sold " + amountSold + " items for " + ChatColor.GOLD + plugin.getConfig().getString("price-unit") + total + "!");
    }

    private void handelClose(Player player, Inventory inv){
        for (int slot = 0; slot < 45; slot++) {
            ItemStack item = inv.getItem(slot);

            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);

            leftovers.values().forEach(drop -> {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            });

            inv.setItem(slot, null);
        }
    }

    private void handleCancel(Player player, Inventory inv){
        for (int slot = 0; slot < 45; slot++) {
            ItemStack item = inv.getItem(slot);

            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);

            leftovers.values().forEach(drop -> {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            });

            inv.setItem(slot, null);
        }
        player.closeInventory();
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

    private InventoryButton sellButton() {
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Sell", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("- Click to sell items", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(mainEvent -> {
            confirmingSale = true;
            Inventory sellInventory = mainEvent.getInventory();
            this.guiManager.openGUI(new ConfirmSaleGUI(result -> {
                confirmingSale = false;
                if (result) {
                    handleSale((Player) mainEvent.getWhoClicked(), sellInventory);
                } else {
                    handleCancel((Player) mainEvent.getWhoClicked(), sellInventory);
                }
            }), (Player) mainEvent.getWhoClicked());
        });
    }

    private InventoryButton cancelButton() {
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Cancel", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("- Cancel sale", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            handleCancel(
                    (Player) event.getWhoClicked(),
                    event.getInventory()
            );
        });
    }



}
