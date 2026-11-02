package net.teuos.skyblock.commands.skyblock;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.logging.Level;

public class SetUpgradeLevelCommand implements SubCommand {

    private final IslandManager islandManager;
    private final IslandDataManager islandDataManager;
    private final MessageLibs messageLibs;
    private final Skyblock plugin;

    public SetUpgradeLevelCommand(IslandManager islandManager, IslandDataManager islandDataManager, MessageLibs messageLibs, Skyblock plugin) {
        this.islandManager = islandManager;
        this.islandDataManager = islandDataManager;
        this.messageLibs = messageLibs;
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "setUpgradeLevel";
    }

    @Override
    public String getPermission() {
        return "skyblock.others.setUpgradeLevel";
    }

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 4) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /skyblockadmin setUpgradeLevel <player> <type> <level>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            messageLibs.sendMessage(player, ChatColor.RED + "Player not found!");
            return true;
        }

        String targetUUID = target.getUniqueId().toString();

        islandDataManager.setUpgradeLevel(targetUUID, args[2].toLowerCase(), Integer.parseInt(args[3]));

        if (args[2].equalsIgnoreCase("border")) {
            try {
                islandManager.updateWorldBorder(targetUUID);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to update world border", e);
                throw new RuntimeException(e);
            }
        }

        if (islandDataManager.getLevel(targetUUID, args[2].toLowerCase()) == Integer.parseInt(args[3])) {
            messageLibs.sendMessage(player, ChatColor.GREEN + args[1] + "'s " + args[2] + " level is now " + args[3] + "!");
        } else {
            messageLibs.sendMessage(player, ChatColor.RED + "Failed to set " + args[1] + "'s " + args[2] + " level to " + args[3] + "!");
        }
        return true;
    }
}
