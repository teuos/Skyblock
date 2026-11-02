package enums;

import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Set;

public enum RegionType {

    FARM(EnumSet.of(Material.WHEAT, Material.CARROT, Material.POTATOES, Material.BEETROOTS, Material.MELON, Material.PUMPKIN, Material.CACTUS, Material.SUGAR_CANE)),

    TREE(EnumSet.of(Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG, Material.MANGROVE_LOG, Material.CHERRY_LOG)),

    ORE(EnumSet.of(Material.COAL_ORE, Material.IRON_ORE, Material.COPPER_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE));


    private final Set<Material> validSubtypes;

    RegionType(Set<Material> validSubtypes) {
        this.validSubtypes = validSubtypes;
    }

    public boolean isValidSubtype(Material material) {
        return validSubtypes.contains(material);
    }

    public Set<Material> getValidSubtypes() {
        return validSubtypes;
    }

}


