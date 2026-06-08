package net.teuos.skyblock.commands.island;

import net.teuos.skyblock.gui.impl.IslandCreateGUI;
import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.GUIManager;
import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandManager;
import net.teuos.skyblock.managers.TemplateDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CreateIslandCommand implements SubCommand {

    private final MessageLibs messageLibs;
    private final IslandManager islandManager;
    private final IslandDataManager islandDataManager;
    private final TemplateDataManager templateDataManager;
    private final GUIManager guiManager;

    public CreateIslandCommand(MessageLibs messageLibs, IslandDataManager islandDataManager, IslandManager islandManager, TemplateDataManager templateDataManager, GUIManager guiManager) {
        this.messageLibs = messageLibs;
        this.islandDataManager = islandDataManager;
        this.islandManager = islandManager;
        this.templateDataManager = templateDataManager;
        this.guiManager = guiManager;
    }


    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getPermission() {
        return "skyblock.island.create";
    }


    @Override
    public boolean execute(Player player, String[] args){
        if (islandDataManager.islandExists(player.getUniqueId().toString())){
            messageLibs.sendMessage(player,ChatColor.RED + "You already have a skyblock island!");
            return true;
        }
        if (args.length < 2) {
            this.guiManager.openGUI(new IslandCreateGUI(templateDataManager, selected -> {
                if (selected != null) {
                    islandManager.createIsland(player.getPlayer().getUniqueId().toString(), selected);
                    World target = Bukkit.getWorld(player.getUniqueId().toString());
                    player.teleport(target.getSpawnLocation());
                    messageLibs.sendMessage(player,ChatColor.GREEN + "Island has been created!");
                } else {
                    messageLibs.sendMessage(player,ChatColor.RED + "Failed to create island!");
                }
            }), player);
            return true;
        }
        if (islandManager.createIsland(player.getPlayer().getUniqueId().toString(), args[1])) {
            World target = Bukkit.getWorld(player.getUniqueId().toString());
            player.teleport(target.getSpawnLocation());
            messageLibs.sendMessage(player,ChatColor.GREEN + "Island has been created!");
            return true;
        } else {
            messageLibs.sendMessage(player,ChatColor.RED + "Failed to create island!");
            return true;
        }
    }

}
