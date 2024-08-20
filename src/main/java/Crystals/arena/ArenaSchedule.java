package Crystals.arena;

import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.TextFormat;

public class ArenaSchedule extends Task {
      public Arena plugin;
      public int time = 0;
      public int time1 = 60;
      public int popup = 0;
      private int ending = 30;
      private boolean joining;
      private int checkAlive = 0;

      ArenaSchedule(Arena plugin) {
            this.plugin = plugin;
      }

      public void onRun(int currentTick) {
            if (this.popup == 0) {
                  this.setJoinSigns();
            }

            ++this.popup;
            if (this.popup == 2) {
                  this.popup = 0;
            }

            if (this.plugin.phase == Arena.GamePhase.LOBBY && !this.plugin.starting) {
                  this.plugin.checkLobby();
            }

            if (this.plugin.starting) {
                  if (this.joining) {
                        this.joining();
                  } else {
                        this.starting();
                  }
            } else if (this.plugin.phase == Arena.GamePhase.GAME) {
                  if (this.plugin.ending) {
                        this.ending();
                  } else {
                        this.running();
                  }
            }

      }

      private void setJoinSigns() {
            Level lobby = this.plugin.plugin.level;
            BlockEntitySign signb = (BlockEntitySign)lobby.getBlockEntity((Vector3)this.plugin.maindata.get("1sign"));
            BlockEntitySign signr = (BlockEntitySign)lobby.getBlockEntity((Vector3)this.plugin.maindata.get("2sign"));
            BlockEntitySign signy = (BlockEntitySign)lobby.getBlockEntity((Vector3)this.plugin.maindata.get("3sign"));
            BlockEntitySign signg = (BlockEntitySign)lobby.getBlockEntity((Vector3)this.plugin.maindata.get("4sign"));
            if (signb != null) {
                  signb.setText(new String[]{"", TextFormat.DARK_BLUE + "[BLUE]", "" + TextFormat.GRAY + this.plugin.getTeam(1).getPlayers().size() + TextFormat.GRAY + " players", ""});
            }

            if (signr != null) {
                  signr.setText(new String[]{"", TextFormat.DARK_RED + "[RED]", "" + TextFormat.GRAY + this.plugin.getTeam(2).getPlayers().size() + TextFormat.GRAY + " players", ""});
            }

            if (signy != null) {
                  signy.setText(new String[]{"", TextFormat.YELLOW + "[YELLOW]", "" + TextFormat.GRAY + this.plugin.getTeam(3).getPlayers().size() + TextFormat.GRAY + " players", ""});
            }

            if (signg != null) {
                  signg.setText(new String[]{"", TextFormat.DARK_GREEN + "[GREEN]", "" + TextFormat.GRAY + this.plugin.getTeam(4).getPlayers().size() + TextFormat.GRAY + " players", ""});
            }

      }

      private void starting() {
            --this.time1;
            this.plugin.barUtil.updateBar(this.time1);
            if (this.time1 <= 0) {
                  this.plugin.selectMap();
                  this.joining = true;
                  this.time1 = 30;
            }

      }

      private void joining() {
            --this.time1;
            this.plugin.barUtil.updateBar(this.time1);
            if (this.time1 <= 0) {
                  this.joining = false;
                  this.plugin.startGame();
            }

      }

      private void running() {
            ++this.checkAlive;
            if (this.checkAlive >= 5) {
                  this.plugin.checkAlive();
            }

            ++this.time;
            this.plugin.barUtil.updateBar(this.time);
            if (this.time % 600 == 0) {
                  this.plugin.bossManager.spawnBosses();
            }

            if (this.time >= 3600) {
                  this.plugin.endGame();
            }

      }

      private void ending() {
            this.plugin.barUtil.updateBar(this.ending);
            --this.ending;
            if (this.ending < 0) {
                  this.plugin.ending = false;
                  this.ending = 30;
                  this.plugin.stopGame();
            }

      }
}
