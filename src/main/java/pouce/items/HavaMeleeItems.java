package pouce.items;

import org.bukkit.inventory.ItemStack;
import pouce.items.rarity.HavaRarity;

public class HavaMeleeItems extends HavaItems {
    private int damage;

    public HavaMeleeItems(ItemStack item) {
        super(item.getItemMeta().getDisplayName(), HavaRarity.COMMUN, item);
        this.damage = 0;
    }

    public HavaMeleeItems(String uniqueName, HavaRarity rarity, ItemStack item, int damage) {
        super(uniqueName, rarity, item);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}