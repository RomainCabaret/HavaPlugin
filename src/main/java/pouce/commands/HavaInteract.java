package pouce.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import pouce.HavaPouce;
import pouce.gui.HavaGui;
import pouce.gui.fixedgui.HavaFixedGui;
import pouce.items.HavaDistanceItems;
import pouce.items.HavaItems;
import pouce.items.HavaMeleeItems;
import pouce.items.HavaUtilitaireItems;
import pouce.items.rarity.HavaRarity;
import pouce.items.spells.HavaSpell;
import pouce.items.spells.utils.HavaSpellUtils;
import pouce.items.utils.HavaItemsUtils;
import pouce.nbt.HavaNBT;

import java.util.Arrays;
import java.util.List;

import static pouce.HavaPouce.*;
import static pouce.gui.fixedgui.HavaFixedGui.*;
import static pouce.items.utils.HavaItemsUtils.deleteItem;

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
            case "backforwardToDonjonItemsMelee": {
                player.openInventory(HavaFixedGui.GetMeleeItemFixedGui());
                player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                break;
            }
            case "backforwardToDonjonItemsDistance": {
                player.openInventory(HavaFixedGui.GetDistanceItemFixedGui());
                player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                break;
            }
            case "backforwardToDonjonItemsUtilitaire" : {
                player.openInventory(HavaFixedGui.GetUtilitaireItemFixedGui());
                player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
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
                    player.getInventory().addItem(HavaItemsUtils.GetItemStack(targerItem));
                } else if (event.getClick().isRightClick()) {
                    player.openInventory(HavaFixedGui.GetEditItemFixedGui(targerItem));
                    player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                }
            } else {
                switch (editAction) {
                    case "name":
                        player.closeInventory();
                        sendHavaMessage(player, "Veuillez entrée un nouveau nom d'item");
                        player.setMetadata(HavaNBT.GetRenameItem(), new FixedMetadataValue(getPlugin(), targerItem.getUniqueName()));

                        BukkitRunnable task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (player.hasMetadata(HavaNBT.GetRenameItem())) {
                                    player.removeMetadata(HavaNBT.GetRenameItem(), getPlugin());
                                    sendHavaError(player, "Action annulée : vous n'avez pas fourni de nouveau nom dans les 30 secondes.");
                                }
                            }
                        };
                        task.runTaskLater(getPlugin(), 600L); // 600 ticks = 30 seconds
                        HavaPouce.getTasks().put(player.getUniqueId(), task);

                        break;
                    case "rarity":

                        List<HavaRarity> rarityList = Arrays.asList(HavaRarity.COMMUN, HavaRarity.ATYPIQUE, HavaRarity.RARE, HavaRarity.EPIQUE, HavaRarity.LEGENDAIRE, HavaRarity.MYTHIQUE);
                        HavaRarity currentRarity = targerItem.getRarity();
                        int rarityIndex = rarityList.indexOf(currentRarity);

                        if (event.getClick().isLeftClick()) {
                            rarityIndex++;
                        } else if (event.getClick().isRightClick()) {
                            rarityIndex--;
                        }

                        if(rarityIndex < 0){
                            rarityIndex = rarityList.size()-1;
                        } else if(rarityIndex > rarityList.size()-1){
                            rarityIndex = 0;
                        }

                        targerItem.setRarity(HavaRarity.valueOf(rarityList.get(rarityIndex).toString()));
                        player.openInventory(HavaFixedGui.GetEditItemFixedGui(targerItem));
                        player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));

                        break;
                    case "damage":
                        if (targerItem instanceof HavaMeleeItems) {

                            HavaMeleeItems meleeItem = (HavaMeleeItems) targerItem;
                            if (event.getClick().isLeftClick()) {
                                meleeItem.setDamage(meleeItem.getDamage() + 1);
                            } else if (event.getClick().isRightClick()) {
                                meleeItem.setDamage(meleeItem.getDamage() - 1);
                            }

                            player.openInventory(HavaFixedGui.GetEditItemFixedGui(meleeItem));
                            player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));

                        } else if (targerItem instanceof HavaDistanceItems) {
                            HavaDistanceItems distanceItem = (HavaDistanceItems) targerItem;

                            if (event.getClick().isLeftClick()) {
                                distanceItem.setDamage(distanceItem.getDamage() + 1);
                            } else if (event.getClick().isRightClick()) {
                                distanceItem.setDamage(distanceItem.getDamage() - 1);
                            }


                            player.openInventory(HavaFixedGui.GetEditItemFixedGui(distanceItem));
                            player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));

                        } else {
                            sendHavaError(player, "L'item n'est pas une arme valide");
                        }
                        break;
                    case "strength":
                        if (targerItem instanceof HavaMeleeItems) {

                            HavaMeleeItems meleeItem = (HavaMeleeItems) targerItem;

                            if (event.getClick().isLeftClick()) {
                                meleeItem.setStrength(meleeItem.getStrength() + 1);
                            } else if (event.getClick().isRightClick()) {
                                meleeItem.setStrength(meleeItem.getStrength() - 1);
                            }

                            player.openInventory(HavaFixedGui.GetEditItemFixedGui(meleeItem));
                            player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));

                        } else if (targerItem instanceof HavaDistanceItems) {
                            HavaDistanceItems distanceItem = (HavaDistanceItems) targerItem;

                            if (event.getClick().isLeftClick()) {
                                distanceItem.setStrength(distanceItem.getStrength() + 1);
                            } else if (event.getClick().isRightClick()) {
                                distanceItem.setStrength(distanceItem.getStrength() - 1);
                            }

                            player.openInventory(HavaFixedGui.GetEditItemFixedGui(distanceItem));
                            player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));

                        }
                        else {
                            sendHavaError(player, "L'item n'est pas une arme valide");
                        }
                    break;
                    case "delete":
                        deleteItem(targerItem.getUniqueName());
                        sendHavaMessage(player, "Item supprimé");
                        if(targerItem instanceof HavaMeleeItems){
                            Inventory gui = GetMeleeItemFixedGui();
                            player.openInventory(gui);
                            player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                        } else if (targerItem instanceof HavaDistanceItems) {
                            Inventory gui = GetDistanceItemFixedGui();
                            player.openInventory(gui);
                            player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                        }
                        else if(targerItem instanceof HavaUtilitaireItems){
                            Inventory gui = GetUtilitaireItemFixedGui();
                            player.openInventory(gui);
                            player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                        }
                        else {
                            sendHavaError(player, "Le type de l'item n'est pas reconnus");
                        }
                        break;
                    case "ability": {
                        Inventory gui = GetEditSpellItemFixedGui(targerItem);
                        player.openInventory(gui);
                        player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                        break;
                    }
                    case "editspell": {
                        NamespacedKey keySelectedSpell = new NamespacedKey(getPlugin(), HavaNBT.GetGuiDonjonSelectedSpellAction());
                        String selectedSpell = item.getItemMeta().getPersistentDataContainer().get(keySelectedSpell, PersistentDataType.STRING);

                        HavaSpell spell = HavaSpellUtils.getSpell(selectedSpell);

                        if(spell == null){
                            sendHavaError(player, "L'item n'est pas un spell valide");
                            return;
                        }

                        if(targerItem instanceof HavaMeleeItems){
                            HavaMeleeItems meleeItem = (HavaMeleeItems) targerItem;
                            meleeItem.setSpell(spell);
                            sendHavaMessage(player, "Spell modifié en " + selectedSpell);
                        }

                        if(targerItem instanceof HavaDistanceItems){
                            sendHavaError(player, "PAS ENCORE FONCTIONNEL");
                            return;
                        }

                        Inventory gui = GetEditItemFixedGui(targerItem);
                        player.openInventory(gui);
                        player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                        break;
                    }
                    case "backforwardToDonjonItemsEdit": {
                        Inventory gui = GetEditItemFixedGui(targerItem);
                        player.openInventory(gui);
                        player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                        break;
                    }

                }
            }

        } catch (Exception e) {
            sendHavaError(player, "l'item n'existe pas");
            sendHavaDev(player, e.getMessage());
        }

    }
}
