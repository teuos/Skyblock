package net.teuos.skyblock.commands.skyblock.resource_region;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.RegionDataManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DeleteResourceRegionCommand implements SubCommand {

    private final MessageLibs messageLibs;
    private final RegionDataManager regionDataManager;

    public DeleteResourceRegionCommand(MessageLibs messageLibs, RegionDataManager regionDataManager) {
        this.messageLibs = messageLibs;
        this.regionDataManager = regionDataManager;
    }

    @Override
    public String getName() { return "delete"; }

    @Override
    public String getPermission() { return "skyblock.region.delete"; }

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 3) {
            messageLibs.sendMessage(player, ChatColor.RED + "Usage: /sba region delete <name>");
            return true;
        }

        String name = args[2].toLowerCase();
        if (!regionDataManager.exists(name)) {
            messageLibs.sendMessage(player, ChatColor.RED + "A region with that name does not exist!");
            return true;
        }

        regionDataManager.deleteRecord(name);
        messageLibs.sendMessage(player, ChatColor.GREEN + "Deleted resource region " + name + "!");
        return true;
    }
}
