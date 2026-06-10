package net.teuos.skyblock.managers;

import net.teuos.skyblock.Skyblock;
import org.bukkit.Material;

public class SellManager {

    private final Skyblock skyblock;

    public SellManager(Skyblock skyblock) {
        this.skyblock = skyblock;
    }


    public Boolean isSellable(Material material) {
        return skyblock.getConfig().contains("sell." + material.name());
    }

    public double getPrice(Material material) {
        return skyblock.getConfig().getDouble("sell." + material.name());
    }


}
