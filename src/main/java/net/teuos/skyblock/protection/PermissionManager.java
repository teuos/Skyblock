package net.teuos.skyblock.protection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.WeightNode;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PermissionManager {

    private LuckPerms luckPerms;
    public PermissionManager(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    public void createGroup(String groupName) {
        GroupManager gm = luckPerms.getGroupManager();

        if (gm.getGroup(groupName) != null) return;

        Group group = gm.createAndLoadGroup(groupName).join();

        gm.saveGroup(group);

    }

    public void addPermissionToGroup(String groupName, String permission) {

        Group group = luckPerms.getGroupManager().getGroup(groupName);
        if (group == null) return;

        Node node = Node.builder(permission).build();

        group.data().add(node);

        luckPerms.getGroupManager().saveGroup(group);
    }

    public void addWeightToGroup(String groupName, int weight) {
        Group group = luckPerms.getGroupManager().getGroup(groupName);
        if (group == null) return;
        Node node = Node.builder("weight." + weight).build();
        group.data().add(node);
        luckPerms.getGroupManager().saveGroup(group);
    }


    public void addInheritanceToGroup(String groupName, String inheritance) {

        Group group = luckPerms.getGroupManager().getGroup(groupName);
        if (group == null) return;

        Node inheritNode = InheritanceNode.builder(inheritance).build();

        group.data().add(inheritNode);

        luckPerms.getGroupManager().saveGroup(group);

    }

    public void setPlayerTrustLevel(OfflinePlayer player, String islandName, String group){

        User user = luckPerms.getUserManager().loadUser(player.getUniqueId()).join();

        if (user == null) return;

        Node node = Node.builder("group.skyblock_" + group).withContext("world", islandName).build();

        user.data().add(node);
        luckPerms.getUserManager().saveUser(user);

    }

    public TrustLevel getPlayerTrustLevel(UUID uuid, String islandName) {

        User user = luckPerms.getUserManager().loadUser(uuid).join();
        if (user == null) return TrustLevel.VISITOR;

        return user.getNodes().stream()
                .filter(n -> n.getKey().startsWith("group.skyblock_"))
                .filter(n -> n.getContexts().getAnyValue("world")
                        .map(w -> w.equals(islandName))
                        .orElse(false))
                .map(n -> n.getKey().replace("group.skyblock_", "").toUpperCase())
                .map(name -> {
                    try {
                        return TrustLevel.valueOf(name);
                    } catch (IllegalArgumentException e) {
                        return TrustLevel.VISITOR;
                    }
                })
                .findFirst()
                .orElse(TrustLevel.VISITOR);
    }

    public void removePlayerTrustLevel(OfflinePlayer player, String islandName){
        User user = luckPerms.getUserManager().loadUser(player.getUniqueId()).join();
        if (user == null) return;
        user.data().clear(node ->
            node.getContexts().getAnyValue("world")
                    .map(world -> world.equals(islandName))
                    .orElse(false)
        );
        luckPerms.getUserManager().saveUser(user);
    }



    public enum TrustLevel {

        VISITOR(Component.text("VISITOR", NamedTextColor.GREEN)),
        INTERACT(Component.text("INTERACT", NamedTextColor.YELLOW)),
        CONTAINER(Component.text("CONTAINER", NamedTextColor.GOLD)),
        BUILDER(Component.text("BUILDER", NamedTextColor.AQUA)),
        MANAGER(Component.text("MANAGER", NamedTextColor.RED));

        private final Component display;

        TrustLevel(Component display) {
            this.display = display;
        }

        public Component displayName() {
            return display;
        }
    }




    public void loadGroups(FileConfiguration config) {

        for (String groupName : config.getKeys(false)) {

            String fullGroup = "skyblock_" + groupName;

            createGroup(fullGroup);

            addWeightToGroup(fullGroup, config.getInt(groupName + ".weight", 0));

            List<String> perms = config.getStringList(groupName + ".permissions");
            for (String perm : perms) {
                addPermissionToGroup(fullGroup, perm);
            }

            List<String> parents = config.getStringList(groupName + ".inherits");
            for (String parent : parents) {
                addInheritanceToGroup(fullGroup, "skyblock_" + parent);
            }

        }

    }


}
