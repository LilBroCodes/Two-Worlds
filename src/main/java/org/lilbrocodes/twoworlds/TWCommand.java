package org.lilbrocodes.twoworlds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public class TWCommand implements CommandExecutor {
    private final TwoWorlds plugin;

    public TWCommand(TwoWorlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getSenderString("This command can only be run by a player."));
            return false;
        }

        if (args.length == 0) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "sync":
                handleSync(sender, args);
                break;
            default:
                sender.sendMessage(plugin.getSenderString("Unknown command. Use /tw sync for more options."));
                break;
        }

        return true;
    }

    private void handleSync(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.getSenderString("Usage: /tw sync <toggle>"));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "toggle":
                handleToggle(sender, args);
                break;
            default:
                sender.sendMessage(plugin.getSenderString("Unknown option. Use /tw sync toggle <player>."));
                break;
        }
    }

    private void handleToggle(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(plugin.getSenderString("Usage: /tw sync toggle <players>"));
            return;
        }

        switch (args[2].toLowerCase()) {
            case "player":
                plugin.npcSync = !plugin.npcSync;
                sender.sendMessage(plugin.getSenderString(String.format("Set npcSync to %b", plugin.npcSync)));
                break;
            default:
                sender.sendMessage(plugin.getSenderString("Unknown option. Use /tw sync toggle <player>."));
                break;
        }
    }
}
