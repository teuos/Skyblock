package net.teuos.skyblock.commands;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.commands.skyblock.AdminTeleportCommand;
import net.teuos.skyblock.commands.skyblock.ReloadCommand;
import net.teuos.skyblock.commands.skyblock.SetUpgradeLevelCommand;
import net.teuos.skyblock.commands.skyblock.resource_region.ResourceRegionCommand;
import net.teuos.skyblock.commands.skyblock.template.TemplateCommand;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.*;
import net.teuos.skyblock.objects.RegionType;
import net.teuos.skyblock.objects.ResourceRegion;
import org.bukkit.Material;
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

public class SkyblockCommands implements CommandExecutor, TabCompleter {

    public final Map<String, SubCommand> subCommands = new HashMap<String, SubCommand>();

    private final IslandLevelManager levelManager;
    private final IslandManager islandManager;
    private final IslandPermissionsManager islandPermissionsManager;
    private final Skyblock plugin;
    private final MessageLibs messageLibs;
    private final IslandDataManager islandDataManager;
    private final File templatesFolder;
    private final TemplateDataManager templateDataManager;
    private final RegionDataManager regionDataManager;

    public SkyblockCommands(IslandLevelManager levelManager, IslandManager islandManager, IslandPermissionsManager islandPermissionsManager, Skyblock plugin, MessageLibs messageLibs, IslandDataManager islandDataManager, File templatesFolder, TemplateDataManager templateDataManager, RegionDataManager regionDataManager) {
        this.levelManager = levelManager;
        this.islandManager = islandManager;
        this.islandPermissionsManager = islandPermissionsManager;
        this.plugin = plugin;
        this.messageLibs = messageLibs;
        this.islandDataManager = islandDataManager;
        this.templatesFolder = templatesFolder;
        this.templateDataManager = templateDataManager;
        this.regionDataManager = regionDataManager;

        register(new TemplateCommand(islandManager, messageLibs));
        register(new AdminTeleportCommand(islandManager, islandDataManager, messageLibs));
        register(new SetUpgradeLevelCommand(islandManager, islandDataManager, messageLibs, plugin));
        register(new ReloadCommand(islandDataManager, regionDataManager, messageLibs, plugin));
        register(new ResourceRegionCommand(messageLibs, regionDataManager));
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
            messageLibs.sendMessage(player, ChatColor.YELLOW + "Usage: /" + label + " <template|region|teleport|setUpgradeLevel|reload>");
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());

        if (subCommand == null) {
            messageLibs.sendMessage(player, ChatColor.RED + "Unknown command!");
            return true;
        }

        if (!player.hasPermission(subCommand.getPermission()) && !player.hasPermission("skyblock.admin")) {
            messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
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

        if (!(sender instanceof Player)) {
            return completions;
        }

        if (args.length == 1) {
            String current = args[0].toLowerCase();

            for (SubCommand subCommand : subCommands.values()) {
                if (!sender.hasPermission(subCommand.getPermission()) && !sender.hasPermission("skyblock.admin")) {
                    continue;
                }
                if (subCommand.getName().toLowerCase().startsWith(current)) {
                    completions.add(subCommand.getName());
                }
            }

            return completions;
        }

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("template")) {
                TemplateCommand templateCommand = (TemplateCommand) subCommands.get("template");
                for (SubCommand subCommand : templateCommand.templateCommands.values()) {
                    completions.add(subCommand.getName());
                }
            }

            if (args[0].equalsIgnoreCase("region")) {
                ResourceRegionCommand regionCommand = (ResourceRegionCommand) subCommands.get("region");
                for (SubCommand subCommand : regionCommand.resourceRegionCommands.values()) {
                    completions.add(subCommand.getName());
                }
            }

            if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("setUpgradeLevel")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
            }

            return filter(completions, args[1]);
        }

        if (args.length == 3) {

            if (args[0].equalsIgnoreCase("template")
                    && (args[1].equalsIgnoreCase("teleport") || args[1].equalsIgnoreCase("delete"))) {

                File[] files = templatesFolder.listFiles();

                if (files != null) {
                    for (File file : files) {
                        completions.add(file.getName().replace(".slime", ""));
                    }
                }
            }

            if (args[0].equalsIgnoreCase("region")
                    && (args[1].equalsIgnoreCase("teleport") || args[1].equalsIgnoreCase("delete"))) {
                for (ResourceRegion region : regionDataManager.getRegions()) {
                    completions.add(region.getName());
                }
            }

            if (args[0].equalsIgnoreCase("setUpgradeLevel")) {
                completions.add("generator");
                completions.add("border");
            }

            return filter(completions, args[2]);
        }

        if (args.length == 4) {

            // /sba region create <name> <type>
            if (args[0].equalsIgnoreCase("region") && args[1].equalsIgnoreCase("create")) {
                for (RegionType type : RegionType.values()) {
                    completions.add(type.name().toLowerCase());
                }
            }

            return filter(completions, args[3]);
        }

        if (args.length == 5) {

            // /sba region create <name> <type> <subtype>
            if (args[0].equalsIgnoreCase("region") && args[1].equalsIgnoreCase("create")) {
                try {
                    RegionType type = RegionType.valueOf(args[3].toUpperCase());
                    for (Material material : type.getValidSubtypes()) {
                        completions.add(material.name().toLowerCase());
                    }
                } catch (IllegalArgumentException ignored) {
                    // Unknown region type, nothing to suggest
                }
            }

            return filter(completions, args[4]);
        }

        return completions;
    }

    private List<String> filter(List<String> completions, String current) {
        List<String> filtered = new ArrayList<>();
        for (String completion : completions) {
            if (completion.toLowerCase().startsWith(current.toLowerCase())) {
                filtered.add(completion);
            }
        }
        return filtered;
    }

}
