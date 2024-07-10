package pouce.boss.sand;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import pouce.HavaPouce;
import pouce.boss.HavaBoss;
import pouce.nbt.HavaNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pouce.HavaPouce.getPlugin;
import static pouce.HavaPouce.sendHavaMessage;

public class HavaMagmaBoss extends HavaBoss {
    private BossBar bossBar;
    private MagmaCube magmaCube;
    private int maxSize = 16;
    private List<BukkitTask> bossTasks = new ArrayList<>();

    public HavaMagmaBoss(String name, double health, double damage) {
        super(name, health, damage);
        bossBar = Bukkit.createBossBar("§cMagmaBoss", BarColor.RED, BarStyle.SEGMENTED_12);
    }

    public void spawn(Location location) {
        kill();
        cancelBossTasks();

        Bukkit.broadcastMessage("§cMagmaboss spawn !");
        magmaCube = location.getWorld().spawn(location, MagmaCube.class);
        magmaCube.setSize(2);

        NamespacedKey bossKey = new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonBoss());
        magmaCube.getPersistentDataContainer().set(bossKey, PersistentDataType.STRING, getName());
        magmaCube.setCustomName("§cMagmaBoss");
        magmaCube.setCustomNameVisible(true);
        magmaCube.setInvulnerable(true); // Rendre invincible pendant la croissance
        magmaCube.setGravity(false);
        magmaCube.setAI(false);
        magmaCube.setPersistent(true);
        magmaCube.setRemoveWhenFarAway(false);


        bossBar.setProgress(0);
        bossBar.setVisible(true);

        updateBossBarPlayers();

        BukkitTask growthTask = new BukkitRunnable() {
            int size = magmaCube.getSize();

            @Override
            public void run() {
                if (size < maxSize) {
                    magmaCube.setInvulnerable(true);
                    size++;
                    magmaCube.setSize(size);

                    double progress = (double) size / maxSize;
                    bossBar.setProgress(progress);

                } else {
                    magmaCube.setMaxHealth(getMaxHealth());
                    magmaCube.setHealth(getMaxHealth());
                    magmaCube.setInvulnerable(false);
                    this.cancel();

                    BukkitTask meteorAttackTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (magmaCube != null && !magmaCube.isDead()) {
//                                launchMeteorAttack();
                                launchFireJetAttack();
                            } else {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(getPlugin(), 0L, 1L); // Toutes les 60 secondes (1200 ticks)

                    bossTasks.add(meteorAttackTask);

                    // Démarrer la tâche de mise à jour de la BossBar après la croissance
                    startBossBarTask();
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 5L); // 20L = 1 seconde (20 ticks)

        bossTasks.add(growthTask);
    }

    private void startBossBarTask() {
        BukkitTask bossBarTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (magmaCube != null && !magmaCube.isDead()) {
                    double health = magmaCube.getHealth();
                    double maxHealth = magmaCube.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    double progress = Math.max(0, Math.min(health / maxHealth, 1)); // Assurer que la valeur soit entre 0 et 1
                    bossBar.setProgress(progress);

                    // Mettre à jour les joueurs à proximité
                    updateBossBarPlayers();
                } else {
                    this.cancel(); // Arrêter la tâche si le Magma Cube est mort
                    bossBar.setVisible(false); // Rendre la BossBar invisible si le boss est mort
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 20L); // Mettre à jour toutes les secondes

        bossTasks.add(bossBarTask);
    }

    public void kill() {
        for (World world : Bukkit.getWorlds()) {
            for (MagmaCube existingBoss : world.getEntitiesByClass(MagmaCube.class)) {
                if (existingBoss.getPersistentDataContainer().has(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonBoss()), PersistentDataType.STRING)) {
                    String bossName = existingBoss.getPersistentDataContainer().get(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonBoss()), PersistentDataType.STRING);
                    if (bossName != null && bossName.equals(getName())) {
                        cancelBossTasks();
                        existingBoss.remove();
                    }
                }
            }
        }
    }

    private void launchMeteorAttack() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(magmaCube.getWorld()) && player.getLocation().distance(magmaCube.getLocation()) < 15) {
                Location playerLocation = player.getLocation().clone().add(0, 20, 0); // Spawner la boule de feu 20 blocs au-dessus du joueur
                Vector direction = new Vector(0, -1, 0).multiply(5); // Direction vers le bas

                Fireball fireball = player.getWorld().spawn(playerLocation, Fireball.class);
                fireball.setDirection(direction);
                fireball.setYield(2); // Taille de l'explosion
                fireball.setIsIncendiary(false); // Ne pas mettre le feu
            }
        }
    }
    private void launchFireJetAttack() {
        // Obtenir la localisation du MagmaCube
        Location startLocation = magmaCube.getLocation().clone().add(0, 1.5, 0); // Positionner les particules à hauteur de tête

        // Trouver tous les joueurs dans un rayon de 15 blocs autour du MagmaCube
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(magmaCube.getWorld()) && player.getLocation().distance(magmaCube.getLocation()) < 15) {
                // Direction du jet vers le joueur
                Vector direction = player.getLocation().toVector().subtract(startLocation.toVector()).normalize();

                // Nombre de segments dans le jet
                int segments = 20;
                double distanceBetweenSegments = 1.0;

                // Itérer à travers chaque segment du jet
                for (int i = 0; i < segments; i++) {
                    // Calculer la position de chaque segment
                    Location segmentLocation = startLocation.clone().add(direction.clone().multiply(distanceBetweenSegments * i));

                    // Afficher les particules
                    segmentLocation.getWorld().spawnParticle(Particle.FLAME, segmentLocation, 10, 0.2, 0.2, 0.2, 0.02);

                    // Appliquer des dégâts aux entités dans chaque segment
                    for (Entity entity : segmentLocation.getWorld().getNearbyEntities(segmentLocation, 1, 1, 1)) {
                        if (entity instanceof Player) {
                            ((Player) entity).damage(5.0, magmaCube); // Par exemple, infliger 5 points de dégâts
                        }
                    }
                }
            }
        }
    }

    private void cancelBossTasks() {
        for (BukkitTask task : bossTasks) {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
        bossTasks.clear();
    }

    private void updateBossBarPlayers() {
        bossBar.removeAll();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(magmaCube.getWorld()) && player.getLocation().distance(magmaCube.getLocation()) < 50) {
                bossBar.addPlayer(player);
            }
        }
    }
}

