package net.teuos.skyblock.commands;

import net.teuos.skyblock.libs.CSVInteract;
import net.teuos.skyblock.managers.CreateIslandManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import net.teuos.skyblock.managers.IslandPermissionsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IslandCommands implements CommandExecutor, TabCompleter {

    private final IslandLevelManager levelManager;
    private final CreateIslandManager islandManager;
    private final IslandPermissionsManager islandPermissionsManager;
    private final CSVInteract csvInteract;


    public IslandCommands(IslandLevelManager levelManager, CreateIslandManager islandManager, IslandPermissionsManager islandPermissionsManager, CSVInteract csvInteract) {
        this.levelManager = levelManager;
        this.islandManager = islandManager;
        this.islandPermissionsManager = islandPermissionsManager;
        this.csvInteract = csvInteract;
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
            sender.sendMessage(ChatColor.GOLD + "-- Skyblock island help --");
            sender.sendMessage(ChatColor.YELLOW + "[/island create] - Create your Skyblock island.");
            sender.sendMessage(ChatColor.YELLOW + "[/island delete] - Delete your Skyblock island.");
            sender.sendMessage(ChatColor.YELLOW + "[/island teleport] - Teleport to your Skyblock island.");
            sender.sendMessage(ChatColor.YELLOW + "[/island level] - level your Skyblock island.");
        }


        if (args[0].equalsIgnoreCase("level")){

            if (args[1].equalsIgnoreCase("generator")){

                try {
                    int level = levelManager.increaseGenLevel(player.getUniqueId().toString());
                    player.sendMessage(ChatColor.GREEN + "Your generator level is now " + level + "!");
                } catch (IOException e){
                    player.sendMessage(ChatColor.RED + "Failed to upgrade level. If you believe this to be a mistake please report the issue!");
                    e.printStackTrace();
                }

            }

            if (args[1].equalsIgnoreCase("border")){
                try {
                    int level = levelManager.increaseBorderLevel(player.getUniqueId().toString());
                    double size = levelManager.getBorderSize(player.getUniqueId().toString());
                    player.sendMessage(ChatColor.GREEN + "Your border level is now " + level + " and is " + size + "!");
                } catch (IOException e){
                    player.sendMessage(ChatColor.RED + "Failed to upgrade level. If you believe this to be a mistake please report the issue!");
                    e.printStackTrace();
                }
            }

        }

        if (args[0].equalsIgnoreCase("create")) {

            if (csvInteract.IslandExists(player.getUniqueId().toString())){
                player.sendMessage(ChatColor.RED + "You already have a skyblock island!");
                return true;
            }

            if (islandManager.createIsland(player.getPlayer().getUniqueId().toString())) {
                World target = Bukkit.getWorld(player.getUniqueId().toString());
                player.teleport(target.getSpawnLocation());
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

            if (!csvInteract.IslandExists(player.getUniqueId().toString())){
                player.sendMessage(ChatColor.RED + "You do not have a skyblock island!");
                return true;
            }

            try {
                islandManager.loadIsland(player.getUniqueId().toString());
                World target = Bukkit.getWorld(player.getUniqueId().toString());
                player.teleport(target.getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "Teleported to your island!");
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + "Failed to teleport your island!");
                throw new RuntimeException(e);
            }
        }

        if (args[0].equalsIgnoreCase("visit")) {

            if (args.length < 2) {
                player.sendMessage("Usage: /island visit <player>");
                return true;
            }

            String playerName = args[1];

            OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);

            try {
                String islandOwner = target.getUniqueId().toString();

                if (csvInteract.IslandExists(islandOwner)){
                    try {
                        islandManager.loadIsland(islandOwner);
                        World world = Bukkit.getWorld(islandOwner);
                        player.teleport(world.getSpawnLocation());
                        player.sendMessage(ChatColor.GREEN + "Teleported to " + playerName + "'s island!");
                    } catch (IOException e) {
                        player.sendMessage(ChatColor.RED + "Failed to teleport to " + playerName + "'s island!");
                        throw new RuntimeException(e);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + playerName + " does not have a island!");
                }


            } catch (IllegalArgumentException e) {
                player.sendMessage("Invalid Player");
                return true;
            }

        }

        if (args[0].equalsIgnoreCase("trust")) {

        }

        if (args[0].equalsIgnoreCase("untrust")) {

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
            completions.add("level");
            completions.add("teleport");
            completions.add("help");
            completions.add("visit");

            return completions;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("level")) {
                completions.add("generator");
                completions.add("border");
            }

            if (args[0].equalsIgnoreCase("visit")) {

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
