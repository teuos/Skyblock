package net.teuos.skyblock.commands.island;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.teuos.skyblock.interfaces.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.help";
    }

    @Override
    public boolean execute(Player player, String[] args){
        player.sendMessage(Component.text("-- Skyblock island help --", NamedTextColor.GOLD));
        player.sendMessage(Component.text("/is menu - Opens the main skyblock menu", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("/is create <none|type> - This lets you create a island.>", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("/is block/unblock - Allows you to block a player from visiting your island", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("/is trust/untrust - Lets you trust or untrust someone on your island", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("/is delete - Deletes your island", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("/is teleport - Teleport to your island", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("/is sell - opens the Sell menu", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("/is visit - lets you visit another persons island", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("/is upgrade <none|border|generator> - lets you upgrade your islands levels", NamedTextColor.YELLOW));
        return true;
    }
}
