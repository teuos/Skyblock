package net.teuos.skyblock.commands.skyblock.resource_region;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.RegionDataManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ResourceRegionCommand implements SubCommand {

    public final Map<String, SubCommand> resourceRegionCommands = new HashMap<>();

    private final MessageLibs messageLibs;

    public ResourceRegionCommand(final MessageLibs messageLibs, final RegionDataManager regionDataManager) {
        this.messageLibs = messageLibs;

        register(new CreateResourceRegionCommand(messageLibs, regionDataManager));
        register(new DeleteResourceRegionCommand(messageLibs, regionDataManager));
        register(new TeleportResourceRegionCommand(messageLibs, regionDataManager));
        register(new ListResourceRegionsCommand(messageLibs, regionDataManager));

    }

    public void register(SubCommand subCommand) {
        resourceRegionCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public String getName() {
        return "region";
    }

    @Override
    public String getPermission() { return "skyblock.region"; }

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 2){
            messageLibs.sendMessage(player, ChatColor.YELLOW + "Usage: /skyblockadmin region <create|delete|teleport|list>");
            return true;
        }

        SubCommand subCommand = resourceRegionCommands.get(args[1].toLowerCase());
        if (subCommand == null) {
            messageLibs.sendMessage(player, ChatColor.RED + "Unknown subcommand!");
            return true;
        }

        if (!player.hasPermission(subCommand.getPermission()) && !player.hasPermission("skyblock.admin")) {
            messageLibs.sendMessage(player, ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        return subCommand.execute(player, args);

    }



}
