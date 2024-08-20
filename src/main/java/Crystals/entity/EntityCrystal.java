package Crystals.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;

public class EntityCrystal extends Entity {
      public static final int NETWORK_ID = 71;
      private int team;

      public EntityCrystal(FullChunk chunk, CompoundTag nbt) {
            super(chunk, nbt);
            this.invulnerable = true;
            this.setDataFlag(0, 36);
            this.setDataFlag(0, 16, true);
            this.team = this.namedTag.getInt("CTeam");
      }

      public int getNetworkId() {
            return 71;
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
