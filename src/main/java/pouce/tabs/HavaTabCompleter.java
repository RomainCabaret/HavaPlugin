package pouce.tabs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
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

        return suggestions;
    }

}