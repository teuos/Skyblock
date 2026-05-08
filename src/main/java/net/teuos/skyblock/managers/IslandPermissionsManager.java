package net.teuos.skyblock.managers;

import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import java.util.UUID;

public class IslandPermissionsManager {

    private final SlimeLoader loader;
    private final WorldGuard worldGuard;
    private final AdvancedSlimePaperAPI api;

    public IslandPermissionsManager(SlimeLoader loader, WorldGuard worldGuard) {
        this.loader = loader;
        this.worldGuard = worldGuard;
        this.api = AdvancedSlimePaperAPI.instance();

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
        global.setFlag(Flags.CREEPER_EXPLOSION, StateFlag.State.DENY);
        global.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);
        global.setFlag(Flags.DAMAGE_ANIMALS, StateFlag.State.DENY);
        global.setFlag(Flags.FIRE_SPREAD, StateFlag.State.DENY);

        global.setFlag(Flags.CHEST_ACCESS, StateFlag.State.ALLOW);
        global.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.OWNERS);
        global.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.MEMBERS);
        global.setFlag(Flags.BUILD, StateFlag.State.ALLOW);
        global.setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.OWNERS);
        global.setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);

        // Set island owner to region owner
        DefaultDomain owner = global.getOwners();
        owner.addPlayer(UUID.fromString(uuid));



    }

}
