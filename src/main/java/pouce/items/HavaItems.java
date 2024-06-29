package pouce.items;

import org.bukkit.inventory.ItemStack;
import pouce.items.rarity.HavaRarity;
import pouce.items.utils.HavaItemsUtils;

public abstract class HavaItems {
    private String uniqueName;
    private HavaRarity rarity;
    private ItemStack item;

    public HavaItems(String uniqueName, HavaRarity rarity, ItemStack item) {
        this.uniqueName = uniqueName;
        this.rarity = rarity;
        this.item = item;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void SetUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public HavaRarity getRarity() {
        return rarity;
    }


    public void setRarity(HavaRarity rarity){
        this.rarity = rarity;
        HavaItemsUtils.saveItem(this);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {

        if(item == null ){
            return;
        }

        this.item = item;
        HavaItemsUtils.saveItem(this);
    }
}
