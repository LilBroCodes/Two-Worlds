package org.lilbrocodes.twoworlds;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCCommand implements CommandExecutor {
    private final TwoWorlds plugin;

    public NPCCommand(TwoWorlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can run this command.");
            return false;
        }
        Player sender = (Player) commandSender;

        // Get the NPC registry and delete all existing NPCs
        NPCRegistry registry = CitizensAPI.getNPCRegistry();

        // Create a new NPC
        NPC npc = registry.createNPC(EntityType.PLAYER, sender.getName());
        npc.spawn(sender.getLocation());

        // Schedule a repeating task to teleport the NPC to the player's location
        int interval = plugin.getNPCTeleportInterval();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!npc.isSpawned() || !sender.isOnline()) {
                    this.cancel();
                    return;
                }
                Location location = sender.getLocation();
                location.setX(location.getX() + plugin.getNPCOffsetX());
                location.setY(location.getY() + plugin.getNPCOffsetY());
                location.setZ(location.getZ() + plugin.getNPCOffsetZ());
                npc.teleport(location, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }.runTaskTimer(plugin, 0L, interval); // Use interval from config

        Entity npcEntity = npc.getEntity();
        if (npcEntity instanceof Player) {
            Player npcPlayer = (Player) npcEntity;
            sender.sendMessage(String.valueOf(npcPlayer.getHealth()));
        } else {
            sender.sendMessage("not pog :(");
        }
        return true;
    }
}
