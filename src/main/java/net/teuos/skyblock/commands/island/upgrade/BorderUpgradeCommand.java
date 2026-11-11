package net.teuos.skyblock.commands.island.upgrade;

import net.milkbowl.vault.economy.EconomyResponse;
import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.gui.impl.ConfirmUpgradeGUI;
import net.teuos.skyblock.interfaces.UpgradeSubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.EcoManager;
import net.teuos.skyblock.managers.GUIManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;

public class BorderUpgradeCommand implements UpgradeSubCommand {


    private final EcoManager ecoManager;
    private final IslandLevelManager islandLevelManager;
    private final MessageLibs messageLibs;
    private final IslandManager islandManager;
    private final GUIManager guiManager;
    private final Skyblock plugin;

    public BorderUpgradeCommand(EcoManager ecoManager, IslandLevelManager islandLevelManager, MessageLibs messageLibs, IslandManager islandManager, GUIManager guiManager, Skyblock plugin) {
        this.ecoManager = ecoManager;
        this.islandLevelManager = islandLevelManager;
        this.messageLibs = messageLibs;
        this.islandManager = islandManager;
        this.guiManager = guiManager;
        this.plugin = plugin;
    }


    @Override
    public String getName(){
        return "border";
    }

    @Override
    public String getPermission(){
        return "skyblock.island.upgrade.border";
    }


    @Override
    public boolean execute(Player player){

        String cost = plugin.getConfig().getString("price-unit") + ecoManager.getNextCost(player, "border");

        EconomyResponse r = ecoManager.withdraw(player, ecoManager.getNextCost(player, "border"));
        if (r.type == EconomyResponse.ResponseType.FAILURE) {
            messageLibs.sendMessage(player, ChatColor.RED + "You don't have enough money to upgrade your border!");
            messageLibs.sendMessage(player, ChatColor.RED + "You have: " + ChatColor.GOLD + ecoManager.getBalance(player) + ChatColor.RED + " You need: " + ChatColor.GOLD + cost);
            return true;
        }
            this.guiManager.openGUI(new ConfirmUpgradeGUI(result -> {
                if (result){
                    try {
                        int level = islandLevelManager.increaseBorderLevel(player.getUniqueId().toString());
                        double size = islandLevelManager.getBorderSize(player.getUniqueId().toString());
                        islandManager.updateWorldBorder(player.getUniqueId().toString());
                        messageLibs.sendMessage(player, ChatColor.GREEN + "Your border level is now " + level + " and is " + size + "!");
                    } catch (IOException e){
                        messageLibs.sendMessage(player,ChatColor.RED + "Failed to upgrade level. If you believe this to be a mistake please report the issue!");
                    }
                } else {
                    messageLibs.sendMessage(player, ChatColor.RED + "Upgrade canceled!");
                }
            }, cost), player);
        return true;
    }


}
