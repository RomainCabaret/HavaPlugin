package pouce.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import pouce.entity.sand.HavaCoruptHusk;
import pouce.entity.sand.HavaRavageur;
import pouce.items.spells.HavaSpell;
import pouce.items.spells.weapons.HavaSpellFurieSanguinaire;
import pouce.items.spells.weapons.HavaSpellNull;
import pouce.items.spells.weapons.HavaSpellSpeedDash;
import pouce.nbt.HavaNBT;

import java.util.*;

import static pouce.HavaPouce.getPlugin;

public class HavaEntityUtils {
    private static Map<String, HavaEntity> customEntitiesMap = new HashMap<>();

    public static String formatEntityHealth(HavaEntity e, double currentHealth){
        return String.format("%s [%.1f/%.1f]", e.getName(), currentHealth, e.getMaxHealth());
    }


    public static void loadCustomEntity(){
        HavaCoruptHusk havaCoruptHusk = new HavaCoruptHusk("CoruptHusk", 150, 10 , EntityType.HUSK);
        HavaRavageur havaRavageur = new HavaRavageur("Ravageur", 550, 100, EntityType.RAVAGER);

        customEntitiesMap.put("CoruptHusk",havaCoruptHusk);
        customEntitiesMap.put("Ravageur",havaRavageur);
    }

    public static HavaEntity getEntity(String uniqueName){
        return customEntitiesMap.get(uniqueName);
    }

    public static LivingEntity spawnEntity(HavaEntity entity, Location location){
        LivingEntity customMob = (LivingEntity) location.getWorld().spawnEntity(location, entity.getType());
        customMob.setCustomName(formatEntityHealth(entity, entity.getMaxHealth()));
        customMob.setMaxHealth(entity.getMaxHealth());
        customMob.setHealth(entity.getMaxHealth());
        customMob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(entity.getDamage());
        customMob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(entity.getSpeed());
        customMob.getPersistentDataContainer().set(new NamespacedKey(getPlugin(), HavaNBT.GetEntityDonjonType()), PersistentDataType.STRING, entity.getName());
        customMob.setCustomNameVisible(true);
        customMob.setPersistent(true);
        customMob.setRemoveWhenFarAway(false);

        if(entity.getEffect() != null){
            customMob.addPotionEffect(entity.getEffect());
        }


        if (entity.isBaby() && customMob instanceof Ageable) {
            ((Ageable) customMob).setBaby();
        }

        // Appliquer l'armure
        if (entity.getArmor() != null) {
            EntityEquipment equipment = customMob.getEquipment();
            if (equipment != null) {
                ItemStack[] armor = entity.getArmor();
                equipment.setHelmet(armor.length > 0 ? makeCosmetic(armor[0]) : null);
                equipment.setChestplate(armor.length > 1 ? makeCosmetic(armor[1]) : null);
                equipment.setLeggings(armor.length > 2 ? makeCosmetic(armor[2]) : null);
                equipment.setBoots(armor.length > 3 ? makeCosmetic(armor[3]) : null);
            }
        }

        // Appliquer les items des mains
        EntityEquipment equipment = customMob.getEquipment();
        if (equipment != null) {
            equipment.setItemInMainHand(entity.getMainHand());
            equipment.setItemInOffHand(entity.getOffHand());
        }

        // Définir la table de drop
        if (entity.getDropTable() != null) {
            customMob.setMetadata("dropTable", new FixedMetadataValue(getPlugin(), entity.getDropTable()));
        }

        // Définir l'XP drop
        customMob.setMetadata("xpDrop", new FixedMetadataValue(getPlugin(), entity.getXpDrop()));

        return customMob;
    }


    public static Map<String, HavaEntity> getEntityMap(){
        return customEntitiesMap;
    }

    private static ItemStack makeCosmetic(ItemStack item) {
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", 0, AttributeModifier.Operation.ADD_NUMBER));
                item.setItemMeta(meta);
            }
        }
        return item;
    }

}
