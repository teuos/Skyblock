package net.teuos.skyblock.commands;

import net.teuos.skyblock.managers.CreateIslandManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkyblockCommands implements CommandExecutor, TabCompleter {

    private final IslandLevelManager levelManager;
    private final CreateIslandManager islandManager;


    public SkyblockCommands(IslandLevelManager levelManager, CreateIslandManager islandManager) {
        this.levelManager = levelManager;
        this.islandManager = islandManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;


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

    public List<String> onTabComplete(CommandSender sender,
                                      Command command,
                                      String alias,
                                      String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("template");

            return completions;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("template")) {
                completions.add("create");
                completions.add("delete");
                completions.add("teleport");
            }

            return completions;
        }

        return completions;
    }

}

