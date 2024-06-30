package pouce.items.spells;

import pouce.gui.HavaGui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HavaSpell {

    private static Map<String, HavaSpell> spellMap = new HashMap<>();

    private String uniqueName;
    private List<String> Lore;
    private float cooldown;

    public HavaSpell(String uniqueName, List<String> Lore, float cooldown) {
        this.uniqueName = uniqueName;
        this.Lore = Lore;
        this.cooldown = cooldown;
    }

    public String getUniqueName(){
        return uniqueName;
    }

    public List<String> getLore(){
        return Lore;
    }

    public void setLore(List<String> Lore){
        this.Lore = Lore;
    }

    public float getCooldown(){
        return cooldown;
    }
    public void setCooldown(float cooldown){
        this.cooldown = cooldown;
    }

    // STATIC

    public static HavaSpell getSpell(String uniqueName){
        return spellMap.get(uniqueName);
    }

    public static void addSpell(String uniqueName, HavaSpell spell){
        spellMap.put(uniqueName, spell);
    }
}
