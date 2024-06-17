package pouce.commands;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pouce.HavaPouce;
import pouce.gui.HavaGui;
import pouce.gui.HavaGuiItem;

import java.util.Set;

import static pouce.HavaPouce.*;

public class HavaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            switch (cmd.getName().toLowerCase()) {
                case "gui":
                    Set<String> validGuiArgs = Set.of("open", "add", "edit", "del");
                    if (args.length != 0) {
                        if (validGuiArgs.contains(args[0])) {
                            switch (args[0]) {
                                case "open":
                                    if (args.length == 2) {
                                        String guiName = args[1];
                                        HavaGui gui = HavaGui.getGuiByName(guiName);
                                        if (gui != null) {
                                            gui.openReadonlyInventory(player);
                                            sendHavaMessage(player, "GUI " + guiName + " ouverte en mode lecture seule.");
                                            return true;
                                        } else {
                                            sendHavaError(player, "GUI " + guiName + " n'existe pas.");
                                        }
                                    } else {
                                        sendHavaMessage(player, "Utilisation : /gui open <nom>");
                                    }
                                    return true;
                                case "add":
                                    if (args.length == 3) {
                                        String guiName = args[1];
                                        int guiSize;
                                        try {
                                            guiSize = Integer.parseInt(args[2]);
                                        } catch (NumberFormatException e) {
                                            sendHavaError(player, "Taille invalide. Veuillez entrer un nombre.");
                                            return true;
                                        }
                                        if (guiSize < 1 || guiSize > 6) {
                                            sendHavaError(player, "Taille invalide. La taille doit être comprise entre 1 et 6.");
                                            return true;
                                        }
                                        if (HavaGui.getGuiByName(guiName) != null) {
                                            sendHavaError(player, "Un GUI avec ce nom existe déjà. Veuillez choisir un autre nom.");
                                            return true;
                                        }
                                        HavaGui gui = new HavaGui(guiName, guiSize * 9);
                                        gui.openInventory(player);
                                        sendHavaMessage(player, "GUI " + guiName + " créée avec une taille de " + guiSize + ".");
                                        return true;
                                    } else {
                                        sendHavaMessage(player, "Utilisation : /gui add <nom> <taille>");
                                    }
                                    break;
                                case "edit":
                                    if (args.length == 2) {
                                        String guiName = args[1];
                                        HavaGui gui = HavaGui.getGuiByName(guiName);
                                        if (gui != null) {
                                            gui.openInventory(player);
                                            sendHavaMessage(player, "GUI " + guiName + " ouverte en mode édition.");
                                            return true;
                                        } else {
                                            sendHavaError(player, "GUI " + guiName + " n'existe pas.");
                                        }
                                    } else {
                                        sendHavaMessage(player, "Utilisation : /gui edit <nom>");
                                    }
                                    return true;
                                case "del":
                                    if (args.length == 2) {
                                        String guiName = args[1];
                                        HavaGui gui = HavaGui.getGuiByName(guiName);
                                        if (gui != null) {
                                            gui.deleteGui();
                                            sendHavaMessage(player, "GUI " + guiName + " supprimée.");
                                            return true;
                                        } else {
                                            sendHavaError(player, "GUI " + guiName + " n'existe pas.");
                                        }
                                    } else {
                                        sendHavaMessage(player, "Utilisation : /gui del <nom>");
                                    }
                                    return true;
                            }
                        } else {
                            sendHavaMessage(player, "Utilisation : /gui <action>");
                            return true;
                        }
                    } else {
                        sendHavaMessage(player, "Utilisation : /gui <action>");
                        return true;
                    }
                    break;
                case "guitem":
                    Set<String> validGuitemArgs = Set.of("get", "add", "edit", "del");
                    if (args.length != 0) {
                        if (validGuitemArgs.contains(args[0])) {
                            switch (args[0]) {
                                case "get": {
                                    if (args.length == 2) {
                                        String uniqueIdValue = args[1];
                                        HavaGuiItem item = HavaGuiItem.getItemByUniqueIdValue(uniqueIdValue);
                                        if (item != null) {
                                            ItemStack itemStack = item.getItemStack().clone();
                                            ItemMeta meta = itemStack.getItemMeta();
                                            if (meta != null) {
                                                meta.setDisplayName(uniqueIdValue);
                                                itemStack.setItemMeta(meta);
                                            }
                                            player.getInventory().addItem(HavaGuiItem.getItemByID(uniqueIdValue));
                                            sendHavaMessage(player, "Item récupéré avec succès pour la valeur " + uniqueIdValue + ".");
                                        } else {
                                            sendHavaError(player, "Aucun item trouvé avec la valeur " + uniqueIdValue + ".");
                                        }
                                    } else {
                                        sendHavaMessage(player, "Utilisation : /guitem get <item>");
                                    }
                                    break;
                                }
                                case "add":
                                    if (args.length == 3) {
                                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                                        String uniqueIdValue = args[1];
                                        String guiTarget = args[2];
                                        if (itemStack == null || itemStack.getType() == Material.AIR) {
                                            sendHavaError(player, "Vous devez tenir un objet dans votre main.");
                                            return true;
                                        }
                                        if (HavaGuiItem.getItemByUniqueIdValue(uniqueIdValue) != null) {
                                            sendHavaError(player, "Un Guitem avec ce nom existe déjà. Veuillez choisir un autre nom.");
                                            return true;
                                        }
                                        if (HavaGui.getGuiByName(guiTarget) == null) {
                                            sendHavaError(player, "GUI " + guiTarget + " n'existe pas.");
                                            return true;
                                        }
                                        NamespacedKey namespacedKey = new NamespacedKey(getPlugin(), "unique_id");
                                        HavaGuiItem item = new HavaGuiItem(itemStack, namespacedKey, uniqueIdValue, guiTarget);
                                        sendHavaMessage(player, "Item enregistré avec la valeur " + uniqueIdValue + " pour ouvrir le GUI " + guiTarget + ".");
                                        return true;
                                    } else {
                                        sendHavaMessage(player, "Utilisation : /guitem add <ID> <guitarget>");
                                    }
                                    break;
                                case "edit":
                                    if (args.length == 3) {
                                        ItemStack itemStack;
                                        String uniqueIdValue = args[1];
                                        String guiTarget = args[2];
                                        HavaGuiItem item = HavaGuiItem.getItemByUniqueIdValue(uniqueIdValue);
                                        if (item == null) {
                                            sendHavaError(player, "L'item " + uniqueIdValue + " n'existe pas.");
                                            return true;
                                        }

                                        itemStack = HavaGuiItem.getItemByUniqueIdValue(uniqueIdValue).getItemStack();

                                        if (HavaGui.getGuiByName(guiTarget) == null) {
                                            sendHavaError(player, "GUI " + guiTarget + " n'existe pas.");
                                            return true;
                                        }
                                        item.updateItem(itemStack, guiTarget);
                                        sendHavaMessage(player, "Item " + uniqueIdValue + " mis à jour pour ouvrir le GUI " + guiTarget + ".");
                                        return true;
                                    } else {
                                        sendHavaMessage(player, "Utilisation : /guitem edit <ID> <guitarget>");
                                    }
                                    break;
                                case "del":
                                    if (args.length == 2) {
                                        String uniqueIdValue = args[1];
                                        HavaGuiItem item = HavaGuiItem.getItemByUniqueIdValue(uniqueIdValue);
                                        if (item != null) {
                                            item.deleteToConfig();
                                            sendHavaMessage(player, "Item supprimé avec succès pour la valeur " + uniqueIdValue + ".");
                                        } else {
                                            sendHavaError(player, "Aucun item trouvé avec la valeur " + uniqueIdValue + ".");
                                        }
                                    } else {
                                        sendHavaMessage(player, "Utilisation : /guitem del <item>");
                                    }
                                    break;
                            }
                        } else {
                            sendHavaMessage(player, "Utilisation : /guitem <action>");
                            return true;
                        }
                    } else {
                        sendHavaMessage(player, "Utilisation : /guitem <action>");
                        return true;
                    }
                    break;
                case "havadev":
                    if (args.length == 0) {
                        changeDebugMod();
                        if (isDebugMod()) {
                            sendHavaMessage(player, "Mode de developpement §2§lactivé");
                        } else {
                            sendHavaMessage(player, "Mode de developpement §c§ldésactivé");
                        }
                        return true;

                    } else {
                        sendHavaMessage(player, "Utilisation : /havedev");
                        return true;
                    }
                case "setnbt": {
                    if (args.length != 2) {
                        sendHavaMessage(player, "Utilisation: /setntb <key> <value>");
                        return true;
                    }

                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item == null) {
                        sendHavaMessage(player, "Vous devez tenir un objet dans votre main.");
                        return true;
                    }

                    String key = args[0];
                    String value = args[1];

                    ItemMeta meta = item.getItemMeta();
                    if (meta == null) {
                        sendHavaError(player, "L'objet en main ne peut pas avoir de NBT.");
                        return true;
                    }

                    NamespacedKey nbtKey = new NamespacedKey(getPlugin(), key);
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    container.set(nbtKey, PersistentDataType.STRING, value);
                    item.setItemMeta(meta);

                    sendHavaMessage(player, "NBT défini : " + key + " = " + value);
                    return true;
                }
                case "nav":
                    if(args.length == 0){

                        ItemStack item = new ItemStack(Material.RECOVERY_COMPASS);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName("§7NavMenu");
                        NamespacedKey key = new NamespacedKey(getPlugin(), "unique_id");
                        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "navGuiClick");
                        item.setItemMeta(meta);

                        player.getInventory().addItem(item);
                        return true;
                    } else{
                        sendHavaMessage(player, "Utilisation : /nav");
                        return true;
                    }
                case "clearnbt":
                    if(args.length == 0){
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (item == null || !item.hasItemMeta()) {
                            sendHavaMessage(player, "Vous devez tenir un objet valide dans votre main.");
                            return true;
                        }

                        ItemMeta meta = item.getItemMeta();
                        if (meta == null) {
                            sendHavaMessage(player, "L'objet en main ne peut pas avoir de NBT.");
                            return true;
                        }

                        PersistentDataContainer container = meta.getPersistentDataContainer();
                        if (container.isEmpty()) {
                            sendHavaMessage(player, "Aucun NBT trouvé sur l'objet.");
                            return true;
                        }

                        for (NamespacedKey key : container.getKeys()) {
                            container.remove(key);
                        }

                        item.setItemMeta(meta);
                        sendHavaMessage(player,"Tous les NBT ont été supprimés de l'objet en main.");
                        return true;
                    } else{
                        sendHavaMessage(player, "Utilisation : /clearnbt");
                        return true;
                    }
                default:
                    sendHavaMessage(player, "Commande inconnue.");
                    break;
            }
            return true;
        }
        return false;
    }
}
