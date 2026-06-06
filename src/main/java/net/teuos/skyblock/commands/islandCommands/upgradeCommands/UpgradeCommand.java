package net.teuos.skyblock.commands.islandCommands.upgradeCommands;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.interfaces.UpgradeSubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.EcoManager;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class UpgradeCommand implements SubCommand {

    private final Map<String, UpgradeSubCommand> upgrades = new HashMap<>();

    private final IslandManager islandManager;
    private final MessageLibs messageLibs;
    private final IslandDataManager islandDataManager;
    private final EcoManager ecoManager;
    private final IslandLevelManager islandLevelManager;

    public UpgradeCommand(IslandManager islandManager, MessageLibs messageLibs, IslandDataManager islandDataManager, EcoManager ecoManager, IslandLevelManager islandLevelManager) {
        this.islandManager = islandManager;
        this.messageLibs = messageLibs;
        this.islandDataManager = islandDataManager;
        this.ecoManager = ecoManager;
        this.islandLevelManager = islandLevelManager;

        register(new GeneratorUpgradeCommand(ecoManager, islandLevelManager, messageLibs));
        register(new BorderUpgradeCommand(ecoManager, islandLevelManager, messageLibs, islandManager));

    }

    private void register(UpgradeSubCommand subCommand) {
        upgrades.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public String getName() {
        return "upgrade";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.upgrade";
    }

    @Override
    public boolean execute(Player player, String[] args){

        if (args.length < 2) {
            messageLibs.sendMessage(player, ChatColor.RED + "Usage: /island upgrade <generator|border>");
            return true;
        }

        UpgradeSubCommand subCommand = upgrades.get(args[0].toLowerCase());

        if (subCommand == null) {
            messageLibs.sendMessage(player, ChatColor.RED + "Unknown upgrade type: " + args[0]);
            return true;
        }

        if (!player.hasPermission(subCommand.getPermission()) && !player.hasPermission("skyblock.admin")) {
            messageLibs.sendMessage(player, ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        return subCommand.execute(player);
    }

}
