package org.lilbrocodes.twoworlds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    private final TwoWorlds plugin;

    public ReloadCommand(TwoWorlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
        if (commandSender.hasPermission("twoworlds.reload")) {
            plugin.reloadPluginConfig();
            commandSender.sendMessage("Configuration reloaded.");
            return true;
        }
        commandSender.sendMessage("You do not have permission to execute this command.");
        return false;
    }
}
