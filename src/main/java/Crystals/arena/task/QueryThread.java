package Crystals.arena.task;

import Crystals.Crystals;
import Crystals.mysql.QueryRegenerateQuery;
import cn.nukkit.InterruptibleThread;
import java.util.HashMap;

public class QueryThread extends Thread implements InterruptibleThread {
      private HashMap data = new HashMap();

      public void run() {
            while(!Crystals.isShuttingDown) {
                  this.data = Crystals.getInstance().refreshQuery(true);
                  new QueryRegenerateQuery(this.data, false);

                  try {
                        Thread.sleep(1000L);
                  } catch (InterruptedException var2) {
                  }
            }

      }
}
