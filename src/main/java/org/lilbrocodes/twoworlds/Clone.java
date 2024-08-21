package org.lilbrocodes.twoworlds;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

@SuppressWarnings({"deprecation"})
public class Clone implements Listener {
    private final TwoWorlds plugin;
    public final Player owner;
    public NPC npc;

    public Clone(TwoWorlds plugin, Player owner) {
        this.plugin = plugin;
        this.owner = owner;
        Bukkit.getLogger().info("Registering event listeners for Clone of player: " + owner.getName());
        Bukkit.getPluginManager().registerEvents(this, plugin);
        create();
    }

    private void create() {
        Bukkit.getLogger().info("Creating NPC for player: " + owner.getName());
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        npc = registry.createNPC(EntityType.PLAYER, owner.getName());

        npc.spawn(owner.getLocation());

        startTeleportTask();
    }

    private void changeNPCSkin(String skinName) {
        if (npc == null || !npc.isSpawned()) {
            return;
        }

        SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
        skinTrait.setSkinName(skinName);
        npc.getEntity().teleport(npc.getEntity().getLocation()); // Update to reflect skin change
    }

    private void startTeleportTask() {
        int interval = plugin.getNPCTeleportInterval();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!npc.isSpawned() || !owner.isOnline()) {
                    this.cancel();
                    return;
                }
                Location location = owner.getLocation();
                location.add(new Vector(plugin.getNPCOffsetX(), plugin.getNPCOffsetY(), plugin.getNPCOffsetZ()));
                npc.getEntity().teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);

                updateNPCInventoryAndEffects();
            }
        }.runTaskTimer(plugin, 0L, interval);
    }

    private void updateNPCInventoryAndEffects() {
        Player npcPlayer = (Player) npc.getEntity();
        npcPlayer.getInventory().setArmorContents(owner.getInventory().getArmorContents());
        npcPlayer.getInventory().setItemInOffHand(owner.getInventory().getItemInOffHand());
        npcPlayer.getInventory().setItemInMainHand(owner.getInventory().getItemInMainHand());
        npcPlayer.setHealth(owner.getHealth());

        npcPlayer.getActivePotionEffects().clear();
        owner.getActivePotionEffects().forEach(npcPlayer::addPotionEffect);
    }

    private boolean isCriticalHit(Player owner) {
        boolean criticalHit = owner.getVelocity().getY() < 0 &&
                !owner.isOnGround() &&
                !owner.isInWater() &&
                !owner.hasPotionEffect(PotionEffectType.BLINDNESS) &&
                !owner.hasPotionEffect(PotionEffectType.SLOW_FALLING) &&
                !owner.isInsideVehicle() &&
                !owner.isSprinting() &&
                !owner.isFlying() &&
                !(owner.getLocation().getBlock().getType().toString().contains("LADDER") ||
                        owner.getLocation().getBlock().getType().toString().contains("VINE"));

        Bukkit.getLogger().info("Critical hit check for player " + owner.getName() + ": " + criticalHit);
        return criticalHit;
    }

    private void onLuckEffectApplied(Player player) {
        changeNPCSkin("derizze");
        npc.setName("&k" + player.getName());
    }

    private void onLuckEffectRemoved(Player player) {
        changeNPCSkin(player.getName());
        npc.setName(player.getName());
    }

    @EventHandler
    public void onPotionEffectChange(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if (player == owner) {
            if (event.getModifiedType().equals(PotionEffectType.LUCK)) {
                if (event.getAction() == EntityPotionEffectEvent.Action.ADDED) {
                    onLuckEffectApplied(owner);
                } else if (event.getAction() == EntityPotionEffectEvent.Action.REMOVED) {
                    onLuckEffectRemoved(owner);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player damaged = (Player) event.getEntity();
        Bukkit.getLogger().info("Entity damaged: " + damaged.getName() + ", NPC entity: " + (npc.getEntity() != null ? npc.getEntity().getName() : "null"));
        if (npc.getEntity().equals(damaged)) {
            Bukkit.getLogger().info("NPC (" + npc.getId() + ") hit by " + event.getDamager().getName() + ". Damage: " + event.getDamage());
            if (owner.isOnline()) {
                handleNPCHit(event);
            } else {
                Bukkit.getLogger().info("Owner is offline, no damage applied.");
            }
        }
    }


    private void handleNPCHit(EntityDamageByEntityEvent event) {
        double damage = event.getDamage();
        Bukkit.getLogger().info("Applying damage to owner: " + damage);
        owner.damage(damage);

        Player damager = (Player) event.getDamager();
        if (isCriticalHit(damager)) {
            Bukkit.getLogger().info("Critical hit detected. Playing critical hit sound.");
            npc.getEntity().getWorld().playSound(npc.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
        } else {
            Bukkit.getLogger().info("Normal hit detected. Playing normal hit sound.");
            npc.getEntity().getWorld().playSound(npc.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().equals(owner)) {
            Bukkit.getLogger().info("Owner left the game. Removing NPC.");
            npc.despawn();
            npc.destroy();
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.getPlayer().equals(owner)) {
            Bukkit.getLogger().info("Owner respawned. Respawning NPC.");
            new BukkitRunnable() {
                @Override
                public void run() {
                    create();
                }
            }.runTaskLater(plugin, 1L);
        }
    }
}
