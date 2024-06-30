package pouce.items.spells;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import pouce.items.spells.weapons.HavaSpellFurieSanguinaire;
import pouce.nbt.HavaNBT;

import java.util.List;

import static pouce.HavaPouce.*;

public class HavaSpellAction  {

    public static void onDonjonSpellUse(Player player, ItemStack item, PlayerInteractEvent event) {

        ItemMeta itemMeta = item.getItemMeta();

        NamespacedKey key = new NamespacedKey(getPlugin(), HavaNBT.GetItemSpell());
        String spellId = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if(spellId == null) {
            return;
        }

        HavaSpell spell = HavaSpell.getSpell(spellId);

        if(spell == null) {
            return;
        }


        switch(spellId) {
            case "FurieSanguinaire":{
                HavaSpellFurieSanguinaire furieSanguinaire = (HavaSpellFurieSanguinaire) spell;
                furieSanguinaire.useSpell(player, item, event);
                return;
            }
            default: {
                sendHavaError(player, "Le spell n'est pas reconnu");
            }
        }



    }
}
