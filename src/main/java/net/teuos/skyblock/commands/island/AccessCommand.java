package net.teuos.skyblock.commands.island;

import net.teuos.skyblock.commands.IslandCommands;
import net.teuos.skyblock.gui.impl.AccessGUI;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.GUIManager;
import net.teuos.skyblock.managers.IslandDataManager;
import org.bukkit.entity.Player;

public class AccessCommand implements SubCommand {


    private final GUIManager guiManager;
    private final IslandDataManager islandDataManager;
    private final MessageLibs messageLibs;
    private final IslandCommands islandCommands;

    public AccessCommand(GUIManager guiManager, IslandDataManager islandDataManager, IslandCommands islandCommands, MessageLibs messageLibs) {
        this.guiManager = guiManager;
        this.islandDataManager = islandDataManager;
        this.messageLibs = messageLibs;
        this.islandCommands = islandCommands;
    }

    @Override
    public String getName() {
        return "access";
    }

    @Override
    public String getPermission(){
        return "skyblock.access";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        this.guiManager.openGUI(new AccessGUI(islandDataManager, islandCommands, messageLibs), player);
        return true;
    }

}
