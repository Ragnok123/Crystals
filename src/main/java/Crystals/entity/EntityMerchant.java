package Crystals.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.utils.TextFormat;

public class EntityMerchant extends Entity {
      public static final int NETOWORK_ID = 15;

      public EntityMerchant(FullChunk chunk, CompoundTag nbt) {
            super(chunk, nbt);
            this.setDataProperty(new IntEntityData(2, 1));
            this.setDataFlag(0, 16, true);
            this.invulnerable = true;
            this.setNameTag(TextFormat.AQUA + "Merchant");
            this.setNameTagAlwaysVisible();
            this.setNameTagVisible();
      }

      public int getNetworkId() {
            return -1;
      }

      public void spawnTo(Player player) {
            super.spawnTo(player);
            AddEntityPacket pk = new AddEntityPacket();
            pk.x = (float)this.x;
            pk.y = (float)this.y;
            pk.z = (float)this.z;
            pk.entityRuntimeId = this.getId();
            pk.entityUniqueId = this.getId();
            pk.type = 15;
            pk.metadata = this.dataProperties;
            pk.attributes = new Attribute[]{Attribute.getAttribute(4).setMaxValue(100.0F).setValue(100.0F).setDefaultValue(100.0F)};
            player.dataPacket(pk);
      }
}
