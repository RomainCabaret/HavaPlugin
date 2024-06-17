package pouce.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

import static pouce.HavaPouce.*;

public class HavaInteract {
    public static void onNavInteract(Player player, ItemStack item){

        NamespacedKey key = new NamespacedKey(getPlugin(), "guiNavAction");
        String uniqueId =  item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if (uniqueId != null) {
            World world = Bukkit.getWorld(uniqueId);

            if (world == null) {
                sendHavaError(player, "Le monde §f" + uniqueId  + " §cn'existe pas.");
                return;
            }

            player.teleport(world.getSpawnLocation());
        }
    }
}
