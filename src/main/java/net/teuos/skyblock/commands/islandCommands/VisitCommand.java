package net.teuos.skyblock.commands.islandCommands;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandManager;
import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;

public class VisitCommand implements SubCommand {

    private final MessageLibs messageLibs;
    private final IslandDataManager islandDataManager;
    private final IslandManager islandManager;

    public VisitCommand(MessageLibs messageLibs, IslandDataManager islandDataManager, IslandManager islandManager) {
        this.messageLibs = messageLibs;
        this.islandDataManager = islandDataManager;
        this.islandManager = islandManager;
    }

    @Override
    public String getName() {
        return "visit";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.visit";
    }

    @Override
    public boolean execute(Player player, String[] args){
        if (args.length < 2) {
            player.sendMessage("Usage: /island visit <player>");
            return true;
        }
        String playerName = args[1];
        OfflinePlayer target = Bukkit.getPlayer(playerName);
        if (target == null) {
            messageLibs.sendMessage(player, ChatColor.RED + "Player " + playerName + " not found!");
            return true;
        }
        try {
            String islandOwner = target.getUniqueId().toString();
            if (islandDataManager.islandExists(islandOwner)){
                if (islandDataManager.getBlockedStatus(islandOwner, player)) {
                    messageLibs.sendMessage(player,ChatColor.RED + "You have been blocked from visiting this island, please contact the island owner if you think this is a mistake!");
                    return true;
                }
                try {
                    islandManager.loadIsland(islandOwner);
                    World world = Bukkit.getWorld(islandOwner);
                    player.teleport(world.getSpawnLocation());
                    messageLibs.sendMessage(player,ChatColor.GREEN + "Teleported to " + playerName + "'s island!");
                    return true;
                } catch (IOException e) {
                    messageLibs.sendMessage(player,ChatColor.RED + "Failed to teleport to " + playerName + "'s island!");
                    throw new RuntimeException(e);
                }
            } else {
                messageLibs.sendMessage(player,ChatColor.RED + playerName + " does not have a island!");
            }
        } catch (IllegalArgumentException e) {
            messageLibs.sendMessage(player,ChatColor.RED + playerName + " does not exist!");
            return true;
        }
        return true;
    }

}
