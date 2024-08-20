package Crystals.arena.object;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Position;

public class Crystal {
      private boolean alive = true;
      private Position position;
      private Team team;
      private int immuneUntil;

      public Crystal(Team team, Position pos) {
            this.team = team;
            this.position = pos;
      }

      public Team getTeam() {
            return this.team;
      }

      public Position getPosition() {
            return this.position;
      }

      public void destroy() {
            if (this.isAlive()) {
                  this.alive = false;
                  this.getPosition().getLevel().setBlock(this.getPosition(), Block.get(49), true, false);
                  this.getTeam().recalculateStatus();
            }
      }

      public void setImmune(int ticks) {
            int tick = Server.getInstance().getTick();
            this.immuneUntil = tick + ticks;
      }

      public boolean isImmune() {
            int tick = Server.getInstance().getTick();
            return this.immuneUntil >= tick;
      }

      public int getImmuneTime() {
            return this.immuneUntil - Server.getInstance().getTick();
      }

      public boolean isAlive() {
            return this.alive;
      }

      public void setAlive(boolean alive) {
            this.alive = alive;
      }
}
