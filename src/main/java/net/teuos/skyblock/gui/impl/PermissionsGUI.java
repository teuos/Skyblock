package net.teuos.skyblock.gui.impl;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.gui.InventoryButton;
import net.teuos.skyblock.gui.InventoryGUI;
import net.teuos.skyblock.listeners.ChatInputListener;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.protection.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.function.Consumer;

public class PermissionsGUI extends InventoryGUI {

    private final Consumer<String> selected;
    private final PermissionManager permissionManager;
    private final IslandDataManager islandDataManager;
    private final Skyblock plugin;
    private final ChatInputListener chatInputListener;

    private static final int PAGE_SIZE = 45;
    private int page;
    private List<String> players;

    public PermissionsGUI(Consumer<String> selected, PermissionManager permissionManager, IslandDataManager islandDataManager, Skyblock plugin, ChatInputListener chatInputListener) {
        this.selected = selected;
        this.permissionManager = permissionManager;
        this.islandDataManager = islandDataManager;
        this.plugin = plugin;
        this.chatInputListener = chatInputListener;
    }

    private int getMaxPages() {
        if (players.isEmpty()) return 1;
        return (int) Math.ceil((double) players.size() / PAGE_SIZE);
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9*6, Component.text("Trusted players", NamedTextColor.AQUA));
    }

    @Override
    public void decorate(Player player) {

        this.clearButtons();
        this.getInventory().clear();

        this.players = new ArrayList<>(islandDataManager.getTrustedPlayers(player.getUniqueId().toString()));

        int start = page*PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, players.size());
        int slot = 0;

        for (int i = start; i < end; i++) {
            String targetPlayer = players.get(i);

            this.addButton(slot, new InventoryButton().creator(p -> {
                ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                if (meta == null){
                    return item;
                }
                UUID uuid = UUID.fromString(targetPlayer);
                PlayerProfile profile = Bukkit.createProfile(uuid);
                meta.setPlayerProfile(profile);
                meta.displayName(Component.text(islandDataManager.getTrustedPlayerName(player.getUniqueId().toString(), uuid), NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                meta.lore(List.of(
                        Component.text("Trust level: ", NamedTextColor.GRAY)
                                .append(permissionManager.getPlayerTrustLevel(uuid, player.getUniqueId().toString()).displayName()).decoration(TextDecoration.ITALIC, false)
                ));
                item.setItemMeta(meta);
                return item;
            }).consumer(event -> {
                event.getWhoClicked().closeInventory();
                event.setCancelled(true);
                selected.accept(targetPlayer);
            }));
            slot++;
        }

        for (int i = 46; i < 53; i++) {
            this.addButton(i, new InventoryButton().creator(p -> {
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


        this.addButton(45, addPlayerButton());
        this.addButton(53, closeButton());
        addNavButtons();
        super.decorate(player);

    }

    private void addNavButtons() {

        if (page > 0) {
            removeButton(46);
            addButton(46, new InventoryButton().creator(p -> {
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
            removeButton(52);
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

    private InventoryButton addPlayerButton(){
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(
                    new ProfileProperty(
                            "textures",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19"
                    )
            );
            meta.setPlayerProfile(profile);
            meta.displayName(Component.text("Trust player", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("- Click to trust a new player", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
            player.sendMessage("Send the player's name in chat... (10 seconds)");
            chatInputListener.requestInput(player, input -> {
                Player target = Bukkit.getPlayerExact(input);
                if (target == null) {
                    player.sendMessage(Component.text("Player not found!", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
                    return;
                }

                selected.accept(target.getUniqueId().toString());
            });

        });
    }

    private InventoryButton closeButton() {
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
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
