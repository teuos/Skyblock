package net.teuos.skyblock.commands.island;

import net.teuos.skyblock.gui.impl.ConfirmIsDeleteGUI;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.GUIManager;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class DeleteIslandCommand implements SubCommand {

    private final GUIManager guiManager;
    private final IslandManager islandManager;
    private final MessageLibs messageLibs;

    public DeleteIslandCommand(GUIManager guiManager, IslandManager islandManager, MessageLibs messageLibs) {

        this.guiManager = guiManager;
        this.islandManager = islandManager;
        this.messageLibs = messageLibs;

    }

    @Override
    public String getName() {
        return "delete";
    }

    public String getPermission() {
        return "skyblock.island.delete";
    }

    @Override
    public boolean execute(Player player, String[] args){

        if (!islandManager.islandExists(player.getUniqueId().toString())) {
            messageLibs.sendMessage(player, ChatColor.RED + "You don't have an island!.");
            return true;
        }

        this.guiManager.openGUI(new ConfirmIsDeleteGUI(result -> {
            if (result == true){
                if (islandManager.deleteIsland(player.getUniqueId().toString())){
                    messageLibs.sendMessage(player,ChatColor.GREEN + "Island has been deleted!");
                } else {
                    messageLibs.sendMessage(player,ChatColor.RED + "Failed to delete island!");
                }
            } else {
                messageLibs.sendMessage(player, ChatColor.YELLOW + "Island deletion cancelled.");
            }
        }), player);
        return true;

    }
}
