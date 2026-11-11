package net.teuos.skyblock.commands.island;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class BlockCommand implements SubCommand {

    private final IslandDataManager islandDataManager;
    private final MessageLibs messageLibs;


    public BlockCommand(IslandDataManager islandDataManager, MessageLibs messageLibs) {
        this.islandDataManager = islandDataManager;
        this.messageLibs = messageLibs;
    }

    @Override
    public String getName() {
        return "block";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.block";
    }

    @Override
    public boolean execute(Player player, String[] args){
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /island block <player>");
            return true;
        }
        if (islandDataManager.islandExists(player.getUniqueId().toString())) {
            islandDataManager.addBlockedPlayer(player.getUniqueId().toString(), Bukkit.getPlayer(args[1]));
            if (islandDataManager.getBlockedStatus(player.getUniqueId().toString(), Bukkit.getPlayer(args[1]))) {
                messageLibs.sendMessage(player,ChatColor.GREEN + args[1] + " is now blocked from your island!");
                if (Bukkit.getPlayer(args[1]).getWorld().equals(Bukkit.getWorld(player.getUniqueId()))) {
                    Bukkit.getPlayer(args[1]).teleport(Bukkit.getWorld("world").getSpawnLocation());
                }
            }  else {
                messageLibs.sendMessage(player,ChatColor.RED + "Somthing went wrong!");
            }
        } else {
            messageLibs.sendMessage(player,ChatColor.RED +  "you do not have a island!");
        }
        return true;
    }

}
