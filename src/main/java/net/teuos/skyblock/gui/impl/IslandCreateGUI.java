package net.teuos.skyblock.gui.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.teuos.skyblock.gui.InventoryButton;
import net.teuos.skyblock.gui.InventoryGUI;
import net.teuos.skyblock.managers.TemplateDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IslandCreateGUI extends InventoryGUI {

    private final Consumer<String> selected;
    private final TemplateDataManager templateDataManager;
    private static final int PAGE_SIZE = 45;
    private int page;
    private List<String> templates;


    public IslandCreateGUI(TemplateDataManager templateDataManager, Consumer<String> selected) {
        this.templateDataManager = templateDataManager;
        this.selected = selected;
        this.page = 0;
    }

    private int getMaxPages() {
        if (templates.isEmpty()) return 1;
        return (int) Math.ceil((double) templates.size() / PAGE_SIZE);
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9*6, "Island Create");
    }

    @Override
    public void decorate(Player player){

        this.clearButtons();
        this.getInventory().clear();

        var secion = templateDataManager.getConfig().getConfigurationSection("templates");
        if (secion == null) {
            this.templates = new ArrayList<>();
            return;
        }
        this.templates = new ArrayList<>(secion.getKeys(false));

        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, templates.size());
        int slot = 0;

        for (int i = start; i < end; i++) {
            String template = templates.get(i);

            this.addButton(slot, new InventoryButton().creator(p -> {
                ItemStack item = new ItemStack(templateDataManager.getItemType(template));
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text(template, NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
                meta.lore(List.of(
                        Component.text("- Click to create", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
                ));
                item.setItemMeta(meta);
                return item;
            }).consumer(event -> {
                event.setCancelled(true);
                selected.accept(template);
                event.getWhoClicked().closeInventory();
            }));

            slot++;

        }

        this.addButton(53, closeButton());

        addNavButtons(player);

        super.decorate(player);

    }


    private void addNavButtons(Player player) {

        if (page > 0) {
            addButton(45, new InventoryButton().creator(p -> {
                ItemStack item = new ItemStack(Material.ARROW);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text("Back", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
                item.setItemMeta(meta);
                return item;
            }).consumer(event -> {
                page--;
                decorate((Player) event.getWhoClicked());
            }));
        }

        if (page < getMaxPages() - 1) {
            addButton(52, new InventoryButton().creator(p -> {
                ItemStack item = new ItemStack(Material.ARROW);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text("Next", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
                item.setItemMeta(meta);
                return item;
            }).consumer(event -> {
                page++;
                decorate((Player) event.getWhoClicked());
            }));
        }

    }

    private InventoryButton closeButton() {
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(Material.BARRIER);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Close", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("- Click to close menu", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
        });
    }

}
