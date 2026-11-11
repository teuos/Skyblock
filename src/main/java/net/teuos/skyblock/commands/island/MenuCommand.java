package net.teuos.skyblock.commands.island;

import net.teuos.skyblock.commands.IslandCommands;
import net.teuos.skyblock.gui.impl.MainGUI;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.managers.GUIManager;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.entity.Player;

public class MenuCommand implements SubCommand {

    private final GUIManager guiManager;
    private final IslandManager islandManager;
    private final IslandCommands islandCommands;

    public MenuCommand(GUIManager guiManager, IslandManager islandManager, IslandCommands islandCommands) {

        this.guiManager = guiManager;
        this.islandManager = islandManager;
        this.islandCommands = islandCommands;
    }

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.menu";
    }

    @Override
    public boolean execute(Player player, String[] args){
        this.guiManager.openGUI(new MainGUI(islandManager, islandCommands), player);
        return true;
    }

}
