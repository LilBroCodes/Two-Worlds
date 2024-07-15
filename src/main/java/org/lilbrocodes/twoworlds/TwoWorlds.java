package org.lilbrocodes.twoworlds;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TwoWorlds extends JavaPlugin {
    private static TwoWorlds instance;

    public static TwoWorlds getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Objects.requireNonNull(this.getCommand("fp")).setExecutor(new NPCCommand(this));
        Objects.requireNonNull(this.getCommand("reloadnpcconfig")).setExecutor(new ReloadCommand(this));
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
