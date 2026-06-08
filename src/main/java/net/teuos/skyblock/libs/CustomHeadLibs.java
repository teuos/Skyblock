package net.teuos.skyblock.libs;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class CustomHeadLibs {
    public ItemStack getCustomHead(String base64Texture){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta == null){
            return head;
        }

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());

        profile.setProperty(
                new ProfileProperty(
                        "textures",
                        base64Texture
                )
        );

        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

}
