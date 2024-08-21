package org.lilbrocodes.twoworlds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TWTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("sync");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("sync")) {
                suggestions.add("toggle");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("sync") && args[1].equalsIgnoreCase("toggle")) {
                suggestions.add("player");
            }
        }

        return filterSuggestions(args[args.length - 1], suggestions);
    }

    private List<String> filterSuggestions(String currentInput, List<String> suggestions) {
        List<String> filtered = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().startsWith(currentInput.toLowerCase())) {
                filtered.add(suggestion);
            }
        }
        return filtered;
    }
}
