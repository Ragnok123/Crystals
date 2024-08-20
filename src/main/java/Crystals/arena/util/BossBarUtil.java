package Crystals.arena.util;

import Crystals.arena.Arena;
import Crystals.arena.manager.VotingManager;
import cn.nukkit.utils.TextFormat;

public class BossBarUtil {
      public String mainLine = "";
      public String other = "";
      private String lineOffset = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
      private String mainLineOffset = "";
      private Arena plugin;

      public BossBarUtil(Arena plugin) {
            this.plugin = plugin;
      }

      public void updateBar(int time) {
            if (this.plugin.phase == Arena.GamePhase.LOBBY) {
                  if (this.plugin.starting) {
                        this.mainLine = TextFormat.GOLD + "Lobby" + TextFormat.GRAY + " | " + TextFormat.WHITE + " Time to start: " + time;
                        this.mainLine = this.recalculateLineOffset() + this.mainLine;
                  } else {
                        this.updateVotes();
                        this.mainLine = TextFormat.GRAY + "                     Welcome to Crystals!";
                        this.plugin.bossBar.setMaxHealth(120);
                        this.plugin.bossBar.setHealth(120);
                  }
            } else if (this.plugin.phase == Arena.GamePhase.GAME && !this.plugin.ending) {
                  this.plugin.bossBar.setMaxHealth(3600);
                  this.plugin.bossBar.setHealth(time);
                  this.mainLine = TextFormat.DARK_PURPLE + "Crystals  " + TextFormat.YELLOW + this.getTimeString(time);
                  this.mainLine = this.recalculateLineOffset() + this.mainLine;
            } else {
                  if (!this.plugin.ending) {
                        return;
                  }

                  this.plugin.bossBar.setMaxHealth(30);
                  this.other = "";
                  this.mainLine = TextFormat.GOLD + "Total time: " + this.getTimeString(this.plugin.task.time) + TextFormat.GRAY + " | " + TextFormat.GREEN + "Restarting in " + time;
                  this.plugin.bossBar.setHealth(time);
            }

            this.update();
      }

      public void updateVotes() {
            VotingManager vm = this.plugin.votingManager;
            String[] votes = new String[]{vm.currentTable[0], vm.currentTable[1], vm.currentTable[2]};
            this.other = "                                          §8Voting §f| §6/vote <map>\n                                        §b[1] §8" + votes[0] + " §c» §a" + vm.stats.get(votes[0]) + " Hlasu\n                                        §b[2] §8" + votes[1] + " §c» §a" + vm.stats.get(votes[1]) + " Hlasu\n                                        §b[3] §8" + votes[2] + " §c» §a" + vm.stats.get(votes[2]) + " Hlasu";
            this.update();
      }

      public void updateTeamStats() {
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

            this.other = "                                                    §8Map: §6" + map + status;
            this.update();
      }

      private void update() {
            this.plugin.bossBar.updateText(this.mainLine + this.lineOffset + this.other);
            this.plugin.bossBar.updateInfo();
      }

      private String getDisplayPhase(int phase) {
            switch(phase) {
            case 0:
                  return TextFormat.YELLOW + "Voting";
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

      private String getTimeString(int time) {
            long hours = (long)time / 3600L;
            long minutes = ((long)time - hours * 3600L) / 60L;
            long seconds = (long)time - hours * 3600L - minutes * 60L;
            return String.format(TextFormat.WHITE + "%02d" + TextFormat.GRAY + ":" + TextFormat.WHITE + "%02d" + TextFormat.GRAY + ":" + TextFormat.WHITE + "%02d", hours, minutes, seconds).replace("-", "");
      }

      private String recalculateLineOffset() {
            int maxLength = 0;
            String[] var2 = this.other.split("\n");
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                  String line = var2[var4];
                  maxLength = Math.max(maxLength, line.length());
            }

            int firstLength = this.mainLine.length();
            return maxLength < firstLength ? "" : (this.mainLineOffset = (new String(new char[(int)((double)((maxLength - firstLength) / 2) * 1.4D)])).replace("\u0000", " "));
      }
}
