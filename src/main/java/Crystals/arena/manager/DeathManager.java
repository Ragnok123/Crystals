package Crystals.arena.manager;

import Crystals.Stat;
import Crystals.arena.Arena;
import Crystals.arena.kit.Kit;
import Crystals.arena.object.PlayerData;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.sound.ExperienceOrbSound;
import cn.nukkit.utils.TextFormat;

public class DeathManager {
      public Arena plugin;

      public DeathManager(Arena plugin) {
            this.plugin = plugin;
      }

      public void onDeath(EntityDamageEvent e) {
            Player p = (Player)e.getEntity();
            PlayerData data = this.plugin.getPlayerData(p);
            this.plugin.addStat(data, Stat.DEATHS);
            String pColor = "" + data.getTeam().getColor();
            String dColor = "";
            boolean escape = false;
            ExperienceOrbSound xpSound;
            if (e instanceof EntityDamageByEntityEvent) {
                  Entity killer = ((EntityDamageByEntityEvent)e).getDamager();
                  Player playerKiller = null;
                  PlayerData killerData = null;
                  if (killer instanceof Player) {
                        playerKiller = (Player)killer;
                        killerData = this.plugin.getPlayerData(playerKiller);
                        this.plugin.addStat(killerData, Stat.KILLS);
                        playerKiller.addExperience(85);
                        xpSound = new ExperienceOrbSound(playerKiller.temporalVector.setComponents(playerKiller.x, playerKiller.y + (double)playerKiller.getEyeHeight(), playerKiller.z));
                        playerKiller.getLevel().addSound(xpSound, playerKiller);
                        if (killerData.getKit() == Kit.BERSERKER && playerKiller.getMaxHealth() < 30) {
                              playerKiller.setMaxHealth(playerKiller.getMaxHealth() + 2);
                              playerKiller.sendAttributes();
                        }
                  }

                  Item item;
                  if (e instanceof EntityDamageByChildEntityEvent) {
                        if (playerKiller != null) {
                              item = playerKiller.getInventory().getItemInHand();
                              if (item.hasCustomName() && !this.plugin.isSoulbound(item)) {
                                    this.plugin.messageAllPlayers("shot_using", new String[]{pColor + p.getName(), killerData.getTeam().getColor() + playerKiller.getName()});
                              } else {
                                    this.plugin.messageAllPlayers("shot", new String[]{pColor + p.getName(), killerData.getTeam().getColor() + playerKiller.getName(), item.getCustomName()});
                              }
                        } else {
                              this.plugin.messageAllPlayers("shot", new String[]{pColor + p.getName(), TextFormat.YELLOW + killer.getName()});
                        }
                  } else if (playerKiller != null) {
                        item = playerKiller.getInventory().getItemInHand();
                        if (item.hasCustomName() && !this.plugin.isSoulbound(item)) {
                              this.plugin.messageAllPlayers("contact_player_using", new String[]{pColor + p.getName(), killerData.getTeam().getColor() + playerKiller.getName(), item.getCustomName()});
                        } else {
                              this.plugin.messageAllPlayers("contact_player", new String[]{pColor + p.getName(), killerData.getTeam().getColor() + playerKiller.getName()});
                        }
                  } else {
                        this.plugin.messageAllPlayers("contact", new String[]{TextFormat.YELLOW + killer.getName(), pColor + p.getName()});
                  }

            } else {
                  String killer = "";
                  PlayerData killerData = data.wasKilled();
                  data.setKiller((PlayerData)null);
                  Player playerKiller = null;
                  if (killerData != null) {
                        escape = true;
                        dColor = "" + killerData.getTeam().getColor();
                        killer = killerData.getName();
                        this.plugin.addStat(killerData, Stat.KILLS);
                        if (killerData.getPlayer().isOnline()) {
                              playerKiller = killerData.getPlayer();
                        }
                  }

                  if (escape && playerKiller != null && this.plugin.inArena(playerKiller)) {
                        playerKiller.addExperience(85);
                        xpSound = new ExperienceOrbSound(playerKiller.temporalVector.setComponents(playerKiller.x, playerKiller.y + (double)playerKiller.getEyeHeight(), playerKiller.z));
                        playerKiller.getLevel().addSound(xpSound, playerKiller);
                        if (killerData.getKit() == Kit.BERSERKER && playerKiller.getMaxHealth() < 30) {
                              playerKiller.setMaxHealth(playerKiller.getMaxHealth() + 1);
                              playerKiller.sendAttributes();
                        }
                  }

                  String arg1 = pColor + p.getName();
                  String arg2 = dColor + killer;
                  switch(e.getCause()) {
                  case CONTACT:
                        if (escape) {
                              this.plugin.messageAllPlayers("cactus_escape", new String[]{arg1, arg2});
                              return;
                        }

                        this.plugin.messageAllPlayers("cactus", new String[]{arg1});
                        break;
                  case SUFFOCATION:
                        this.plugin.messageAllPlayers("suffocate", new String[]{arg1});
                        break;
                  case FALL:
                        if (escape) {
                              this.plugin.messageAllPlayers("fall_escape", new String[]{arg1, arg2});
                        } else {
                              this.plugin.messageAllPlayers("fall", new String[]{arg1});
                        }
                        break;
                  case FIRE:
                        if (escape) {
                              this.plugin.messageAllPlayers("fire_escape", new String[]{arg1, arg2});
                        } else {
                              this.plugin.messageAllPlayers("fire", new String[]{arg1});
                        }
                        break;
                  case FIRE_TICK:
                        if (escape) {
                              this.plugin.messageAllPlayers("fire_tick_escape", new String[]{arg1, arg2});
                        } else {
                              this.plugin.messageAllPlayers("fire_tick", new String[]{arg1});
                        }
                        break;
                  case LAVA:
                        if (escape) {
                              this.plugin.messageAllPlayers("lava_escape", new String[]{arg1, arg2});
                        } else {
                              this.plugin.messageAllPlayers("lava", new String[]{arg1});
                        }
                        break;
                  case DROWNING:
                        if (escape) {
                              this.plugin.messageAllPlayers("drowning_escape", new String[]{arg1, arg2});
                        } else {
                              this.plugin.messageAllPlayers("drowning", new String[]{arg1});
                        }
                        break;
                  case BLOCK_EXPLOSION:
                  case ENTITY_EXPLOSION:
                        this.plugin.messageAllPlayers("explosion", new String[]{arg1});
                        break;
                  case VOID:
                        if (escape) {
                              this.plugin.messageAllPlayers("fall_escape", new String[]{arg1, arg2});
                        } else {
                              this.plugin.messageAllPlayers("void", new String[]{arg1});
                        }
                        break;
                  default:
                        this.plugin.messageAllPlayers("unknown", new String[]{arg1});
                  }

            }
      }
}
