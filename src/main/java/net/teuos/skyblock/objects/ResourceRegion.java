package net.teuos.skyblock.objects;

import org.bukkit.Location;
import org.bukkit.Material;

public class ResourceRegion {

    private final String name;
    private final RegionType type;
    private final Material subtype;
    private final String world;
    private final int minX, minY, minZ, maxX, maxY, maxZ;


    public ResourceRegion(String name, RegionType type, Material subtype, String world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.name = name;
        this.type = type;
        this.subtype = subtype;
        this.world = world;
        this.minX = minX; this.minY = minY; this.minZ = minZ;
        this.maxX = maxX; this.maxY = maxY; this.maxZ = maxZ;
    }

    public boolean contains(Location loc) {
        if (loc.getWorld() == null || !loc.getWorld().getName().equals(world)) {
            return false;
        }
        int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;

    }

    public String getName() { return name; }
    public RegionType getType() { return type; }
    public Material getSubtype() { return subtype; }
    public String getWorld() { return world; }
    public int getMinX() { return minX; }
    public int getMinY() { return minY; }
    public int getMinZ() { return minZ; }
    public int getMaxX() { return maxX; }
    public int getMaxY() { return maxY; }
    public int getMaxZ() { return maxZ; }

}
