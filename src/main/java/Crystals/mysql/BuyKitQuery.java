package Crystals.mysql;

import Crystals.Crystals;
import Crystals.arena.kit.Kit;
import GTCore.MTCore;
import GTCore.Mysql.AsyncQuery;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import java.util.HashMap;

public class BuyKitQuery extends AsyncQuery {
      public static final int ACTION_BUY = 0;
      public static final int ACTION_INFO = 1;
      private Kit kit;
      private Integer action;
      private String originalPlayer = "";
      private String msg = "";

      public BuyKitQuery(Crystals plugin, String player, Kit kit, int action) {
            this.player = player.toLowerCase();
            this.originalPlayer = player;
            this.kit = kit;
            this.action = action;
            this.table = "crystals";
            plugin.getServer().getScheduler().scheduleAsyncTask(this);
      }

      public void onQuery(HashMap data) {
            if (this.action == 0) {
                  int cost = this.kit.getCost();
                  if (((String)data.get("kits")).contains(this.kit.getName())) {
                        this.msg = TextFormat.GREEN + "◆ Tento kit mas jiz koupeny";
                  } else if (this.getApi().getTokens(this.getPlayer()) < cost) {
                        this.msg = TextFormat.RED + "◆ Nemas dostatek tokenu\n" + TextFormat.ITALIC + TextFormat.GRAY + "http://mc.gameteam.cz/vip-a-kredit/";
                  } else {
                        this.msg = Crystals.getPrefix() + TextFormat.GREEN + "Koupil sis " + this.kit + " za " + TextFormat.AQUA + cost + TextFormat.GREEN + " tokenu";
                        this.addKit(this.player, this.kit.getName());
                        this.getApi().subtractTokens(this.originalPlayer, cost);
                  }
            } else {
                  String purchaseMessage = ((String)data.get("kits")).contains(this.kit.getName()) ? TextFormat.GREEN + "◆ Tento kit mas jiz koupeny" : TextFormat.YELLOW + "◆ Pro zakoupeni tohoto kitu klikni na NPC gold ingotem";
                  String infoMessage = this.kit.getMessage();
                  this.msg = purchaseMessage + "\n" + infoMessage;
            }

      }

      public void onCompletion(Server server) {
            MTCore plugin = MTCore.getInstance();
            if (plugin != null && plugin.isEnabled()) {
                  Player p = server.getPlayerExact(this.player);
                  if (p != null && p.isOnline()) {
                        p.sendMessage(this.msg);
                  }
            }
      }
}
