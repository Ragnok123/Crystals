package Crystals.arena;

import Crystals.Crystals;
import Crystals.Stat;
import Crystals.arena.kit.Kit;
import Crystals.arena.manager.ArenaManager;
import Crystals.arena.manager.BossManager;
import Crystals.arena.manager.DeathManager;
import Crystals.arena.manager.KitManager;
import Crystals.arena.manager.VotingManager;
import Crystals.arena.manager.WorldManager;
import Crystals.arena.object.ArenaData;
import Crystals.arena.object.Crystal;
import Crystals.arena.object.PlayerData;
import Crystals.arena.object.Resource;
import Crystals.arena.object.Team;
import Crystals.arena.task.BlockRegenerateTask;
import Crystals.arena.task.FireworkTask;
import Crystals.arena.task.WorldCopyTask;
import Crystals.arena.util.Border2D;
import Crystals.arena.util.BossBarUtil;
import Crystals.arena.util.Utils;
import Crystals.entity.CombatLoggerNPC;
import Crystals.entity.EntityCrystal;
import Crystals.entity.EntityMerchant;
import Crystals.entity.EntityPotionMerchant;
import Crystals.entity.EntityWitherBoss;
import Crystals.form.FormWindowManager;
import Crystals.lang.Language;
import Crystals.mysql.QuitQuery;
import GTCore.MTCore;
import GTCore.Object.BossBar;
import GTCore.player.GTPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.ShortEntityData;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityCombustByEntityEvent;
import cn.nukkit.event.entity.EntityCombustEvent;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.inventory.InventoryCloseEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.event.player.PlayerAchievementAwardedEvent;
import cn.nukkit.event.player.PlayerBedEnterEvent;
import cn.nukkit.event.player.PlayerBucketFillEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerFoodLevelChangeEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.inventory.EnchantInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.level.particle.HugeExplodeSeedParticle;
import cn.nukkit.level.sound.ExperienceOrbSound;
import cn.nukkit.level.sound.ExplodeSound;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.TextFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

public class Arena extends ArenaManager implements Listener {
      public Crystals plugin;
      public String id;
      public ArenaSchedule task;
      private FireworkTask fireworkTask;
      public KitManager kitManager;
      public VotingManager votingManager;
      public WorldManager worldManager;
      public BossManager bossManager;
      public DeathManager deathManager;
      private FormWindowManager formWindowManager = new FormWindowManager(this);
      private static Arena instance;
      public Arena.GamePhase phase;
      public boolean starting;
      public boolean ending;
      public Level level;
      public final HashMap playersData;
      public final HashMap players;
      public ArenaData data;
      public HashMap maindata;
      public MTCore mtcore;
      public Team winnerTeam;
      public String map;
      public Border2D mapZone;
      public final BossBar bossBar;
      public final BossBarUtil barUtil;
      private final Set placedBlocks;
      private HashSet cobblestone;
      public boolean isLevelLoaded;

      public Arena(String id, Crystals plugin) {
            this.phase = Arena.GamePhase.LOBBY;
            this.starting = false;
            this.ending = false;
            this.playersData = new HashMap();
            this.players = new HashMap();
            this.placedBlocks = new HashSet();
            this.cobblestone = new HashSet();
            this.isLevelLoaded = false;
            super.plugin = this;
            this.plugin = plugin;
            this.id = id;
            this.maindata = (HashMap)this.plugin.arenas.get(id);
            this.mtcore = this.plugin.mtcore;
            this.bossBar = new BossBar(plugin);
            this.barUtil = new BossBarUtil(this);
            this.kitManager = new KitManager(this);
            this.votingManager = new VotingManager(this);
            this.worldManager = new WorldManager();
            this.bossManager = new BossManager(this);
            this.deathManager = new DeathManager(this);
            this.votingManager.createVoteTable();
            this.plugin.getServer().getScheduler().scheduleRepeatingTask(this.task = new ArenaSchedule(this), 20);
            instance = this;
            this.barUtil.updateBar(0);
      }

      public static Arena getInstance() {
            return instance;
      }

      @EventHandler
      public void onPlayerQuit(PlayerQuitEvent e) {
            this.handlePlayerQuit(e.getPlayer());
      }

      public void handlePlayerQuit(Player p) {
            if (this.inArena(p)) {
                  PlayerData data = this.getPlayerData(p);
                  Team team = data.getTeam();
                  if (team != null) {
                        team.removePlayer(p);
                        if (this.phase == Arena.GamePhase.GAME && p.isAlive() && p.getInventory() != null) {
                              data.saveInventory(p);
                              this.createLeaveNPC(p, data);
                        }

                        if (team.getCrystal() != null && !team.getCrystal().isAlive()) {
                              this.playersData.remove(p.getName().toLowerCase());
                        }
                  }

                  p.setMaxHealth(20);
                  data.setInLobby(false);
                  this.players.remove(p.getName().toLowerCase());
                  this.bossBar.removePlayer(p);
                  if (this.phase == Arena.GamePhase.GAME) {
                        this.checkAlive();
                  }

                  new QuitQuery(p.getName(), data);
                  if (p.isOnline()) {
                        this.plugin.mtcore.setLobby(p);
                  }
            }

      }

      public boolean joinToArena(Player p) {
            if (this.inArena(p)) {
                  return true;
            } else {
                  boolean wasInGame = false;
                  PlayerData data = this.getPlayerData(p);
                  if (data != null) {
                        data = this.getPlayerData(p);
                        if (data.getTeam() != null) {
                              wasInGame = true;
                        }
                  }

                  if (this.task.time > 600 && !this.canJoin() && !wasInGame && !p.hasPermission("gameteam.helper")) {
                        p.sendMessage(Crystals.getPrefix() + Language.translate("join_high_phase", p));
                        return false;
                  } else {
                        if (data == null) {
                              data = this.createPlayerData(p);
                        } else {
                              data.setBaseData(this.mtcore.getPlayerData(p));
                        }

                        if (data.npc != null && !data.npc.closed) {
                              p.sendMessage(Crystals.getPrefix() + Language.translate("npc_alive", data.getBaseData()));
                              return false;
                        } else {
                              if (data.npcKilled) {
                                    p.sendMessage(Crystals.getPrefix() + Language.translate("npc_killed", data.getBaseData()));
                              }

                              data.npc = null;
                              data.npcKilled = false;
                              if (wasInGame && this.phase == Arena.GamePhase.GAME && !data.getTeam().getCrystal().isAlive()) {
                                    p.sendMessage(Crystals.getPrefix() + Language.translate("join_nexus_destroyed", data.getBaseData()));
                                    return false;
                              } else {
                                    this.players.put(p.getName().toLowerCase(), p);
                                    this.plugin.mtcore.unsetLobby(p);
                                    if (wasInGame) {
                                          this.addToTeam(p, data.getTeam());
                                          if (this.phase == Arena.GamePhase.GAME) {
                                                this.teleportToArena(p);
                                                this.bossBar.addPlayer(p);
                                                return true;
                                          }

                                          data.setInLobby(true);
                                          this.kitManager.addKitSelectItem(p);
                                    } else {
                                          p.setNameTag(p.getName());
                                          data.setInLobby(true);
                                          this.kitManager.addKitSelectItem(p);
                                    }

                                    this.bossBar.addPlayer(p);
                                    Vector3 newPos = (Vector3)this.maindata.get("inLobby");
                                    p.setSpawn(newPos);
                                    this.checkLobby();
                                    return true;
                              }
                        }
                  }
            }
      }

      @EventHandler
      public void onHurt(EntityDamageEvent e) {
            Entity entity = e.getEntity();
            if (e.getCause() == DamageCause.VOID && entity instanceof Player) {
                  PlayerData data = this.getPlayerData((Player)entity);
                  if (data != null && data.isInLobby()) {
                        entity.teleport((Vector3)this.maindata.get("inLobby"));
                        e.setCancelled();
                        return;
                  }
            }

            if (!e.isCancelled()) {
                  Entity damager;
                  Player killer;
                  PlayerData data;
                  if (entity instanceof EntityCrystal) {
                        e.setCancelled();
                        EntityCrystal crystal = (EntityCrystal)entity;
                        if (e instanceof EntityDamageByEntityEvent) {
                              damager = ((EntityDamageByEntityEvent)e).getDamager();
                              if (damager instanceof Player) {
                                    killer = (Player)damager;
                                    data = this.getPlayerData(killer);
                                    if (data == null) {
                                          return;
                                    }

                                    if (data.getTeam().getIndex() == crystal.getTeam()) {
                                          return;
                                    }

                                    Team team = this.getTeam(crystal.getTeam() + 1);
                                    if (team == null) {
                                          return;
                                    }

                                    if (team.getCrystal().isAlive()) {
                                          this.destroyCrystal(killer, team, crystal);
                                    }
                              }
                        }

                  } else {
                        Entity damager;
                        PlayerData killerData;
                        if (entity instanceof CombatLoggerNPC) {
                              if (e.getFinalDamage() >= entity.getHealth()) {
                                    CombatLoggerNPC npc = (CombatLoggerNPC)entity;
                                    List items = new ArrayList();
                                    items.add(npc.getItemInHand());
                                    Collections.addAll(items, npc.getItems());
                                    Iterator var21 = items.iterator();

                                    while(var21.hasNext()) {
                                          Item item = (Item)var21.next();
                                          if (!this.isSoulbound(item)) {
                                                this.level.dropItem(entity, item);
                                          }
                                    }

                                    killerData = this.getPlayerData(npc.getPlayerName());
                                    if (killerData != null) {
                                          killerData.npc = null;
                                          killerData.npcKilled = true;
                                          killerData.inventory = null;
                                    }

                                    entity.close();
                                    if (e instanceof EntityDamageByEntityEvent) {
                                          damager = ((EntityDamageByEntityEvent)e).getDamager();
                                          if (damager instanceof Player) {
                                                ((Player)damager).addExperience(85);
                                          }
                                    }
                              }

                        } else if (entity instanceof EntityWitherBoss) {
                              EntityWitherBoss boss = (EntityWitherBoss)entity;
                              if (e instanceof EntityDamageByEntityEvent && !(e instanceof EntityDamageByChildEntityEvent)) {
                                    damager = ((EntityDamageByEntityEvent)e).getDamager();
                                    if (damager instanceof Player) {
                                          killer = (Player)damager;
                                          data = this.getPlayerData(killer);
                                          if (data == null) {
                                                e.setCancelled();
                                                return;
                                          }

                                          if (data.getTeam().getIndex() != boss.getTeam()) {
                                                killer.sendMessage(Language.translate("wrong_boss", killer));
                                                e.setCancelled();
                                                return;
                                          }

                                          boss.damagers.compute(killer.getName(), (k, v) -> {
                                                return (v == null ? 0.0F : v) + e.getFinalDamage();
                                          });
                                          if (e.getFinalDamage() >= boss.getHealth()) {
                                                Entry[] sorted = (Entry[])boss.damagers.entrySet().stream().sorted(Entry.comparingByValue()).toArray((x$0) -> {
                                                      return new Entry[x$0];
                                                });
                                                String[] args = new String[6];

                                                int i;
                                                for(i = 0; i < sorted.length; ++i) {
                                                      Entry entry = sorted[i];
                                                      args[i * 2 - 1] = (String)entry.getKey();
                                                      args[i * 2] = String.valueOf(entry.getValue());
                                                }

                                                for(i = 0; i < args.length; ++i) {
                                                      if (args[i] == null) {
                                                            args[i] = i % 2 == 0 ? "---" : "0";
                                                      }
                                                }

                                                this.translateToPlayers("boss_death", data.getTeam().getPlayers().values(), args);
                                                this.bossManager.applyReward(data.getTeam());
                                                boss.close();
                                                Entity[] var30 = this.level.getEntities();
                                                int var31 = var30.length;

                                                for(int var11 = 0; var11 < var31; ++var11) {
                                                      Entity ent = var30[var11];
                                                      if (ent instanceof EntityWitherBoss) {
                                                            ent.close();
                                                      }
                                                }
                                          } else {
                                                boss.updateNameTag();
                                          }
                                    }

                              } else {
                                    e.setCancelled();
                              }
                        } else if (entity instanceof Player && this.inArena((Player)entity)) {
                              Player p = (Player)entity;
                              PlayerData data = this.getPlayerData((Player)entity);
                              if (data == null) {
                                    e.setCancelled();
                              } else if (data.isInLobby()) {
                                    e.setCancelled();
                                    if (e.getCause() == DamageCause.VOID) {
                                          entity.teleport((Vector3)this.maindata.get("inLobby"));
                                    }

                              } else if (e.getCause() == DamageCause.FALL && data.getKit() == Kit.ACROBAT) {
                                    e.setCancelled();
                              } else {
                                    if (e instanceof EntityDamageByChildEntityEvent) {
                                          killer = (Player)((EntityDamageByChildEntityEvent)e).getDamager();
                                          Player victim = (Player)e.getEntity();
                                          if (killer != null && victim != null) {
                                                PlayerData killerData = this.getPlayerData(killer);
                                                if (killerData.getTeam().getIndex() == data.getTeam().getIndex() || this.phase == Arena.GamePhase.LOBBY) {
                                                      e.setCancelled();
                                                      return;
                                                }

                                                if (killerData.getKit() == Kit.ARCHER) {
                                                      e.setDamage(e.getDamage() + 1.0F);
                                                }

                                                data.setKiller(killerData);
                                          }
                                    } else if (e instanceof EntityDamageByEntityEvent) {
                                          Entity victim = e.getEntity();
                                          damager = ((EntityDamageByEntityEvent)e).getDamager();
                                          if (damager instanceof Player && victim instanceof Player) {
                                                Player killer = (Player)damager;
                                                PlayerData killerData = this.getPlayerData(killer);
                                                if (killerData.getTeam() == null || data.getTeam() == null) {
                                                      e.setCancelled();
                                                      return;
                                                }

                                                if (!this.inArena(killer) || killerData.getTeam().getIndex() == data.getTeam().getIndex() || this.phase == Arena.GamePhase.LOBBY) {
                                                      e.setCancelled();
                                                      return;
                                                }

                                                data.setKiller(killerData);
                                          }
                                    }

                                    if (!e.isCancelled() && e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)e).getDamager() instanceof Player) {
                                          killerData = this.getPlayerData((Player)((EntityDamageByEntityEvent)e).getDamager());
                                          if (killerData != null) {
                                                Kit kit = killerData.getKit();
                                                if (e instanceof EntityDamageByChildEntityEvent) {
                                                      if (kit == Kit.ARCHER) {
                                                            e.setDamage(e.getDamage() + 1.0F);
                                                      }
                                                } else if (kit == Kit.WARRIOR) {
                                                      e.setDamage(e.getDamage() + 1.0F);
                                                }
                                          }
                                    }

                                    if (p.getHealth() - e.getFinalDamage() < 1.0F) {
                                          e.setCancelled();
                                          this.respawn(p, data, e);
                                    }

                              }
                        }
                  }
            }
      }

      @EventHandler
      public void onCombust(EntityCombustEvent e) {
            Entity entity = e.getEntity();
            if (e instanceof EntityCombustByEntityEvent) {
                  Entity combuster = ((EntityCombustByEntityEvent)e).getCombuster();
                  if (!(combuster instanceof Player)) {
                        return;
                  }

                  Player damager = (Player)combuster;
                  if (entity instanceof Player) {
                        Player p = (Player)entity;
                        PlayerData data = this.getPlayerData(p);
                        if (data == null) {
                              return;
                        }

                        PlayerData data1 = this.getPlayerData(damager);
                        if (data1 == null) {
                              e.setCancelled();
                              return;
                        }

                        if (data1.getTeam().getIndex() == data.getTeam().getIndex()) {
                              e.setCancelled();
                        }
                  } else if (entity instanceof CombatLoggerNPC) {
                        PlayerData data = this.getPlayerData(damager);
                        if (data == null || data.getTeam() == null) {
                              e.setCancelled();
                              return;
                        }

                        int team = ((CombatLoggerNPC)entity).getTeam();
                        if (team == data.getTeam().getIndex()) {
                              e.setCancelled();
                        }
                  }
            }

      }

      @EventHandler
      public void onBlockBreak(BlockBreakEvent e) {
            Block b = e.getBlock();
            Player player = e.getPlayer();
            if (!e.isCancelled() && !e.isFastBreak()) {
                  e.setInstaBreak(true);
                  if (this.inArena(player)) {
                        if (this.phase == Arena.GamePhase.LOBBY) {
                              e.setCancelled();
                        } else if (this.nearNexus(b)) {
                              e.setCancelled();
                              player.sendMessage(Language.translate("near_nexus_break", player));
                        } else if (!this.mapZone.isVectorInside((Vector3)b)) {
                              e.setCancelled();
                              player.sendMessage(Language.translate("outside_zone", player));
                        } else {
                              Resource resource;
                              if ((resource = Resource.fromId(b.getId())) != null) {
                                    e.setDrops(new Item[0]);
                                    if (b.getId() == 13) {
                                          e.setCancelled();
                                          player.getInventory().addItem(this.getGravelDrops());
                                          this.level.setBlock(b, Block.get(4));
                                          this.cobblestone.add(b.getLocationHash());
                                    } else if (b.getId() == 103) {
                                          player.getInventory().addItem(new Item[]{Item.get(360, 0, (new Random()).nextInt(5) + 3)});
                                    } else {
                                          boolean miner = resource.getType() == Resource.ResourceType.ORE;
                                          boolean lumberjack = resource.getType() == Resource.ResourceType.WOOD;
                                          Kit kit = this.getPlayerData(player).getKit();
                                          int count = (kit != Kit.MINER || !miner) && (kit != Kit.LUMBERJACK || !lumberjack) ? 1 : 2;
                                          if (miner) {
                                                e.setCancelled();
                                                this.level.setBlock(b, Block.get(4));
                                                this.cobblestone.add(b.getLocationHash());
                                          }

                                          Item drop = resource.getDrops(b, b.getDrops(e.getItem()));
                                          drop.count *= count;
                                          player.getInventory().addItem(new Item[]{drop.clone()});
                                    }

                                    if (resource.getXp() > 0) {
                                          Team team = this.getPlayerTeam(player);
                                          player.addExperience(resource.getXp() * (team.isDoubleXp() ? 2 : 1));
                                          ExperienceOrbSound xpSound = new ExperienceOrbSound(player.temporalVector.setComponents(player.x, player.y + (double)player.getEyeHeight(), player.z));
                                          player.getLevel().addSound(xpSound, player);
                                    }

                                    Item item = player.getInventory().getItemInHand();
                                    if (item.isTool()) {
                                          item.setDamage(Math.max(0, item.getDamage() - 1));
                                          player.getInventory().sendHeldItem(new Player[]{player});
                                    }

                                    this.plugin.getServer().getScheduler().scheduleDelayedTask(new BlockRegenerateTask(b), resource.getDelay() * 20);
                                    b.getLevel().addParticle(new DestroyBlockParticle(b.add(0.5D), b));
                              } else if (b.getId() == 4 && this.cobblestone.contains(b.getLocationHash())) {
                                    e.setCancelled();
                              } else {
                                    BlockVector3 blockPos = new BlockVector3(b.getFloorX(), b.getFloorY(), b.getFloorZ());
                                    if (!this.placedBlocks.contains(blockPos)) {
                                          player.sendMessage(Language.translate("block_part_map", player));
                                          e.setCancelled();
                                    } else {
                                          this.placedBlocks.remove(blockPos);
                                    }
                              }

                              player.getInventory().sendContents(player);
                        }
                  }
            }
      }

      public Item[] getGravelDrops() {
            Random rand = new Random();
            Item arrows = Item.get(262, Math.max(rand.nextInt(5) - 2, 0));
            Item flint = Item.get(318, Math.max(rand.nextInt(4) - 2, 0));
            Item feathers = Item.get(288, Math.max(rand.nextInt(4) - 2, 0));
            Item string = Item.get(287, Math.max(rand.nextInt(5) - 3, 0));
            Item bones = Item.get(352, Math.max(rand.nextInt(4) - 2, 0));
            return new Item[]{arrows, flint, feathers, string, bones};
      }

      @EventHandler
      public void onBlockPlace(BlockPlaceEvent e) {
            Player player = e.getPlayer();
            Block b = e.getBlock();
            if (this.inArena(player) && !this.inLobby(player)) {
                  if (this.phase != Arena.GamePhase.LOBBY) {
                        if (b.getId() == 7 && !player.isOp()) {
                              e.setCancelled();

                              for(int i = 0; i < player.getInventory().getSize(); ++i) {
                                    Item item = player.getInventory().getItem(i);
                                    if (item.getId() == 7) {
                                          player.getInventory().clear(i);
                                    }
                              }
                        }

                        if (!this.mapZone.isVectorInside((Vector3)b)) {
                              e.setCancelled();
                              player.sendMessage(Language.translate("outside_zone", player));
                        } else if (this.nearNexus(b)) {
                              e.setCancelled();
                              player.sendMessage(Language.translate("near_nexus_break", player));
                        }
                  }
            }
      }

      @EventHandler(
            priority = EventPriority.NORMAL,
            ignoreCancelled = false
      )
      public void onBucketFill(PlayerBucketFillEvent e) {
            Player p = e.getPlayer();
            if (!p.isOp()) {
                  e.setCancelled();
            }

      }

      @EventHandler(
            priority = EventPriority.NORMAL,
            ignoreCancelled = false
      )
      public void onAchievement(PlayerAchievementAwardedEvent e) {
            e.setCancelled();
      }

      public void respawn(Player p, PlayerData data, EntityDamageEvent e) {
            e.setCancelled();
            EntityEventPacket pk3 = new EntityEventPacket();
            pk3.event = 2;
            pk3.eid = p.getId();
            p.dataPacket(pk3);
            pk3.eid = p.getId();
            pk3.encode();
            pk3.isEncoded = true;
            Server.broadcastPacket(p.getViewers().values(), pk3);
            p.setSprinting(false, true);
            p.setSneaking(false);
            p.setDataProperty(new ShortEntityData(7, 400), false);
            p.deadTicks = 0;
            p.noDamageTicks = 60;
            p.setLastDamageCause(e);
            p.sendData(p);
            p.setMovementSpeed(0.1F);
            p.getAdventureSettings().update();
            if (this.phase == Arena.GamePhase.GAME) {
                  this.deathManager.onDeath(e);
                  ArrayList drops = new ArrayList();
                  Iterator var6 = p.getInventory().getContents().values().iterator();

                  while(var6.hasNext()) {
                        Item drop = (Item)var6.next();
                        if (!this.isSoulbound(drop)) {
                              drops.add(drop);
                        }
                  }

                  p.setHealth(20.0F);
                  p.getFoodData().setLevel(20, 40.0F);
                  p.setExperience(0, 0);
                  p.getInventory().clearAll();
                  Position deathPos = p.clone();
                  if (!data.getTeam().getCrystal().isAlive()) {
                        this.handlePlayerQuit(p);
                        this.mtcore.transferToLobby(p);
                  } else {
                        this.kitManager.giveKit(p);
                        data.currentCompassTeam = data.getTeam().getIndex();
                        this.pointCompass(data, (Item)null, data.currentCompassTeam);
                        p.removeAllEffects();
                        p.extinguish();
                        p.setOnFire(0);
                        Effect resist = Effect.getEffect(11);
                        resist.setDuration(160);
                        resist.setAmplifier(99999999);
                        p.addEffect(resist);
                        p.teleport(data.getTeam().getSpawnLocation());
                        this.pointCompass(data, (Item)null, data.currentCompassTeam);
                        if (data.getKit() == Kit.BERSERKER) {
                              p.setMaxHealth(14);
                              p.sendAttributes();
                        }
                  }

                  Iterator var11 = drops.iterator();

                  while(var11.hasNext()) {
                        Item item = (Item)var11.next();
                        deathPos.level.dropItem(deathPos, item, (Vector3)null, true, 20);
                  }
            }

      }

      @EventHandler
      public void onEntityInteract(PlayerInteractEntityEvent e) {
            Player p = e.getPlayer();
            if (this.inArena(p)) {
                  Entity entity = e.getEntity();
                  if (entity instanceof EntityCrystal) {
                        this.formWindowManager.addKitWindow(p);
                  } else if (entity instanceof EntityMerchant) {
                        this.formWindowManager.addEnchantShopWindow(p);
                  } else if (entity instanceof EntityPotionMerchant) {
                        this.formWindowManager.addPotionShopWindow(p);
                  }

            }
      }

      @EventHandler
      public void onInteract(PlayerInteractEvent e) {
            Block b = e.getBlock();
            Player p = e.getPlayer();
            Item item = e.getItem();
            if (item != null) {
                  if (item.getId() == 259) {
                        e.setCancelled();
                  } else if (e.isCancelled() || e.getAction() != Action.LEFT_CLICK_AIR) {
                        PlayerData data;
                        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                              if (this.inLobby(p)) {
                                    if (item.getId() == 276) {
                                          this.formWindowManager.addKitWindow(p);
                                    }

                                    if (item.getId() == 35) {
                                          if (this.teams != null) {
                                                this.formWindowManager.addTeamSelectWindow(p);
                                          } else {
                                                p.sendMessage(Language.translate("wait_voting", p));
                                          }
                                    }
                              }

                              if (this.phase == Arena.GamePhase.GAME && !e.isCancelled() && this.isSoulbound(item)) {
                                    data = this.getPlayerData(p);
                                    if (item.getId() == 345) {
                                          ++data.currentCompassTeam;
                                          if (data.currentCompassTeam > 4) {
                                                data.currentCompassTeam = 1;
                                          }

                                          this.pointCompass(data, item, data.currentCompassTeam);
                                    }
                              }
                        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK && this.phase == Arena.GamePhase.GAME && b.getId() == 49) {
                              data = this.getPlayerData(p);
                              Team team = data.getTeam();
                              if (team != null) {
                                    Crystal crystal = team.getCrystal();
                                    if (crystal.getPosition().equals(b)) {
                                          if (item.getId() == 264 && item.getCount() >= 64) {
                                                this.messageAllPlayers("revived_cr", false, new String[]{"" + team.getColor(), team.getName()});
                                                this.reviveCrystal(team);
                                          } else {
                                                p.sendMessage(Language.translate("revive_req", p));
                                          }
                                    }
                              }
                        }

                  }
            }
      }

      @EventHandler
      public void onChat(PlayerChatEvent e) {
            if (!e.isCancelled()) {
                  e.setCancelled();
                  Player p = e.getPlayer();
                  PlayerData data = this.getPlayerData(p);
                  if (data != null) {
                        if (data.getTeam() == null || e.getMessage().startsWith("!") && e.getMessage().length() > 1) {
                              this.messageAllPlayers(e.getMessage(), p, data);
                        } else {
                              data.getTeam().message(e.getMessage(), p, data);
                              String msg = TextFormat.GRAY + "[" + data.getTeam().getColor() + "Team" + TextFormat.GRAY + "]   " + p.getDisplayName() + TextFormat.RESET + TextFormat.GRAY + " > " + data.getBaseData().getChatColor() + e.getMessage();
                              Iterator var5 = this.playersData.values().iterator();

                              while(var5.hasNext()) {
                                    PlayerData data1 = (PlayerData)var5.next();
                                    if (data1.getTeam() == null) {
                                          data1.getPlayer().sendMessage(msg);
                                    }
                              }

                        }
                  }
            }
      }

      public void teleportToArena(Player p) {
            Team team = this.getPlayerTeam(p);
            p.teleport(team.getSpawnLocation());
            this.bossBar.update(p, true);
            p.getInventory().clearAll();
            p.setSpawn(team.getSpawnLocation());
            p.setGamemode(0);
            PlayerData data = this.getPlayerData(p);
            data.setInLobby(false);
            VirtualInventory inv = data.getSavedInventory();
            if (inv != null) {
                  if (data.getKit() == Kit.BERSERKER) {
                        p.setMaxHealth(inv.maxHealth);
                        p.sendAttributes();
                  }

                  this.loadInventory(p, inv);
                  data.removeInventory();
            } else {
                  if (data.getKit() == Kit.BERSERKER) {
                        p.setMaxHealth(14);
                        p.setHealth(14.0F);
                        p.sendAttributes();
                  }

                  p.setExperience(0, 0);
                  p.getFoodData().setLevel(20);
                  p.getFoodData().setFoodSaturationLevel(20.0F);
                  this.kitManager.giveKit(p);
                  data.currentCompassTeam = data.getTeam().getIndex();
                  this.pointCompass(data, (Item)null, data.currentCompassTeam);
            }
      }

      public void stopGame() {
            this.phase = Arena.GamePhase.LOBBY;
            Crystals.isShuttingDown = true;
            MTCore.isShuttingDown = true;
            HashMap datas = new HashMap();
            Iterator var2 = this.playersData.values().iterator();

            while(var2.hasNext()) {
                  PlayerData data = (PlayerData)var2.next();
                  datas.put(data.getPlayer().getName(), data);
                  ((GTPlayer)data.getPlayer()).transferByName("lobby1");
            }

            this.unsetAllPlayers();
            this.votingManager.createVoteTable();
            this.ending = false;
            this.starting = false;
            this.task.time = 0;
            this.task.time1 = 60;
            if (this.fireworkTask != null) {
                  this.fireworkTask.cancel();
                  this.fireworkTask = null;
            }

            this.task.popup = 0;
            this.map = null;
            this.plugin.getLogger().info("stop game");
            datas.forEach((n, d) -> {
                  new QuitQuery(n, d);
            });
            this.plugin.getServer().getScheduler().scheduleDelayedTask(this.plugin, MTCore::restart, 30);
      }

      public void startGame() {
            this.startGame(false);
      }

      public void startGame(boolean force) {
            if (this.isLevelLoaded) {
                  if (this.getAllPlayers().size() < 1 && !force) {
                        this.messageAllPlayers("min_players", true, new String[0]);
                        this.starting = false;
                        this.task.time1 = 60;
                  } else {
                        this.starting = false;
                        this.phase = Arena.GamePhase.GAME;
                        this.level.setTime(0);
                        this.level.stopTime();
                        this.barUtil.updateTeamStats();
                        Iterator var2 = this.getPlayersInTeam().values().iterator();

                        while(var2.hasNext()) {
                              Player p = (Player)var2.next();
                              p.setHealth((float)p.getMaxHealth());
                              this.teleportToArena(p);
                        }

                  }
            }
      }

      public void checkAlive() {
            if (!this.ending) {
                  int count = 0;
                  Team[] var2 = this.teams;
                  int var3 = var2.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                        Team t = var2[var4];
                        if (t != null && t.isAlive()) {
                              ++count;
                        }
                  }

                  if (count <= 0) {
                        this.stopGame();
                  }

            }
      }

      public void joinTeam(Player p, int team) {
            this.joinTeam(p, team, false);
      }

      public void joinTeam(Player p, int team2, boolean forceJoin) {
            if (this.teams == null) {
                  p.sendMessage(Language.translate("wait_voting", p));
            } else {
                  Team team = this.getTeam(team2);
                  if (this.inLobby(p)) {
                        PlayerData data = this.getPlayerData(p);
                        Team pTeam = data.getTeam();
                        if (pTeam != null && pTeam.getIndex() == team.getIndex()) {
                              p.sendMessage(Crystals.getPrefix() + Language.translate("already_in_team", data.getBaseData(), team.getColor() + team.getName()));
                        } else if (!this.isTeamFree(team) && !p.hasPermission("gameteam.vip")) {
                              p.sendMessage(Crystals.getPrefix() + Language.translate("full_team", data.getBaseData()));
                        } else if (this.task.time > 600 && !this.canJoin() && !forceJoin && !p.hasPermission("gameteam.helper")) {
                              p.sendMessage(Crystals.getPrefix() + Language.translate("join_high_phase", data.getBaseData()));
                              MTCore.getInstance().transferToLobby(p);
                        } else if (pTeam != null) {
                              p.sendMessage(Crystals.getPrefix() + Language.translate("switch_teams", data.getBaseData()));
                        } else {
                              this.addToTeam(p, team);
                              p.sendMessage(Crystals.getPrefix() + Language.translate("team_join", data.getBaseData(), team.getColor() + team.getName()));
                              if (this.phase == Arena.GamePhase.GAME) {
                                    this.teleportToArena(p);
                              } else {
                                    p.getInventory().setItem(2, Item.get(35, Utils.textFormatToDyeColor(team.getColor()).getWoolData()));
                              }

                        }
                  }
            }
      }

      public void checkLobby() {
            if (this.phase != Arena.GamePhase.GAME) {
                  if (this.getAllPlayers().size() >= 16) {
                        this.starting = true;
                  }

            }
      }

      public void selectMap() {
            this.selectMap(false);
      }

      public void selectMap(boolean force) {
            if (this.getAllPlayers().size() < 14 && !force) {
                  this.messageAllPlayers("min_players", true, new String[0]);
                  this.starting = false;
                  this.task.time1 = 60;
            } else {
                  String map = "";
                  int points = -10;
                  Iterator var4 = this.votingManager.stats.entrySet().iterator();

                  while(var4.hasNext()) {
                        Entry entry = (Entry)var4.next();
                        if (points < (Integer)entry.getValue()) {
                              map = (String)entry.getKey();
                              points = (Integer)entry.getValue();
                        }
                  }

                  if (this.plugin.getServer().isLevelLoaded(map)) {
                        this.plugin.getServer().unloadLevel(this.plugin.getServer().getLevelByName(map));
                  }

                  this.plugin.getServer().getScheduler().scheduleAsyncTask(this.plugin, new WorldCopyTask(this.id, map, Server.getInstance().getDataPath(), force));
                  this.map = map;
                  this.data = (ArenaData)this.plugin.maps.get(map);
                  this.registerTeams();
                  this.mapZone = this.data.mapZone;
                  this.updateTeamData();
                  this.messageAllPlayers("select_map", true, new String[]{map});
            }
      }

      public void loadInventory(Player p, VirtualInventory inv) {
            PlayerInventory newInv = p.getInventory();
            newInv.setContents(inv.getContents());

            for(int i = 0; i < 4; ++i) {
                  newInv.setArmorItem(i, inv.armor[i]);
            }

            p.setExperience(inv.xp, inv.xplevel);
            p.getFoodData().setLevel(inv.hunger);
            newInv.sendContents(p);
            newInv.sendArmorContents(p);
            p.setHealth(inv.health);
      }

      public void unsetAllPlayers() {
            this.playersData.clear();
            this.players.clear();
      }

      public void onItemHeld(PlayerItemHeldEvent e) {
            Player p = e.getPlayer();
            if (this.inArena(p)) {
                  Item item = e.getItem();
                  if (this.inLobby(p) && item.getId() == 276) {
                        this.formWindowManager.addKitWindow(p);
                  }

            }
      }

      public boolean inLobby(Player p) {
            PlayerData data = this.getPlayerData(p);
            return data != null && data.isInLobby();
      }

      public boolean wasInArena(Player p) {
            return this.getPlayerData(p).wasInGame();
      }

      public void destroyCrystal(Player p, Team damagedTeam, EntityCrystal entity) {
            Crystal crystal = damagedTeam.getCrystal();
            if (crystal.isImmune()) {
                  p.sendMessage(Language.translate("crstl_immune", p, "" + crystal.getImmuneTime() / 20));
            } else {
                  Position pos = crystal.getPosition();
                  PlayerData data = this.getPlayerData(p);
                  this.addStat(data, Stat.CRYSTALS);
                  LevelEventPacket pk = new LevelEventPacket();
                  pk.evid = 3501;
                  pk.data = 0;
                  LevelEventPacket explode = new LevelEventPacket();
                  explode.x = (float)pos.x;
                  explode.y = (float)pos.y;
                  explode.z = (float)pos.z;
                  explode.evid = 3501;
                  explode.data = 0;
                  crystal.destroy();
                  entity.close();
                  this.barUtil.updateTeamStats();
                  this.level.setBlock(pos, Block.get(49), true, false);
                  Server.broadcastPacket(this.level.getChunkPlayers((int)pos.x >> 4, (int)pos.z >> 4).values(), explode);
                  this.level.addParticle(new HugeExplodeSeedParticle(pos.add(0.5D, 0.0D, 0.5D)));
                  this.level.addSound(new ExplodeSound(pos.add(0.5D, 0.0D, 0.5D)));
                  Team team = data.getTeam();
                  Iterator var10 = this.playersData.values().iterator();

                  while(var10.hasNext()) {
                        PlayerData pData = (PlayerData)var10.next();
                        Player pl = pData.getPlayer();
                        if (pData.getTeam() != null) {
                              if (pData.getTeam().getIndex() == damagedTeam.getIndex()) {
                                    pl.setSpawn(this.plugin.mainLobby);
                              }

                              if (pl != null && pl.isOnline()) {
                                    pk.x = (float)pl.getX();
                                    pk.y = (float)pl.getY() + pl.getEyeHeight();
                                    pk.z = (float)pl.getZ();
                                    pl.dataPacket(pk);
                                    pl.sendMessage(Language.translate("nexus_destroy", pData.getBaseData(), "" + damagedTeam.getColor(), team.getColor() + p.getName(), team.getColor() + team.getName(), damagedTeam.getColor() + damagedTeam.getName()));
                              }
                        }
                  }

                  Utils.enderDragonEffect(p.getLevel(), crystal.getPosition().add(0.0D, 1.5D));
                  data.getBaseData().addShard(6);
                  this.checkNexuses();
            }
      }

      @EventHandler(
            priority = EventPriority.NORMAL,
            ignoreCancelled = false
      )
      public void onItemDrop(PlayerDropItemEvent e) {
            Player p = e.getPlayer();
            if (this.inArena(p) && !this.inLobby(p)) {
                  Item item = e.getItem();
                  if (this.isSoulbound(item)) {
                        e.setCancelled();
                        p.getInventory().remove(item);
                        GTCore.utils.Utils.addSound("mob.blaze.hit", 1.0F, 0.5F, new Player[]{p});
                  }

            } else {
                  e.setCancelled(true);
            }
      }

      @EventHandler
      public void onInventoryClose(InventoryCloseEvent e) {
            Inventory inv = e.getInventory();
            if (inv instanceof EnchantInventory) {
                  inv.setItem(1, Item.get(0));
            }

      }

      public void checkNexuses() {
            Set alive = new HashSet();

            for(int i = 1; i < 5; ++i) {
                  if (this.getTeam(i).getCrystal().isAlive()) {
                        alive.add(i);
                  }
            }

            if (alive.size() == 1) {
                  Integer winner;
                  for(Iterator var4 = alive.iterator(); var4.hasNext(); this.winnerTeam = this.getTeam(winner)) {
                        winner = (Integer)var4.next();
                  }

                  this.endGame();
            }

      }

      public void endGame() {
            if (this.winnerTeam == null) {
                  this.selectWinner();
            }

            this.ending = true;
            GTCore.utils.Utils.addSound("mob.enderdragon.death", 1.0F, 1.0F, this.players.values());
            this.messageAllPlayers("end_game", false, new String[]{"" + this.winnerTeam.getColor(), this.winnerTeam.getColor() + this.winnerTeam.getName()});
            Iterator var1 = this.playersData.values().iterator();

            while(var1.hasNext()) {
                  PlayerData data = (PlayerData)var1.next();
                  if (data.getTeam() != null && data.getTeam().getIndex() == this.winnerTeam.getIndex()) {
                        data.getPlayer().sendMessage(Language.translate("tokens", data.getBaseData()));
                        this.addStat(data, Stat.WINS);
                  }
            }

            this.fireworkTask = new FireworkTask(this);
            this.plugin.getServer().getScheduler().scheduleRepeatingTask(this.fireworkTask, 10);
      }

      private void selectWinner() {
            boolean crystal = false;
            int playerCount = 0;
            Team winner = null;

            for(int i = 1; i < 5; ++i) {
                  Team team = this.getTeam(i);
                  if (team.getCrystal().isAlive() && (!crystal || playerCount < team.getPlayers().size())) {
                        crystal = team.getCrystal().isAlive();
                        playerCount = team.getPlayers().size();
                        winner = team;
                  }
            }

            this.winnerTeam = winner;
      }

      @EventHandler
      public void onBedEnter(PlayerBedEnterEvent e) {
            e.setCancelled();
      }

      @EventHandler
      public void onFoodChange(PlayerFoodLevelChangeEvent e) {
            Player p = e.getPlayer();
            if (this.inLobby(p)) {
                  e.setCancelled();
            }

      }

      @EventHandler
      public void onTeleport(PlayerTeleportEvent e) {
            Player p = e.getPlayer();
            if (this.inArena(p)) {
                  this.bossBar.update(p, true);
            }

      }

      @EventHandler
      public void onFormResponded(PlayerFormRespondedEvent e) {
            Player p = e.getPlayer();
            this.formWindowManager.handleResponse(p, e.getFormID(), e.getResponse());
      }

      @EventHandler
      public void onInventoryOpen(InventoryOpenEvent e) {
            Inventory inventory = e.getInventory();
            if (inventory instanceof EnchantInventory) {
                  inventory.setItem(1, Item.get(351, 4, 64));
            }

      }

      public static enum GamePhase {
            LOBBY,
            GAME;
      }
}
