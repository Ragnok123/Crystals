package Crystals.arena.task;

import cn.nukkit.scheduler.Task;

public class BlockEntityTask extends Task {
      private Runnable[] runnables;

      public BlockEntityTask(Runnable... runnables) {
            this.runnables = runnables;
      }

      public void onRun(int i) {
            Runnable[] var2 = this.runnables;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                  Runnable runnable = var2[var4];
                  runnable.run();
            }

      }
}
