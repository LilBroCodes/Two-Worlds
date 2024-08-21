package org.lilbrocodes.twoworlds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class TwoWorlds extends JavaPlugin {
    public boolean npcSync = true;
    private final ArrayList<Clone> clones = new ArrayList<>();
    @Override
    public void onEnable() {
        saveDefaultConfig();
        sendStartupMessage();

        Objects.requireNonNull(this.getCommand("tw")).setExecutor(new TWCommand(this));
        Objects.requireNonNull(this.getCommand("tw")).setTabCompleter(new TWTabCompleter());

        Objects.requireNonNull(this.getCommand("reloadnpcconfig")).setExecutor(new ReloadCommand(this));


        startEventLoop();
    }

    @Override
    public void onDisable() {

    }

    private void startEventLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                onTick();
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    public void onTick() {
        if (npcSync) {
            Set<Player> onlinePlayers = new HashSet<>(Bukkit.getOnlinePlayers());

            clones.removeIf(clone -> !onlinePlayers.contains(clone.owner));

            Set<Player> clonedPlayers = new HashSet<>();
            for (Clone clone : clones) {
                clonedPlayers.add(clone.owner);
            }

            for (Player player : onlinePlayers) {
                if (!clonedPlayers.contains(player)) {
                    clones.add(new Clone(this, player));
                }
            }
        } else {
            clones.clear();
        }
    }

    public String getSenderString(String message) {
        String sNeonBlue = ChatColor.translateAlternateColorCodes('&', "&x&4&6&6&f&f&f");           // #466fff
        String sNeonPurple = ChatColor.translateAlternateColorCodes('&', "&x&a&8&0&1&e&9");         // #a801e9
        String white = ChatColor.translateAlternateColorCodes('&', "&x&f&f&f&f&f&f");               // #FFFFFF

        String prefix = String.format("<%sTwo%sWorlds%s> ", sNeonPurple, sNeonBlue, white);
        return prefix + message;
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

    private void sendStartupMessage() {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String neonPurple = ChatColor.translateAlternateColorCodes('&', "&x&b&c&1&3&f&e");          // #BC13FE
        String sNeonPurple = ChatColor.translateAlternateColorCodes('&', "&x&a&8&0&1&e&9");         // #a801e9
        String neonBlue = ChatColor.translateAlternateColorCodes('&', "&x&1&f&5&1&f&f");            // #1F51FF
        String sNeonBlue = ChatColor.translateAlternateColorCodes('&', "&x&4&6&6&f&f&f");           // #466fff
        String white = ChatColor.translateAlternateColorCodes('&', "&x&f&f&f&f&f&f");               // #FFFFFF

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
}