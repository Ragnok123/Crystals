package Crystals.arena.object;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.math.NukkitRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum Resource {
      WOOD(Item.get(17), 15, 10, Resource.ResourceType.WOOD, (i) -> {
            return Item.get(5, i % 4, 4);
      }),
      WOOD2(Item.get(162), 15, 10, Resource.ResourceType.WOOD, (i) -> {
            return Item.get(5, i % 2 + 4, 4);
      }),
      MELON(Item.get(103), 5, 10, Resource.ResourceType.CUSTOM, (i) -> {
            return Item.get(360, 0, (new NukkitRandom()).nextRange(4, 7));
      }),
      PUMPKIN(Item.get(86), 5, 10, Resource.ResourceType.CUSTOM, (i) -> {
            return Item.get(400);
      }),
      HAY(Item.get(170), 5, 10, Resource.ResourceType.CUSTOM, (i) -> {
            return Item.get(297);
      }),
      GOLD(Item.get(14), 30, 15, Resource.ResourceType.ORE, (i) -> {
            return Item.get(266);
      }),
      DIAMOND(Item.get(56), 40, 20, Resource.ResourceType.ORE, (i) -> {
            return Item.get(264);
      }),
      EMERALD(Item.get(129), 60, 20, Resource.ResourceType.ORE, (i) -> {
            return Item.get(388);
      }),
      COAL(Item.get(16), 15, 10, Resource.ResourceType.ORE, (i) -> {
            return Item.get(173);
      }),
      IRON(Item.get(15), 25, 15, Resource.ResourceType.ORE, (i) -> {
            return Item.get(265);
      });

      private static final Map ID_MAP = new HashMap();
      final Item item;
      final int xp;
      final int delay;
      final Resource.ResourceType type;
      final Function drops;

      public Item getDrops(Block b, Item... origin) {
            return this.drops == null ? origin[0] : (Item)this.drops.apply(b.getDamage());
      }

      public static Resource fromId(int id) {
            return (Resource)ID_MAP.get(id);
      }

      private Resource(Item item, int xp, int delay, Resource.ResourceType type, Function drops) {
            this.item = item;
            this.xp = xp;
            this.delay = delay;
            this.type = type;
            this.drops = drops;
      }

      public Item getItem() {
            return this.item;
      }

      public int getXp() {
            return this.xp;
      }

      public int getDelay() {
            return this.delay;
      }

      public Resource.ResourceType getType() {
            return this.type;
      }

      public Function getDrops() {
            return this.drops;
      }

      static {
            Resource[] var0 = values();
            int var1 = var0.length;

            for(int var2 = 0; var2 < var1; ++var2) {
                  Resource res = var0[var2];
                  ID_MAP.put(res.item.getId(), res);
            }

      }

      public static enum ResourceType {
            ORE,
            WOOD,
            CUSTOM,
            NONE;
      }
}
