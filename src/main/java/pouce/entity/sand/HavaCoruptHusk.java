package pouce.entity.sand;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import pouce.entity.HavaEntity;

import java.util.ArrayList;
import java.util.List;

public class HavaCoruptHusk extends HavaEntity {
    public HavaCoruptHusk(String name, double health, double damage, EntityType type) {
        super(name, health, damage, type);

        this.setBaby(true);

        ItemStack[] armor = {
                new ItemStack(Material.DIAMOND_HELMET),
                new ItemStack(Material.DIAMOND_CHESTPLATE),
                new ItemStack(Material.DIAMOND_LEGGINGS),
                new ItemStack(Material.DIAMOND_BOOTS)
        };
        this.setArmor(armor);

        this.setMainHand(new ItemStack(Material.DIAMOND_SWORD));
        this.setOffHand(new ItemStack(Material.SHIELD));

        List<DropItem> dropTable = new ArrayList<>();
        dropTable.add(new HavaEntity.DropItem(new ItemStack(Material.GOLD_INGOT), 4, 15, 1.0));
        dropTable.add(new HavaEntity.DropItem(new ItemStack(Material.DIAMOND_SWORD), 1, 1, 1.0));
        this.setDropTable(dropTable);
    }
}
