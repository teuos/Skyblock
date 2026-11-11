package net.teuos.skyblock.commands.skyblock.template;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;

public class TeleportTemplateCommand implements SubCommand {

    private final IslandManager islandManager;
    private final MessageLibs messageLibs;

    public TeleportTemplateCommand(IslandManager islandManager, MessageLibs messageLibs) {
        this.islandManager = islandManager;
        this.messageLibs = messageLibs;
    }

    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public String getPermission() {
        return "skyblock.template.teleport";
    }

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 3) {
            messageLibs.sendMessage(player, ChatColor.YELLOW + "Usage: /skyblockadmin template teleport <template>");
            return true;
        }

        try {
            if (islandManager.loadTemplate(args[2])) {
                World target = Bukkit.getWorld(args[2]);
                player.teleport(target.getSpawnLocation());
                messageLibs.sendMessage(player, ChatColor.GREEN + "Teleported to " + args[2] + "!");
            }
        } catch (IOException e) {
            messageLibs.sendMessage(player, ChatColor.RED + "Failed to teleport to " + args[2] + "!");
            throw new RuntimeException(e);
        }
        return true;
    }
}
