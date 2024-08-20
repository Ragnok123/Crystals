package Crystals.mysql;

import Crystals.Crystals;
import Crystals.arena.object.PlayerData;
import Crystals.arena.object.Stats;
import GTCore.Mysql.AsyncQuery;
import cn.nukkit.Server;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuitQuery extends AsyncQuery {
      private PlayerData data;

      public QuitQuery(String p, PlayerData data) {
            this.player = p;
            this.data = data;
            if (!Crystals.isShuttingDown) {
                  Server.getInstance().getScheduler().scheduleAsyncTask(this);
            } else {
                  this.onRun();
            }

      }

      public void onRun() {
            Stats stats = this.data.stats;
            List kits = new ArrayList();
            this.data.getKits().forEach((k) -> {
                  kits.add(k.name());
            });

            try {
                  PreparedStatement statement = this.getMysqli().prepareStatement("UPDATE crystals SET kills = kills + '" + stats.killsDelta + "', deaths = deaths + '" + stats.deathsDelta + "', wins = wins + '" + stats.winsDelta + "', losses = losses + '" + stats.lossesDelta + "', crystals = crystals + '" + stats.crystalsDelta + "', crystalsrevived = crystalsrevived + '" + stats.crystalReviveDelta + "', kits = '" + String.join(",", kits) + "' WHERE name = '" + this.player.trim().toLowerCase() + "'");
                  statement.executeUpdate();
            } catch (SQLException var4) {
                  var4.printStackTrace();
            }

      }
}
