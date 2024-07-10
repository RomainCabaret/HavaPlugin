package pouce.boss;

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
import pouce.boss.sand.HavaMagmaBoss;
import pouce.entity.HavaEntity;
import pouce.entity.sand.HavaCoruptHusk;
import pouce.nbt.HavaNBT;

import java.util.*;

import static pouce.HavaPouce.getPlugin;

public class HavaBossUtils {
    private static Map<String, HavaBoss> customBossMap = new HashMap<>();


    public static void loadCustomBoss(){
        HavaBoss havaMagma = new HavaMagmaBoss("MagmaBoss", 1500, 10 );

        customBossMap.put("MagmaBoss",havaMagma);
    }

    public static HavaBoss getBoss(String uniqueName){
        return customBossMap.get(uniqueName);
    }

    public static Map<String, HavaBoss> getBossMap(){
        return customBossMap;
    }

}
