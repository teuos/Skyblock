package net.teuos.skyblock.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.teuos.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatInputListener implements Listener {

    private final Map<UUID, Consumer<String>> callbacks = new HashMap<>();
    private final Skyblock plugin;

    public ChatInputListener(Skyblock plugin) {
        this.plugin = plugin;
    }

    public void requestInput(Player player, Consumer<String> callback){
        UUID uuid = player.getUniqueId();
        callbacks.put(uuid, callback);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (callbacks.remove(uuid) != null) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    p.sendMessage(Component.text("Input timed out!", NamedTextColor.RED));
                }
            }
        }, 20L * 10);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        Consumer<String> callback = callbacks.remove(uuid);
        if (callback == null) {return;}
        event.setCancelled(true);
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        Bukkit.getScheduler().runTask(plugin, () -> {
            callback.accept(message);
        });

    }


}

