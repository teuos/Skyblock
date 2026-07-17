package net.teuos.skyblock.commands.island;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class BlockCommand implements SubCommand {

    private final IslandDataManager islandDataManager;
    private final MessageLibs messageLibs;
    private final Skyblock plugin;


    public BlockCommand(IslandDataManager islandDataManager, MessageLibs messageLibs, Skyblock plugin) {
        this.islandDataManager = islandDataManager;
        this.messageLibs = messageLibs;
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "block";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.block";
    }

    @Override
    public boolean execute(Player player, String[] args){
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /island block <player>");
            return true;
        }
        if (islandDataManager.islandExists(player.getUniqueId().toString())) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (!target.hasPlayedBefore() && !target.isOnline()) {
                messageLibs.sendMessage(player, ChatColor.RED + "Player not found!");
                return true;
            }
            islandDataManager.addBlockedPlayer(player.getUniqueId().toString(), target);
            if (islandDataManager.getBlockedStatus(player.getUniqueId().toString(), target)) {
                messageLibs.sendMessage(player,ChatColor.GREEN + args[1] + " is now blocked from your island!");
                Player online = target.getPlayer();
                if (online != null && online.getWorld().getName().equals(player.getUniqueId().toString())) {
                    String spawnWorld = plugin.getConfig().getString("spawn.spawn-world-name", "world");
                    online.teleport(Bukkit.getWorld(spawnWorld).getSpawnLocation());
                }
            }  else {
                messageLibs.sendMessage(player,ChatColor.RED + "Somthing went wrong!");
            }
        } else {
            messageLibs.sendMessage(player,ChatColor.RED +  "you do not have a island!");
        }
        return true;
    }

}
