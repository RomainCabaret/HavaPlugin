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
import pouce.boss.HavaBoss;
import pouce.entity.HavaEntity;
import pouce.entity.HavaEntityUtils;
import pouce.entity.sand.HavaRavageur;
import pouce.nbt.HavaNBT;

import java.util.*;

import static pouce.HavaPouce.getPlugin;
import static pouce.HavaPouce.sendHavaDev;

public class HavaMagmaBoss extends HavaBoss {
    private BossBar bossBar;
    private MagmaCube magmaCube;
    private int maxSize = 16;
    private List<BukkitTask> bossTasks = new ArrayList<>();
    private Map<Integer, List<Runnable>> phaseAttacks;
    private Random random = new Random();
    private boolean attackInProgress = false;
    private List<BukkitTask> currentAttackTasks = new ArrayList<>();
    private int currentPhase;

    public HavaMagmaBoss(String name, double health, double damage) {
        super(name, health, damage);
        bossBar = Bukkit.createBossBar("§cMagmaBoss", BarColor.RED, BarStyle.SEGMENTED_12);

        phaseAttacks = new HashMap<>();
        phaseAttacks.put(1, Arrays.asList(this::launchMeteorAttack, this::launchFireJetAttack, this::launchLaserAttack));
        phaseAttacks.put(2, Arrays.asList(this::launchMeteorAttack, this::launchFireJetAttack, this::launchLaserAttack));

        currentPhase = 1;
    }

    private void startAttackTask() {
        BukkitTask attackTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (magmaCube != null && !magmaCube.isDead() && !attackInProgress) {
                    attackInProgress = true;
                    List<Runnable> attacks = phaseAttacks.get(currentPhase);
                    int attackIndex = random.nextInt(attacks.size());
                    currentAttackTasks.clear();
                    attacks.get(attackIndex).run();
                    Bukkit.broadcastMessage(attackIndex + "");
                } else if (magmaCube == null || magmaCube.isDead()) {
                    this.cancel();
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 10L); // 200L = 10 seconds (20 ticks per second * 10 seconds)

        bossTasks.add(attackTask);
    }


    public void spawn(Location location) {
        attackInProgress = false;
        currentPhase = 1;
        kill();
        cancelBossTasks();

        Bukkit.broadcastMessage("§cMagmaboss spawn !");
        magmaCube = location.getWorld().spawn(location, MagmaCube.class);
        magmaCube.setSize(2);

        NamespacedKey bossKey = new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonBoss());
        magmaCube.getPersistentDataContainer().set(bossKey, PersistentDataType.STRING, getName());
        magmaCube.setCustomName("§cMagmaBoss");
        magmaCube.setCustomNameVisible(true);
        magmaCube.setInvulnerable(true);
        magmaCube.setGravity(false);
        magmaCube.setAI(false);
        magmaCube.setPersistent(true);
        magmaCube.setRemoveWhenFarAway(false);

        bossBar.setProgress(0);
        bossBar.setVisible(true);

        updateBossBarPlayers();

        startGrowthAnimation();
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

    public void onDead(){
        cancelCurrentAttacks();
        cancelBossTasks();
        bossBar.setVisible(false);
    }

    private void startHealthCheckTask() {
        BukkitTask healthCheckTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (magmaCube != null && !magmaCube.isDead()) {
                    double health = magmaCube.getHealth();
                    double maxHealth = magmaCube.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    double healthPercentage = (health / maxHealth) * 100;

                    if (healthPercentage <= 70 && currentPhase == 1) {
                        Bukkit.broadcastMessage("§cMagmaBoss entre en phase 2 !");
                        spawnRavageurAttack();
                        startRotateAnimation();
                        currentPhase++;
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 20L); // 20 ticks = 1 second

        bossTasks.add(healthCheckTask);
    }

    private void startBossBarTask() {
        BukkitTask bossBarTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (magmaCube != null && !magmaCube.isDead()) {
                    double health = magmaCube.getHealth();
                    double maxHealth = magmaCube.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    double progress = Math.max(0, Math.min(health / maxHealth, 1));
                    bossBar.setProgress(progress);
                    updateBossBarPlayers();
                } else {
                    this.cancel();
                    bossBar.setVisible(false);
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 20L);

        bossTasks.add(bossBarTask);
    }


    private void cancelCurrentAttacks() {
        for (BukkitTask task : currentAttackTasks) {
            task.cancel();
        }
        attackInProgress = true;
        currentAttackTasks.clear();
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

    // --------------- ANIMATION ---------------


    private void startGrowthAnimation() {
        BukkitTask growthTask = new BukkitRunnable() {
            int size = magmaCube.getSize();

            @Override
            public void run() {
                if (size < maxSize) {
                    size++;
                    magmaCube.setSize(size);

                    double progress = (double) size / maxSize;
                    bossBar.setProgress(progress);
                } else {
                    magmaCube.setMaxHealth(getMaxHealth());
                    magmaCube.setHealth(getMaxHealth());
                    magmaCube.setInvulnerable(false);
                    magmaCube.setGravity(true);
                    magmaCube.setAI(true);
                    this.cancel();
                    startAttackTask();
                    startBossBarTask();
                    startHealthCheckTask();
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 5L);

        bossTasks.add(growthTask);
    }

    private void startRotateAnimation() {
        cancelCurrentAttacks();
        magmaCube.setInvulnerable(true);

        BukkitTask rotateAnimationTask = new BukkitRunnable() {
            int count = 0;
            int maxCount = 200; // 10 seconds at 20 ticks per second

            @Override
            public void run() {
                if (count < maxCount) {
                    float rotation = (720.0f / maxCount) * count; // 720 degrees for 2 full rotations

                    magmaCube.setRotation(rotation, 0);
                    count++;
                } else {
                    this.cancel();
                    attackInProgress = false;
                    magmaCube.setRotation(0, 0);
                    magmaCube.setInvulnerable(false);
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 1L); // 1 tick interval between each execution

        bossTasks.add(rotateAnimationTask);
    }


    // --------------- Attack ---------------


    private void spawnRavageurAttack() {
        Location bossLocation = magmaCube.getLocation().clone();

        HavaEntity ravageur = HavaEntityUtils.getEntity("Ravageur");

        if (ravageur != null) {
            Location spawnOne = bossLocation.clone().add(20, 0, 20);
            Location spawnTwo = bossLocation.clone().add(20, 0, -20);

            Location spawnThree = bossLocation.clone().add(-20, 0, 20);
            Location spawnFour = bossLocation.clone().add(-20, 0, -20);

            HavaEntityUtils.spawnEntity(ravageur, spawnOne);
            HavaEntityUtils.spawnEntity(ravageur, spawnTwo);
            HavaEntityUtils.spawnEntity(ravageur, spawnThree);
            HavaEntityUtils.spawnEntity(ravageur, spawnFour);

            bossLocation.getWorld().playSound(bossLocation, Sound.ENTITY_ALLAY_HURT, 100.0f, 0);
        } else {
            System.out.println("HavaErreur : Ravageur is null");
        }

    }

    private void launchLaserAttack() {
        BukkitTask laserTask = new BukkitRunnable() {
            int chargeTime = 60; // 3 seconds charge (20 ticks per second)
            int activeTime = 40; // 2 seconds active laser (20 ticks per second)
            int totalTime = chargeTime + activeTime;
            int elapsedTime = 0;
            boolean firstStrike = true;

            @Override
            public void run() {
                if (elapsedTime < totalTime) {
                    Location bossLocation = magmaCube.getLocation().clone().add(0, 1.5, 0);

                    // Directions for lasers: front, back, left, right
                    Vector[] directions = {
                            new Vector(1, 0, 0),  // Front
                            new Vector(-1, 0, 0), // Back
                            new Vector(0, 0, 1),  // Left
                            new Vector(0, 0, -1)  // Right
                    };

                    for (Vector direction : directions) {
                        for (int i = 0; i < 30; i++) { // 30 blocks long
                            Location segmentLocation = bossLocation.clone().add(direction.clone().multiply(i));

                            if (elapsedTime < chargeTime) {
                                segmentLocation.getWorld().spawnParticle(Particle.CRIT_MAGIC, segmentLocation, 10, 0.2, 0.2, 0.2, 0.02);
                                if (elapsedTime % 20 == 0) {
                                    float pitch = 0.5f + (elapsedTime / 20) * 0.1f; // Adjust pitch to be lower
                                    bossLocation.getWorld().playSound(bossLocation, Sound.BLOCK_NOTE_BLOCK_BASS, 100.0f, pitch);
                                }
                            } else {
                                if (firstStrike) {
                                    bossLocation.getWorld().playSound(bossLocation, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 100.0f, 2);
                                    firstStrike = false;
                                }
                                segmentLocation.getWorld().spawnParticle(Particle.END_ROD, segmentLocation, 5, 0.1, 0.1, 0.1, 0.01);
                                for (Entity entity : segmentLocation.getWorld().getNearbyEntities(segmentLocation, 1, 1, 1)) {
                                    if (entity instanceof Player) {
                                        ((Player) entity).damage(20.0, magmaCube); // Deal 20 points of damage
                                    }
                                }
                            }
                        }
                    }
                    elapsedTime++;
                } else {
                    this.cancel();
                    attackInProgress = false;
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 1L); // 1 tick = 0.05 seconds

        bossTasks.add(laserTask);
        currentAttackTasks.add(laserTask);

    }

    private void launchMagmaExplosion() {
        BukkitTask explosionTask = new BukkitRunnable() {
            int currentCount = 0;
            int maxCount = 3;

            @Override
            public void run() {
                if (currentCount < maxCount) {
                    magmaCube.getWorld().createExplosion(magmaCube.getLocation(), 4.0F, false, false);

                    for (Entity entity : magmaCube.getNearbyEntities(10, 10, 10)) {
                        if (entity instanceof Player) {
                            ((Player) entity).damage(10.0, magmaCube); // Infliger 10 points de dégâts
                            Vector knockback = entity.getLocation().toVector().subtract(magmaCube.getLocation().toVector()).normalize().multiply(2);
                            entity.setVelocity(knockback);
                        }
                    }
                    currentCount++;
                } else {
                    this.cancel();
                    attackInProgress = false;
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 40L); // 40L = 2 secondes (20 ticks)

        bossTasks.add(explosionTask);
        currentAttackTasks.add(explosionTask);

    }

    private void launchFireJetAttack() {
        BukkitTask launchFireJetTask = new BukkitRunnable() {
            private int elapsedTime = 0;
            private int maxTime = 100; // 5s

            @Override
            public void run() {
                boolean playerInRange = false;
                Location startLocation = magmaCube.getLocation().clone().add(0, 1.5, 0);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getWorld().equals(magmaCube.getWorld()) && player.getLocation().distance(magmaCube.getLocation()) < 20) {
                        playerInRange = true;
                        Vector direction = player.getLocation().toVector().subtract(startLocation.toVector()).normalize();

                        int segments = 20;
                        double distanceBetweenSegments = 1.0;

                        for (int i = 0; i < segments; i++) {
                            Location segmentLocation = startLocation.clone().add(direction.clone().multiply(distanceBetweenSegments * i));

                            segmentLocation.getWorld().spawnParticle(Particle.FLAME, segmentLocation, 10, 0.2, 0.2, 0.2, 0.02);

                            for (Entity entity : segmentLocation.getWorld().getNearbyEntities(segmentLocation, 1, 1, 1)) {
                                if (entity instanceof Player) {
                                    ((Player) entity).damage(5.0, magmaCube);
                                }
                            }
                        }
                    }
                }

                if (!playerInRange) {
                    this.cancel();
                    attackInProgress = false;
                    return;
                }

                elapsedTime += 2;

                if (elapsedTime >= maxTime) {
                    this.cancel();
                    attackInProgress = false;
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 2L); // 2 ticks = 0.1 seconde

        bossTasks.add(launchFireJetTask);
        currentAttackTasks.add(launchFireJetTask);

    }

    private void launchMeteorAttack() {
        BukkitTask meteorTask = new BukkitRunnable() {
            int currentCount = 0;
            int maxCount = 5;

            @Override
            public void run() {
                if (currentCount < maxCount) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getWorld().equals(magmaCube.getWorld()) && player.getLocation().distance(magmaCube.getLocation()) < 50) {
                            Location playerLocation = player.getLocation().clone().add(0, 10, 0); // Spawner la boule de feu 20 blocs au-dessus du joueur
                            Vector direction = new Vector(0, -1, 0).multiply(5); // Direction vers le bas

                            Fireball fireball = player.getWorld().spawn(playerLocation, Fireball.class);
                            fireball.setDirection(direction);
                            fireball.setYield(2);
                            fireball.setIsIncendiary(false);
                        }
                    }
                    currentCount++;
                } else {
                    this.cancel();
                    attackInProgress = false;
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 10L); // 10L = 0.5 seconde (10 ticks)

        bossTasks.add(meteorTask);
        currentAttackTasks.add(meteorTask);
    }
}

