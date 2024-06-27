package pouce.commands;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import pouce.gui.HavaGui;
import pouce.gui.fixedgui.HavaFixedGui;
import pouce.items.HavaDistanceItems;
import pouce.items.HavaItems;
import pouce.items.HavaMeleeItems;
import pouce.items.utils.HavaItemsUtils;
import pouce.nbt.HavaNBT;

import static pouce.HavaPouce.*;
import static pouce.gui.fixedgui.HavaFixedGui.*;

public class HavaInteract {
    public static void onNavInteract(Player player, ItemStack item) {

        NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiNavAction());
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
        NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonNavAction());
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
                player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                break;
            }
            case "distance": {
                Inventory gui = GetDistanceItemFixedGui();
                player.openInventory(gui);
                player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                break;
            }
            case "utilitaire": {
                Inventory gui = GetUtilitaireItemFixedGui();
                player.openInventory(gui);
                player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                break;
            }
        }
    }

    public static void onDonjonItemInteract(Player player, ItemStack item, InventoryClickEvent event) {
        NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiDonjonItemAction());
        String uniqueId = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if (uniqueId == null) {
            return;
        }

        try {
            HavaItems targerItem = HavaItemsUtils.loadItem(uniqueId);
            ItemMeta targetMeta = targerItem.getItem().getItemMeta();

            targetMeta.getPersistentDataContainer().remove(key);
            targerItem.getItem().setItemMeta(targetMeta);

            NamespacedKey keyEdit = new NamespacedKey(getPlugin(), HavaNBT.GetNBTGuiEditDonjonItemAction());
            String editAction = item.getItemMeta().getPersistentDataContainer().get(keyEdit, PersistentDataType.STRING);


            if (editAction == null) {
                if (event.getClick().isLeftClick()) {
                    player.getInventory().addItem(targerItem.getItem());
                } else if (event.getClick().isRightClick()) {
                    HavaFixedGui.GetEditItemFixedGui(targerItem);
                    player.openInventory(HavaFixedGui.GetEditItemFixedGui(targerItem));
                    player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                }
            } else {
                switch (editAction) {
                    case "name":
                        sendHavaDev(player, "name");
                        break;
                    case "lore":
                        sendHavaDev(player, "lore");
                        break;
                    case "damage":
                        if (targerItem instanceof HavaMeleeItems) {

                            HavaMeleeItems meleeItem = (HavaMeleeItems) targerItem;
                            if (event.getClick().isRightClick()) {
                                meleeItem.setDamage(meleeItem.getDamage() + 1);
                            } else if (event.getClick().isLeftClick()) {
                                meleeItem.setDamage(meleeItem.getDamage() - 1);
                            }

                            HavaFixedGui.GetEditItemFixedGui(targerItem);
                            player.openInventory(HavaFixedGui.GetEditItemFixedGui(targerItem));
                            player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));

                        } else if (targerItem instanceof HavaDistanceItems) {
                            HavaDistanceItems distanceItem = (HavaDistanceItems) targerItem;

                            if (event.getClick().isRightClick()) {
                                distanceItem.setDamage(distanceItem.getDamage() + 1);
                            } else if (event.getClick().isLeftClick()) {
                                distanceItem.setDamage(distanceItem.getDamage() - 1);
                            }

                            HavaFixedGui.GetEditItemFixedGui(targerItem);
                            player.openInventory(HavaFixedGui.GetEditItemFixedGui(targerItem));
                            player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));

                        } else {
                            sendHavaError(player, "L'item n'est pas une arme valide");
                        }
                        break;
                    case "strength":
                        if (targerItem instanceof HavaMeleeItems) {

                            HavaMeleeItems meleeItem = (HavaMeleeItems) targerItem;

                            if (event.getClick().isRightClick()) {
                                meleeItem.setStrength(meleeItem.getStrength() + 1);
                            } else if (event.getClick().isLeftClick()) {
                                meleeItem.setStrength(meleeItem.getStrength() - 1);
                            }

                            HavaFixedGui.GetEditItemFixedGui(targerItem);
                            player.openInventory(HavaFixedGui.GetEditItemFixedGui(targerItem));
                            player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));

                        } else if (targerItem instanceof HavaDistanceItems) {
                            HavaDistanceItems distanceItem = (HavaDistanceItems) targerItem;

                            if (event.getClick().isRightClick()) {
                                distanceItem.setStrength(distanceItem.getStrength() + 1);
                            } else if (event.getClick().isLeftClick()) {
                                distanceItem.setStrength(distanceItem.getStrength() - 1);
                            }

                            HavaFixedGui.GetEditItemFixedGui(targerItem);
                            player.openInventory(HavaFixedGui.GetEditItemFixedGui(targerItem));
                            player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));

                        }
                        else {
                            sendHavaError(player, "L'item n'est pas une arme valide");
                        }
                    break;
                }
            }

        } catch (Exception e) {
            sendHavaError(player, "l'item n'existe pas");
        }

    }
}
