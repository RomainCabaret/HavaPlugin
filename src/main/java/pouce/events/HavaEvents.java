package pouce.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pouce.HavaPouce;
import pouce.gui.HavaGui;
import pouce.gui.HavaGuiItem;

import java.util.UUID;

import static pouce.HavaPouce.*;
import static pouce.commands.HavaInteract.onDonjonNavInteract;
import static pouce.commands.HavaInteract.onNavInteract;
import static pouce.gui.HavaGui.getGuiByName;

public class HavaEvents implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

// ---

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            if (itemInHand != null && itemInHand.getType() == Material.DIAMOND_SWORD) {
                // Annuler les dégâts de base de l'épée
                event.setDamage(0);

                // Définir les dégâts de base
                double baseDamage = 5.0;
                double strengthDamage = 0.0;
                double strengthMultiplier = 1.0;

                // Vérifiez si le joueur a un effet de potion de force
                if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                    PotionEffect effect = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    if (effect != null) {
                        strengthMultiplier += 0.3 * (effect.getAmplifier() + 1); // Chaque niveau de force augmente les dégâts de 30%
                        strengthDamage = baseDamage * (strengthMultiplier - 1); // Dégâts supplémentaires dus à l'effet de force
                    }
                }

                // Calculer les dégâts totaux
                double totalDamage = baseDamage * strengthMultiplier;

                // Appliquer les dégâts calculés à l'entité
                event.setDamage(totalDamage);

                // Envoyer un message au joueur avec les dégâts infligés
                player.sendMessage(ChatColor.GRAY + "---------");
                player.sendMessage(ChatColor.WHITE + " - Dégâts de base: " + baseDamage + " ⚔");
                if (strengthDamage > 0) {
                    player.sendMessage(ChatColor.RED + " - Bonus de force: " + Math.round(strengthDamage) + " \uD83D\uDC89");
                    player.sendMessage(ChatColor.YELLOW + " - Dégâts totaux : " + totalDamage + " ⚔ + \uD83D\uDC89 ");
                }
                player.sendMessage(ChatColor.GRAY + "---------");

            }

        }
    }

//    INVENTORY

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (player.hasMetadata("GuiProtect")) {
            event.setCancelled(true);
            if(item != null){
                if (item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        onNavInteract(player, item);
                        onDonjonNavInteract(player, item);

                        NamespacedKey key = new NamespacedKey(getPlugin(), "unique_id");
                        String uniqueId = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                        if (uniqueId != null) {

                            sendHavaDev(player, "Unique ID: " + uniqueId);
                            try {
                                if(HavaGuiItem.getItemByUniqueIdValue(uniqueId) != null){
                                    if(uniqueId.equals(HavaGuiItem.getItemByUniqueIdValue(uniqueId).getUniqueIdValue()) && player.hasMetadata("GuiProtect")){
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


        if (player.hasMetadata("GuiProtect")) {
            player.removeMetadata("GuiProtect", getPlugin());
        }

        for (HavaGui gui : HavaGui.GetGuiMap().values()) {
            if (gui.getGuiContent().equals(closedInventory) && !player.hasMetadata("GuiProtect")) {
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
                if(item.getType() == Material.RECOVERY_COMPASS) {
                    if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§7NavMenu")){
                        HavaGui gui = HavaGui.getGuiByName("nav");
                        if(gui != null){
                            gui.openReadonlyInventory(player);
                        } else{
                            HavaPouce.sendHavaError(player, "Merci de crée un gui ayant pour nom nav");
                        }

                        player.setMetadata("GuiProtect", new FixedMetadataValue(getPlugin(), "GuiProtect"));
                    }
                }
            }
        }
    }


}


