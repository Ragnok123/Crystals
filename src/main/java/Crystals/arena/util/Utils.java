package Crystals.arena.util;

import cn.nukkit.Server;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.level.Level;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TextFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class Utils {
      private static Random random = new Random();

      public static int rand(int min, int max) {
            return min + random.nextInt(max - min);
      }

      public static void enderDragonEffect(Level level, Vector3 pos) {
            Collection players = level.getChunkPlayers(pos.getFloorX() >> 4, pos.getFloorZ() >> 4).values();
            long eid = (long)(Entity.entityCount++);
            AddEntityPacket pk = new AddEntityPacket();
            pk.entityUniqueId = eid;
            pk.entityRuntimeId = eid;
            pk.type = 53;
            pk.x = (float)pos.x;
            pk.y = (float)pos.y;
            pk.z = (float)pos.z;
            pk.metadata = (new EntityMetadata()).putLong(0, 65536L).putShort(7, 400).putShort(43, 400).putString(4, "").putLong(38, -1L).putFloat(39, 0.0F);
            pk.attributes = new Attribute[]{Attribute.getAttribute(4).setValue(0.0F).setMaxValue(300.0F)};
            RemoveEntityPacket rpk = new RemoveEntityPacket();
            rpk.eid = eid;
            Server.broadcastPacket(players, pk);
            level.getServer().getScheduler().scheduleDelayedTask(() -> {
                  Server.broadcastPacket(players, rpk);
            }, 240);
      }

      public static TextFormat colorFromString(String name) {
            String var1 = name.toLowerCase();
            byte var2 = -1;
            switch(var1.hashCode()) {
            case -1852648987:
                  if (var1.equals("dark_aqua")) {
                        var2 = 14;
                  }
                  break;
            case -1852623997:
                  if (var1.equals("dark_blue")) {
                        var2 = 9;
                  }
                  break;
            case -1852469876:
                  if (var1.equals("dark_gray")) {
                        var2 = 8;
                  }
                  break;
            case -1846156123:
                  if (var1.equals("dark_purple")) {
                        var2 = 11;
                  }
                  break;
            case -1591987974:
                  if (var1.equals("dark_green")) {
                        var2 = 6;
                  }
                  break;
            case -1008851410:
                  if (var1.equals("orange")) {
                        var2 = 1;
                  }
                  break;
            case -734239628:
                  if (var1.equals("yellow")) {
                        var2 = 4;
                  }
                  break;
            case 112785:
                  if (var1.equals("red")) {
                        var2 = 15;
                  }
                  break;
            case 3002044:
                  if (var1.equals("aqua")) {
                        var2 = 3;
                  }
                  break;
            case 3027034:
                  if (var1.equals("blue")) {
                        var2 = 2;
                  }
                  break;
            case 3178592:
                  if (var1.equals("gold")) {
                        var2 = 0;
                  }
                  break;
            case 3181155:
                  if (var1.equals("gray")) {
                        var2 = 7;
                  }
                  break;
            case 3441014:
                  if (var1.equals("pink")) {
                        var2 = 13;
                  }
                  break;
            case 93818879:
                  if (var1.equals("black")) {
                        var2 = 10;
                  }
                  break;
            case 98619139:
                  if (var1.equals("green")) {
                        var2 = 5;
                  }
                  break;
            case 1331038981:
                  if (var1.equals("light_purple")) {
                        var2 = 12;
                  }
                  break;
            case 1741368392:
                  if (var1.equals("dark_red")) {
                        var2 = 16;
                  }
            }

            switch(var2) {
            case 0:
            case 1:
                  return TextFormat.GOLD;
            case 2:
                  return TextFormat.BLUE;
            case 3:
                  return TextFormat.AQUA;
            case 4:
                  return TextFormat.YELLOW;
            case 5:
                  return TextFormat.GREEN;
            case 6:
                  return TextFormat.DARK_GREEN;
            case 7:
                  return TextFormat.GRAY;
            case 8:
                  return TextFormat.DARK_GRAY;
            case 9:
                  return TextFormat.DARK_BLUE;
            case 10:
                  return TextFormat.BLACK;
            case 11:
                  return TextFormat.DARK_PURPLE;
            case 12:
            case 13:
                  return TextFormat.LIGHT_PURPLE;
            case 14:
                  return TextFormat.DARK_AQUA;
            case 15:
                  return TextFormat.RED;
            case 16:
                  return TextFormat.DARK_RED;
            default:
                  return TextFormat.WHITE;
            }
      }

      public static CompoundTag getNbt(Vector3 pos) {
            return getNbt(pos, new Vector3());
      }

      public static CompoundTag getNbt(Vector3 pos, Vector3 motion) {
            return (new CompoundTag()).putList((new ListTag("Pos")).add(new DoubleTag("", pos.x)).add(new DoubleTag("", pos.y)).add(new DoubleTag("", pos.z))).putList((new ListTag("Motion")).add(new DoubleTag("", motion.x)).add(new DoubleTag("", motion.y)).add(new DoubleTag("", motion.z))).putList((new ListTag("Rotation")).add(new FloatTag("", 0.0F)).add(new FloatTag("", 0.0F)));
      }

      public static List getNearbyEntities(Level level, Vector3 center, double distance, Entity target, Predicate entitySelector) {
            List value = new ArrayList();
            int minX = NukkitMath.floorDouble((center.x - distance) / 16.0D);
            int maxX = NukkitMath.ceilDouble((center.x + distance) / 16.0D);
            int minZ = NukkitMath.floorDouble((center.z - distance) / 16.0D);
            int maxZ = NukkitMath.ceilDouble((center.z + distance) / 16.0D);
            double distSquared = distance * distance;

            for(int x = minX; x <= maxX; ++x) {
                  for(int z = minZ; z <= maxZ; ++z) {
                        Collection entities = level.getChunkEntities(x, z).values();
                        Iterator var16 = entities.iterator();

                        while(var16.hasNext()) {
                              Entity e = (Entity)var16.next();
                              if (e.getId() != target.getId() && e.distanceSquared(center) <= distSquared && entitySelector.test(e)) {
                                    value.add(e);
                              }
                        }
                  }
            }

            return value;
      }

      public static Entity findFirst(Level level, Vector3 center, double distance, Entity target, Predicate entitySelector) {
            int minX = NukkitMath.floorDouble((center.x - distance) / 16.0D);
            int maxX = NukkitMath.ceilDouble((center.x + distance) / 16.0D);
            int minZ = NukkitMath.floorDouble((center.z - distance) / 16.0D);
            int maxZ = NukkitMath.ceilDouble((center.z + distance) / 16.0D);
            double distSquared = distance * distance;

            for(int x = minX; x <= maxX; ++x) {
                  for(int z = minZ; z <= maxZ; ++z) {
                        Collection entities = level.getChunkEntities(x, z).values();
                        Iterator var15 = entities.iterator();

                        while(var15.hasNext()) {
                              Entity e = (Entity)var15.next();
                              if (e.getId() != target.getId() && e.distanceSquared(center) <= distSquared && entitySelector.test(e)) {
                                    return e;
                              }
                        }
                  }
            }

            return null;
      }

      public static DyeColor textFormatToDyeColor(TextFormat textFormat) {
            switch(textFormat) {
            case GOLD:
                  return DyeColor.ORANGE;
            case RED:
            case DARK_RED:
                  return DyeColor.RED;
            case BLUE:
            case DARK_BLUE:
                  return DyeColor.BLUE;
            case GREEN:
                  return DyeColor.LIME;
            case DARK_GREEN:
                  return DyeColor.GREEN;
            case YELLOW:
                  return DyeColor.YELLOW;
            case LIGHT_PURPLE:
                  return DyeColor.PINK;
            case DARK_PURPLE:
                  return DyeColor.MAGENTA;
            case BLACK:
                  return DyeColor.BLACK;
            case AQUA:
                  return DyeColor.LIGHT_BLUE;
            case GRAY:
                  return DyeColor.LIGHT_GRAY;
            case DARK_GRAY:
                  return DyeColor.GRAY;
            case DARK_AQUA:
                  return DyeColor.CYAN;
            case WHITE:
                  return DyeColor.WHITE;
            default:
                  return DyeColor.WHITE;
            }
      }
}
