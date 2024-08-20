package Crystals.arena.task;

import Crystals.arena.Arena;
import Crystals.arena.util.FireworkUtils;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.scheduler.Task;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FireworkTask extends Task {
      private Arena plugin;
      private int fireworkIndex = 0;

      public FireworkTask(Arena plugin) {
            this.plugin = plugin;
      }

      public void onRun(int i) {
            this.spawnFireworks();
      }

      private void spawnFireworks() {
            List positions = new ArrayList();

            for(int i = 1; i < 5; ++i) {
                  positions.add(this.plugin.getTeam(i).getSpawnLocation());
            }

            if (this.fireworkIndex > 3) {
                  this.fireworkIndex = 0;
            }

            Item firework = (Item)FireworkUtils.of(this.plugin.winnerTeam).get(this.fireworkIndex++);
            CompoundTag itemTag = NBTIO.putItemHelper(firework);
            Iterator var4 = positions.iterator();

            while(var4.hasNext()) {
                  Vector3 pos = (Vector3)var4.next();
                  CompoundTag nbt = (new CompoundTag()).putList((new ListTag("Pos")).add(new DoubleTag("", pos.x + 0.5D)).add(new DoubleTag("", pos.y + 0.5D)).add(new DoubleTag("", pos.z + 0.5D))).putList((new ListTag("Motion")).add(new DoubleTag("", 0.0D)).add(new DoubleTag("", 0.0D)).add(new DoubleTag("", 0.0D))).putList((new ListTag("Rotation")).add(new FloatTag("", 0.0F)).add(new FloatTag("", 0.0F))).putCompound("FireworkItem", itemTag);
                  EntityFirework entity = new EntityFirework(this.plugin.level.getChunk(pos.getFloorX() >> 4, pos.getFloorZ() >> 4), nbt);
                  entity.spawnToAll();
            }

      }
}
