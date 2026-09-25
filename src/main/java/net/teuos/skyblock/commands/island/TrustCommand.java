package net.teuos.skyblock.commands.island;

import net.kyori.adventure.text.Component;
import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.gui.impl.ManagePlayerPermissionsGUI;
import net.teuos.skyblock.gui.impl.PermissionsGUI;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.listeners.ChatInputListener;
import net.teuos.skyblock.managers.GUIManager;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandManager;
import net.teuos.skyblock.managers.IslandPermissionsManager;
import net.teuos.skyblock.protection.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class TrustCommand implements SubCommand {

    private final IslandDataManager islandDataManager;
    private final IslandManager islandManager;
    private final PermissionManager permissionManager;
    private final MessageLibs messageLibs;
    private final GUIManager guiManager;
    private final Skyblock plugin;
    private final ChatInputListener chatInputListener;

    public TrustCommand(IslandDataManager islandDataManager, IslandManager islandManager, PermissionManager permissionManager, MessageLibs messageLibs, GUIManager guiManager, Skyblock plugin, ChatInputListener chatInputListener) {
        this.islandDataManager = islandDataManager;
        this.islandManager = islandManager;
        this.permissionManager = permissionManager;
        this.messageLibs = messageLibs;
        this.guiManager = guiManager;
        this.plugin = plugin;
        this.chatInputListener = chatInputListener;
    }

    @Override
    public String getName() {
        return "trust";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.trust";
    }

    @Override
    public boolean execute(Player player, String[] args){

        if (args.length < 2) {
            if (islandDataManager.islandExists(player.getUniqueId().toString())) {
                guiManager.openGUI(new PermissionsGUI(selected -> {
                    guiManager.openGUI(new ManagePlayerPermissionsGUI(level -> {
                        if (level.equals("remove")){
                            untrustPlayer(player, Bukkit.getOfflinePlayer(UUID.fromString(selected)), player.getUniqueId().toString());
                            islandDataManager.removeTrustedPlayer(player.getUniqueId().toString(), Bukkit.getOfflinePlayer(UUID.fromString(selected)));
                        } else {
                            trustPlayer(player, Bukkit.getOfflinePlayer(UUID.fromString(selected)), player.getUniqueId().toString(), PermissionManager.TrustLevel.valueOf(level.toUpperCase()));
                        }
                    }, UUID.fromString(selected), player.getUniqueId().toString(), islandDataManager), player);
                }, permissionManager, islandDataManager, plugin, chatInputListener), player);
            } else {
                messageLibs.sendMessage(player,ChatColor.RED +  "You do not have a island!");
            }
            return true;
        }

        if (args.length < 3) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /island trust <player> <level>");
            return true;
        }
        PermissionManager.TrustLevel level;
        try {
            level = PermissionManager.TrustLevel.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            messageLibs.sendMessage(player,
                    ChatColor.RED + "Invalid level: " + args[2] + "! You must select <manager|builder|container|interact|visitor>");
            return true;

        }

        if (islandDataManager.islandExists(player.getUniqueId().toString())) {
            String targetWorld = player.getUniqueId().toString();
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
            if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
                messageLibs.sendMessage(player, ChatColor.RED + "Player not found!");
                return true;
            }
            trustPlayer(player, targetPlayer, targetWorld, level);
        } else {
            messageLibs.sendMessage(player,ChatColor.RED +  "You do not have a island!");
        }
        return true;
    }

    private void untrustPlayer(Player player, OfflinePlayer targetPlayer, String targetWorld) {
        if (targetPlayer == null){
            messageLibs.sendMessage(player,ChatColor.RED + "Player not found!");
            return;
        }
        if(targetPlayer.equals(player)){
            messageLibs.sendMessage(player, ChatColor.RED + "You cannot change your own trust level!");
            return;
        }
        permissionManager.removePlayerTrustLevel(targetPlayer, targetWorld);
        messageLibs.sendMessage(player, ChatColor.GREEN + "Removed " + targetPlayer.getName() + "'s trust!");

    }

    private void trustPlayer(Player player, OfflinePlayer targetPlayer, String targetWorld, PermissionManager.TrustLevel level) {
        if (targetPlayer == null) {
            messageLibs.sendMessage(player,
                    ChatColor.RED + "Player not found!");
            return;
        }
        if (targetPlayer.equals(player)) {
            messageLibs.sendMessage(player,
                    ChatColor.RED + "You cannot change your own trust level!");
            return;
        }
        PermissionManager.TrustLevel currentLevel = permissionManager.getPlayerTrustLevel(targetPlayer.getUniqueId(), targetWorld);
        if (currentLevel == level) {
            messageLibs.sendMessage(
                    player,
                    ChatColor.YELLOW + targetPlayer.getName()
                            + " already has that trust level!"
            );
            return;
        }
        permissionManager.removePlayerTrustLevel(targetPlayer, targetWorld);
        permissionManager.setPlayerTrustLevel(targetPlayer, targetWorld, level.name());
        player.sendMessage(
                Component.text(targetPlayer.getName())
                        .append(Component.text(" has been trusted with level "))
                        .append(level.displayName())
        );
        islandDataManager.addTrustedPlayer(targetWorld, targetPlayer);
    }

}
