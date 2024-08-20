package Crystals.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.potion.Effect;

public class EntityWitherSkull extends EntityProjectile {
      public static final int NETWORK_ID = 89;

      public EntityWitherSkull(FullChunk chunk, CompoundTag nbt) {
            super(chunk, nbt);
            double dirx = this.x - this.motionX;
            double diry = this.y - this.motionY;
            double dirz = this.z - this.motionZ;
            double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
            dirx /= len;
            diry /= len;
            dirz /= len;
            double pitch = Math.asin(diry);
            double yaw = Math.atan2(dirz, dirx);
            this.setRotation(yaw, pitch);
      }

      public float getHeight() {
            return 0.6F;
      }

      public float getWidth() {
            return 0.6F;
      }

      public float getLength() {
            return 0.6F;
      }

      public int getNetworkId() {
            return 89;
      }

      public boolean entityBaseTick(int tickDiff) {
            boolean hasUpdate = super.entityBaseTick(tickDiff);
            this.move(this.motionX, this.motionY, this.motionZ);
            this.updateMovement();
            if (this.age++ > 60 || this.isCollided) {
                  this.close();
            }

            return hasUpdate && !this.closed;
      }

      public void onCollideWithEntity(Entity entity) {
            if (entity instanceof Player) {
                  Player p = (Player)entity;
                  p.addEffect(Effect.getEffect(20).setAmplifier(1).setDuration(100));
                  Explosion explosion = new Explosion(this, 1.0D, this);
                  explosion.explodeB();
                  this.close();
            }
      }

      public void spawnTo(Player player) {
            super.spawnTo(player);
            AddEntityPacket pk = new AddEntityPacket();
            pk.x = (float)this.x;
            pk.y = (float)this.y;
            pk.z = (float)this.z;
            pk.entityRuntimeId = this.getId();
            pk.entityUniqueId = this.getId();
            pk.type = this.getNetworkId();
            pk.metadata = this.dataProperties;
            pk.attributes = new Attribute[]{Attribute.getAttribute(4).setMaxValue(100.0F).setValue(100.0F).setDefaultValue(100.0F)};
            player.dataPacket(pk);
      }
}
