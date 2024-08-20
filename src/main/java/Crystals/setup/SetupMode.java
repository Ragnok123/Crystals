package Crystals.setup;

import Crystals.arena.object.ArenaData;
import Crystals.arena.util.Border2D;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class SetupMode {
      public static ArenaData loadMap(Config cfg) {
            ArenaData data = new ArenaData();
            Iterator var2 = cfg.getStringList("teams").iterator();

            String posHash;
            while(var2.hasNext()) {
                  posHash = (String)var2.next();
                  data.teams.put(posHash, new ArenaData.TeamData());
            }

            var2 = cfg.getSection("spawn").entrySet().iterator();

            ArenaData.TeamData teamData;
            Entry entry;
            while(var2.hasNext()) {
                  entry = (Entry)var2.next();
                  teamData = (ArenaData.TeamData)data.teams.get(entry.getKey());
                  Iterator var5 = ((List)entry.getValue()).iterator();

                  while(var5.hasNext()) {
                        String posHash = (String)var5.next();
                        teamData.spawns.add(readVectorFromHash(posHash));
                  }
            }

            for(var2 = cfg.getSection("nexus").entrySet().iterator(); var2.hasNext(); ((ArenaData.TeamData)data.teams.get(entry.getKey())).crystal = readVectorFromHash((String)entry.getValue())) {
                  entry = (Entry)var2.next();
            }

            var2 = cfg.getSection("boss").entrySet().iterator();

            while(var2.hasNext()) {
                  entry = (Entry)var2.next();
                  data.bosses.put(entry.getKey(), readVectorFromHash((String)entry.getValue()));
            }

            var2 = cfg.getStringList("npc").iterator();

            while(var2.hasNext()) {
                  posHash = (String)var2.next();
                  data.npcs.add(readVectorFromHash(posHash));
            }

            data.centerNpc = readVectorFromHash(cfg.getString("centerNpc"));

            String hash;
            for(var2 = cfg.getSection("zone").entrySet().iterator(); var2.hasNext(); teamData.zone = readRange(hash)) {
                  entry = (Entry)var2.next();
                  teamData = data.getTeam((String)entry.getKey());
                  hash = ((ConfigSection)entry.getValue()).getString("nexus");
            }

            data.mapZone = readRange(cfg.getString("mapZone"));
            return data;
      }

      private static void writeVector3(String key, Vector3 v, ConfigSection section) {
            ConfigSection section1 = new ConfigSection();
            section1.set("x", v.x);
            section1.set("y", v.y);
            section1.set("z", v.z);
            section.set(key, section1);
      }

      private static Vector3 readVector3(ConfigSection section) {
            return readVector3((String)null, section);
      }

      private static Vector3 readVector3(String key, ConfigSection section) {
            ConfigSection section1 = key != null ? section.getSection(key) : section;
            Vector3 vector = new Vector3();
            vector.x = section1.getDouble("x");
            vector.y = section1.getDouble("y");
            vector.z = section1.getDouble("z");
            return vector;
      }

      private static Vector3 readVectorFromHash(String hash) {
            String[] args = hash.split(",");
            return new Vector3(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
      }

      private static Border2D readRange(String hash) {
            String[] args = hash.split("->");
            String[] args1 = args[0].split(",");
            String[] args2 = args[1].split(",");
            double x1 = Double.parseDouble(args1[0]);
            double z1 = Double.parseDouble(args1[1]);
            double x2 = Double.parseDouble(args2[0]);
            double z2 = Double.parseDouble(args2[1]);
            return new Border2D(new Vector2(x1, z1), new Vector2(x2, z2));
      }
}
