package pouce.boss;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import pouce.entity.HavaEntity;
import pouce.nbt.HavaNBT;

import java.util.List;
import java.util.Random;

import static pouce.HavaPouce.getPlugin;
import static pouce.HavaPouce.sendHavaDev;

public class HavaBossAction {

    public static void onDonjonBossIsHit(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (entity.getPersistentDataContainer().has(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonBoss()), PersistentDataType.STRING)) {
                String bossName = entity.getPersistentDataContainer().get(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonBoss()), PersistentDataType.STRING);
                HavaBoss boss = HavaBossUtils.getBoss(bossName);

                if (entity.isInvulnerable()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static void onDonjonBossIsDead(EntityDeathEvent event) {
        if(event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (entity.getPersistentDataContainer().has(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonBoss()), PersistentDataType.STRING)) {
                String bossName = entity.getPersistentDataContainer().get(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonBoss()), PersistentDataType.STRING);
                HavaBoss boss = HavaBossUtils.getBoss(bossName);

                boss.onDead();
            }
        }
    }

    public static void onDonjonBossSlit(SlimeSplitEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();

        if (entity.getPersistentDataContainer().has(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonBoss()), PersistentDataType.STRING)) {
            String bossName = entity.getPersistentDataContainer().get(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonBoss()), PersistentDataType.STRING);
            HavaBoss boss = HavaBossUtils.getBoss(bossName);
            event.setCancelled(true);
        }
    }
}
