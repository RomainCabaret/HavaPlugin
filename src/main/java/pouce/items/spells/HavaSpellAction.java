package pouce.items.spells;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import pouce.items.spells.utils.HavaSpellUtils;
import pouce.items.spells.weapons.HavaSpellFurieSanguinaire;
import pouce.items.spells.weapons.HavaSpellSpeedDash;
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

        HavaSpell spell = HavaSpellUtils.getSpell(spellId);

        if(spell == null) {
            return;
        }

        if (HavaCooldown.isOnCooldown(player, spellId)) {
            long timeRemaining = HavaCooldown.getCooldownRemaining(player, spellId) / 1000;
            player.sendMessage("§f[§3Cooldowns§f] §7Veuillez patienter " + (timeRemaining + 1) + " secondes.");
            return;
        }

        switch(spellId) {
            case "FurieSanguinaire":{
                HavaSpellFurieSanguinaire furieSanguinaire = (HavaSpellFurieSanguinaire) spell;
                furieSanguinaire.useSpell(player, item, event);
                HavaCooldown.setCooldown(player, spellId, (long) (furieSanguinaire.getCooldown() * 1000)); // Convertir les secondes en millisecondes
                player.setCooldown(item.getType(), (int) (furieSanguinaire.getCooldown() * 20));
                return;
            }
            case "SpeedDash": {
                HavaSpellSpeedDash dash = (HavaSpellSpeedDash) spell;
                dash.useSpell(player, item, event);
                HavaCooldown.setCooldown(player, spellId, (long) (dash.getCooldown() * 1000)); // Convertir les secondes en millisecondes
                player.setCooldown(item.getType(), (int) (dash.getCooldown() * 20));

                return;
            }
            case "none":{
                return;
            }
            default: {
                sendHavaError(player, "Le spell n'est pas reconnu");
            }
        }
    }
}
