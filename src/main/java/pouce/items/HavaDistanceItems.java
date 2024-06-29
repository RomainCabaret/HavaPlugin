package pouce.items;

import org.bukkit.inventory.ItemStack;
import pouce.items.rarity.HavaRarity;
import pouce.items.utils.HavaItemsUtils;

public class HavaDistanceItems extends HavaItems{
    private final String type = "distance";

    private int damage;
    private int strength;

    public HavaDistanceItems(ItemStack item) {
        super(item.getItemMeta().getDisplayName(), HavaRarity.COMMUN, item);
        this.damage = 0;
        this.strength = 0;
    }

    public HavaDistanceItems(String uniqueName, HavaRarity rarity, ItemStack item, int damage, int strength) {
        super(uniqueName, rarity, item);
        this.damage = damage;
        this.strength = strength;
    }

    public int getDamage() {
        return damage;
    }

    public int getStrength(){
        return strength;
    }

    public String getType(){
        return type;
    }

    public void setDamage(int damage) {

        if(damage < 0){
            return;
        }

        this.damage = damage;
        HavaItemsUtils.saveItem(this);
    }

    public void setStrength(int strength){

        if(strength < 0){
            return;
        }

        this.strength = strength;
        HavaItemsUtils.saveItem(this);
    }


}
