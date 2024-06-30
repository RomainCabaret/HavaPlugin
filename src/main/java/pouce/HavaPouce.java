package pouce;

import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import pouce.commands.HavaCommand;
import pouce.events.HavaEvents;
import pouce.gui.HavaGui;
import pouce.gui.HavaGuiItem;
import pouce.items.HavaDistanceItems;
import pouce.items.HavaItems;
import pouce.items.HavaMeleeItems;
import pouce.items.HavaUtilitaireItems;
import pouce.items.rarity.HavaRarity;
import pouce.items.spells.HavaSpell;
import pouce.items.spells.weapons.HavaSpellFurieSanguinaire;
import pouce.items.utils.HavaItemsUtils;
import pouce.tabs.HavaTabCompleter;

import java.util.*;

public final class HavaPouce extends JavaPlugin {

    private static final Map<UUID, BukkitRunnable> renameTasks = new HashMap<>();


    private static HavaPouce instance;
    private static boolean debugMod = true;

    public static HavaPouce getPlugin() {
        return instance;
    }

    public static boolean isDebugMod(){ return debugMod; }
    public static void changeDebugMod(){ debugMod = !debugMod; }
    public static Map<UUID, BukkitRunnable> getTasks(){ return renameTasks; }


    @Override
    public void onEnable() {

        instance = this;

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(ArmorStand.class)) {
                ArmorStand armorStand = (ArmorStand) entity;
                if (armorStand.getPersistentDataContainer().has(new NamespacedKey(this, "donjonDeleted"), PersistentDataType.BYTE)) {
                    armorStand.remove();
                }
            }
        }

        saveDefaultConfig();

        ConfigurationSerialization.registerClass(ItemStack.class);


        getLogger().info("HavaPouce has been enabled!");

        //  --- COMMANDE ---

        HavaCommand command = new HavaCommand();
        HavaTabCompleter tab = new HavaTabCompleter();


        getCommand("gui").setExecutor(command);
        getCommand("guitem").setExecutor(command);

        getCommand("havadev").setExecutor(command);
        getCommand("nav").setExecutor(command);

        getCommand("nbt").setExecutor(command);

        getCommand("donjonitems").setExecutor(command);

        getCommand("vision").setExecutor(command);


        //  --- EVENEMENTS ---

        getServer().getPluginManager().registerEvents(new HavaEvents(), this);

        //  --- TAB  ---

        getCommand("gui").setTabCompleter(tab);
        getCommand("guitem").setTabCompleter(tab);
        getCommand("nbt").setTabCompleter(tab);
        getCommand("donjonitems").setTabCompleter(tab);

        //   --- CONFIG  ---

        HavaGui.loadGuisFromConfig();
        HavaGuiItem.loadItemsFromConfig();
        HavaItemsUtils.loadItemsConfig();

        // INSTANCE

        HavaSpell.addSpell("FurieSanguinaire", new HavaSpellFurieSanguinaire("FurieSanguinaire",  new ArrayList<String>(), 5));

    }

    @Override
    public void onDisable() {
        for (String gui : HavaGui.getAllGuiNames()) {
            HavaGui.getGuiByName(gui).saveToConfig();
        }
    }

    public static void sendHavaMessage(Player player, String msg) {
        player.sendMessage("(§f§lHava§3§lPouce§f) ➺ " + ChatColor.GRAY + msg);
    }

    public static void sendHavaError(Player player, String msg) {
        player.sendMessage("(§f§lHava§c§lError§f) ➺ " + ChatColor.RED + msg);
    }

    public static void sendHavaDev(Player player, String msg) {
        if(isDebugMod()){
            player.sendMessage("(§f§lHava§9§lDev§f) ➺ " + ChatColor.WHITE + msg);
        }
    }

}
