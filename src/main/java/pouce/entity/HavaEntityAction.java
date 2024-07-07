package pouce.entity;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import pouce.nbt.HavaNBT;

import java.util.List;
import java.util.Random;

import static pouce.HavaPouce.*;

public class HavaEntityAction {
    public static void onDonjonEntityIsHit(EntityDamageEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();

        if (entity.getPersistentDataContainer().has(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonType()), PersistentDataType.STRING)) {
            String entityName = entity.getPersistentDataContainer().get(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonType()), PersistentDataType.STRING);
            HavaEntity havaEntity = HavaEntityUtils.getEntity(entityName);

            double currentHealth = entity.getHealth() - event.getFinalDamage();
            String healthBar = HavaEntityUtils.formatEntityHealth(havaEntity, currentHealth);
            entity.setCustomName(healthBar);
        }

    }

    public static void onDonjonEntityIsDead(EntityDeathEvent event) {
        NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonType());

        String name = event.getEntity().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (name != null) {
            event.getEntity().getPersistentDataContainer().remove(key);
            List<MetadataValue> metadataValues = event.getEntity().getMetadata("dropTable");
            if (!metadataValues.isEmpty()) {
                MetadataValue value = metadataValues.get(0);
                if (value != null && value.value() instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<HavaEntity.DropItem> dropTable = (List<HavaEntity.DropItem>) value.value();
                    event.getDrops().clear();

                    Random random = new Random();
                    for (HavaEntity.DropItem dropItem : dropTable) {
                        if (random.nextDouble() <= dropItem.getProbability()) {
                            int amount = random.nextInt(dropItem.getMaxAmount() - dropItem.getMinAmount() + 1) + dropItem.getMinAmount();
                            ItemStack itemStack = dropItem.getItem().clone();
                            itemStack.setAmount(amount);
                            event.getDrops().add(itemStack);
                        }
                    }
                }
            }
            // Gérer l'XP drop
            List<MetadataValue> xpValues = event.getEntity().getMetadata("xpDrop");
            if (!xpValues.isEmpty()) {
                MetadataValue xpValue = xpValues.get(0);
                if (xpValue != null && xpValue.value() instanceof Integer) {
                    int xpDrop = (Integer) xpValue.value();
                    event.setDroppedExp(xpDrop);
                }
            }
        }
    }

    public static void onDonjonEntityKillPlayer(PlayerDeathEvent event) {

//        Player player = event.getEntity();
//        sendHavaMessage(player, "hio");
//
//        if (player.getKiller() instanceof LivingEntity) {
//            LivingEntity killer = player.getKiller();
//            sendHavaMessage(player, "hio");
//
//            if (killer.getPersistentDataContainer().has(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonType()), PersistentDataType.STRING)) {
//                String entityName = killer.getPersistentDataContainer().get(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonType()), PersistentDataType.STRING);
//                HavaEntity havaEntity = HavaEntityUtils.getEntity(entityName);
//                event.setDeathMessage("Killed by " + havaEntity.getName());
//                sendHavaMessage(player, "hio");
//            }
//        }

    }
}