package net.teuos.skyblock.commands.islandCommands;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UnblockCommand implements SubCommand {

    private final IslandDataManager islandDataManager;
    private final MessageLibs messageLibs;

    public UnblockCommand(IslandDataManager islandDataManager, MessageLibs messageLibs) {
        this.islandDataManager = islandDataManager;
        this.messageLibs = messageLibs;
    }

    @Override
    public String getName() {
        return "unblock";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.unblock";
    }

    @Override
    public boolean execute(Player player, String[] args){
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /island unblock <player>");
            return true;
        }
        if (islandDataManager.islandExists(player.getUniqueId().toString())) {
            islandDataManager.removeBlockedPlayer(player.getUniqueId().toString(), Bukkit.getPlayer(args[1]));
            if (!islandDataManager.getBlockedStatus(player.getUniqueId().toString(), Bukkit.getPlayer(args[1]))) {
                messageLibs.sendMessage(player, ChatColor.GREEN + args[1] + " is no longer blocked from your island!");
            } else {
                messageLibs.sendMessage(player,ChatColor.RED + "Somthing went wrong!");
            }
        } else {
            messageLibs.sendMessage(player,ChatColor.RED +  "you do not have a island!");
        }
        return true;
    }

}

