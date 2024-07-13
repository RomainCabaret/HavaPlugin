package pouce.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public abstract class HavaEntity {
    private String name;
    private double maxHealth;
    private double damage;
    private EntityType type;
    private boolean isBaby;
    private ItemStack[] armor;
    private ItemStack mainHand;
    private ItemStack offHand;
    private List<DropItem> dropTable;
    private int xpDrop;
    private float speed;
    private PotionEffect effect;


    public HavaEntity(String name, double health, double damage, EntityType type) {
        this.name = name;
        this.maxHealth = health;
        this.damage = damage;
        this.type = type;
        this.isBaby = false;
        this.xpDrop = 0;
        this.speed = 0.3f;
    }

    public String getName() {
        return name;
    }
    public double getMaxHealth() {
        return maxHealth;
    }
    public double getDamage() {
        return damage;
    }
    public EntityType getType() {
        return type;
    }

    public float getSpeed(){
        return speed;
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public PotionEffect getEffect() {
        return effect;
    }
    public void setEffect(PotionEffect effect) {
        this.effect = effect;
    }

    public void setBaby(boolean b) {
        this.isBaby = b;
    }

    public boolean isBaby() {
        return isBaby;
    }
    public ItemStack[] getArmor() {
        return armor;
    }

    public void setArmor(ItemStack[] armor) {
        this.armor = armor;
    }

    public ItemStack getMainHand() {
        return mainHand;
    }

    public void setMainHand(ItemStack mainHand) {
        this.mainHand = mainHand;
    }

    public ItemStack getOffHand() {
        return offHand;
    }

    public void setOffHand(ItemStack offHand) {
        this.offHand = offHand;
    }

    public int getXpDrop() {
        return xpDrop;
    }

    public void setXpDrop(int xpDrop) {
        this.xpDrop = xpDrop;
    }

    public List<DropItem> getDropTable() {
        return dropTable;
    }

    public void setDropTable(List<DropItem> dropTable) {
        this.dropTable = dropTable;
    }

    public static class DropItem {
        private final ItemStack item;
        private final int minAmount;
        private final int maxAmount;
        private final double probability;

        public DropItem(ItemStack item, int minAmount, int maxAmount, double probability) {
            this.item = item;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.probability = probability;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getMinAmount() {
            return minAmount;
        }

        public int getMaxAmount() {
            return maxAmount;
        }

        public double getProbability() {
            return probability;
        }
    }
}
