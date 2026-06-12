package net.teuos.skyblock.commands.island;

import net.kyori.adventure.text.Component;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandManager;
import net.teuos.skyblock.managers.IslandPermissionsManager;
import net.teuos.skyblock.protection.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;

public class TrustCommand implements SubCommand {

    private final IslandDataManager islandDataManager;
    private final IslandManager islandManager;
    private final PermissionManager permissionManager;
    private final MessageLibs messageLibs;

    public TrustCommand(IslandDataManager islandDataManager, IslandManager islandManager, PermissionManager permissionManager, MessageLibs messageLibs) {
        this.islandDataManager = islandDataManager;
        this.islandManager = islandManager;
        this.permissionManager = permissionManager;
        this.messageLibs = messageLibs;
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
            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                messageLibs.sendMessage(player,
                        ChatColor.RED + "Player not found!");
                return true;
            }
            if (targetPlayer.equals(player)) {
                messageLibs.sendMessage(player,
                        ChatColor.RED + "You cannot change your own trust level!");
                return true;
            }
            PermissionManager.TrustLevel currentLevel = permissionManager.getPlayerTrustLevel(targetPlayer, targetWorld);
            if (currentLevel == level) {
                messageLibs.sendMessage(
                        player,
                        ChatColor.YELLOW + targetPlayer.getName()
                                + " already has that trust level!"
                );
                return true;
            }
            permissionManager.removePlayerTrustLevel(targetPlayer, targetWorld);
            permissionManager.setPlayerTrustLevel(targetPlayer, targetWorld, level.name());
            player.sendMessage(
                    Component.text(targetPlayer.getName())
                            .append(Component.text(" has been trusted with level "))
                            .append(level.displayName())
            );
        } else {
            messageLibs.sendMessage(player,ChatColor.RED +  "You do not have a island!");
        }
        return true;
    }

}
