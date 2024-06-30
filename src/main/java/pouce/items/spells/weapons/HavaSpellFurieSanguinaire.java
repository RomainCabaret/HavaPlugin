package pouce.items.spells.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import pouce.items.spells.HavaSpell;

import java.util.List;

import static pouce.HavaPouce.getPlugin;
import static pouce.HavaPouce.sendHavaError;

public class HavaSpellFurieSanguinaire extends HavaSpell {

    public HavaSpellFurieSanguinaire(String uniqueName, List<String> Lore, float cooldown) {
        super(uniqueName, Lore, cooldown);
    }

    public void useSpell(Player player, ItemStack item, PlayerInteractEvent event) {


        player.playSound(player, Sound.ENTITY_WOLF_GROWL, 1 ,1.5f );

        // Crée un BukkitRunnable pour gérer le lancement des haches avec un intervalle
        new BukkitRunnable() {
            int axesLaunched = 0;

            @Override
            public void run() {

                if (axesLaunched >= 3) {
                    cancel();
                    return;
                }

                Location targetLocation = player.getTargetBlock(null, 50).getLocation();
                Location playerLocation = player.getEyeLocation();
                // Ajuste légèrement la direction pour chaque hache
                Vector direction = targetLocation.toVector().subtract(playerLocation.toVector()).normalize();
                Vector adjustedDirection = direction.clone().add(new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).multiply(0.1));

                // Crée et configure un ArmorStand pour agir comme une hache lancée
                ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(playerLocation, EntityType.ARMOR_STAND);
                armorStand.setInvulnerable(true);
                armorStand.setVisible(false);
                armorStand.setGravity(false);
                armorStand.setSmall(true);
                armorStand.setMarker(true);
                armorStand.setArms(true);
                armorStand.setItemInHand(new ItemStack(Material.IRON_AXE));
                armorStand.getPersistentDataContainer().set(new NamespacedKey(getPlugin(), "donjonDeleted"), PersistentDataType.BYTE, (byte) 1);


                // Crée un runnable pour déplacer l'ArmorStand
                new BukkitRunnable() {
                    int ticksLived = 0;

                    @Override
                    public void run() {
                        if (ticksLived > 50 || armorStand.isDead()) {
                            armorStand.remove();
                            cancel();
                            return;
                        }

                        Location newLocation = armorStand.getLocation().add(adjustedDirection);

                        if (newLocation.getBlock().getType().isSolid()) {
                            player.playSound(player, Sound.ITEM_SHIELD_BREAK, 1 ,0.2f );
                            armorStand.remove();
                            cancel();
                            return;
                        }

                        armorStand.teleport(newLocation);


                        for (Entity entity : armorStand.getWorld().getNearbyEntities(newLocation, 0.5, 0.5, 0.5)) {
                            if (entity != player && entity != armorStand && !entity.getPersistentDataContainer().has(new NamespacedKey(getPlugin(), "donjonDeleted"), PersistentDataType.BYTE) && entity instanceof LivingEntity) {
                                ((LivingEntity) entity).damage(10, player);
                                armorStand.remove();
                                cancel();
                                break;
                            }
                        }

                        ticksLived++;
                    }
                }.runTaskTimer(getPlugin(), 1, 1);

                player.playSound(player, Sound.ITEM_FLINTANDSTEEL_USE, 1 ,1.2f );
                axesLaunched++;
            }
        }.runTaskTimer(getPlugin(), 0, 1);
    }
}
