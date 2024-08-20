package Crystals.entity;

import Crystals.arena.util.Utils;
import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.utils.TextFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class EntityWitherBoss extends Entity {
      public static final int NETWORK_ID = 52;
      private int team;
      public Map damagers = new LinkedHashMap();

      public EntityWitherBoss(FullChunk chunk, CompoundTag nbt) {
            super(chunk, nbt);
            this.invulnerable = true;
            this.setDataFlag(0, 16, true);
            this.team = this.namedTag.getInt("CTeam");
            this.setMaxHealth(700);
            this.setHealth(700.0F);
            this.setNameTagAlwaysVisible(true);
            this.setNameTagVisible(true);
            this.updateNameTag();
      }

      public int getNetworkId() {
            return 52;
      }

      public boolean attack(EntityDamageEvent source) {
            return false;
      }

      public boolean onUpdate(int currentTick) {
            if (currentTick % 140 == 0) {
                  Entity target = Utils.findFirst(this.level, this, 4.0D, this, (e) -> {
                        return e instanceof Player;
                  });
                  if (target != null) {
                        Random rnd = new Random();
                        if (rnd.nextInt(3) == 0) {
                              EntityPrimedTNT tnt = new EntityPrimedTNT(this.chunk, Utils.getNbt(this, new Vector3(Math.random() * 0.1D, 0.6D, Math.random() * 0.1D)));
                              tnt.spawnToAll();
                        } else {
                              EntityWitherSkull skull = new EntityWitherSkull(this.chunk, Utils.getNbt(this, target.subtract(this).normalize()));
                              skull.spawnToAll();
                        }
                  }
            }

            return true;
      }

      public void updateNameTag() {
            this.setNameTag(TextFormat.GRAY + "HP: " + TextFormat.GOLD + this.getHealth() / 700.0F + "%");
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

      public int getTeam() {
            return this.team;
      }
}
