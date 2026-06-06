package net.teuos.skyblock.commands.islandCommands;

import net.teuos.skyblock.interfaces.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.help";
    }

    @Override
    public boolean execute(Player player, String[] args){
        player.sendMessage(ChatColor.GOLD + "-- Skyblock island help --");
        player.sendMessage(ChatColor.YELLOW + "[/island create] - Create your Skyblock island.");
        player.sendMessage(ChatColor.YELLOW + "[/island delete] - Delete your Skyblock island.");
        player.sendMessage(ChatColor.YELLOW + "[/island teleport] - Teleport to your Skyblock island.");
        player.sendMessage(ChatColor.YELLOW + "[/island upgrade] - upgrade your Skyblock island.");
        return true;
    }
}
