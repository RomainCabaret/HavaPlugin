package pouce.items.spells;

import java.util.List;

public abstract class HavaSpell {

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

}
