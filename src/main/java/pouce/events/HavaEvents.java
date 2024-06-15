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

import static pouce.HavaPouce.getPlugin;
import static pouce.HavaPouce.sendHavaMessage;

public class HavaEvents implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ItemStack item = new ItemStack(Material.COMPASS, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§7 Menu Gui");
        item.setItemMeta(itemMeta);

//        ItemStack item2 = new ItemStack(Material.DIAMOND_SWORD);
//        ItemMeta meta2 = item2.getItemMeta();
//        meta2.setDisplayName("§7SWORD");
//        NamespacedKey key = new NamespacedKey(getPlugin(), "unique_id");
//        meta2.getPersistentDataContainer().set(key, PersistentDataType.STRING, "Menu PVP");
//        item2.setItemMeta(meta2);



//        player.getInventory().clear();
        player.getInventory().setItem(0, item);
//        player.getInventory().addItem(item2);

    }

    @EventHandler
    public void onCompassInteract(PlayerInteractEvent event) {


        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Action action = event.getAction();

        if(item != null){
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                if(item.getType() == Material.COMPASS) {
                    if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§7 Menu Gui")){
                        Inventory inventory = Bukkit.createInventory(null, 54, "§6Menu de navigation");

                        ItemStack item2 = new ItemStack(Material.RED_BED, 1);
                        ItemMeta itemMeta2 = item2.getItemMeta();
                        itemMeta2.setDisplayName("§7 BED WARS");
                        item2.setItemMeta(itemMeta2);
                        inventory.setItem(51, item2);


//                        player.getInventory().addItem(item2);


                        for(Player p : Bukkit.getOnlinePlayers()){
                            ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
                            SkullMeta meta = (SkullMeta) head.getItemMeta();
                            meta.setOwner(p.getName());
                            meta.setDisplayName("§c" + p.getName());
                            head.setItemMeta(meta);

                            inventory.addItem(head);

                        }
                        player.openInventory(inventory);
                        player.setMetadata("GuiProtect", new FixedMetadataValue(getPlugin(), "GuiProtect"));
                    }
                }
            }
        }
    }
//    @EventHandler
//    public void onClick(InventoryClickEvent event) {
//        Player player = (Player) event.getWhoClicked();
//        ItemStack item = event.getCurrentItem();
//        if(item != null){
//            if(item.getType() == Material.RED_BED){
//                if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§7 BED WARS")){
//                    Inventory inv = Bukkit.createInventory(null, 9, "combat");
//
//                    ItemStack woodSword = new ItemStack(Material.WOODEN_SWORD);
//                    ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
//                    ItemMeta woodMeta = woodSword.getItemMeta();
//                    woodMeta.addEnchant(Enchantment.DAMAGE_ALL, 250, true);
//                    ItemMeta stoneMeta = stoneSword.getItemMeta();
//                    woodSword.setItemMeta(woodMeta);
//                    stoneSword.setItemMeta(stoneMeta);
//
//                    inv.setItem(0, woodSword);
//                    inv.setItem(2, woodSword);
//                    player.openInventory(inv);
//
//
//                }
//            }
//        }
//    }
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
    @EventHandler
    public void onClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        if(player.hasMetadata("GuiProtect")){
            event.setCancelled(true);
        }


    }
    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();

        if(player.hasMetadata("GuiProtect")){
            player.removeMetadata("GuiProtect", getPlugin());
        }
    }


    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    String uniqueIdValue = meta.getDisplayName(); // Utiliser la valeur de l'affichage du nom comme identifiant unique
                    HavaGuiItem guiItem = HavaGuiItem.getItemByUniqueIdValue(uniqueIdValue);
                    if (guiItem != null) {
                        HavaGui gui = HavaGui.getGuiByName(guiItem.getGuiTarget());
                        if (gui != null) {
                            gui.openInventory(player);
                        } else {
                            sendHavaMessage(player, "Le GUI cible n'existe pas.");
                        }
                    }
                }
            }
        }
    }
}


