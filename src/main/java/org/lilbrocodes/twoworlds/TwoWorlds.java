package org.lilbrocodes.twoworlds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.N;

import java.util.Objects;

public final class TwoWorlds extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        NPCCommand npcCommand = new NPCCommand(this);

        Objects.requireNonNull(this.getCommand("fp")).setExecutor(npcCommand);
        Objects.requireNonNull(this.getCommand("reloadnpcconfig")).setExecutor(new ReloadCommand(this));

        getServer().getPluginManager().registerEvents(npcCommand, this);

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String neonPurple = ChatColor.translateAlternateColorCodes('&', "&x&b&c&1&3&f&e");          // #BC13FE
        String sNeonPurple = ChatColor.translateAlternateColorCodes('&', "&x&a&8&0&1&e&9");         // #a801e9
        String neonBlue = ChatColor.translateAlternateColorCodes('&', "&x&1&f&5&1&f&f");            // #1F51FF
        String sNeonBlue = ChatColor.translateAlternateColorCodes('&', "&x&4&6&6&f&f&f");           // #466fff
        String white = ChatColor.translateAlternateColorCodes('&', "&x&f&f&f&f&f&f");// #FFFFFF

        console.sendMessage("");
        console.sendMessage(neonPurple +  "                   ░▒▓████████▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░░▒▓██████▓▒░                      ");
        console.sendMessage(neonPurple +  "                      ░▒▓█▓▒░   ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░                     ");
        console.sendMessage(neonPurple +  "                      ░▒▓█▓▒░   ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░                     ");
        console.sendMessage(neonPurple +  "                      ░▒▓█▓▒░   ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░                     ");
        console.sendMessage(neonPurple +  "                      ░▒▓█▓▒░   ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░                     ");
        console.sendMessage(neonPurple +  "                      ░▒▓█▓▒░   ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░                     ");
        console.sendMessage(neonPurple +  "                      ░▒▓█▓▒░    ░▒▓█████████████▓▒░ ░▒▓██████▓▒░                      ");
        console.sendMessage(sNeonPurple + "                               Enabling Two Worlds v1.1                                ");
        console.sendMessage(white +       "███████████████████████████████████████████████████████████████████████████████████████");
        console.sendMessage(sNeonBlue +   "                              Made with ♥ by LilBroCodes                               ");
        console.sendMessage(neonBlue +    "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░░▒▓██████▓▒░░▒▓███████▓▒░░▒▓█▓▒░      ░▒▓███████▓▒░ ░▒▓███████▓▒░ ");
        console.sendMessage(neonBlue +    "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░        ");
        console.sendMessage(neonBlue +    "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░        ");
        console.sendMessage(neonBlue +    "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓███████▓▒░░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░░▒▓██████▓▒░  ");
        console.sendMessage(neonBlue +    "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░      ░▒▓█▓▒░ ");
        console.sendMessage(neonBlue +    "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░      ░▒▓█▓▒░ ");
        console.sendMessage(neonBlue +    " ░▒▓█████████████▓▒░ ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░▒▓███████▓▒░░▒▓███████▓▒░  ");
        console.sendMessage("");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadPluginConfig() {
        reloadConfig();
    }

    public int getNPCTeleportInterval() {
        return getConfig().getInt("npc-teleport-interval", 1);
    }

    public double getNPCOffsetX() {
        return getConfig().getDouble("npc-offset.x", 5.0);
    }

    public double getNPCOffsetY() {
        return getConfig().getDouble("npc-offset.y", 0.0);
    }

    public double getNPCOffsetZ() {
        return getConfig().getDouble("npc-offset.z", 0.0);
    }
}
