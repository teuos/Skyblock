package net.teuos.skyblock.commands.skyblock;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;

public class AdminTeleportCommand implements SubCommand {

    private final IslandManager islandManager;
    private final IslandDataManager islandDataManager;
    private final MessageLibs messageLibs;

    public AdminTeleportCommand(IslandManager islandManager, IslandDataManager islandDataManager, MessageLibs messageLibs) {
        this.islandManager = islandManager;
        this.islandDataManager = islandDataManager;
        this.messageLibs = messageLibs;
    }

    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public String getPermission() {
        return "skyblock.others.teleport";
    }

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /skyblockadmin teleport <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            messageLibs.sendMessage(player, ChatColor.RED + "Player not found!");
            return true;
        }

        String targetUUID = target.getUniqueId().toString();

        if (islandDataManager.islandExists(targetUUID)) {
            try {
                islandManager.loadIsland(targetUUID);
                player.teleport(Bukkit.getWorld(targetUUID).getSpawnLocation());
                messageLibs.sendMessage(player, ChatColor.GREEN + "Teleported to " + target.getName() + "'s island!");
            } catch (IOException e) {
                messageLibs.sendMessage(player, ChatColor.RED + "Failed to teleport to " + target.getName() + "'s island!");
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
