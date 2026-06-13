package net.teuos.skyblock.commands;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.commands.island.*;
import net.teuos.skyblock.commands.island.upgrade.UpgradeCommand;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.*;
import net.teuos.skyblock.protection.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IslandCommands implements CommandExecutor, TabCompleter {

    public final Map<String, SubCommand> subCommands = new HashMap<String, SubCommand>();

    private final IslandLevelManager levelManager;
    private final IslandManager islandManager;
    private final IslandPermissionsManager islandPermissionsManager;
    private final IslandDataManager islandDataManager;
    private final MessageLibs messageLibs;
    private final EcoManager ecoManager;
    private final Skyblock plugin;
    private final File templatesFolder;
    private final GUIManager guiManager;
    private final TemplateDataManager templateDataManager;
    private final SellManager sellManager;
    private final PermissionManager permissionManager;

    public IslandCommands(IslandLevelManager levelManager, IslandManager islandManager, IslandPermissionsManager islandPermissionsManager, IslandDataManager islandDataManager, MessageLibs messageLibs, EcoManager ecoManager, Skyblock plugin, File templatesFolder, GUIManager guiManager, TemplateDataManager templateDataManager, SellManager sellManager, PermissionManager permissionManager) {
        this.levelManager = levelManager;
        this.islandManager = islandManager;
        this.islandPermissionsManager = islandPermissionsManager;
        this.islandDataManager = islandDataManager;
        this.messageLibs = messageLibs;
        this.ecoManager = ecoManager;
        this.plugin = plugin;
        this.templatesFolder = templatesFolder;
        this.guiManager = guiManager;
        this.templateDataManager = templateDataManager;
        this.sellManager = sellManager;
        this.permissionManager = permissionManager;

        register(new HelpCommand());
        register(new TeleportCommand(islandManager, messageLibs));
        register(new UpgradeCommand(islandManager, messageLibs, ecoManager, levelManager, islandDataManager, guiManager, plugin));
        register(new CreateIslandCommand(messageLibs, islandDataManager, islandManager, templateDataManager, guiManager));
        register(new DeleteIslandCommand(guiManager, islandManager, messageLibs));
        register(new VisitCommand(messageLibs, islandDataManager, islandManager));
        register(new TrustCommand(islandDataManager, islandManager, permissionManager, messageLibs, guiManager));
        register(new UntrustCommand(islandDataManager, islandManager, permissionManager, messageLibs));
        register(new BlockCommand(islandDataManager, messageLibs));
        register(new UnblockCommand(islandDataManager, messageLibs));
        register(new SellCommand(guiManager, ecoManager, sellManager, plugin));
        register(new MenuCommand(guiManager, islandManager, this));

    }


    public void register(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            plugin.getLogger().warning("Only players can use this command!");
            return true;
        }

        if (args.length == 0) {
            SubCommand subCommand = subCommands.get("menu");
            return subCommand.execute(player, args);
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());

        if (subCommand == null) {
            player.sendMessage(ChatColor.RED + "Unknown command!");
            return true;

        }

        if (!player.hasPermission(subCommand.getPermission()) && !player.hasPermission("skyblock.admin")) {
            messageLibs.sendMessage(player, ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        return subCommand.execute(player, args);

    }


    @Override
    public List<String> onTabComplete(CommandSender sender,
                                      Command command,
                                      String alias,
                                      String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String current = args[0].toLowerCase();

            for (String commandName : subCommands.keySet()) {
                if (commandName.startsWith(current)) {
                    completions.add(commandName);
                }
            }

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

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("trust")) {
                completions.add("manager");
                completions.add("builder");
                completions.add("container");
                completions.add("interact");
                completions.add("visitor");
            }
            return completions;
        }

        return completions;
    }


}
