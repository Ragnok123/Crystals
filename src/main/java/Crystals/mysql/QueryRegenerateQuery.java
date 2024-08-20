package Crystals.mysql;

import Crystals.Crystals;
import GTCore.Mysql.AsyncQuery;
import cn.nukkit.Server;
import cn.nukkit.utils.MainLogger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class QueryRegenerateQuery extends AsyncQuery {
      HashMap data;

      public QueryRegenerateQuery(HashMap data) {
            this(data, true);
      }

      public QueryRegenerateQuery(HashMap data, boolean async) {
            this.data = null;
            this.table = "servers";
            this.data = data;
            if (!Crystals.isShuttingDown && async) {
                  Server.getInstance().getScheduler().scheduleAsyncTask(this);
            } else {
                  this.onRun();
            }

      }

      public void onRun() {
            try {
                  String players = (String)this.data.get("players");
                  String maxplayers = (String)this.data.get("maxplayers");
                  String line1 = (String)this.data.get("line1");
                  String line2 = (String)this.data.get("line2");
                  String line3 = (String)this.data.get("line3");
                  String line4 = (String)this.data.get("line4");
                  String text = "INSERT INTO servers ( id, players, maxplayers, line1, line2, line3, line4, multi) VALUES ('" + (String)this.data.get("id") + "', '" + players + "', '" + maxplayers + "', '" + line1 + "', '" + line2 + "', '" + line3 + "', '" + line4 + "', '" + (Crystals.multiPlatform ? 1 : 0) + "') ON DUPLICATE KEY UPDATE players = '" + players + "', maxplayers = '" + maxplayers + "', line1 = '" + line1 + "', line2 = '" + line2 + "', line3 = '" + line3 + "', line4 = '" + line4 + "', multi = '" + (Crystals.multiPlatform ? 1 : 0) + "'";
                  PreparedStatement e = this.getMysqli().prepareStatement(text);
                  e.executeUpdate();
            } catch (SQLException var9) {
                  MainLogger.getLogger().logException(var9);
            }

      }
}
