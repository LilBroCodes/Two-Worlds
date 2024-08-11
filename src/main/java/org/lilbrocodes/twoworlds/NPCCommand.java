package org.lilbrocodes.twoworlds;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings({"NullableProblems", "ApiStatus.Experimental"})
public class NPCCommand implements CommandExecutor, Listener {
    private final TwoWorlds plugin;
    private final ArrayList<NPC> npcList = new ArrayList<>();
    private final HashMap<NPC, Player> npcOwners = new HashMap<>(); // To store the owners of each NPC

    public NPCCommand(TwoWorlds plugin) {
        this.plugin = plugin;
    }

    public void changeNPCSkin(NPC npc, String skinName) {
        if (npc == null || !npc.isSpawned()) {
            return;
        }

        SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
        skinTrait.setSkinName(skinName);
        npc.getEntity().teleport(npc.getEntity().getLocation());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can run this command.");
            return false;
        }
        Player sender = (Player) commandSender;

        NPCRegistry registry = CitizensAPI.getNPCRegistry();

        NPC npc = registry.createNPC(EntityType.PLAYER, sender.getName());
        npc.spawn(sender.getLocation());

        npcList.add(npc);
        npcOwners.put(npc, sender);

        int interval = plugin.getNPCTeleportInterval();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!npc.isSpawned() || !sender.isOnline()) {
                    this.cancel();
                    return;
                }
                Location location = sender.getLocation();
                location.add(new Vector(plugin.getNPCOffsetX(), plugin.getNPCOffsetY(), plugin.getNPCOffsetZ()));
                npc.getEntity().teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);

                Player npcPlayer = (Player) npc.getEntity();
                npcPlayer.getInventory().setArmorContents(sender.getInventory().getArmorContents());
                npcPlayer.getInventory().setItemInOffHand(sender.getInventory().getItemInOffHand());
                npcPlayer.getInventory().setItemInMainHand(sender.getInventory().getItemInMainHand());
                npcPlayer.setHealth(sender.getHealth());

                npcPlayer.getActivePotionEffects().clear();
                sender.getActivePotionEffects().forEach(npcPlayer::addPotionEffect);
            }
        }.runTaskTimer(plugin, 0L, interval);


        return true;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player npcPlayer = (Player) entity;
            NPC npc = findNpcByPlayer(npcPlayer);

            if (npc != null) {
                Player owner = npcOwners.get(npc);
                if (owner != null && owner.isOnline()) {
                    double damage = event.getDamage();

                    // Apply damage to the owner
                    DamageSource damageSource = DamageSource.builder(DamageType.PLAYER_ATTACK).build();
                    owner.damage(damage, damageSource);

                    // Check if critical hit conditions are met
                    boolean isFalling = owner.getVelocity().getY() < 0;
                    boolean isNotOnGround = !owner.isOnGround();
                    boolean isNotOnLadderOrVine = !(owner.getLocation().getBlock().getType().toString().contains("LADDER") || owner.getLocation().getBlock().getType().toString().contains("VINE"));
                    boolean isNotInWater = !owner.isInWater();
                    boolean isNotBlind = !owner.hasPotionEffect(PotionEffectType.BLINDNESS);
                    boolean isNotSlowFalling = !owner.hasPotionEffect(PotionEffectType.SLOW_FALLING);
                    boolean isNotRiding = !owner.isInsideVehicle();
                    boolean isNotFlyingOrSprinting = !owner.isSprinting() && !owner.isFlying();

                    if (isFalling && isNotOnGround && isNotOnLadderOrVine && isNotInWater && isNotBlind && isNotSlowFalling && isNotRiding && isNotFlyingOrSprinting) {
                        // Play critical hit sound
                        owner.getWorld().playSound(owner.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
                    } else {
                        owner.getWorld().playSound(owner.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);
                    }

                    event.setCancelled(true); // Cancel the damage to the NPC
                }
            }
        }
    }


    private NPC findNpcByPlayer(Player player) {
        for (NPC npc : npcList) {
            if (npc.getName().equals(player.getName())) {
                return npc;
            }
        }
        return null;
    }

    @EventHandler
    public void onPotionEffectChange(EntityPotionEffectEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // Check if the event is about the LUCK effect
            if (event.getModifiedType().equals(PotionEffectType.LUCK)) {
                if (event.getAction() == EntityPotionEffectEvent.Action.ADDED) {
                    onLuckEffectApplied(player);
                } else if (event.getAction() == EntityPotionEffectEvent.Action.REMOVED) {
                    onLuckEffectRemoved(player);
                }
            }
        }
    }

    private void onLuckEffectApplied(Player player) {
        player.sendMessage("onLuckEffectApplied");
        NPC npc = null;
        int i = 0;
        while (npc == null) {
            if (i >= npcList.size()) {
                return;
            }
            NPC cNpc = npcList.get(i);
            if (Objects.equals(cNpc.getName(), player.getName())) {
                npc = cNpc;
            }
            i++;
        }
        player.sendMessage("Change Skin");
        changeNPCSkin(npc, "derizze");
        npc.setName("&k" + player.getName());
    }

    private void onLuckEffectRemoved(Player player) {
        player.sendMessage("onLuckEffectRemoved");
        NPC cNpc = findNpcByPlayer(player);

        if (cNpc == null) {
            return;
        }
        player.sendMessage("Revert Skin");
        changeNPCSkin(cNpc, player.getName());
        cNpc.setName(player.getName());
    }
}
