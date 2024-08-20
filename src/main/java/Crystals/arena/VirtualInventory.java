package Crystals.arena;

import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import java.util.Map;

public class VirtualInventory {
      public int[] hotbar = new int[9];
      public Item[] armor = new Item[4];
      public Map contents;
      public int heldItemIndex;
      public int xp = 0;
      public int xplevel = 0;
      public int hunger = 0;
      public float health = 0.0F;
      public int maxHealth = 14;

      public VirtualInventory(Player p) {
            PlayerInventory inv = p.getInventory();
            this.contents = inv.getContents();

            int i;
            for(i = 0; i < 4; ++i) {
                  this.armor[i] = inv.getArmorItem(i);
            }

            for(i = 0; i < 9; ++i) {
                  this.hotbar[i] = inv.getHotbarSlotIndex(i);
            }

            this.hunger = p.getFoodData().getLevel();
            this.xp = p.getExperience();
            this.xplevel = p.getExperienceLevel();
            this.health = p.getHealth();
      }

      public Map getContents() {
            return this.contents;
      }
}
