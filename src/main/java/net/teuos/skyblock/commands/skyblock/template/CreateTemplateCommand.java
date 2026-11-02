package net.teuos.skyblock.commands.skyblock.template;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CreateTemplateCommand implements SubCommand {

    private final IslandManager islandManager;
    private final MessageLibs messageLibs;

    public CreateTemplateCommand(IslandManager islandManager, MessageLibs messageLibs) {
        this.islandManager = islandManager;
        this.messageLibs = messageLibs;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getPermission() {
        return "skyblock.template.create";
    }

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 3) {
            messageLibs.sendMessage(player, ChatColor.RED + "Please supply a name for the template!");
            messageLibs.sendMessage(player, ChatColor.GOLD + "Example usage: skyblock template create templateNameHere");
            return true;
        }

        islandManager.createTemplate(player, args[2]);
        return true;
    }
}
