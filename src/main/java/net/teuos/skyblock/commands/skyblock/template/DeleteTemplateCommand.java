package net.teuos.skyblock.commands.skyblock.template;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DeleteTemplateCommand implements SubCommand {

    private final IslandManager islandManager;
    private final MessageLibs messageLibs;

    public DeleteTemplateCommand(IslandManager islandManager, MessageLibs messageLibs) {
        this.islandManager = islandManager;
        this.messageLibs = messageLibs;
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getPermission() {
        return "skyblock.template.delete";
    }

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 3) {
            messageLibs.sendMessage(player, ChatColor.YELLOW + "Usage: /skyblockadmin template delete <template>");
            return true;
        }

        switch (islandManager.deleteTemplate(args[2])) {
            case (0):
                messageLibs.sendMessage(player, ChatColor.GREEN + "Template deleted!");
                break;
            case (1):
                messageLibs.sendMessage(player, ChatColor.RED + "Template does not exist!");
                break;
            case (2):
                messageLibs.sendMessage(player, ChatColor.RED + "Failed to delete template!");
        }
        return true;
    }
}
