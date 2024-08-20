package Crystals;

import Crystals.arena.Arena;
import Crystals.arena.object.PlayerData;
import Crystals.arena.object.Stats;
import Crystals.arena.task.QueryThread;
import Crystals.arena.util.FireworkUtils;
import Crystals.entity.CombatLoggerNPC;
import Crystals.entity.EntityCrystal;
import Crystals.entity.EntityMerchant;
import Crystals.entity.EntityPotionMerchant;
import Crystals.entity.EntityWitherBoss;
import Crystals.lang.Language;
import Crystals.mysql.JoinQuery;
import Crystals.mysql.QueryRegenerateQuery;
import Crystals.setup.SetupMode;
import GTCore.MTCore;
import GTCore.MySQLManager;
import GTCore.Event.PlayerLoadDataEvent;
import GTCore.Task.MessageTask;
import GTCore.minigame.Minigame;
import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.level.ChunkUnloadEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.TextFormat;
import gt.creeperface.gadgets.AddonType;
import gt.creeperface.gadgets.GTGadgets;
import gt.creeperface.gadgets.PlayerAddonsData;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class Crystals extends PluginBase implements Listener, Minigame {
      public HashMap arenas = new HashMap();
      public static boolean isShuttingDown = false;
      public HashMap maps = new HashMap();
      public ConcurrentHashMap ins = new ConcurrentHashMap();
      public Level level;
      public Position mainLobby;
      public MTCore mtcore;
      private static Crystals instance;
      public String id = null;
      public static boolean multiPlatform = true;
      private QueryThread queryThread = null;
      public static HashMap kitMessages = new HashMap();

      public void onLoad() {
            instance = this;
            Entity.registerEntity("CombatLoggerNPC", CombatLoggerNPC.class, true);
            Entity.registerEntity("GameCrystal", EntityCrystal.class, true);
            Entity.registerEntity("Merchant", EntityMerchant.class, true);
            Entity.registerEntity("PotionMerchant", EntityPotionMerchant.class, true);
            Entity.registerEntity("WitherBoss", EntityWitherBoss.class, true);
            MTCore.getInstance().minigame = this;
            FireworkUtils.init();
            this.initMySQL();
      }

      public void onEnable() {
            this.saveDefaultConfig();
            this.id = this.getConfig().getString("id");
            multiPlatform = this.getConfig().getBoolean("multiplatform", true);
            this.initLanguage();
            this.level = this.getServer().getDefaultLevel();
            this.mtcore = MTCore.getInstance();
            this.getLogger().info("" + TextFormat.GREEN + "Crystals enabled");
            this.mainLobby = this.level.getSpawnLocation();
            this.level.setTime(5000);
            this.level.stopTime();
            this.loadMapsFromConfig();
            this.setArenasData();
            this.registerArena(this.id);
            this.getServer().getPluginManager().registerEvents(this, this);
            List english = (List)MessageTask.languages.get(0);
            english.add("" + TextFormat.AQUA + "Brew potions and defeat other teams faster!");
            english.add("" + TextFormat.AQUA + "Kill the boss and get the rare item");
            english.add("" + TextFormat.AQUA + "Use different kits for better game experience");
            List czech = (List)MessageTask.languages.get(1);
            czech.add(TextFormat.AQUA + "Delej lektvary a vyrid ostatni teamy driv");
            czech.add(TextFormat.AQUA + "Zabij bosse a ziskej specialni item!");
            czech.add(TextFormat.AQUA + "Pouzivej ruzne kity pro vetsi zazitek ze hry!");
            this.mtcore.enableItemRemoving();
            this.queryThread = new QueryThread();
            this.queryThread.start();
      }

      public void onDisable() {
            this.getLogger().info("" + TextFormat.RED + "Crystals disabled");
            isShuttingDown = true;
            Arena arena = this.getArena(this.id);
            if (arena.phase == Arena.GamePhase.GAME) {
                  arena.stopGame();
            }

            this.refreshQuery(false);
      }

      public void registerArena(String arena) {
            Arena a = new Arena(arena, this);
            this.getServer().getPluginManager().registerEvents(a, this);
            this.ins.put(arena, a);
      }

      @EventHandler
      public void onJoin(PlayerLoadDataEvent e) {
            Player p = e.getPlayer();
            new JoinQuery(this, p.getName().toLowerCase());
      }

      public static String getPrefix() {
            return "§6[Crystals]§r§f " + TextFormat.RESET + TextFormat.WHITE;
      }

      public void setArenasData() {
            this.arenas.put(this.id, new HashMap());
            HashMap anni1 = (HashMap)this.arenas.get(this.id);
            anni1.put("sign", new Vector3(128.0D, 37.0D, 184.0D));
            anni1.put("1sign", new Vector3(-198.0D, 27.0D, -21.0D));
            anni1.put("2sign", new Vector3(-198.0D, 27.0D, -23.0D));
            anni1.put("3sign", new Vector3(-196.0D, 27.0D, -25.0D));
            anni1.put("4sign", new Vector3(-194.0D, 27.0D, -25.0D));
            anni1.put("inLobby", new Vector3(-158.0D, 26.0D, -17.0D));
      }

      public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (sender instanceof Player) {
                  Arena arena = this.getPlayerArena((Player)sender);
                  switch(cmd.getName().toLowerCase()) {
                  case "blue":
                        if (arena != null) {
                              arena.joinTeam((Player)sender, 1);
                        }
                        break;
                  case "red":
                        if (arena != null) {
                              arena.joinTeam((Player)sender, 2);
                        }
                        break;
                  case "yellow":
                        if (arena != null) {
                              arena.joinTeam((Player)sender, 3);
                        }
                        break;
                  case "green":
                        if (arena != null) {
                              arena.joinTeam((Player)sender, 4);
                        }
                        break;
                  case "stats":
                        PlayerData data;
                        if (arena != null && (data = arena.getPlayerData((Player)sender)) != null) {
                              Stats stats = data.stats;
                              sender.sendMessage(Language.translate("stats", data.getBaseData(), String.valueOf(stats.getKills()), String.valueOf(stats.getDeaths()), String.valueOf(stats.getWins()), String.valueOf(stats.getLosses()), String.valueOf(stats.getCrystals()), String.valueOf(stats.getCrystalRevive())));
                        }
                        break;
                  case "vote":
                        if (arena != null) {
                              if (args.length != 1) {
                                    sender.sendMessage(getPrefix() + TextFormat.GRAY + "use /vote [map]");
                              } else {
                                    arena.votingManager.onVote((Player)sender, args[0]);
                              }
                        }
                        break;
                  case "start":
                        if (sender.isOp() && arena != null && arena.phase != Arena.GamePhase.GAME) {
                              arena.selectMap(true);
                        }
                        break;
                  case "npc":
                        if (sender.isOp() && args.length == 1) {
                              String var10 = args[0];
                        }
                        break;
                  case "removeall":
                        if (sender.isOp()) {
                              int count = 0;
                              Entity[] var16 = ((Player)sender).getLevel().getEntities();
                              int var13 = var16.length;

                              for(int var14 = 0; var14 < var13; ++var14) {
                                    Entity e = var16[var14];
                                    if (!(e instanceof Player)) {
                                          e.despawnFromAll();
                                          e.close();
                                          ++count;
                                    }
                              }

                              sender.sendMessage(MTCore.getPrefix() + TextFormat.GREEN + "Removed " + TextFormat.BLUE + count + TextFormat.GREEN + " entities");
                        }
                        break;
                  case "remove":
                        if (!sender.isOp()) {
                        }
                        break;
                  case "cleantiles":
                        if (sender.isOp()) {
                              if (args.length == 1) {
                                    int var12 = Integer.valueOf(args[0]);
                              } else {
                                    this.fixBlocks();
                              }
                        }
                        break;
                  case "phase":
                        if (arena != null) {
                              arena.bossBar.update((Player)sender, true);
                        }
                  }
            }

            return true;
      }

      public Arena getPlayerArena(Player p) {
            Iterator var2 = this.ins.entrySet().iterator();

            Arena a;
            do {
                  if (!var2.hasNext()) {
                        return null;
                  }

                  Entry entry = (Entry)var2.next();
                  a = (Arena)entry.getValue();
            } while(!a.inArena(p));

            return a;
      }

      public HashMap refreshQuery(boolean async) {
            try {
                  HashMap data = new HashMap() {
                        {
                              Arena arena = Crystals.this.getArena(Crystals.this.id);
                              this.put("id", Crystals.this.id);
                              this.put("players", "" + Crystals.this.getServer().getOnlinePlayers().size());
                              this.put("maxplayers", "100");
                              String map = arena.map;
                              if (arena.phase == Arena.GamePhase.LOBBY || Crystals.isShuttingDown) {
                                    map = "---";
                              }

                              String game = TextFormat.GREEN + "Lobby";
                              if (!Crystals.isShuttingDown) {
                                    if (arena.phase == Arena.GamePhase.GAME) {
                                          int time = arena.task.time;
                                          game = TextFormat.DARK_PURPLE + String.format("%02d:%02d", time / 3600, time / 60);
                                    }
                              } else {
                                    game = TextFormat.BLACK + "Restarting...";
                                    this.put("players", "0");
                              }

                              this.put("line1", TextFormat.GOLD + "> " + Crystals.this.id + " <");
                              this.put("line2", "" + TextFormat.BLACK + Crystals.this.getServer().getOnlinePlayers().size() + "/100");
                              this.put("line3", game);
                              this.put("line4", "" + TextFormat.BOLD + TextFormat.BLACK + map + TextFormat.RESET + (Crystals.multiPlatform ? "" : "\n\n" + TextFormat.DARK_RED + TextFormat.BOLD + "PE ONLY"));
                        }
                  };
                  if (isShuttingDown) {
                        new QueryRegenerateQuery(data, false);
                  }

                  return data;
            } catch (Exception var3) {
                  MainLogger.getLogger().logException(var3);
                  return null;
            }
      }

      public static Crystals getInstance() {
            return instance;
      }

      public Arena getArena(String name) {
            return (Arena)this.ins.get(name);
      }

      private void fixBlocks() {
            this.fixBlocks(false);
      }

      private void fixBlocks(boolean tiles) {
            Iterator var2 = this.maps.entrySet().iterator();

            while(true) {
                  Entry e;
                  String name;
                  Level level;
                  do {
                        if (!var2.hasNext()) {
                              return;
                        }

                        e = (Entry)var2.next();
                        name = (String)e.getKey();
                        this.getServer().loadLevel(name);
                        level = this.getServer().getLevelByName(name);
                        new Vector3();
                  } while(level == null);

                  int count = 0;
                  int countB = 0;
                  List metaBlocks = new ArrayList();
                  level.setAutoSave(true);
                  Map map = (Map)e.getValue();
                  Vector3 pos1 = (Vector3)map.get("corner1");
                  Vector3 pos2 = (Vector3)map.get("corner2");
                  int minX = (int)Math.min(pos1.x, pos2.x);
                  int minZ = (int)Math.min(pos1.z, pos2.z);
                  int maxX = (int)Math.max(pos1.x, pos2.x);
                  int maxZ = (int)Math.max(pos1.z, pos2.z);
                  int prevX = Integer.MAX_VALUE;
                  int prevZ = Integer.MAX_VALUE;

                  for(int x = minX; x <= maxX; ++x) {
                        for(int z = minZ; z <= maxZ; ++z) {
                              for(int y = 0; y <= 128; ++y) {
                                    int id = level.getBlockIdAt(x, y, z);
                                    switch(id) {
                                    case 36:
                                    case 84:
                                    case 122:
                                    case 130:
                                    case 137:
                                    case 138:
                                    case 166:
                                    case 168:
                                    case 169:
                                    case 176:
                                    case 177:
                                          level.setBlockIdAt(x, y, z, 248);
                                          level.setBlockDataAt(x, y, z, 0);
                                          break;
                                    case 95:
                                          level.setBlockIdAt(x, y, z, 20);
                                          break;
                                    case 125:
                                          level.setBlockIdAt(x, y, z, 157);
                                          break;
                                    case 126:
                                          level.setBlockIdAt(x, y, z, 158);
                                          break;
                                    case 160:
                                          level.setBlockIdAt(x, y, z, 102);
                                          level.setBlockDataAt(x, y, z, 0);
                                          break;
                                    case 188:
                                          level.setBlockIdAt(x, y, z, 85);
                                          level.setBlockDataAt(x, y, z, 1);
                                          break;
                                    case 189:
                                          level.setBlockIdAt(x, y, z, 85);
                                          level.setBlockDataAt(x, y, z, 2);
                                          break;
                                    case 190:
                                          level.setBlockIdAt(x, y, z, 85);
                                          level.setBlockDataAt(x, y, z, 3);
                                          break;
                                    case 191:
                                          level.setBlockIdAt(x, y, z, 85);
                                          level.setBlockDataAt(x, y, z, 4);
                                          break;
                                    case 192:
                                          level.setBlockIdAt(x, y, z, 85);
                                          level.setBlockDataAt(x, y, z, 5);
                                    }
                              }

                              if (x >> 4 != prevX || z >> 4 != prevZ) {
                                    prevX = x >> 4;
                                    prevZ = z >> 4;
                                    FullChunk chunk = level.getChunk(prevX, prevZ);

                                    for(Iterator var28 = (new HashMap(chunk.getBlockEntities())).values().iterator(); var28.hasNext(); ++count) {
                                          BlockEntity be = (BlockEntity)var28.next();
                                          be.close();
                                    }
                              }
                        }
                  }

                  level.save();
                  System.out.println(name + ": " + count + " blockentities");
                  System.out.println(name + ": " + count + " blockentities");
                  System.out.println(name + ": " + countB + " blocks");
                  String blocks = name + ": meta blocks: ";

                  Integer i;
                  for(Iterator var25 = metaBlocks.iterator(); var25.hasNext(); blocks = blocks + i + "|") {
                        i = (Integer)var25.next();
                  }

                  System.out.println(blocks);
            }
      }

      private void cleanTiles(Position pos, int radius) {
            Level level = pos.getLevel();
            Iterator var4 = level.getBlockEntities().values().iterator();

            while(var4.hasNext()) {
                  BlockEntity be = (BlockEntity)var4.next();
                  if (be.distance(pos) <= (double)radius) {
                  }
            }

      }

      public void initLanguage() {
            this.saveResource("English.yml");
            this.saveResource("Czech.yml");
            Map langs = new HashMap();
            langs.put(1, new Config(this.getDataFolder() + "/Czech.yml", 2));
            langs.put(0, new Config(this.getDataFolder() + "/English.yml", 2));
            Language.init(langs);
      }

      @EventHandler
      public void onChunkUnload(ChunkUnloadEvent e) {
            Iterator var2 = e.getChunk().getEntities().values().iterator();

            while(true) {
                  Entity entity;
                  do {
                        if (!var2.hasNext()) {
                              return;
                        }

                        entity = (Entity)var2.next();
                  } while(!(entity instanceof CombatLoggerNPC) && !(entity instanceof EntityWitherBoss));

                  e.setCancelled();
            }
      }

      private void loadMapsFromConfig() {
            try {
                  File file = new File(this.getDataFolder(), "maps");
                  file.mkdirs();
                  File[] files = file.listFiles((i) -> {
                        return i.getName().toLowerCase().endsWith(".yml");
                  });
                  if (files == null || files.length == 0) {
                        return;
                  }

                  File[] var3 = files;
                  int var4 = files.length;

                  for(int var5 = 0; var5 < var4; ++var5) {
                        File target = var3[var5];
                        Config config = new Config(target, 2);
                        String name = target.getName().substring(0, target.getName().length() - 4);
                        this.maps.put(name, SetupMode.loadMap(config));
                  }
            } catch (Exception var9) {
                  MainLogger.getLogger().logException(var9);
            }

      }

      public String getInfoAbout() {
            return "";
      }

      public String getShortName() {
            return "cryst";
      }

      public String getMinigameName() {
            return "Crystals";
      }

      public boolean sendStatus() {
            return false;
      }

      public void onLevelChange(EntityLevelChangeEvent e) {
            if (e.getEntity() instanceof Player) {
                  Player p = (Player)e.getEntity();
                  Level level = e.getTarget();
                  PlayerAddonsData data = GTGadgets.getData(p);
                  if (data == null) {
                        return;
                  }

                  if (level.getId() != this.getServer().getDefaultLevel().getId()) {
                        data.disable(AddonType.COSTUME);
                        data.disable(AddonType.GADGET);
                        data.disable(AddonType.PET);
                        data.disable(AddonType.RIDING);
                  } else {
                        data.enable(AddonType.COSTUME);
                        data.enable(AddonType.GADGET);
                        data.enable(AddonType.PET);
                        data.enable(AddonType.RIDING);
                  }
            }

      }

      @EventHandler
      public void onExplode(EntityExplodeEvent e) {
            e.setBlockList(new ArrayList());
      }

      private void initMySQL() {
            try {
                  PreparedStatement statement = MySQLManager.getMysqlConnection().prepareStatement("CREATE TABLE IF NOT EXISTS crystals(name VARCHAR(20) PRIMARY KEY, kills INT DEFAULT '0', deaths INT DEFAULT '0', wins INT DEFAULT '0', losses INT DEFAULT '0', crystals INT DEFAULT '0',crystalsrevived INT DEFAULT '0', kits TEXT)");

                  try {
                        statement.executeUpdate();
                  } finally {
                        if (Collections.singletonList(statement).get(0) != null) {
                              statement.close();
                        }

                  }
            } catch (SQLException var6) {
                  MainLogger.getLogger().logException(var6);
            }

      }
}
