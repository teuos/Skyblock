package net.teuos.skyblock.commands.skyblock.resource_region;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.RegionDataManager;
import net.teuos.skyblock.objects.ResourceRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportResourceRegionCommand implements SubCommand {

    private final MessageLibs messageLibs;
    private final RegionDataManager regionDataManager;

    public TeleportResourceRegionCommand(MessageLibs messageLibs, RegionDataManager regionDataManager) {
        this.messageLibs = messageLibs;
        this.regionDataManager = regionDataManager;
    }

    @Override
    public String getName() { return "teleport"; }

    @Override
    public String getPermission() { return "skyblock.region.teleport"; }

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 3) {
            messageLibs.sendMessage(player, ChatColor.RED + "Usage: /sba region teleport <name>");
            return true;
        }

        String name = args[2].toLowerCase();
        ResourceRegion region = regionDataManager.getRegion(name);
        if (region == null) {
            messageLibs.sendMessage(player, ChatColor.RED + "A region with that name does not exist!");
            return true;
        }

        World world = Bukkit.getWorld(region.getWorld());
        if (world == null) {
            messageLibs.sendMessage(player, ChatColor.RED + "The world " + region.getWorld() + " for this region is not loaded!");
            return true;
        }

        // Centre of the region on the X/Z axis, standing on top of it
        double x = (region.getMinX() + region.getMaxX()) / 2.0 + 0.5;
        double z = (region.getMinZ() + region.getMaxZ()) / 2.0 + 0.5;
        double y = region.getMaxY() + 1;

        player.teleport(new Location(world, x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch()));
        messageLibs.sendMessage(player, ChatColor.GREEN + "Teleported to resource region " + name + "!");
        return true;
    }
}
