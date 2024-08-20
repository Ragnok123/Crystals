package Crystals.entity.finder;

import cn.nukkit.entity.Entity;
import cn.nukkit.math.Vector3;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.target.TargetFinder;

public class IronGolemFinder extends TargetFinder {
      private int radius;
      private boolean first = true;
      private Entity forceTarget;

      public IronGolemFinder(MovingEntity entity, long interval, int radius) {
            super(entity, interval);
            this.radius = radius;
      }

      protected void find() {
            if (this.forceTarget == null || this.forceTarget.distance(this.getEntity()) > (double)this.radius) {
                  this.forceTarget = null;
            }

            if (this.forceTarget != null) {
                  this.getEntity().setTarget(this.forceTarget, this.getEntity().getName(), this.first);
                  this.getEntity().setHate(this.forceTarget);
            } else {
                  this.getEntity().setTarget((Vector3)null, this.getEntity().getName());
                  this.getEntity().setHate((Entity)null);
            }

            this.first = false;
      }

      public void setForceTarget(Entity forceTarget) {
            this.forceTarget = forceTarget;
            this.first = true;
      }

      public Entity getForceTarget() {
            return this.forceTarget;
      }
}
