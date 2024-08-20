package Crystals.arena.object;

import Crystals.Crystals;
import cn.nukkit.Player;
import cn.nukkit.inventory.ChestInventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;

public class Shop {
      public void onTransaction(Player p, int slot, Item item, ChestInventory inv, int phase) {
            if (item.getId() != 266) {
                  if (!p.getInventory().canAddItem(item)) {
                        p.sendMessage(Crystals.getPrefix() + TextFormat.RED + "Tvuj inventar je plny");
                  } else if (item.getId() == 377 && phase < 4) {
                        p.sendMessage(TextFormat.RED + "Tento item muzes koupit az od faze IV");
                  } else {
                        Item cost = inv.getItem(slot + 1);
                        if (!p.getInventory().contains(cost)) {
                              p.sendMessage(TextFormat.RED + "Nemas dostatek zlata");
                        } else {
                              p.sendMessage(TextFormat.GRAY + "Koupil sis " + TextFormat.YELLOW + item.getName());
                              p.getInventory().removeItem(new Item[]{cost});
                              p.getInventory().addItem(new Item[]{item});
                              p.getInventory().sendContents(p);
                        }
                  }
            }
      }
}
