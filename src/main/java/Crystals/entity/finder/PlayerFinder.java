package Crystals.entity.finder;

import Crystals.arena.Arena;
import Crystals.entity.CombatLoggerNPC;
import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import java.util.Iterator;
import me.onebone.actaeon.target.TargetFinder;

public class PlayerFinder extends TargetFinder {
      private int radius;
      private boolean first = true;
      private Player forceTarget = null;
      private int team;
      private Arena arean;

      public PlayerFinder(CombatLoggerNPC entity, long interval, int radius) {
            super(entity, interval);
            this.radius = radius;
            this.team = entity.getTeam();
            this.arean = entity.getArena();
      }

      protected void find() {
            Player near = this.forceTarget;
            double nearest = (double)(this.radius * this.radius);
            if (this.forceTarget == null || !this.forceTarget.isOnline() || this.forceTarget.distanceSquared(this.getEntity()) >= nearest) {
                  this.forceTarget = null;
                  Iterator var4 = this.getEntity().getLevel().getPlayers().values().iterator();

                  while(var4.hasNext()) {
                        Player player = (Player)var4.next();
                        if (this.getEntity().distanceSquared(player) < nearest && this.arean.getPlayerTeam(player).getIndex() != this.team) {
                              near = player;
                              nearest = this.getEntity().distance(player);
                        }
                  }
            }

            if (near != null) {
                  this.getEntity().setTarget(near, this.getEntity().getName(), this.first);
                  this.getEntity().setHate(near);
            } else {
                  this.getEntity().setTarget((Vector3)null, this.getEntity().getName());
            }

            this.first = false;
      }

      public Player getForceTarget() {
            return this.forceTarget;
      }

      public void setForceTarget(Player forceTarget) {
            this.forceTarget = forceTarget;
      }
}
