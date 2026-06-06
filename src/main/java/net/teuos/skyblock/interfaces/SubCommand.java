package net.teuos.skyblock.interfaces;

import org.bukkit.entity.Player;

public interface SubCommand {

    String getName();
    String getPermission();
    boolean execute(Player player, String[] args);
}
