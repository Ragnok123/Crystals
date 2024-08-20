package Crystals.arena.task;

import cn.nukkit.block.Block;
import cn.nukkit.level.sound.BlockPlaceSound;
import cn.nukkit.scheduler.Task;

public class BlockRegenerateTask extends Task {
      private Block block;

      public BlockRegenerateTask(Block b) {
            this.block = b;
      }

      public void onRun(int currentTick) {
            this.block.getLevel().setBlock(this.block, this.block);
            this.block.getLevel().addSound(new BlockPlaceSound(this.block.add(0.5D, 0.5D, 0.5D), this.block.getId()));
      }
}
