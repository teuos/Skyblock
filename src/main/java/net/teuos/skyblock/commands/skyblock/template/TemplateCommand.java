package net.teuos.skyblock.commands.skyblock.template;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TemplateCommand implements SubCommand {

    public final Map<String, SubCommand> templateCommands = new HashMap<>();

    private final MessageLibs messageLibs;

    public TemplateCommand(IslandManager islandManager, MessageLibs messageLibs) {
        this.messageLibs = messageLibs;

        register(new CreateTemplateCommand(islandManager, messageLibs));
        register(new DeleteTemplateCommand(islandManager, messageLibs));
        register(new TeleportTemplateCommand(islandManager, messageLibs));
        register(new SetSpawnTemplateCommand(islandManager, messageLibs));
    }

    private void register(SubCommand subCommand) {
        templateCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public String getName() {
        return "template";
    }

    @Override
    public String getPermission() {
        return "skyblock.template.create";
    }

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 2) {
            messageLibs.sendMessage(player, ChatColor.YELLOW + "Usage: /skyblockadmin template <create|delete|teleport|setSpawn>");
            return true;
        }

        SubCommand subCommand = templateCommands.get(args[1].toLowerCase());

        if (subCommand == null) {
            messageLibs.sendMessage(player, ChatColor.RED + "Unknown command!");
            return true;
        }

        if (!player.hasPermission(subCommand.getPermission()) && !player.hasPermission("skyblock.admin")) {
            messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        return subCommand.execute(player, args);
    }
}
