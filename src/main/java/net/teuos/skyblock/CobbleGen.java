package net.teuos.skyblock;

import net.teuos.skyblock.libs.CSVInteract;
import net.teuos.skyblock.managers.IslandLevelManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.io.IOException;

public class CobbleGen implements Listener {

    private final IslandLevelManager levelManager;
    private final CSVInteract csvInteract;

    public CobbleGen(IslandLevelManager levelManager, CSVInteract csvInteract) {
        this.levelManager = levelManager;
        this.csvInteract = csvInteract;
    }


    @EventHandler
    public void onBlockFormEvent(BlockFormEvent event) throws IOException {
        if (event.getNewState().getType() == Material.COBBLESTONE) {
            if (csvInteract.getGenLevel(event.getNewState().getLocation().getWorld().getName()) >= 1) {
                event.getNewState().setType(Material.IRON_ORE);
            }
            else {
                event.getNewState().setType(Material.COAL_ORE);
            }
        }

    }
}


