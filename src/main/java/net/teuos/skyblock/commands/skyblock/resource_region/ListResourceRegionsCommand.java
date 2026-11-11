package net.teuos.skyblock.commands.skyblock.resource_region;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.RegionDataManager;
import net.teuos.skyblock.objects.ResourceRegion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ListResourceRegionsCommand implements SubCommand {

    private final MessageLibs messageLibs;
    private final RegionDataManager regionDataManager;

    public ListResourceRegionsCommand(MessageLibs messageLibs, RegionDataManager regionDataManager) {
        this.messageLibs = messageLibs;
        this.regionDataManager = regionDataManager;
    }

    @Override
    public String getName() { return "list"; }

    @Override
    public String getPermission() { return "skyblock.region.list"; }

    @Override
    public boolean execute(Player player, String[] args) {

        Collection<ResourceRegion> regions = regionDataManager.getRegions();

        if (regions.isEmpty()) {
            messageLibs.sendMessage(player, ChatColor.YELLOW + "There are no resource regions.");
            return true;
        }

        messageLibs.sendMessage(player, ChatColor.GOLD + "-- Resource regions (" + regions.size() + ") --");
        for (ResourceRegion region : regions) {
            player.sendMessage(ChatColor.YELLOW + region.getName()
                    + ChatColor.GRAY + " - " + region.getType().name().toLowerCase()
                    + " (" + region.getSubtype().name().toLowerCase() + ")"
                    + " in " + region.getWorld()
                    + " [" + region.getMinX() + ", " + region.getMinY() + ", " + region.getMinZ()
                    + " -> " + region.getMaxX() + ", " + region.getMaxY() + ", " + region.getMaxZ() + "]");
        }
        return true;
    }
}
