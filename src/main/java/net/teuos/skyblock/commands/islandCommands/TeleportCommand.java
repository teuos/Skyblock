package net.teuos.skyblock.commands.islandCommands;


import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeleportCommand implements SubCommand {

    private final IslandManager islandManager;
    private final MessageLibs messageLibs;

    public TeleportCommand(IslandManager islandManager, MessageLibs messageLibs) {
        this.islandManager = islandManager;
        this.messageLibs = messageLibs;
    }

    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.teleport";
    }

    @Override
    public boolean execute(Player player, String[] args){
        switch (islandManager.teleportIsland(player.getUniqueId().toString(), player)){
            case (2):
                messageLibs.sendMessage(player, ChatColor.RED + "Failed to teleport you to your island!");
                return true;
            case (1):
                messageLibs.sendMessage(player,ChatColor.RED + "You do not have a skyblock island!");
                return true;
            case (0):
                messageLibs.sendMessage(player,ChatColor.GREEN + "Teleported to your island!");
                return true;
        }
        return true;
    }

}
