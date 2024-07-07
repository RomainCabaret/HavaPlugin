package pouce.items.spells.utils;

import pouce.items.spells.HavaSpell;
import pouce.items.spells.weapons.HavaSpellFurieSanguinaire;
import pouce.items.spells.weapons.HavaSpellNull;
import pouce.items.spells.weapons.HavaSpellSpeedDash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HavaSpellUtils {

    private static Map<String, HavaSpell> spellMap = new HashMap<>();

    public static void loadSpell(){
        addSpell("none", new HavaSpellNull());
        List<String> furtieLore = new ArrayList<>();
        furtieLore.add("§7  - §e§lCLICK DROIT §7pour lancé 3 hache");
        furtieLore.add("§7  infligeant §610\uD83E\uDE93§7 par coups (§35s Cooldown§7)");

        addSpell("FurieSanguinaire", new HavaSpellFurieSanguinaire("FurieSanguinaire",  furtieLore, 5));

        List<String> SpeedDashLore = new ArrayList<>();
        SpeedDashLore.add("§7  - §e§lCLICK DROIT §7pour dash en avant");
        SpeedDashLore.add("§7  infligeant §610\uD83E\uDE93§7 par mobs touché (§35s Cooldown§7)");

        addSpell("SpeedDash", new HavaSpellSpeedDash("SpeedDash", SpeedDashLore, 2));

    }

    public static HavaSpell getSpell(String uniqueName){
        return spellMap.get(uniqueName);
    }

    public static Map<String, HavaSpell> getSpellMap(){
        return spellMap;
    }

    public static void addSpell(String uniqueName, HavaSpell spell){
        spellMap.put(uniqueName, spell);
    }


}
