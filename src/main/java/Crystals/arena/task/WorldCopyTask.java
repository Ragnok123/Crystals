package Crystals.arena.task;

import Crystals.Crystals;
import Crystals.arena.Arena;
import Crystals.arena.manager.WorldManager;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;

public class WorldCopyTask extends AsyncTask {
      private String arena;
      private String map;
      private String path;
      private boolean force;

      public WorldCopyTask(String arena, String map, String path, boolean force) {
            this.map = map;
            this.arena = arena;
            this.path = path;
            this.force = force;
      }

      public void onRun() {
            WorldManager.deleteWorld(this.map, this.path);
            WorldManager.addWorld(this.map, this.path);
      }

      public void onCompletion(Server server) {
            Crystals plugin = Crystals.getInstance();
            Arena arena = plugin.getArena(this.arena);
            if (server.loadLevel(this.map)) {
                  arena.isLevelLoaded = true;
                  arena.level = server.getLevelByName(this.map);
                  arena.spawnNPCs();
                  arena.updateTeamData();
            }

            if (this.force) {
                  arena.startGame(true);
            }

      }
}
