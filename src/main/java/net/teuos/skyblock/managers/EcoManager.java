package net.teuos.skyblock.managers;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.teuos.skyblock.Skyblock;
import org.bukkit.entity.Player;

public class EcoManager {

    private final Skyblock plugin;
    private final Economy economy;

    private EcoManager(Skyblock plugin, Economy economy) {
        this.plugin = plugin;
        this.economy = economy;
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

}
