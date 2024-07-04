package pouce.items;

import org.bukkit.inventory.ItemStack;
import pouce.items.rarity.HavaRarity;
import pouce.items.spells.HavaSpell;
import pouce.items.spells.utils.HavaSpellUtils;
import pouce.items.spells.weapons.HavaSpellNull;
import pouce.items.utils.HavaItemsUtils;

public class HavaMeleeItems extends HavaItems {
    private final String type = "melee";
    private int damage;
    private int strength;
    private HavaSpell spell;

    public HavaMeleeItems(ItemStack item) {
        super(item.getItemMeta().getDisplayName(), HavaRarity.COMMUN, item);
        this.damage = 0;
        this.strength = 0;
        this.spell = HavaSpellUtils.getSpell("none");
    }

    public HavaMeleeItems(String uniqueName, HavaRarity rarity, ItemStack item, int damage, int strength, HavaSpell spell) {
        super(uniqueName, rarity, item);
        this.damage = damage;
        this.strength = strength;
        this.spell = spell;
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

    public HavaSpell getSpell() {
        return spell;
    }

    public void setDamage(int damage) {

        if(damage < 0 ){
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

    public void setSpell(HavaSpell spell){
        this.spell = spell;
        HavaItemsUtils.saveItem(this);
    }
}