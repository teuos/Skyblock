package net.teuos.skyblock.managers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.teuos.skyblock.Skyblock;
import org.bukkit.World;
import org.bukkit.entity.Player;


import java.util.UUID;
import java.util.logging.Level;

public class IslandPermissionsManager {


    private final IslandDataManager islandDataManager;
    private final Skyblock plugin;

    public IslandPermissionsManager(IslandDataManager islandDataManager, Skyblock plugin) {
        this.islandDataManager = islandDataManager;
        this.plugin = plugin;
    }


    public void applyDefaultFlags(World world, String uuid) {

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        if (regions == null) {
            return;
        }

        GlobalProtectedRegion global = (GlobalProtectedRegion) regions.getRegion("__global__");

        if (global == null) {
            global = new GlobalProtectedRegion("__global__");
            regions.addRegion(global);
        }

        // Apply default flags
        global.setFlag(Flags.PVP, StateFlag.State.DENY);
        global.setFlag(Flags.TNT, StateFlag.State.DENY);
        global.setFlag(Flags.CREEPER_EXPLOSION, StateFlag.State.DENY);
        global.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);
        global.setFlag(Flags.DAMAGE_ANIMALS, StateFlag.State.DENY);
        global.setFlag(Flags.FIRE_SPREAD, StateFlag.State.DENY);
        global.setFlag(Flags.LAVA_FIRE, StateFlag.State.DENY);

        global.setFlag(Flags.CHEST_ACCESS, StateFlag.State.ALLOW);
        global.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.OWNERS);
        global.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.MEMBERS);
        global.setFlag(Flags.BUILD, StateFlag.State.ALLOW);
        global.setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.OWNERS);
        global.setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);
        global.setFlag(Flags.MOB_DAMAGE, StateFlag.State.ALLOW);
        global.setFlag(Flags.MOB_DAMAGE.getRegionGroupFlag(), RegionGroup.OWNERS);
        global.setFlag(Flags.MOB_DAMAGE.getRegionGroupFlag(), RegionGroup.MEMBERS);
        global.setFlag(Flags.DAMAGE_ANIMALS, StateFlag.State.ALLOW);
        global.setFlag(Flags.DAMAGE_ANIMALS.getRegionGroupFlag(), RegionGroup.OWNERS);
        global.setFlag(Flags.DAMAGE_ANIMALS.getRegionGroupFlag(), RegionGroup.MEMBERS);

        // Set island owner to region owner
        DefaultDomain owner = global.getOwners();
        owner.addPlayer(UUID.fromString(uuid));

        try {
            regions.save();
        } catch (StorageException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save region", e);
        }

    }


    public void applyTemplateFlags(World world){

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        if (regions == null) {
            return;
        }

        GlobalProtectedRegion global = (GlobalProtectedRegion) regions.getRegion("__global__");

        if (global == null) {
            global = new GlobalProtectedRegion("__global__");
            regions.addRegion(global);
        }

        global.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
        global.setFlag(Flags.BLOCK_PLACE, StateFlag.State.DENY);
        global.setFlag(Flags.INTERACT, StateFlag.State.DENY);
        global.setFlag(Flags.USE, StateFlag.State.DENY);
        global.setFlag(Flags.CHEST_ACCESS, StateFlag.State.DENY);
        global.setFlag(Flags.PVP, StateFlag.State.DENY);
        global.setFlag(Flags.TNT, StateFlag.State.DENY);
        global.setFlag(Flags.CREEPER_EXPLOSION, StateFlag.State.DENY);
        global.setFlag(Flags.MOB_SPAWNING, StateFlag.State.DENY);

        try {
            regions.save();
        } catch (StorageException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save region", e);
        }

    }


    public boolean addMember(Player player, World world){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        if (regions == null) {
            return false;
        }

        GlobalProtectedRegion global = (GlobalProtectedRegion) regions.getRegion("__global__");

        global.getMembers().addPlayer(player.getUniqueId());

        try {
            regions.save();
        } catch (StorageException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save region", e);
        }

        return global.getMembers().contains(player.getUniqueId());
    }

    public boolean removeMember(Player player, World world){

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));
        if (regions == null) {
            return false;
        }

        GlobalProtectedRegion global = (GlobalProtectedRegion) regions.getRegion("__global__");

        global.getMembers().removePlayer(player.getUniqueId());

        try {
            regions.save();
        } catch (StorageException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save region", e);
        }

        return !global.getMembers().contains(player.getUniqueId());
    }


    public boolean blockPlayer(Player player, String islandName) {
        islandDataManager.addBlockedPlayer(islandName, player);
        return islandDataManager.getBlockedStatus(islandName, player);
    }

    public boolean unblockPlayer(Player player, String islandName) {
        islandDataManager.removeBlockedPlayer(islandName, player);
        return !islandDataManager.getBlockedStatus(islandName, player);
    }

}
