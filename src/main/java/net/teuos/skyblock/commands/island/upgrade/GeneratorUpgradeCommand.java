package net.teuos.skyblock.commands.island.upgrade;

import net.milkbowl.vault.economy.EconomyResponse;
import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.gui.impl.ConfirmUpgradeGUI;
import net.teuos.skyblock.interfaces.UpgradeSubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.EcoManager;
import net.teuos.skyblock.managers.GUIManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;

public class GeneratorUpgradeCommand implements UpgradeSubCommand {

    private final EcoManager ecoManager;
    private final IslandLevelManager islandLevelManager;
    private final MessageLibs messageLibs;
    private final GUIManager guiManager;
    private final Skyblock plugin;

    public GeneratorUpgradeCommand(EcoManager ecoManager, IslandLevelManager islandLevelManager, MessageLibs messageLibs, GUIManager guiManager, Skyblock plugin) {
        this.ecoManager = ecoManager;
        this.islandLevelManager = islandLevelManager;
        this.messageLibs = messageLibs;
        this.guiManager = guiManager;
        this.plugin = plugin;
    }

    @Override
    public String getName(){
        return "generator";
    }

    @Override
    public String getPermission(){
        return "skyblock.island.upgrade.generator";
    }

    @Override
    public boolean execute(Player player){
        double cost = ecoManager.getNextCost(player, "generator");
        String costAppend = plugin.getConfig().getString("price-unit") + ecoManager.getNextCost(player, "generator");
        EconomyResponse r = ecoManager.withdraw(player, cost);
        if (r.type == EconomyResponse.ResponseType.FAILURE) {
            messageLibs.sendMessage(player, ChatColor.RED + "You don't have enough money to upgrade your generator!");
            messageLibs.sendMessage(player, ChatColor.RED + "You have: " + ChatColor.GOLD + ecoManager.getBalance(player) + ChatColor.RED + " You need: " + ChatColor.GOLD + ecoManager.getNextCost(player, "generator"));
            return true;
        }

        this.guiManager.openGUI(new ConfirmUpgradeGUI(result -> {
            try {
                int level = islandLevelManager.increaseGenLevel(player.getUniqueId().toString());
                messageLibs.sendMessage(player,ChatColor.GREEN + "Your generator level is now " + level + "! and cost: Ƿ" + cost);
            } catch (IOException e){
                messageLibs.sendMessage(player,ChatColor.RED + "Failed to upgrade level. If you believe this to be a mistake please report the issue!");
            }
        }, costAppend), player);

        return true;

    }

}
