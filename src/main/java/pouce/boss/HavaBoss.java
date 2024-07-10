package pouce.boss;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import pouce.entity.HavaEntity;

import java.util.List;

public abstract class HavaBoss {
    private String name;
    private double maxHealth;
    private double damage;
    private List<HavaEntity.DropItem> dropTable;
    private int xpDrop;


    public HavaBoss(String name, double health, double damage) {
        this.name = name;
        this.maxHealth = health;
        this.damage = damage;
        this.xpDrop = 0;
    }

    public abstract void spawn(Location location);
    public abstract void kill();

    public String getName() {
        return name;
    }
    public double getMaxHealth() {
        return maxHealth;
    }
    public double getDamage() {
        return damage;
    }
    public List<HavaEntity.DropItem> getDropTable() {
        return dropTable;
    }
    public int getXpDrop() {
        return xpDrop;
    }

}
