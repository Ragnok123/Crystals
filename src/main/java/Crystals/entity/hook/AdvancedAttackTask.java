package Crystals.entity.hook;

import Crystals.entity.Attackable;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.network.protocol.EntityEventPacket;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.task.MovingEntityTask;

public class AdvancedAttackTask extends MovingEntityTask {
      private Entity target;
      private float damage;
      private double viewAngle;
      private boolean effectual;
      private AdvancedAttackHook.DamageCalculator calculator;
      private AdvancedAttackHook.EntityFilter filter;

      public AdvancedAttackTask(MovingEntity entity, Entity target, AdvancedAttackHook.DamageCalculator calculator, AdvancedAttackHook.EntityFilter filter, double viewAngle, boolean effectual) {
            super(entity);
            this.target = target;
            this.calculator = calculator;
            this.viewAngle = viewAngle;
            this.effectual = effectual;
            this.filter = filter;
      }

      public void onUpdate(int tick) {
            if (!this.filter.accept(this.target)) {
                  this.entity.updateBotTask((MovingEntityTask)null);
            } else {
                  double angle = Math.atan2(this.target.z - this.entity.z, this.target.x - this.entity.x);
                  double yaw = angle * 180.0D / 3.141592653589793D - 90.0D;
                  double min = this.entity.yaw - this.viewAngle / 2.0D;
                  double max = this.entity.yaw + this.viewAngle / 2.0D;
                  boolean valid;
                  if (min < 0.0D) {
                        valid = yaw > 360.0D + min || yaw < max;
                  } else if (max > 360.0D) {
                        valid = yaw < max - 360.0D || yaw > min;
                  } else {
                        valid = yaw < max && yaw > min;
                  }

                  if (valid && this.effectual && this.target.noDamageTicks <= 0) {
                        EntityDamageByEntityEvent event;
                        this.target.attack(event = new EntityDamageByEntityEvent(this.getEntity(), this.target, DamageCause.ENTITY_ATTACK, this.calculator.calculate(this.target)));
                        this.target.noDamageTicks = 10;
                        if (!event.isCancelled() && this.entity instanceof Attackable) {
                              ((Attackable)this.entity).attackEntity(this.target);
                              EntityEventPacket pk = new EntityEventPacket();
                              pk.eid = this.entity.getId();
                              pk.event = 4;
                              Server.broadcastPacket(this.getEntity().getViewers().values(), pk);
                        }
                  }

                  this.entity.updateBotTask((MovingEntityTask)null);
            }
      }

      public void forceStop() {
      }
}
