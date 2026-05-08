package net.teuos.skyblock.libs;

import net.teuos.skyblock.Skyblock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageLibs {

    private final Skyblock plugin;

    public MessageLibs(Skyblock plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(Player player, String message) {
        String prefix = plugin.getConfig().getString("messages.prefix");

        if (prefix != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix) + " " + message);
        } else {
            player.sendMessage(message);
        }
    }

}
