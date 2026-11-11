package net.teuos.skyblock.gui.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.commands.IslandCommands;
import net.teuos.skyblock.gui.InventoryButton;
import net.teuos.skyblock.gui.InventoryGUI;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.managers.EcoManager;
import net.teuos.skyblock.managers.GUIManager;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.Consumer;

public class UpgradeGUI extends InventoryGUI {

    private final Consumer<String> resultHandler;

    private final IslandDataManager islandDataManager;
    private final EcoManager ecoManager;
    private final Skyblock plugin;

    public UpgradeGUI(Consumer<String> resultHandler, IslandDataManager islandDataManager, EcoManager ecoManager, Skyblock plugin) {
        this.islandDataManager = islandDataManager;
        this.resultHandler = resultHandler;
        this.ecoManager = ecoManager;
        this.plugin = plugin;
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9*3, Component.text("Upgrades", NamedTextColor.GOLD));
    }

    @Override
    public void decorate(Player player) {
        int inventorySize = this.getInventory().getSize();

        for (int i = 0; i < inventorySize; i++) {

            if (i == 11) {
                this.addButton(i, this.upgradeBorder());
            } else if (i == 15) {
                this.addButton(i, this.upgradeGenerator());
            } else {this.addButton(i, this.createNullButton());}
        }

        super.decorate(player);

    }


    private InventoryButton upgradeBorder(){
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(Material.IRON_BARS);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Upgrade Border", NamedTextColor.GOLD));
            meta.lore(List.of(
                    Component.text("Next level: ", NamedTextColor.YELLOW).append(Component.text(islandDataManager.getLevel(player.getUniqueId().toString(), "border") + 1, NamedTextColor.GOLD)).decoration(TextDecoration.ITALIC, false),
                    Component.text("Cost: ", NamedTextColor.YELLOW).append(Component.text(plugin.getConfig().getString("price-unit") + ecoManager.getNextCost(player, "border"), NamedTextColor.GOLD)).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
            resultHandler.accept("border");
        });
    }


    private InventoryButton upgradeGenerator(){
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(Material.DIAMOND_ORE);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Upgrade Generator", NamedTextColor.GOLD));
            meta.lore(List.of(
                Component.text("Next level: ", NamedTextColor.YELLOW).append(Component.text(islandDataManager.getLevel(player.getUniqueId().toString(), "generator") + 1, NamedTextColor.GOLD)).decoration(TextDecoration.ITALIC, false),
                Component.text("Cost: ", NamedTextColor.YELLOW).append(Component.text(plugin.getConfig().getString("price-unit") + ecoManager.getNextCost(player, "generator"), NamedTextColor.GOLD)).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
            resultHandler.accept("generator");
        });
    }


    private InventoryButton createNullButton(){
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(" "));
            meta.setHideTooltip(true);
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            event.setCancelled(true);
        });
    }

}
