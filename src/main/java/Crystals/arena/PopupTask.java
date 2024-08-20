package Crystals.arena;

import Crystals.arena.manager.VotingManager;
import Crystals.arena.object.Team;
import Crystals.arena.util.FireworkUtils;
import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.item.Item;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.TextFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PopupTask extends Task {
      public Arena plugin;
      public ArenaSchedule task;
      public int ending = 0;
      public int fireworkIndex = 0;

      public PopupTask(Arena plugin) {
            this.plugin = plugin;
      }

      public void onRun(int currentTick) {
            if (this.plugin.phase == Arena.GamePhase.GAME && !this.plugin.ending) {
                  this.sendTeamsStats();
            } else if (this.plugin.ending) {
                  this.spawnFireworks();
            } else {
                  if (this.plugin.phase == Arena.GamePhase.LOBBY) {
                        this.sendVotes();
                  }

            }
      }

      public void sendVotes() {
            VotingManager vm = this.plugin.votingManager;
            String[] votes = new String[]{vm.currentTable[0], vm.currentTable[1], vm.currentTable[2]};
            String tip = "                                                   §8Voting §f| §6/vote <map>\n                                                 §b[1] §8" + votes[0] + " §c» §a" + vm.stats.get(votes[0]) + " Hlasu\n                                                 §b[2] §8" + votes[1] + " §c» §a" + vm.stats.get(votes[1]) + " Hlasu\n                                                 §b[3] §8" + votes[2] + " §c» §a" + vm.stats.get(votes[2]) + " Hlasu";
            Iterator var4 = this.plugin.getAllPlayers().values().iterator();

            while(var4.hasNext()) {
                  Player p = (Player)var4.next();
                  p.sendPopup(tip);
            }

      }

      public void sendTeamsStats() {
            String map = this.plugin.map;
            int destroyed = 0;
            String status = "";
            String[] statuses = new String[]{this.plugin.getTeam(1).getStatus(), this.plugin.getTeam(2).getStatus(), this.plugin.getTeam(3).getStatus(), this.plugin.getTeam(4).getStatus()};
            String[] var5 = statuses;
            int var6 = statuses.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                  String s = var5[var7];
                  if (!s.equals("")) {
                        status = status + s;
                  } else {
                        ++destroyed;
                  }
            }

            String tip = "                                                    §8Map: §6" + map + status;
            Iterator var10 = this.plugin.getAllPlayers().values().iterator();

            while(var10.hasNext()) {
                  Player p = (Player)var10.next();
                  p.sendPopup(tip);
            }

      }

      public static String getDisplayPhase(int phase) {
            switch(phase) {
            case 0:
                  return "§8Voting §f| §6/vote <map>";
            case 1:
                  return TextFormat.GOLD + "Phase: " + TextFormat.DARK_GREEN + "I";
            case 2:
                  return TextFormat.GOLD + "Phase: " + TextFormat.DARK_GREEN + "II";
            case 3:
                  return TextFormat.RED + "Phase: " + TextFormat.DARK_PURPLE + "III";
            case 4:
                  return TextFormat.RED + "Phase: " + TextFormat.DARK_PURPLE + "IV";
            case 5:
                  return TextFormat.RED + "Phase: " + TextFormat.DARK_PURPLE + "V";
            default:
                  return "";
            }
      }

      private void spawnFireworks() {
            List positions = new ArrayList();
            Team[] var2 = this.plugin.teams;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                  Team team = var2[var4];
                  positions.add(team.getSpawnLocation());
            }

            if (this.fireworkIndex > 3) {
                  this.fireworkIndex = 0;
            }

            Item firework = (Item)FireworkUtils.of(this.plugin.winnerTeam).get(this.fireworkIndex++);
            CompoundTag itemTag = NBTIO.putItemHelper(firework);
            Iterator var10 = positions.iterator();

            while(var10.hasNext()) {
                  Vector3 pos = (Vector3)var10.next();
                  CompoundTag nbt = (new CompoundTag()).putList((new ListTag("Pos")).add(new DoubleTag("", pos.x + 0.5D)).add(new DoubleTag("", pos.y + 0.5D)).add(new DoubleTag("", pos.z + 0.5D))).putList((new ListTag("Motion")).add(new DoubleTag("", 0.0D)).add(new DoubleTag("", 0.0D)).add(new DoubleTag("", 0.0D))).putList((new ListTag("Rotation")).add(new FloatTag("", 0.0F)).add(new FloatTag("", 0.0F))).putCompound("FireworkItem", itemTag);
                  EntityFirework entity = new EntityFirework(this.plugin.level.getChunk(pos.getFloorX() >> 4, pos.getFloorZ() >> 4), nbt);
                  entity.motionX = (double)(new NukkitRandom()).nextRange(-5, 5) / 10.0D;
                  entity.motionZ = (double)(new NukkitRandom()).nextRange(-5, 5) / 10.0D;
                  entity.spawnToAll();
            }

      }
}
