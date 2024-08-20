package Crystals.arena.manager;

import Crystals.Crystals;
import Crystals.Stat;
import Crystals.arena.Arena;
import Crystals.arena.object.ArenaData;
import Crystals.arena.object.PlayerData;
import Crystals.arena.object.Stats;
import Crystals.arena.object.Team;
import Crystals.arena.util.Utils;
import Crystals.entity.CombatLoggerNPC;
import Crystals.entity.EntityCrystal;
import Crystals.entity.EntityMerchant;
import Crystals.entity.EntityPotionMerchant;
import Crystals.lang.Language;
import GTCore.MTCore;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.TextFormat;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class ArenaManager {
      protected Arena plugin;
      public Team[] teams = null;

      protected void registerTeams() {
            int i = 0;
            this.teams = new Team[this.plugin.data.teams.size()];

            String name;
            Color color;
            for(Iterator var2 = this.plugin.data.teams.keySet().iterator(); var2.hasNext(); this.teams[i++] = new Team(i - 1, name, Utils.colorFromString(name), color.getRGB())) {
                  name = (String)var2.next();
                  color = Color.WHITE;

                  try {
                        Field field = Color.class.getField(name);
                        color = (Color)field.get((Object)null);
                  } catch (Exception var6) {
                        MainLogger.getLogger().logException(var6);
                  }
            }

      }

      public Team getPlayerTeam(Player p) {
            return this.getPlayerData(p).getTeam();
      }

      public void addToTeam(Player player, Team team) {
            PlayerData data = this.getPlayerData(player);
            data.setTeam(team);
            team.addPlayer(player);
            player.setDisplayName(TextFormat.GRAY + "[" + TextFormat.GREEN + data.getBaseData().getLevel() + TextFormat.GRAY + "]" + data.getBaseData().getPrefix() + " " + team.getColor() + player.getName() + TextFormat.RESET);
            player.setNameTag(team.getColor() + player.getName());
      }

      public void updateTeamData() {
            Iterator var1 = this.plugin.data.teams.entrySet().iterator();

            while(var1.hasNext()) {
                  Entry teamEntry = (Entry)var1.next();
                  this.getTeam((String)teamEntry.getKey()).setData((ArenaData.TeamData)teamEntry.getValue(), this.plugin);
            }

      }

      public Team getTeam(int id) {
            return this.teams[id - 1];
      }

      public Team getTeam(String name) {
            Team[] var2 = this.teams;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                  Team team = var2[var4];
                  if (team.getName().equalsIgnoreCase(name)) {
                        return team;
                  }
            }

            return null;
      }

      public HashMap getAllPlayers() {
            return this.plugin.players;
      }

      public HashMap getPlayersInTeam() {
            HashMap players = new HashMap();
            players.putAll(this.getTeam(1).getPlayers());
            players.putAll(this.getTeam(2).getPlayers());
            players.putAll(this.getTeam(3).getPlayers());
            players.putAll(this.getTeam(4).getPlayers());
            return players;
      }

      public void messageAllPlayers(String message) {
            this.messageAllPlayers(message, false);
      }

      public void messageAllPlayers(String message, String... args) {
            this.messageAllPlayers(message, (Player)null, (PlayerData)null, false, args);
      }

      public void messageAllPlayers(String message, boolean addPrefix, String... args) {
            this.messageAllPlayers(message, (Player)null, (PlayerData)null, addPrefix, args);
      }

      public void messageAllPlayers(String message, Player player, PlayerData data) {
            this.messageAllPlayers(message, player, data, false);
      }

      public void messageAllPlayers(String message, Player player, PlayerData data, boolean addPrefix, String... args) {
            Iterator var11;
            if (player == null) {
                  HashMap translations = Language.getTranslations(message, args);
                  var11 = this.plugin.playersData.values().iterator();

                  while(var11.hasNext()) {
                        PlayerData pData = (PlayerData)var11.next();
                        Player p = pData.getBaseData().getPlayer();
                        if (p.isOnline()) {
                              p.sendMessage((addPrefix ? Crystals.getPrefix() : "") + (String)translations.get(pData.getBaseData().getLanguage()));
                        }
                  }

            } else {
                  String msg;
                  if (data.getTeam() == null) {
                        msg = TextFormat.GRAY + "[" + TextFormat.DARK_PURPLE + "Lobby" + TextFormat.GRAY + "] " + player.getDisplayName() + TextFormat.RESET + TextFormat.DARK_AQUA + " > " + data.getBaseData().getChatColor() + message;
                  } else {
                        String color = "" + data.getTeam().getColor();
                        msg = TextFormat.GRAY + "[" + color + "All" + TextFormat.GRAY + "]   " + player.getDisplayName() + TextFormat.RESET + TextFormat.GRAY + " > " + data.getBaseData().getChatColor() + message.substring(1);
                  }

                  var11 = (new ArrayList(this.getAllPlayers().values())).iterator();

                  while(var11.hasNext()) {
                        Player p = (Player)var11.next();
                        p.sendMessage(msg);
                  }

            }
      }

      public void translateToPlayers(String trans, Collection players, String... args) {
            HashMap translations = Language.getTranslations(trans, args);
            Iterator var5 = players.iterator();

            while(var5.hasNext()) {
                  Player p = (Player)var5.next();
                  if (p.isOnline()) {
                        p.sendMessage((String)translations.get(MTCore.getInstance().getPlayerData(p).getLanguage()));
                  }
            }

      }

      public PlayerData getPlayerData(Player p) {
            return this.getPlayerData(p.getName());
      }

      public PlayerData getPlayerData(String p) {
            return (PlayerData)this.plugin.playersData.get(p.toLowerCase());
      }

      public PlayerData createPlayerData(Player p) {
            PlayerData data = new PlayerData(p.getName(), MTCore.getInstance().getPlayerData(p));
            data.stats = new Stats();
            this.plugin.playersData.put(p.getName().toLowerCase(), data);
            return data;
      }

      protected boolean canJoin() {
            Team[] var1 = this.teams;
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                  Team team = var1[var3];
                  if (!team.isAlive()) {
                        return false;
                  }
            }

            return true;
      }

      public boolean inArena(Player p) {
            return this.plugin.players.containsKey(p.getName().toLowerCase());
      }

      public boolean isTeamFree(Team team) {
            int players = team.getPlayers().size();
            ArrayList teams = new ArrayList();

            for(int t = 1; t < 5; ++t) {
                  Team teamm = this.getTeam(t);
                  if (teamm.getIndex() != team.getIndex()) {
                        teams.add(teamm.getPlayers().size());
                  }
            }

            switch(teams.size()) {
            case 1:
                  return players - (Integer)teams.get(0) < 3;
            case 2:
                  return players - Math.min((Integer)teams.get(0), (Integer)teams.get(1)) < 3;
            case 3:
                  return players - Math.min((Integer)teams.get(2), Math.min((Integer)teams.get(0), (Integer)teams.get(1))) < 3;
            default:
                  return true;
            }
      }

      public boolean isSoulbound(Item item) {
            String[] lore = item.getLore();
            return lore != null && lore.length >= 1 && lore[0].equals("" + TextFormat.RESET + TextFormat.GOLD + "SoulBound");
      }

      public void addStat(PlayerData data, Stat stat) {
            switch(stat) {
            case KILLS:
                  data.getBaseData().addShard(2);
                  ++data.stats.killsDelta;
                  break;
            case DEATHS:
                  ++data.stats.deathsDelta;
                  break;
            case WINS:
                  data.getBaseData().addShard(15);
                  ++data.stats.winsDelta;
                  break;
            case LOSSES:
                  ++data.stats.lossesDelta;
                  break;
            case CRYSTALS_REVIVE:
                  ++data.stats.crystalReviveDelta;
                  break;
            case CRYSTALS:
                  ++data.stats.crystalsDelta;
            }

            data.getBaseData().addMoney(stat.getTokens());
            data.getBaseData().addExp(stat.getXp());
      }

      public void pointCompass(PlayerData data, Item item, int team) {
            Player p = data.getPlayer();
            Team targetTeam = this.getTeam(data.currentCompassTeam);
            Vector3 pos = targetTeam.getCrystal().getPosition();
            SetSpawnPositionPacket pk = new SetSpawnPositionPacket();
            pk.spawnType = 1;
            pk.x = pos.getFloorX();
            pk.y = pos.getFloorY();
            pk.z = pos.getFloorZ();
            p.dataPacket(pk);
            if (item == null) {
                  Iterator var8 = p.getInventory().getContents().entrySet().iterator();

                  while(var8.hasNext()) {
                        Entry entry = (Entry)var8.next();
                        int slot = (Integer)entry.getKey();
                        Item itemm = (Item)entry.getValue();
                        if (itemm.getId() == 345 && this.plugin.isSoulbound(itemm)) {
                              itemm.setCustomName(targetTeam.getColor() + "Pointing to " + targetTeam.getName() + " nexus");
                              p.getInventory().sendSlot(slot, p);
                        }
                  }
            } else {
                  item.setCustomName(targetTeam.getColor() + "Pointing to " + targetTeam.getName() + " nexus");
                  p.getInventory().setItemInHand(item);
                  p.getInventory().sendHeldItem(new Player[]{p});
                  p.sendActionBar(item.getCustomName(), 5, 40, 5);
            }

      }

      public void spawnNPCs() {
            Vector3 pos = this.plugin.data.centerNpc.add(0.5D, 0.0D, 0.5D);
            Level level = this.plugin.level;
            FullChunk chunk = level.getChunk(pos.getFloorX() >> 4, pos.getFloorZ() >> 4);
            if (chunk != null) {
                  EntityPotionMerchant entity = new EntityPotionMerchant(chunk, Utils.getNbt(pos));
                  entity.spawnToAll();
            }

            Iterator var10 = this.plugin.data.npcs.iterator();

            while(var10.hasNext()) {
                  Vector3 npc = (Vector3)var10.next();
                  npc = npc.add(0.5D, 0.0D, 0.5D);
                  chunk = level.getChunk(npc.getFloorX() >> 4, npc.getFloorZ() >> 4);
                  if (chunk != null) {
                        EntityMerchant entity = new EntityMerchant(chunk, Utils.getNbt(npc));
                        entity.spawnToAll();
                  }
            }

            Team[] var11 = this.plugin.teams;
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
                  Team team = var11[var13];
                  Vector3 cr = team.getCrystal().getPosition().add(0.5D, 0.0D, 0.5D);
                  chunk = level.getChunk(cr.getFloorX() >> 4, cr.getFloorZ() >> 4);
                  if (chunk != null) {
                        EntityCrystal entity = new EntityCrystal(chunk, Utils.getNbt(cr).putInt("CTeam", team.getIndex()));
                        entity.spawnToAll();
                  }
            }

      }

      protected void reviveCrystal(Team team) {
            Vector3 cr = team.getCrystal().getPosition().add(0.5D, 0.0D, 0.5D);
            this.plugin.level.setBlock(cr, Block.get(0), true, false);
            FullChunk chunk = this.plugin.level.getChunk(cr.getFloorX() >> 4, cr.getFloorZ() >> 4);
            if (chunk != null) {
                  EntityCrystal entity = new EntityCrystal(chunk, Utils.getNbt(cr).putInt("CTeam", team.getIndex()));
                  entity.spawnToAll();
            }

            this.plugin.barUtil.updateTeamStats();
      }

      protected void createLeaveNPC(Player p, PlayerData data) {
            if (p.getInventory() != null && data.getTeam() != null && data.getTeam().getCrystal().isAlive()) {
                  CompoundTag nbt = (new CompoundTag()).putList((new ListTag("Pos")).add(new DoubleTag("", p.x)).add(new DoubleTag("", p.y)).add(new DoubleTag("", p.z))).putList((new ListTag("Motion")).add(new DoubleTag("", 0.0D)).add(new DoubleTag("", 0.0D)).add(new DoubleTag("", 0.0D))).putList((new ListTag("Rotation")).add(new FloatTag("", (float)p.yaw)).add(new FloatTag("", (float)p.pitch))).putShort("Health", (int)p.getHealth()).putInt("playerTeam", data.getTeam().getIndex()).putCompound("playerItem", NBTIO.putItemHelper(p.getInventory().getItemInHand())).putString("playerName", p.getName().toLowerCase());
                  ListTag armor = new ListTag("playerArmor");

                  for(int i = 0; i < 4; ++i) {
                        armor.add(NBTIO.putItemHelper(p.getInventory().getArmorItem(i)));
                  }

                  nbt.putList(armor);
                  ListTag items = new ListTag("playerItems");
                  Iterator var6 = p.getInventory().getContents().values().iterator();

                  while(var6.hasNext()) {
                        Item item = (Item)var6.next();
                        items.add(NBTIO.putItemHelper(item));
                  }

                  nbt.putList(items);
                  CombatLoggerNPC npc = new CombatLoggerNPC(this.plugin.level.getChunk(p.getFloorX() >> 4, p.getFloorZ() >> 4), nbt);
                  npc.setNameTag(p.getNameTag());
                  npc.setNameTagVisible(true);
                  npc.setNameTagAlwaysVisible(false);
                  npc.setHealth(p.getHealth());
                  data.npc = npc;
                  npc.spawnToAll();
            }
      }

      protected boolean nearNexus(Vector3 pos) {
            Iterator var2 = this.plugin.data.teams.values().iterator();

            ArenaData.TeamData data;
            do {
                  if (!var2.hasNext()) {
                        return false;
                  }

                  data = (ArenaData.TeamData)var2.next();
            } while(!data.zone.isVectorInside(pos));

            return true;
      }
}
