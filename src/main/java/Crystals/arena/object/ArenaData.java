package Crystals.arena.object;

import Crystals.arena.util.Border2D;
import cn.nukkit.math.Vector3;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaData {
      public String name;
      public Map teams = new HashMap();
      public Map bosses = new HashMap();
      public List npcs = new ArrayList();
      public Vector3 centerNpc;
      public Border2D mapZone;

      public ArenaData.TeamData getTeam(String name) {
            return (ArenaData.TeamData)this.teams.get(name);
      }

      public String getName() {
            return this.name;
      }

      public static class TeamData {
            public List spawns = new ArrayList();
            public Vector3 crystal;
            public Border2D zone;

            public List getSpawns() {
                  return this.spawns;
            }

            public Vector3 getCrystal() {
                  return this.crystal;
            }

            public Border2D getZone() {
                  return this.zone;
            }
      }
}
