package pouce.items.spells.weapons;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import pouce.items.spells.HavaSpell;

import java.util.List;

import static pouce.HavaPouce.getPlugin;
import static pouce.HavaPouce.sendHavaDev;

public class HavaSpellSpeedDash extends HavaSpell {
    public HavaSpellSpeedDash(String uniqueName, List<String> Lore, float cooldown) {
        super(uniqueName, Lore, cooldown);
    }
    public void useSpell(Player player, ItemStack item, PlayerInteractEvent event) {
        player.playSound(player, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1.5f);

        Vector direction = player.getLocation().getDirection().normalize().multiply(2); // Vitesse du dash

        player.setVelocity(direction);

        new BukkitRunnable() {
            int ticksLived = 0;
            int dashDuration = 10; // Durée du dash en ticks

            @Override
            public void run() {
                if (ticksLived > dashDuration) {
                    cancel();
                    return;
                }

                if(!player.hasPotionEffect(PotionEffectType.SPEED)){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20, 1, true, false));
                }

                Location playerLocation = player.getLocation();

                player.getWorld().spawnParticle(Particle.FLAME, playerLocation, 5, 0.1, 0.1, 0.1, 0.01);

                for (Entity entity : player.getWorld().getNearbyEntities(playerLocation, 1, 1, 1)) {
                    if (entity != player && entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;

                        // Calcule la direction du saut
                        Vector jumpDirection = new Vector(0, 1, 0).multiply(1.5); // Ajustez la force du saut ici

                        // Applique le saut
                        livingEntity.setVelocity(jumpDirection);

                        // Inflige des dégâts
                        livingEntity.damage(5, player);
                    }
                }

                ticksLived++;
            }
        }.runTaskTimer(getPlugin(), 0, 1);
    }
}
