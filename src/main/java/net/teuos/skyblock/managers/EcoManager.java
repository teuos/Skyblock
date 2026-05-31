package net.teuos.skyblock.managers;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.teuos.skyblock.Skyblock;
import org.bukkit.entity.Player;

public class EcoManager {

    private final Skyblock plugin;
    private final Economy economy;
    private final IslandDataManager islandDataManager;

    public EcoManager(Skyblock plugin, Economy economy, IslandDataManager islandDataManager) {
        this.plugin = plugin;
        this.economy = economy;
        this.islandDataManager = islandDataManager;
    }

    public double getBalance(Player player){
        return economy.getBalance(player);
    }

    public boolean hasAmount(Player player, double amount){
        return economy.has(player, amount);
    }

    public EconomyResponse withdraw(Player player, double amount){
        return economy.withdrawPlayer(player, amount);
    }

    public EconomyResponse deposit(Player player, double amount){
        return economy.depositPlayer(player, amount);
    }

    public double getNextCost(Player player, String type){

        double basePrice = plugin.getConfig().getDouble("upgrade-" + type + "-price");
        double scale = plugin.getConfig().getDouble("upgrade-" + type + "-scale");

        int level = islandDataManager.getLevel(
                player.getUniqueId().toString(),
                type
        );

        if (scale < 0){
            return basePrice * level;
        }

        return basePrice * Math.pow(1 + scale, level);
        
    }
}
