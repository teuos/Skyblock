package net.teuos.skyblock.commands.island;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.gui.impl.SellGUI;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.managers.EcoManager;
import net.teuos.skyblock.managers.GUIManager;
import net.teuos.skyblock.managers.SellManager;
import org.bukkit.entity.Player;

public class SellCommand implements SubCommand {

    private final GUIManager guiManager;
    private final EcoManager ecoManager;
    private final SellManager sellManager;
    private final Skyblock plugin;


    public SellCommand(GUIManager guiManager, EcoManager ecoManager, SellManager sellManager, Skyblock plugin) {
        this.guiManager = guiManager;
        this.ecoManager = ecoManager;
        this.sellManager = sellManager;
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "sell";
    }

    @Override
    public String getPermission() {
        return "skyblock.sell";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        this.guiManager.openGUI(new SellGUI(plugin, ecoManager, sellManager, guiManager), player);
        return true;
    }

}
