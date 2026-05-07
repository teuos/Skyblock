package net.teuos.skyblock;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;


public class Listeners implements Listener {

    @EventHandler
    public void onBlockFormToEvent(BlockFromToEvent event) {

        if (event.getBlock().getType() == Material.COBBLESTONE) {
            //event.getNewState().setType(Material.IRON_ORE);
            event.getBlock().setType(Material.COBBLESTONE);
        }

    }

}
