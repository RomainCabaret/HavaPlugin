package pouce.tabs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import pouce.boss.HavaBossUtils;
import pouce.entity.HavaEntityUtils;
import pouce.gui.HavaGui;
import pouce.gui.HavaGuiItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class HavaTabCompleter implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        Set<String> validGuiArgs = Set.of("open", "add", "edit", "del");
        Set<String> validGuitemArgs = Set.of("get", "add", "edit", "del");
        Set<String> validNbtArgs = Set.of("get", "add", "del");

        Set<String> validItemDonjonArgs = Set.of("add");
        Set<String> validTypeItemDonjonArgs = Set.of("melee", "distance", "utilitaire");

        Set<String> validDonjonBossmArgs = Set.of("spawn", "kill");


        // ---------- GUI ----------


        // GUI <Action>
        if (Objects.equals(command.getName().toLowerCase(), "gui") && args.length == 1) {
            for (String arg : validGuiArgs) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(arg);
                }
            }
        }

        // GUI <Action> <TargetGUI>
        else if (Objects.equals(command.getName().toLowerCase(), "gui") && Set.of("open", "edit", "del").contains(args[0].toLowerCase()) && args.length == 2) {
            for (String guiName : HavaGui.getAllGuiNames()) {
                if (guiName.toLowerCase().startsWith(args[1].toLowerCase())) {
                    suggestions.add(guiName);
                }
            }
        }

        // ---------- GUITEM ----------

        // Guitem <Action>
        else if (Objects.equals(command.getName().toLowerCase(), "guitem") && args.length == 1) {
            for (String arg : validGuitemArgs) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(arg);
                }
            }

        }

        // Guitem <Action> <item>

        else if (Objects.equals(command.getName().toLowerCase(), "guitem") && Set.of("get", "edit", "del").contains(args[0].toLowerCase()) && args.length == 2) {
            for (String guitemName : HavaGuiItem.getAllGuitem()) {
                if (guitemName.toLowerCase().startsWith(args[1].toLowerCase())) {
                    suggestions.add(guitemName);
                }
            }
        }
        // Guitem <add/edit> <ItemName> <TargetGui>

        else if (Objects.equals(command.getName().toLowerCase(), "guitem") && Set.of("add", "edit").contains(args[0].toLowerCase()) && args.length == 3) {
            for (String guiName : HavaGui.getAllGuiNames()) {
                if (guiName.toLowerCase().startsWith(args[2].toLowerCase())) {
                    suggestions.add(guiName);
                }
            }
        }

        // ---------- NBT ----------

        // nbt <Action>
        else if (Objects.equals(command.getName().toLowerCase(), "nbt") && args.length == 1) {
            for (String arg : validNbtArgs) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(arg);
                }
            }

        }

        // ---------- donjonitems ----------


        // donjonitems <Action>
        else if (Objects.equals(command.getName().toLowerCase(), "donjonitems") && args.length == 1) {
            for (String arg : validItemDonjonArgs) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(arg);
                }
            }
        }

        // donjonitems add <Type>
        else if (Objects.equals(command.getName().toLowerCase(), "donjonitems") && args.length == 2) {
            for (String arg : validTypeItemDonjonArgs) {
                if (arg.toLowerCase().startsWith(args[1].toLowerCase())) {
                    suggestions.add(arg);
                }
            }
        }

        // ---------- donjonmob ----------

        else if (Objects.equals(command.getName().toLowerCase(), "donjonmob") &&  args.length == 1){
            for (String mob : HavaEntityUtils.getEntityMap().keySet()) {
                if(mob.toLowerCase().startsWith(args[0].toLowerCase())){
                    suggestions.add(mob);
                }
            }
        }

        // ---------- donjonBoss ----------

        else if (Objects.equals(command.getName().toLowerCase(), "donjonboss") && args.length == 1) {
            for (String arg : validDonjonBossmArgs) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(arg);
                }
            }
        }

        else if (Objects.equals(command.getName().toLowerCase(), "donjonboss") && args.length == 2){
            for (String boss : HavaBossUtils.getBossMap().keySet()) {
                if(boss.toLowerCase().startsWith(args[1].toLowerCase())){
                    suggestions.add(boss);
                }
            }
        }





        return suggestions;
    }

}