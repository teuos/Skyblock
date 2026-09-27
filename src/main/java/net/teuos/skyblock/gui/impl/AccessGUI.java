package net.teuos.skyblock.gui.impl;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.teuos.skyblock.commands.IslandCommands;
import net.teuos.skyblock.gui.InventoryButton;
import net.teuos.skyblock.gui.InventoryGUI;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class AccessGUI extends InventoryGUI {

    private final IslandDataManager islandDataManager;
    private final MessageLibs messageLibs;
    private final IslandCommands islandCommands;


    public AccessGUI(IslandDataManager islandDataManager, IslandCommands islandCommands, MessageLibs messageLibs) {
        this.islandDataManager = islandDataManager;
        this.messageLibs = messageLibs;
        this.islandCommands = islandCommands;

    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9 * 3, Component.text("Island Access Settings", NamedTextColor.GOLD));
    }

    @Override
    public void decorate(Player player) {

        for (int i = 0; i < 27; i++) {
            this.addButton(i, createNullButton(Material.BLACK_STAINED_GLASS_PANE));
        }
        this.removeButton(11);
        this.addButton(11, createIslandPrivateToggleButton());
        this.removeButton(13);
        this.addButton(13, createTrustGUIButton());
        super.decorate(player);
    }


    private InventoryButton createIslandPrivateToggleButton() {
        return new InventoryButton().creator(player -> {
            boolean privateStatus = islandDataManager.getPrivate(player.getUniqueId().toString());

            Material material = privateStatus ? Material.GREEN_DYE : Material.RED_DYE;

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();


            Component target = privateStatus ? Component.text("Private", NamedTextColor.GREEN) : Component.text("Public", NamedTextColor.RED);
            Component oppositeTarget = privateStatus ? Component.text("Public", NamedTextColor.GREEN) : Component.text("Private", NamedTextColor.RED);

            meta.displayName(Component.text("Island visibility", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("ISLAND IS CURRENTLY: ", NamedTextColor.RED).append(target).decoration(TextDecoration.ITALIC, false),
                    Component.text("Public will let anyone visit your island!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("- Click to toggle to ", NamedTextColor.GOLD).append(oppositeTarget).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            Player player = (Player) event.getWhoClicked();
            islandDataManager.togglePrivate(player.getUniqueId().toString());
            boolean privateStatus = islandDataManager.getPrivate(player.getUniqueId().toString());

            messageLibs.sendMessage(player, "&cYour island is now " + (privateStatus ? "Private" : "Public") );
            this.decorate(player);
        });
    }

    private InventoryButton createTrustGUIButton() {
        return new InventoryButton().creator(player -> {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(
                    new ProfileProperty(
                            "textures",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc5YTVjOTVlZTE3YWJmZWY0NWM4ZGMyMjQxODk5NjQ5NDRkNTYwZjE5YTQ0ZjE5ZjhhNDZhZWYzZmVlNDc1NiJ9fX0="
                    )
            );
            meta.displayName(Component.text("Player Trust", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Manage trusted players on your island!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("- Click to open trust GUI!", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
            return item;
        }).consumer(event -> {
            Player player = (Player) event.getWhoClicked();

            SubCommand subCommand = islandCommands.subCommands.get("trust");

            if (subCommand == null){
                messageLibs.sendMessage(player, "&cUnknown Command!");
                player.closeInventory();
                return;
            }

            if (!player.hasPermission(subCommand.getPermission()) && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, "&cYou don't have permission to do that!");
                player.closeInventory();
                return;
            }

            player.closeInventory();
            subCommand.execute(player, null);
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
