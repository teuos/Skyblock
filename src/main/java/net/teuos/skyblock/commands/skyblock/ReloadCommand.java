package net.teuos.skyblock.commands.skyblock;

import net.teuos.skyblock.Skyblock;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.RegionDataManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReloadCommand implements SubCommand {

    private final IslandDataManager islandDataManager;
    private final RegionDataManager regionDataManager;
    private final MessageLibs messageLibs;
    private final Skyblock plugin;

    public ReloadCommand(IslandDataManager islandDataManager, RegionDataManager regionDataManager, MessageLibs messageLibs, Skyblock plugin) {
        this.islandDataManager = islandDataManager;
        this.regionDataManager = regionDataManager;
        this.messageLibs = messageLibs;
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "skyblock.reload";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        islandDataManager.reload();
        regionDataManager.reload();
        plugin.reloadConfig();

        messageLibs.sendMessage(player, ChatColor.GREEN + "Config reloaded!");
        return true;
    }
}
