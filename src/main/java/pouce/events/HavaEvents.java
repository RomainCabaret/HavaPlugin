package pouce.events;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pouce.HavaPouce;
import pouce.gui.HavaGui;
import pouce.gui.HavaGuiItem;
import pouce.gui.fixedgui.HavaFixedGui;
import pouce.items.HavaDistanceItems;
import pouce.items.HavaItems;
import pouce.items.HavaMeleeItems;
import pouce.items.spells.HavaSpellAction;
import pouce.items.utils.HavaItemsUtils;
import pouce.nbt.HavaNBT;

import java.util.List;
import java.util.UUID;

import static pouce.HavaPouce.*;
import static pouce.commands.HavaInteract.*;
import static pouce.gui.HavaGui.getGuiByName;
import static pouce.items.utils.HavaItemsUtils.UpdatePlayerInventoryItems;

public class HavaEvents implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        UpdatePlayerInventoryItems(player);
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata(HavaNBT.GetRenameItem())) {
            player.removeMetadata(HavaNBT.GetRenameItem(), getPlugin());
        }

        if (player.hasMetadata(HavaNBT.GetNBTGuiProtect())) {
            player.removeMetadata(HavaNBT.GetNBTGuiProtect(), getPlugin());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {


            Player player = (Player) event.getDamager();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if(!itemInHand.hasItemMeta()) {
                return;
            }

            ItemMeta itemMeta = itemInHand.getItemMeta();

            NamespacedKey keyDonjonType = new NamespacedKey(getPlugin(), HavaNBT.GetItemDonjonType());

            if (itemInHand != null && itemMeta.getPersistentDataContainer().has(keyDonjonType)) {

                try{

                    NamespacedKey keyID = new NamespacedKey(getPlugin(), HavaNBT.GetItemDonjonID());
                    String uniqueName = itemMeta.getPersistentDataContainer().get(keyID, PersistentDataType.STRING);

                    HavaItems item = HavaItemsUtils.loadItem(uniqueName);

                    double baseDamage = 0;
                    int strength = 0;

                    if (item instanceof HavaMeleeItems) {
                        HavaMeleeItems meleeItems = (HavaMeleeItems) item;
                        baseDamage = meleeItems.getDamage();
                        strength = meleeItems.getStrength();
                    } else if (item instanceof HavaDistanceItems) {
                        HavaDistanceItems distanceItems = (HavaDistanceItems) item;
                        baseDamage = distanceItems.getDamage();
                        strength = distanceItems.getStrength();
                    }

                    double strengthMultiplier = 1 + (strength / 100.0);
                    double totalDamage = baseDamage * strengthMultiplier;
                    double strengthDamage = totalDamage - baseDamage;

                    event.setDamage(totalDamage);

                    player.sendMessage(ChatColor.GRAY + "---------");
                    player.sendMessage(ChatColor.WHITE + " - Dégâts de base: " + baseDamage + " \uD83E\uDE93");
                    if (strengthDamage > 0) {
                        player.sendMessage(ChatColor.RED + " - Bonus de force: " + Math.round(strengthDamage) + " \uD83D\uDCA5");
                        player.sendMessage(ChatColor.YELLOW + " - Dégâts totaux : " + Math.round(totalDamage) + " \uD83E\uDE93 + \uD83D\uDCA5 ");
                    }
                    player.sendMessage(ChatColor.GRAY + "---------");

                }
                catch (Exception e){
                    sendHavaError(player, e.toString());
                }

            }

        }
    }

//    INVENTORY

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (player.hasMetadata(HavaNBT.GetNBTGuiProtect())) {
            event.setCancelled(true);
            if(item != null){
                if (item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        onNavInteract(player, item);
                        onDonjonNavInteract(player, item);
                        onDonjonItemInteract(player, item, event);

                        NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetNBTUniqueId());
                        String uniqueId = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                        if (uniqueId != null) {

                            sendHavaDev(player, "Unique ID: " + uniqueId);
                            try {
                                if(HavaGuiItem.getItemByUniqueIdValue(uniqueId) != null){
                                    if(uniqueId.equals(HavaGuiItem.getItemByUniqueIdValue(uniqueId).getUniqueIdValue()) && player.hasMetadata(HavaNBT.GetNBTGuiProtect())){
                                        if(HavaGuiItem.getItemByUniqueIdValue(uniqueId).getGuiTarget() != null){
                                            getGuiByName(HavaGuiItem.getItemByUniqueIdValue(uniqueId).getGuiTarget()).openReadonlyInventory(player);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("[HavaPouce] : Erreur a corriger plus tard : " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory closedInventory = event.getInventory();


        if (player.hasMetadata(HavaNBT.GetNBTGuiProtect())) {
            player.removeMetadata(HavaNBT.GetNBTGuiProtect(), getPlugin());
        }

        for (HavaGui gui : HavaGui.GetGuiMap().values()) {
            if (gui.getGuiContent().equals(closedInventory) && !player.hasMetadata(HavaNBT.GetNBTGuiProtect())) {
                gui.saveToConfig();
                break;
            }
        }
    }

    // INTERACT

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {


        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Action action = event.getAction();

        if(item != null){
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                if(item.hasItemMeta()){
                    HavaSpellAction.onDonjonSpellUse(player, item, event);
                }

                if(item.getType() == Material.RECOVERY_COMPASS) {
                    if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§7NavMenu")){
                        HavaGui gui = HavaGui.getGuiByName("nav");
                        if(gui != null){
                            gui.openReadonlyInventory(player);
                        } else{
                            HavaPouce.sendHavaError(player, "Merci de crée un gui ayant pour nom nav");
                        }

                        player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (player.hasMetadata(HavaNBT.GetRenameItem())) {
            event.setCancelled(true);

            String newName = event.getMessage();
            List<MetadataValue> metadataValues = player.getMetadata(HavaNBT.GetRenameItem());

            if (metadataValues.isEmpty()) {
                sendHavaError(player, "Erreur : aucun item à renommer.");
                return;
            }

            String uniqueId = metadataValues.get(0).asString();

            try {
                HavaItems item = HavaItemsUtils.loadItem(uniqueId);
                if (item == null) {
                    sendHavaError(player, "Erreur : l'item n'existe pas.");
                    return;
                }

                ItemStack itemStack = item.getItem();
                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', newName));
                    itemStack.setItemMeta(meta);
                }

                item.setItem(itemStack);

                player.sendMessage(ChatColor.GREEN + "Item renommé en : " + ChatColor.RESET + meta.getDisplayName());
                player.removeMetadata(HavaNBT.GetRenameItem(), getPlugin());

                // Annuler la tâche programmée
                BukkitRunnable task = HavaPouce.getTasks().remove(playerId);
                if (task != null) {
                    task.cancel();
                }

                // Utilisez une tâche synchrone pour ouvrir l'inventaire
                Bukkit.getScheduler().runTask(getPlugin(), () -> {
                    player.openInventory(HavaFixedGui.GetEditItemFixedGui(item));
                    player.setMetadata(HavaNBT.GetNBTGuiProtect(), new FixedMetadataValue(getPlugin(), HavaNBT.GetNBTGuiProtect()));
                });

            } catch (Exception e) {
                sendHavaError(player, "Erreur lors de la mise à jour de l'item : " + e.getMessage());
            }
        }
    }

}


