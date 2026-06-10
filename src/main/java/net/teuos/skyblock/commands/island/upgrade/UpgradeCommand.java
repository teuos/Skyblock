package net.teuos.skyblock.commands.island.upgrade;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.commands.IslandCommands;
import net.teuos.skyblock.gui.impl.UpgradeGUI;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.interfaces.UpgradeSubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpgradeCommand implements SubCommand {

    private final Map<String, UpgradeSubCommand> upgrades = new HashMap<>();

    private final MessageLibs messageLibs;
    private final GUIManager guiManager;
    private final IslandDataManager islandDataManager;
    private final Skyblock plugin;
    private final EcoManager ecoManager;

    public UpgradeCommand(IslandManager islandManager, MessageLibs messageLibs, EcoManager ecoManager, IslandLevelManager islandLevelManager, IslandDataManager islandDataManager, GUIManager guiManager, Skyblock plugin) {
        this.messageLibs = messageLibs;
        this.guiManager = guiManager;
        this.plugin = plugin;
        this.islandDataManager = islandDataManager;
        this.ecoManager = ecoManager;

        register(new GeneratorUpgradeCommand(ecoManager, islandLevelManager, messageLibs, guiManager, plugin));
        register(new BorderUpgradeCommand(ecoManager, islandLevelManager, messageLibs, islandManager, guiManager, plugin));

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

        if (args == null || args.length < 2) {
            this.guiManager.openGUI(new UpgradeGUI(result -> {

                System.out.println("RESULT FROM GUI: " + result);
                System.out.println("AVAILABLE KEYS: " + upgrades.keySet());
                System.out.println("PLAYER: " + player.getName());

                UpgradeSubCommand subCommand = upgrades.get(result.toLowerCase());

                if (subCommand == null) {
                    messageLibs.sendMessage(player, ChatColor.RED + "Unknown upgrade type: " + result);
                    return;
                }

                if (!player.hasPermission(subCommand.getPermission()) && !player.hasPermission("skyblock.admin")) {
                    messageLibs.sendMessage(player, ChatColor.RED + "You do not have permission to use this command!");
                    return;
                }

                subCommand.execute(player);

            }, islandDataManager, ecoManager, plugin), player);
            return true;
        }

        UpgradeSubCommand subCommand = upgrades.get(args[1].toLowerCase());

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
