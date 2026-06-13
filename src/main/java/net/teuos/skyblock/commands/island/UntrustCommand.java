package net.teuos.skyblock.commands.island;

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

public class UntrustCommand implements SubCommand {

    private final IslandDataManager islandDataManager;
    private final IslandManager islandManager;
    private final PermissionManager permissionManager;
    private final MessageLibs messageLibs;

    public UntrustCommand(IslandDataManager islandDataManager, IslandManager islandManager, PermissionManager permissionManager, MessageLibs messageLibs) {
        this.islandDataManager = islandDataManager;
        this.islandManager = islandManager;
        this.permissionManager = permissionManager;
        this.messageLibs = messageLibs;
    }

    @Override
    public String getName() {
        return "untrust";
    }

    public String getPermission() {
        return "skyblock.island.untrust";
    }

    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /island untrust <player>");
            return true;
        }
        if (islandDataManager.islandExists(player.getUniqueId().toString())) {
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
                permissionManager.removePlayerTrustLevel(targetPlayer, targetWorld);
                messageLibs.sendMessage(player,
                        ChatColor.GREEN + "Untrusted " + targetWorld + " from your island!");
                islandDataManager.removeTrustedPlayer(targetWorld, targetPlayer);
                return true;
            }
        } else {
            messageLibs.sendMessage(player,ChatColor.RED +  "you do not have a island!");
        }
        return true;
    }

}

