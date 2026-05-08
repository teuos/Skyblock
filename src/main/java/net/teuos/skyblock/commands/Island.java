package net.teuos.skyblock.commands;

import net.teuos.skyblock.managers.CreateIslandManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import org.bukkit.Bukkit;
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

            if (islandManager.createIsland(player.getPlayer().getUniqueId().toString())) {
                player.sendMessage(ChatColor.GREEN + "Island has been created!");
            } else {
                player.sendMessage(ChatColor.RED + "Failed to create island!");
            }


        }


        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length >= 2 && args[1].equalsIgnoreCase("confirm")){

                if (islandManager.deleteIsland(player.getUniqueId().toString())){
                    player.sendMessage(ChatColor.GREEN + "Island has been deleted!");
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to delete island!");
                }

            } else {
                player.sendMessage(ChatColor.RED + "THIS ACTION CAN NOT BE UNDONE ALL PROGRESS WILL BE LOST. Are you sure you want to delete your island?");
                player.sendMessage(ChatColor.RED + "if so run /island delete confirm");
            }
        }


        if (args[0].equalsIgnoreCase("teleport")) {
            try {
                islandManager.teleportIsland(player.getUniqueId().toString());
                World target = Bukkit.getWorld(player.getUniqueId().toString());
                player.teleport(target.getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "Teleported to your island!");
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + "Failed to teleport your island!");
                throw new RuntimeException(e);
            }
        }


        if (args[0].equalsIgnoreCase("template")) {
            if (args[1].equalsIgnoreCase("create")){
                islandManager.createTemplate(player);
            }

            if (args[1].equalsIgnoreCase("teleport")){
                try {
                    if (islandManager.teleportIsland("skyblock_template")) {
                        World target = Bukkit.getWorld("skyblock_template");
                        player.teleport(target.getSpawnLocation());
                        player.sendMessage(ChatColor.GREEN + "Teleported to skyblock_template!");
                    }
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + "Failed to teleport skyblock_template!");
                    throw new RuntimeException(e);
                }
            }

        }


        return true;
    }

}
