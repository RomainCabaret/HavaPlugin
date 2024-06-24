package pouce.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import pouce.gui.HavaGui;
import pouce.gui.fixedgui.HavaFixedGui;

import java.util.Objects;

import static pouce.HavaPouce.*;
import static pouce.gui.fixedgui.HavaFixedGui.*;

public class HavaInteract {
    public static void onNavInteract(Player player, ItemStack item) {

        NamespacedKey key = new NamespacedKey(getPlugin(), "guiNavAction");
        String uniqueId = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if (uniqueId != null) {
            World world = Bukkit.getWorld(uniqueId);

            if (world == null) {
                sendHavaError(player, "Le monde §f" + uniqueId + " §cn'existe pas.");
                return;
            }

            player.teleport(world.getSpawnLocation());
        }
    }

    public static void onDonjonNavInteract(Player player, ItemStack item) {
        NamespacedKey key = new NamespacedKey(getPlugin(), "guiDonjonNavAction");
        String uniqueId = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if (uniqueId == null) {
            return;
        }


        switch (uniqueId) {
            case "backforwardToDonjonItems": {
                HavaGui gui = HavaGui.getGuiByName("DonjonItems");
                if (gui != null) {
                    gui.openReadonlyInventory(player);
                } else {
                    sendHavaError(player, "Merci de definir le gui DonjonItems");
                }
            break;
            }
            case "melee": {
                Inventory gui = GetMeleeItemFixedGui();
                player.openInventory(gui);
                player.setMetadata("GuiProtect", new FixedMetadataValue(getPlugin(), "GuiProtect"));
                break;
            }
            case "distance": {
                Inventory gui = GetDistanceItemFixedGui();
                player.openInventory(gui);
                player.setMetadata("GuiProtect", new FixedMetadataValue(getPlugin(), "GuiProtect"));
                break;
            }
            case "utilitaire": {
                Inventory gui = GetUtilitaireItemFixedGui();
                player.openInventory(gui);
                player.setMetadata("GuiProtect", new FixedMetadataValue(getPlugin(), "GuiProtect"));
                break;
            }
        }
    }
}
