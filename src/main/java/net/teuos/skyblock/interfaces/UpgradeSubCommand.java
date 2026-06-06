package net.teuos.skyblock.interfaces;

import org.bukkit.entity.Player;

public interface UpgradeSubCommand {

    String getName();
    String getPermission();
    boolean execute(Player player);

}
