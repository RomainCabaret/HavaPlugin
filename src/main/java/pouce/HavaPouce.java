package pouce;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pouce.commands.HavaCommand;
import pouce.events.HavaEvents;
import pouce.gui.HavaGui;
import pouce.gui.HavaGuiItem;
import pouce.tabs.HavaTabCompleter;

public final class HavaPouce extends JavaPlugin {

    private static HavaPouce instance;
    private static boolean debugMod = true;

    public static HavaPouce getPlugin() {
        return instance;
    }

    public static boolean isDebugMod(){ return debugMod; }
    public static void changeDebugMod(){ debugMod = !debugMod; }


    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("HavaPouce has been enabled!");

        //  --- COMMANDE ---

        HavaCommand command = new HavaCommand();
        HavaTabCompleter tab = new HavaTabCompleter();


        getCommand("gui").setExecutor(command);
        getCommand("guitem").setExecutor(command);
        getCommand("havadev").setExecutor(command);
        getCommand("nav").setExecutor(command);
        getCommand("setnbt").setExecutor(command);
        getCommand("clearnbt").setExecutor(command);




        //  --- EVENEMENTS ---

        getServer().getPluginManager().registerEvents(new HavaEvents(), this);

        //  --- TAB  ---

        getCommand("gui").setTabCompleter(tab);
        getCommand("guitem").setTabCompleter(tab);


        HavaGui.loadGuisFromConfig();
        HavaGuiItem.loadItemsFromConfig();

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
