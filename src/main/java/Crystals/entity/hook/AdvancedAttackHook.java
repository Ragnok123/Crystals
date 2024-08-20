package Crystals.entity.hook;

import cn.nukkit.entity.Entity;
import java.util.Random;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.hook.MovingEntityHook;

public class AdvancedAttackHook extends MovingEntityHook {
      private long lastAttack = 0L;
      private double attackDistance;
      private long coolDown;
      private int effectual;
      private double viewAngle;
      private boolean jump;
      private float damage;
      private AdvancedAttackHook.EntityFilter filter;
      private AdvancedAttackHook.DamageCalculator calculator;

      public AdvancedAttackHook(MovingEntity bot, double attackDistance, AdvancedAttackHook.DamageCalculator calculator, AdvancedAttackHook.EntityFilter filter, long coolDown, int effectual, double viewAngle) {
            super(bot);
            this.attackDistance = attackDistance;
            this.calculator = calculator;
            this.coolDown = coolDown;
            this.effectual = effectual;
            this.viewAngle = viewAngle;
            this.filter = filter;
      }

      public float getDamage() {
            return this.damage;
      }

      public void setDamage(float damage) {
            this.damage = damage;
      }

      public long getCoolDown() {
            return this.coolDown;
      }

      public void setCoolDown(long coolDown) {
            this.coolDown = coolDown;
      }

      public long getLastAttack() {
            return this.lastAttack;
      }

      public boolean canJump() {
            return this.jump;
      }

      public AdvancedAttackHook setJump(boolean jump) {
            this.jump = jump;
            return this;
      }

      public boolean shouldExecute() {
            Entity hate = this.entity.getHate();
            return hate != null && this.entity.distance(hate) <= this.attackDistance;
      }

      public void onUpdate(int tick) {
            Entity hate = this.entity.getHate();
            if (System.currentTimeMillis() - this.lastAttack > this.coolDown) {
                  if (this.entity.getTask() == null) {
                        this.entity.updateBotTask(new AdvancedAttackTask(this.entity, hate, this.calculator, this.filter, this.viewAngle, (new Random()).nextInt(10) < this.effectual));
                  }

                  this.lastAttack = System.currentTimeMillis();
                  if (this.jump && (new Random()).nextBoolean()) {
                        this.entity.jump();
                  }
            }

      }

      public interface EntityFilter {
            boolean accept(Entity var1);
      }

      public interface DamageCalculator {
            float calculate(Entity var1);
      }
}
