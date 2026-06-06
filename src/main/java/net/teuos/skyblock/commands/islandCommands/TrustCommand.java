package net.teuos.skyblock.commands.islandCommands;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandManager;
import net.teuos.skyblock.managers.IslandPermissionsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;

public class TrustCommand implements SubCommand {

    private final IslandDataManager islandDataManager;
    private final IslandManager islandManager;
    private final IslandPermissionsManager islandPermissionsManager;
    private final MessageLibs messageLibs;

    public TrustCommand(IslandDataManager islandDataManager, IslandManager islandManager, IslandPermissionsManager islandPermissionsManager, MessageLibs messageLibs) {
        this.islandDataManager = islandDataManager;
        this.islandManager = islandManager;
        this.islandPermissionsManager = islandPermissionsManager;
        this.messageLibs = messageLibs;
    }

    @Override
    public String getName() {
        return "trust";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.trust";
    }

    @Override
    public boolean execute(Player player, String[] args){
        try {
            if (args.length < 2) {
                player.sendMessage(ChatColor.YELLOW + "Usage: /island trust <player>");
                return true;
            }
            if (islandDataManager.islandExists(player.getUniqueId().toString())) {
                islandManager.loadIsland(player.getUniqueId().toString());
                World target = Bukkit.getWorld(player.getUniqueId().toString());
                if (islandPermissionsManager.addMember(Bukkit.getPlayer(args[1]), target)) {
                    messageLibs.sendMessage(player,ChatColor.GREEN + args[1] + " is now trusted on your island!");
                } else {
                    messageLibs.sendMessage(player,ChatColor.RED + "Somthing went wrong!");
                }
            } else {
                messageLibs.sendMessage(player,ChatColor.RED +  "You do not have a island!");
            }
        } catch (IOException e) {
            messageLibs.sendMessage(player,ChatColor.RED + "Something went wrong!");
        }
        return true;
    }

}
