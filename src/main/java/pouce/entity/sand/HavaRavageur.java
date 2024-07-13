package pouce.entity.sand;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pouce.entity.HavaEntity;

import java.util.ArrayList;
import java.util.List;

public class HavaRavageur extends HavaEntity{
    public HavaRavageur(String name, double health, double damage, EntityType type) {
        super(name, health, damage, type);
        this.setEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 10, true, false));
    }
}
