package Crystals.mysql;

import Crystals.Crystals;
import Crystals.arena.Arena;
import Crystals.arena.kit.Kit;
import Crystals.arena.object.PlayerData;
import GTCore.MTCore;
import GTCore.Mysql.AsyncQuery;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.MainLogger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class JoinQuery extends AsyncQuery {
      private HashMap data;

      public JoinQuery(Crystals plugin, String player) {
            this.player = player;
            this.table = "crystals";
            plugin.getServer().getScheduler().scheduleAsyncTask(this);
      }

      public void onQuery(HashMap data) {
            if (data != null && !data.isEmpty()) {
                  this.data = data;
            } else {
                  this.data = this.registerPlayer(this.player);
            }

      }

      public void onCompletion(Server server) {
            final Player p = server.getPlayerExact(this.player);
            if (p != null && p.isOnline()) {
                  Crystals plugin = Crystals.getInstance();
                  final Arena arena = plugin.getArena(plugin.id);
                  final HashMap taskData = this.data;
                  server.getScheduler().scheduleDelayedTask(new Runnable() {
                        public void run() {
                              if (p.isOnline()) {
                                    if (!arena.joinToArena(p)) {
                                          MTCore.getInstance().transferToLobby(p);
                                    } else {
                                          PlayerData data = arena.getPlayerData(p);
                                          data.stats.init(taskData);
                                          Set kits = data.getKits();
                                          String kity = (String)taskData.get("kits");
                                          kits.clear();
                                          if (!kity.isEmpty()) {
                                                Iterator var4 = Arrays.asList(kity.split(",")).iterator();

                                                while(var4.hasNext()) {
                                                      String kit = (String)var4.next();

                                                      try {
                                                            kits.add(Kit.valueOf(kit.toUpperCase()));
                                                      } catch (IllegalArgumentException var7) {
                                                            MainLogger.getLogger().error("Trying to load kit '" + kit + "'", var7);
                                                      }
                                                }
                                          }

                                    }
                              }
                        }
                  }, 20);
            }
      }

      private HashMap registerPlayer(String player) {
            String name = player.toLowerCase().trim();
            HashMap data = new HashMap();
            data.put("name", name);
            data.put("kills", 0);
            data.put("deaths", 0);
            data.put("wins", 0);
            data.put("losses", 0);
            data.put("crystals", 0);
            data.put("crystalsrevived", 0);
            data.put("kits", "civilian,handyman");

            try {
                  PreparedStatement e = this.getMysqli().prepareStatement("INSERT INTO crystals ( name, kills, deaths, wins, losses, crystals, crystalsrevived, kits) VALUES ('" + name + "', '" + 0 + "', ' " + 0 + "', ' " + 0 + "', ' " + 0 + "', ' " + 0 + "', ' " + 0 + "', 'civilian,handyman')");
                  e.executeUpdate();
            } catch (SQLException var5) {
                  var5.printStackTrace();
            }

            return data;
      }
}
