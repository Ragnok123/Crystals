package Crystals.arena.object;

import Crystals.arena.Arena;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBootsLeather;
import cn.nukkit.item.ItemChestplateLeather;
import cn.nukkit.item.ItemHelmetLeather;
import cn.nukkit.item.ItemLeggingsLeather;
import cn.nukkit.level.Position;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.utils.TextFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Team {
      private String name;
      private TextFormat color;
      private int id;
      private HashMap players = new HashMap();
      private Crystal crystal;
      private List spawns;
      private int decColor;
      private Item[] soulBound;
      private String status;
      private int doubleXpUntil;
      private static final String space = "\n                                                ";

      public Team(int id, String name, TextFormat color, int decColor) {
            this.id = id;
            this.name = name;
            this.color = color;
            this.decColor = decColor;
            CompoundTag nbt = (new CompoundTag()).putInt("customColor", this.getDecimalColor()).putCompound("display", (new CompoundTag("display")).putList((new ListTag("Lore")).add(new StringTag("", "" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"))));
            ItemHelmetLeather helmet = new ItemHelmetLeather();
            helmet.setCompoundTag(nbt);
            ItemChestplateLeather chestplate = new ItemChestplateLeather();
            chestplate.setCompoundTag(nbt);
            ItemLeggingsLeather leggings = new ItemLeggingsLeather();
            leggings.setCompoundTag(nbt);
            ItemBootsLeather boots = new ItemBootsLeather();
            boots.setCompoundTag(nbt);
            this.soulBound = new Item[]{helmet, chestplate, leggings, boots};
      }

      public void setData(ArenaData.TeamData data, Arena plugin) {
            this.crystal = new Crystal(this, Position.fromObject(data.getCrystal(), plugin.level));
            this.recalculateStatus();
            this.spawns = (List)data.spawns.stream().map((i) -> {
                  return Position.fromObject(i, plugin.level);
            }).collect(Collectors.toList());
      }

      public int getIndex() {
            return this.id;
      }

      public void addPlayer(Player p) {
            this.players.put(p.getName().toLowerCase(), p);
            p.namedTag.putInt("team", this.getIndex());
      }

      public void removePlayer(Player p) {
            this.players.remove(p.getName().toLowerCase());
            p.namedTag.remove("team");
      }

      public String getName() {
            return this.name;
      }

      public TextFormat getColor() {
            return this.color;
      }

      public Crystal getCrystal() {
            return this.crystal;
      }

      public HashMap getPlayers() {
            return this.players;
      }

      public boolean isAlive() {
            return this.crystal.isAlive() || this.players.size() > 0;
      }

      public void message(String message) {
            this.message(message, (Player)null, (PlayerData)null);
      }

      public void message(String message, Player player, PlayerData data) {
            if (player == null) {
                  Iterator var8 = this.getPlayers().entrySet().iterator();

                  while(var8.hasNext()) {
                        Entry entry = (Entry)var8.next();
                        ((Player)entry.getValue()).sendMessage(message);
                  }

            } else {
                  String msg = TextFormat.GRAY + "[" + this.color + "Team" + TextFormat.GRAY + "]   " + player.getDisplayName() + TextFormat.RESET + TextFormat.GRAY + " > " + data.getBaseData().getChatColor() + message;
                  Iterator var5 = this.getPlayers().entrySet().iterator();

                  while(var5.hasNext()) {
                        Entry entry = (Entry)var5.next();
                        ((Player)entry.getValue()).sendMessage(msg);
                  }

            }
      }

      public Position getSpawnLocation() {
            return (Position)this.spawns.get((new Random()).nextInt(this.spawns.size()));
      }

      public int getDecimalColor() {
            return this.decColor;
      }

      public Item[] getArmor() {
            return this.soulBound;
      }

      public void recalculateStatus() {
            if (!this.getCrystal().isAlive() && this.players.size() == 0) {
                  this.status = "";
            } else {
                  String name = (this.getCrystal().isAlive() ? TextFormat.RED : TextFormat.GRAY) + "❤     " + this.getColor() + this.getName().substring(0, 1).toUpperCase() + this.getName().substring(1) + "";
                  this.status = "\n                                                " + name;
            }
      }

      public void setDoubleXp(int ticks) {
            int tick = Server.getInstance().getTick();
            this.doubleXpUntil = tick + ticks;
      }

      public boolean isDoubleXp() {
            int tick = Server.getInstance().getTick();
            return this.doubleXpUntil >= tick;
      }

      public String getStatus() {
            return this.status;
      }
}
