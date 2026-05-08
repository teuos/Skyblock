package net.teuos.skyblock.commands;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import net.teuos.skyblock.managers.IslandPermissionsManager;
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
    private final IslandManager islandManager;
    private final IslandPermissionsManager islandPermissionsManager;
    private final Skyblock plugin;
    private final MessageLibs messageLibs;

    public SkyblockCommands(IslandLevelManager levelManager, IslandManager islandManager, IslandPermissionsManager islandPermissionsManager, Skyblock plugin, MessageLibs messageLibs) {
        this.levelManager = levelManager;
        this.islandManager = islandManager;
        this.islandPermissionsManager = islandPermissionsManager;
        this.plugin = plugin;
        this.messageLibs = messageLibs;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;


        if (args[0].equalsIgnoreCase("template")) {

            if (!player.hasPermission("skyblock.template.create") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if (args[1].equalsIgnoreCase("create")){

                if (!player.hasPermission("skyblock.template.create") && !player.hasPermission("skyblock.admin")) {
                    messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }

                islandManager.createTemplate(player);
            }

            if (args[1].equalsIgnoreCase("teleport")){

                if (!player.hasPermission("skyblock.template.teleport") && !player.hasPermission("skyblock.admin")) {
                    messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }

                try {
                    if (islandManager.loadIsland("skyblock_template")) {
                        World target = Bukkit.getWorld("skyblock_template");
                        player.teleport(target.getSpawnLocation());
                        messageLibs.sendMessage(player, ChatColor.GREEN + "Teleported to skyblock_template!");
                    }
                } catch (IOException e) {
                    messageLibs.sendMessage(player, ChatColor.RED + "Failed to teleport skyblock_template!");
                    throw new RuntimeException(e);
                }
            }

        }

        if (args[0].equalsIgnoreCase("reload")) {

            if (!player.hasPermission("skyblock.reload") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            plugin.reloadConfig();

            messageLibs.sendMessage(player, ChatColor.GREEN + "Config reloaded!");

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
            completions.add("reload");

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

