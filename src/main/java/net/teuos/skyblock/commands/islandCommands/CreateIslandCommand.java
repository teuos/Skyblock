package net.teuos.skyblock.commands.islandCommands;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CreateIslandCommand implements SubCommand {

    //            if (!player.hasPermission("skyblock.island.create") && !player.hasPermission("skyblock.admin")) {
//                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
//                return true;
//            }
//


    private final MessageLibs messageLibs;
    private final IslandManager islandManager;
    private final IslandDataManager islandDataManager;

    public CreateIslandCommand(MessageLibs messageLibs, IslandDataManager islandDataManager, IslandManager islandManager) {
        this.messageLibs = messageLibs;
        this.islandDataManager = islandDataManager;
        this.islandManager = islandManager;

    }


    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.create";
    }


    @Override
    public boolean execute(Player player, String[] args){
        if (args.length < 2) {
            messageLibs.sendMessage(player, ChatColor.RED + "Please select an island type!");
            messageLibs.sendMessage(player, ChatColor.GOLD + "Example usage: island create <islandType>");
            return true;
        }
        if (islandDataManager.islandExists(player.getUniqueId().toString())){
            messageLibs.sendMessage(player,ChatColor.RED + "You already have a skyblock island!");
            return true;
        }
        if (islandManager.createIsland(player.getPlayer().getUniqueId().toString(), args[1])) {
            World target = Bukkit.getWorld(player.getUniqueId().toString());
            player.teleport(target.getSpawnLocation());
            messageLibs.sendMessage(player,ChatColor.GREEN + "Island has been created!");
            return true;
        } else {
            messageLibs.sendMessage(player,ChatColor.RED + "Failed to create island!");
            return true;
        }
    }

}
