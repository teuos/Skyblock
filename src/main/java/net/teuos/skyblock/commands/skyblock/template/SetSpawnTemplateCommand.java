package net.teuos.skyblock.commands.skyblock.template;

import net.teuos.skyblock.interfaces.SubCommand;
import net.teuos.skyblock.libs.MessageLibs;
import net.teuos.skyblock.managers.IslandManager;
import net.teuos.skyblock.objects.SpawnPoint;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetSpawnTemplateCommand implements SubCommand {

    private final IslandManager islandManager;
    private final MessageLibs messageLibs;

    public SetSpawnTemplateCommand(IslandManager islandManager, MessageLibs messageLibs) {
        this.islandManager = islandManager;
        this.messageLibs = messageLibs;
    }

    @Override
    public String getName() {
        return "setSpawn";
    }

    @Override
    public String getPermission() {
        return "skyblock.template.setSpawn";
    }

    @Override
    public boolean execute(Player player, String[] args) {

        String world = player.getLocation().getWorld().getName();

        if (islandManager.templateExists(world)) {
            islandManager.updateTemplateSpawn(world, new SpawnPoint(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
        } else {
            messageLibs.sendMessage(player, ChatColor.RED + "This isn't a template island!");
        }
        return true;
    }
}
