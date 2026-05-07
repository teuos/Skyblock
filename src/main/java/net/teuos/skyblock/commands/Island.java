package net.teuos.skyblock.commands;

import net.teuos.skyblock.managers.CreateIslandManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Island implements CommandExecutor {

    private final IslandLevelManager levelManager;
    private final CreateIslandManager islandManager;

    public Island(IslandLevelManager levelManager, CreateIslandManager islandManager) {
        this.levelManager = levelManager;
        this.islandManager = islandManager;
    }




    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /island help");
        }

        if (args[0].equalsIgnoreCase("help")){
            sender.sendMessage(ChatColor.BLUE + "-- Skyblock island help --");
            sender.sendMessage(ChatColor.BLUE + "[/island create] - Create a Skyblock island.");
            sender.sendMessage(ChatColor.BLUE + "[/island level] - level your Skyblock island.");
        }

        if (args[0].equalsIgnoreCase("create")){

        }

        if (args[0].equalsIgnoreCase("level")){
            World world = player.getWorld();

            if (args[1].equalsIgnoreCase("generator")){

                try {
                    int level = levelManager.increaseGenLevel(world.getName());
                    player.sendMessage(ChatColor.GREEN + "Generator level is now " + level + "!");
                } catch (IOException e){
                    player.sendMessage(ChatColor.RED + "Failed to upgrade level. If you believe this to be a mistake please report the issue!");
                    e.printStackTrace();
                }


            }

        }

        if (args[0].equalsIgnoreCase("create")) {

            islandManager.createIsland(player.getPlayer().getUniqueId().toString());


        }

        return true;
    }

}
