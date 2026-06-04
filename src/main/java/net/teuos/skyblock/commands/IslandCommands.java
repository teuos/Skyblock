package net.teuos.skyblock.commands;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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

public class IslandCommands implements CommandExecutor, TabCompleter {

    private final IslandLevelManager levelManager;
    private final IslandManager islandManager;
    private final IslandPermissionsManager islandPermissionsManager;
    private final IslandDataManager islandDataManager;
    private final MessageLibs messageLibs;
    private final EcoManager ecoManager;
    private final Skyblock plugin;
    private final File templatesFolder;

    public IslandCommands(IslandLevelManager levelManager, IslandManager islandManager, IslandPermissionsManager islandPermissionsManager, IslandDataManager islandDataManager, MessageLibs messageLibs, EcoManager ecoManager, Skyblock plugin, File templatesFolder) {
        this.levelManager = levelManager;
        this.islandManager = islandManager;
        this.islandPermissionsManager = islandPermissionsManager;
        this.islandDataManager = islandDataManager;
        this.messageLibs = messageLibs;
        this.ecoManager = ecoManager;
        this.plugin = plugin;
        this.templatesFolder = templatesFolder;
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

            if (!player.hasPermission("skyblock.island.help") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            sender.sendMessage(ChatColor.GOLD + "-- Skyblock island help --");
            sender.sendMessage(ChatColor.YELLOW + "[/island create] - Create your Skyblock island.");
            sender.sendMessage(ChatColor.YELLOW + "[/island delete] - Delete your Skyblock island.");
            sender.sendMessage(ChatColor.YELLOW + "[/island teleport] - Teleport to your Skyblock island.");
            sender.sendMessage(ChatColor.YELLOW + "[/island upgrade] - upgrade your Skyblock island.");
        }


        else if (args[0].equalsIgnoreCase("upgrade")){

            if (!player.hasPermission("skyblock.island.upgrade") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if (args[1].equalsIgnoreCase("generator")){

                if (!player.hasPermission("skyblock.island.upgrade.generator") && !player.hasPermission("skyblock.admin")) {
                    messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }

                double cost = ecoManager.getNextCost(player, "generator");

                EconomyResponse r = ecoManager.withdraw(player, cost);

                if (r.type == EconomyResponse.ResponseType.FAILURE) {
                    messageLibs.sendMessage(player, ChatColor.RED + "You don't have enough money to upgrade your generator!");
                    messageLibs.sendMessage(player, ChatColor.RED + "You have: " + ChatColor.GOLD + ecoManager.getBalance(player) + ChatColor.RED + " You need: " + ChatColor.GOLD + ecoManager.getNextCost(player, "generator"));
                    return true;
                }

                try {
                    int level = levelManager.increaseGenLevel(player.getUniqueId().toString());
                    messageLibs.sendMessage(player,ChatColor.GREEN + "Your generator level is now " + level + "! and cost: Ƿ" + cost);
                } catch (IOException e){
                    messageLibs.sendMessage(player,ChatColor.RED + "Failed to upgrade level. If you believe this to be a mistake please report the issue!");
                    e.printStackTrace();
                }

            }

            else if (args[1].equalsIgnoreCase("border")){

                if (!player.hasPermission("skyblock.island.upgrade.border") && !player.hasPermission("skyblock.admin")) {
                    messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }

                EconomyResponse r = ecoManager.withdraw(player, ecoManager.getNextCost(player, "border"));

                if (r.type == EconomyResponse.ResponseType.FAILURE) {
                    messageLibs.sendMessage(player, ChatColor.RED + "You don't have enough money to upgrade your border!");
                    messageLibs.sendMessage(player, ChatColor.RED + "You have: " + ChatColor.GOLD + ecoManager.getBalance(player) + ChatColor.RED + " You need: " + ChatColor.GOLD + ecoManager.getNextCost(player, "border"));
                    return true;
                }

                try {
                    int level = levelManager.increaseBorderLevel(player.getUniqueId().toString());
                    double size = levelManager.getBorderSize(player.getUniqueId().toString());
                    islandManager.updateWorldBorder(player.getUniqueId().toString());
                    messageLibs.sendMessage(player,ChatColor.GREEN + "Your border level is now " + level + " and is " + size + "!");
                } catch (IOException e){
                    messageLibs.sendMessage(player,ChatColor.RED + "Failed to upgrade level. If you believe this to be a mistake please report the issue!");
                    e.printStackTrace();
                }
            }

            else {
                messageLibs.sendMessage(player, ChatColor.RED + "Command does not exist!");
            }

        }

        else if (args[0].equalsIgnoreCase("create")) {

            if (!player.hasPermission("skyblock.island.create") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

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
            } else {
                messageLibs.sendMessage(player,ChatColor.RED + "Failed to create island!");
            }


        }

        else if (args[0].equalsIgnoreCase("delete")) {

            if (!player.hasPermission("skyblock.island.delete") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if (args.length >= 2 && args[1].equalsIgnoreCase("confirm")){

                if (islandManager.deleteIsland(player.getUniqueId().toString())){
                    messageLibs.sendMessage(player,ChatColor.GREEN + "Island has been deleted!");
                } else {
                    messageLibs.sendMessage(player,ChatColor.RED + "Failed to delete island!");
                }

            } else {
                player.sendMessage(ChatColor.RED + "THIS ACTION CAN NOT BE UNDONE ALL PROGRESS WILL BE LOST. Are you sure you want to delete your island?");
                player.sendMessage(ChatColor.RED + "if so run /island delete confirm");
            }
        }

        else if (args[0].equalsIgnoreCase("teleport")) {

            if (!player.hasPermission("skyblock.island.teleport") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if (!islandDataManager.islandExists(player.getUniqueId().toString())){
                messageLibs.sendMessage(player,ChatColor.RED + "You do not have a skyblock island!");
                return true;
            }

            try {
                islandManager.loadIsland(player.getUniqueId().toString());
                World target = Bukkit.getWorld(player.getUniqueId().toString());
                player.teleport(target.getSpawnLocation());
                messageLibs.sendMessage(player,ChatColor.GREEN + "Teleported to your island!");
            } catch (IOException e) {
                messageLibs.sendMessage(player,ChatColor.RED + "Failed to teleport your island!");
                throw new RuntimeException(e);
            }
        }

        else if (args[0].equalsIgnoreCase("visit")) {

            if (!player.hasPermission("skyblock.island.visit") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

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


            //OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);

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

        }

        else if (args[0].equalsIgnoreCase("trust")) {

            if (!player.hasPermission("skyblock.island.trust") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            try {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.YELLOW + "Usage: /island trust <player>");
                    return true;
                }

                if (islandDataManager.islandExists(player.getUniqueId().toString())) {
                    islandManager.loadIsland(player.getUniqueId().toString());
                    World target = Bukkit.getWorld(player.getUniqueId().toString());

                    if (islandPermissionsManager.addMember(Bukkit.getPlayer(args[1]), target)) {
                        messageLibs.sendMessage(player,ChatColor.GREEN + args[1] + " is now trusted on your island!");
                    } else {
                        messageLibs.sendMessage(player,ChatColor.RED + "Somthing went wrong!");
                    }
                } else {
                    messageLibs.sendMessage(player,ChatColor.RED +  "You do not have a island!");
                }



            } catch (IOException e) {
                messageLibs.sendMessage(player,ChatColor.RED + "Something went wrong!");
            }
        }

        else if (args[0].equalsIgnoreCase("untrust")) {

            if (!player.hasPermission("skyblock.island.untrust") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            try {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.YELLOW + "Usage: /island untrust <player>");
                    return true;
                }

                if (islandDataManager.islandExists(player.getUniqueId().toString())) {
                    islandManager.loadIsland(player.getUniqueId().toString());
                    World target = Bukkit.getWorld(player.getUniqueId().toString());

                    if (islandPermissionsManager.removeMember(Bukkit.getPlayer(args[1]), target)) {
                        messageLibs.sendMessage(player,ChatColor.GREEN + args[1] + " is no longer trusted on your island!");
                    } else {
                        messageLibs.sendMessage(player,ChatColor.RED + "Somthing went wrong!");
                    }
                } else {
                    messageLibs.sendMessage(player,ChatColor.RED +  "you do not have a island!");
                }

            } catch (IOException e) {
                messageLibs.sendMessage(player,ChatColor.RED + "Somthing went wrong!");
            }

        }

        else if (args[0].equalsIgnoreCase("block")) {
            if (!player.hasPermission("skyblock.island.block") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

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
        }

        else if (args[0].equalsIgnoreCase("unblock")) {
            if (!player.hasPermission("skyblock.island.unblock") && !player.hasPermission("skyblock.admin")) {
                messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

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

        }

        else {
            messageLibs.sendMessage(player,ChatColor.RED +  "Command does not exist!");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,
                                      Command command,
                                      String alias,
                                      String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("create");
            completions.add("delete");
            completions.add("upgrade");
            completions.add("teleport");
            completions.add("help");
            completions.add("visit");
            completions.add("trust");
            completions.add("untrust");
            completions.add("block");
            completions.add("unblock");

            return completions;
        }

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("create")) {

                File[] files = templatesFolder.listFiles();

                if (files != null) {
                    for (File file : files) {
                        completions.add(file.getName().replace(".slime", ""));
                    }
                }

            }

            if (args[0].equalsIgnoreCase("upgrade")) {
                completions.add("generator");
                completions.add("border");
            }

            if (args[0].equalsIgnoreCase("visit")) {

                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (Player p : players) {
                    completions.add(p.getName());
                }
            }

            if (args[0].equalsIgnoreCase("trust")) {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (Player p : players) {
                    completions.add(p.getName());
                }
            }

            if (args[0].equalsIgnoreCase("untrust")) {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (Player p : players) {
                    completions.add(p.getName());
                }
            }

            if (args[0].equalsIgnoreCase("block")) {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (Player p : players) {
                    completions.add(p.getName());
                }
            }

            if (args[0].equalsIgnoreCase("unblock")) {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (Player p : players) {
                    completions.add(p.getName());
                }
            }

            return completions;
        }

        return completions;
    }


}
