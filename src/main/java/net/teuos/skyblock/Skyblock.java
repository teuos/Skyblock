package net.teuos.skyblock;

import org.bukkit.plugin.java.JavaPlugin;

public final class Skyblock extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        System.out.println("Skyblock is enabled");

        // Register listeners
        getServer().getPluginManager().registerEvents(new Listeners(), this);


    }

    @Override
    public void onDisable() {
        System.out.println("Skyblock is disabled");
    }
}
