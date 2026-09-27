package net.teuos.skyblock.commands.island;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.GUIManager;
import net.teuos.skyblock.managers.IslandDataManager;
import org.bukkit.entity.Player;

public class IslandPrivateCommand implements SubCommand {

    private final IslandDataManager islandDataManager;
    private final MessageLibs messageLibs;
    private final GUIManager guiManager;

    public IslandPrivateCommand(IslandDataManager islandDataManager, MessageLibs messageLibs, GUIManager guiManager) {
        this.islandDataManager = islandDataManager;
        this.messageLibs = messageLibs;
        this.guiManager = guiManager;
    }

    @Override
    public String getName() {
        return "toggleprivate";
    }

    @Override
    public String getPermission() { return "skyblock.island.toggleprivate"; }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!(islandDataManager.islandExists(player.getUniqueId().toString()))) {
            messageLibs.sendMessage(player, "&cYou do not have a island!");
            return true;
        }


        boolean isPrivate =  islandDataManager.getPrivate(player.getUniqueId().toString());
        islandDataManager.togglePrivate(player.getUniqueId().toString());
        if (isPrivate) {
            messageLibs.sendMessage(player, "&aYour island is now public!");
        } else {
            messageLibs.sendMessage(player, "&aYour island is now private!");
        }

        return true;
    }

}
