package net.teuos.skyblock.commands;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.*;
import net.teuos.skyblock.objects.SpawnPoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SkyblockCommands implements CommandExecutor, TabCompleter {

    private final IslandLevelManager levelManager;
    private final IslandManager islandManager;
    private final IslandPermissionsManager islandPermissionsManager;
    private final Skyblock plugin;
    private final MessageLibs messageLibs;
    private final IslandDataManager islandDataManager;
    private final File templatesFolder;
    private final TemplateDataManager templateDataManager;

    public SkyblockCommands(IslandLevelManager levelManager, IslandManager islandManager, IslandPermissionsManager islandPermissionsManager, Skyblock plugin, MessageLibs messageLibs, IslandDataManager islandDataManager, File templatesFolder, TemplateDataManager templateDataManager) {
        this.levelManager = levelManager;
        this.islandManager = islandManager;
        this.islandPermissionsManager = islandPermissionsManager;
        this.plugin = plugin;
        this.messageLibs = messageLibs;
        this.islandDataManager = islandDataManager;
        this.templatesFolder = templatesFolder;
        this.templateDataManager = templateDataManager;
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

                if (args.length < 3) {
                    messageLibs.sendMessage(player, ChatColor.RED + "Please supply a name for the template!");
                    messageLibs.sendMessage(player, ChatColor.GOLD + "Example usage: skyblock template create templateNameHere");
                    return true;
                }

                islandManager.createTemplate(player, args[2]);
            }

            else if (args[1].equalsIgnoreCase("delete")){
                if (!player.hasPermission("skyblock.template.delete") && !player.hasPermission("skyblock.admin")) {
                    messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }

                switch (islandManager.deleteTemplate(args[2])) {
                    case (0):
                        messageLibs.sendMessage(player, ChatColor.GREEN + "Template deleted!");
                        break;
                    case (1):
                        messageLibs.sendMessage(player, ChatColor.RED + "Template does not exist!");
                        break;
                    case (2):
                        messageLibs.sendMessage(player, ChatColor.RED + "Failed to delete template!");
                }
            }


            else if (args[1].equalsIgnoreCase("teleport")){

                if (!player.hasPermission("skyblock.template.teleport") && !player.hasPermission("skyblock.admin")) {
                    messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }

                try {
                    if (islandManager.loadTemplate(args[2])) {
                        World target = Bukkit.getWorld(args[2]);
                        player.teleport(target.getSpawnLocation());
                        messageLibs.sendMessage(player, ChatColor.GREEN + "Teleported to " + args[2] + "!");
                    }
                } catch (IOException e) {
                    messageLibs.sendMessage(player, ChatColor.RED + "Failed to teleport to " + args[2] + "!");
                    throw new RuntimeException(e);
                }
            }

            else if (args[1].equalsIgnoreCase("setSpawn")){

                if (!player.hasPermission("skyblock.template.setSpawn") && !player.hasPermission("skyblock.admin")) {
                    messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }

                String world = player.getLocation().getWorld().getName();

                if (islandManager.templateExists(world)){
                    islandManager.updateTemplateSpawn(world, new SpawnPoint(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
                } else {
                    messageLibs.sendMessage(player, ChatColor.RED + "This isn't a template island!");
                }
            }

            else {
                messageLibs.sendMessage(player, ChatColor.RED + "Unknown command!");
            }

        } else if (args[0].equalsIgnoreCase("teleport")){
            if (!player.hasPermission("skyblock.others.teleport") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(ChatColor.YELLOW + "Usage: /skyblock teleport <player>");
            }

            if (islandDataManager.islandExists(Bukkit.getPlayer(args[1]).getUniqueId().toString())) {
                try {
                    islandManager.loadIsland(Bukkit.getPlayer(args[1]).getUniqueId().toString());
                    player.teleport(Bukkit.getWorld(Bukkit.getPlayer(args[1]).getUniqueId().toString()).getSpawnLocation());
                    messageLibs.sendMessage(player, ChatColor.GREEN + "Teleported to " + Bukkit.getPlayer(args[1]).getName() + "'s island!");
                } catch (IOException e) {
                    messageLibs.sendMessage(player, ChatColor.RED + "Failed to teleport to " + Bukkit.getPlayer(args[1]).getName() + "'s island!");
                    throw new RuntimeException(e);
                }
            }

        } else if (args[0].equalsIgnoreCase("setUpgradeLevel")){

            if (!player.hasPermission("skyblock.others.setUpgradeLevel") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if (args.length < 4) {
                player.sendMessage(ChatColor.YELLOW + "Usage: /skyblock setUpgradeLevel <player> <type> <level>");
            }

            Player target = Bukkit.getPlayer(args[1]);
            String targetUUID = target.getUniqueId().toString();


            islandDataManager.setUpgradeLevel(targetUUID, args[2].toLowerCase(), Integer.parseInt(args[3]));

            if (args[2].equalsIgnoreCase("border")) {
                try {
                    islandManager.updateWorldBorder(targetUUID);
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to update world border", e);
                    throw new RuntimeException(e);
                }
            }

            if (islandDataManager.getLevel(targetUUID, args[2].toLowerCase()) == Integer.parseInt(args[3])) {
                messageLibs.sendMessage(player, ChatColor.GREEN + args[1] + "'s "  + args[2] + " level is now " + args[3] + "!");
            } else {
                messageLibs.sendMessage(player, ChatColor.RED + "Failed to set " + args[1] + "'s "  + args[2] + " level to " + args[3] + "!");
            }

        } else if (args[0].equalsIgnoreCase("reload")) {

            if (!player.hasPermission("skyblock.reload") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            islandDataManager.reload();
            plugin.reloadConfig();

            messageLibs.sendMessage(player, ChatColor.GREEN + "Config reloaded!");

        } else {
            messageLibs.sendMessage(player, ChatColor.RED + "Unknown command!");
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender,
                                      Command command,
                                      String alias,
                                      String[] args) {

        if (!(sender instanceof Player)) {
            return new ArrayList<>();
        }

        if (!sender.hasPermission("skyblock.command.*")) {

        }

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("skyblock.command.template.*")) {completions.add("template");}
            if (sender.hasPermission("skyblock.command.reload")) {completions.add("reload");}
            if (sender.hasPermission("skyblock.others.setUpgradeLevel")) {completions.add("setUpgradeLevel");}
            if (sender.hasPermission("skyblock.others.teleport")) {completions.add("teleport");}
            return completions;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("template")) {
                completions.add("create");
                completions.add("delete");
                completions.add("teleport");
                completions.add("setSpawn");
            }

            if (args[0].equalsIgnoreCase("setUpgradeLevel")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
            return completions;
        }

        if (args.length == 3) {


            if (args[0].equalsIgnoreCase("template")) {

                if (args[1].equalsIgnoreCase("teleport")) {
                    File[] files = templatesFolder.listFiles();

                    if (files != null) {
                        for (File file : files) {
                            completions.add(file.getName().replace(".slime", ""));
                        }
                    }
                }

                if (args[1].equalsIgnoreCase("delete")) {
                    File[] files = templatesFolder.listFiles();

                    if (files != null) {
                        for (File file : files) {
                            completions.add(file.getName().replace(".slime", ""));
                        }
                    }
                }

                if (args[1].equalsIgnoreCase("setSpawn")) {
                    File[] files = templatesFolder.listFiles();

                    if (files != null) {
                        for (File file : files) {
                            completions.add(file.getName().replace(".slime", ""));
                        }
                    }
                }

            }

            if (args[0].equalsIgnoreCase("setUpgradeLevel")) {
                completions.add("generator");
                completions.add("border");
            }
        }


        return completions;
    }

}

