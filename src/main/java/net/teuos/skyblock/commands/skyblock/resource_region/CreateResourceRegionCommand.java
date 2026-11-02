package net.teuos.skyblock.commands.skyblock.resource_region;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.RegionDataManager;
import net.teuos.skyblock.objects.RegionType;
import net.teuos.skyblock.objects.ResourceRegion;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CreateResourceRegionCommand implements SubCommand {


    private final MessageLibs messageLibs;
    private final RegionDataManager regionDataManager;


    public CreateResourceRegionCommand(MessageLibs messageLibs, RegionDataManager regionDataManager) {
        this.messageLibs = messageLibs;
        this.regionDataManager = regionDataManager;
    }

    @Override
    public String getName() { return "create"; }

    @Override
    public String getPermission() { return "skyblock.region.create"; }

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 5) {
            messageLibs.sendMessage(player, ChatColor.RED + "Usage: /sba region create <name> <farm|tree|ore> <subtype>");
            return true;
        }

        String name = args[2].toLowerCase();
        if (regionDataManager.exists(name)) {
            messageLibs.sendMessage(player, ChatColor.RED + "A region with that name already exists!");
            return true;
        }

        RegionType type;
        Material subtype;
        try {
            type = RegionType.valueOf(args[3].toUpperCase());
            subtype = Material.valueOf(args[4].toUpperCase());
        } catch (IllegalArgumentException e) {
            messageLibs.sendMessage(player, ChatColor.RED + "Invalid region type or subtype!");
            return true;
        }

        if (!type.isValidSubtype(subtype)) {
            messageLibs.sendMessage(player, ChatColor.RED + subtype.name() + " is not a valid " + type.name() + " subtype!");
            return true;
        }


        LocalSession session = WorldEdit.getInstance()
                .getSessionManager()
                .get(BukkitAdapter.adapt(player));

        Region selection;

        try {
            selection = session.getSelection(BukkitAdapter.adapt(player.getWorld()));
        } catch (IncompleteRegionException e) {
            messageLibs.sendMessage(player, ChatColor.RED + "Please make a WorldEdit selection first!");
            return true;
        }

        BlockVector3 min = selection.getMinimumPoint();
        BlockVector3 max = selection.getMaximumPoint();

        ResourceRegion region = new ResourceRegion(name, type, subtype, player.getWorld().getName(), min.x(), min.y(), min.z(), max.x(), max.y(), max.z());

        regionDataManager.createRecord(region);
        messageLibs.sendMessage(player, ChatColor.GREEN + "Created a resource region!");
        return true;
    }
}
