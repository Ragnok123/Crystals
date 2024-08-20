package Crystals.arena.kit;

import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.TextFormat;

public enum Kit {
      ACROBAT(Item.get(288), 0, "", 1000, "https://image.ibb.co/eHeRFb/Wg1B.png", new Item[]{Item.get(268, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(270, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(271, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(58, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(345, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(261, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(262, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"})}),
      ARCHER(Item.get(261), 1, "", 1000, "https://thumb.ibb.co/gVnz1G/xXGg.png", new Item[]{Item.get(261, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(270, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(271, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(373, 21, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(345, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(262, 0, 16)}),
      BERSERKER(Item.get(303), 2, "", 1000, "https://thumb.ibb.co/cfpJvb/ZuLl.png", new Item[]{Item.get(272, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(270, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(271, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(373, 21, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(345, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"})}),
      CIVILIAN(Item.get(58), 3, "", 0, "https://thumb.ibb.co/khPOTw/lWVX.png", new Item[]{Item.get(268, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(274, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(275, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(58, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(345, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"})}),
      HANDYMAN(Item.get(145), 4, "", 0, "https://thumb.ibb.co/n8mgFb/Bx9L.png", new Item[]{Item.get(268, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(270, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(271, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(58, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(345, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"})}),
      LUMBERJACK(Item.get(275), 5, "", 500, "https://thumb.ibb.co/m26L8w/b0d1.png", new Item[]{Item.get(268, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(270, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(275, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(58, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(345, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"})}),
      MINER(Item.get(274), 6, "", 1000, "https://thumb.ibb.co/fwkrFb/WmsJ.png", new Item[]{Item.get(268, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), addEnchantment(Item.get(274, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Enchantment.getEnchantment(15)), Item.get(271, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(58, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(345, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"})}),
      OPERATIVE(Item.get(88), 7, "", 500, "https://thumb.ibb.co/bVydvb/Fjfj.png", new Item[]{Item.get(268, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(270, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(271, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(88, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(345, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"})}),
      SCOUT(Item.get(346), 8, "", 1000, "https://thumb.ibb.co/dPc5ab/Kpxs.png", new Item[]{Item.get(283, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(346, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(270, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(271, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(345, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"})}),
      WARRIOR(Item.get(272), 11, "", 500, "https://thumb.ibb.co/i6ZOTw/WYoh.png", new Item[]{Item.get(272, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(270, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(271, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(373, 21, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"}), Item.get(345, 0, 1).setLore(new String[]{"" + TextFormat.RESET + TextFormat.GOLD + "SoulBound"})});

      private Item item;
      private Item[] items;
      private Integer id;
      private String message;
      private Integer cost;
      private String url;

      private Kit(Item item, Integer id, String message, Integer cost, String imageURL, Item... items) {
            this.item = item;
            this.id = id;
            this.message = message;
            this.items = items;
            this.cost = cost;
            this.url = imageURL;
      }

      public Integer getId() {
            return this.id;
      }

      public String getName() {
            return this.name().toLowerCase();
      }

      public Item[] getItems() {
            return (Item[])this.items.clone();
      }

      public Integer getCost() {
            return this.cost;
      }

      public String getMessage() {
            return this.message;
      }

      public Item getItem() {
            return this.item.clone();
      }

      public boolean isFree() {
            return this.cost <= 0;
      }

      public String getImageUrl() {
            return this.url;
      }

      public void give(Player p) {
            PlayerInventory inv = p.getInventory();
            Item[] items = this.getItems();

            for(int i = 0; i < items.length; ++i) {
                  inv.setItem(i, this.items[i]);
            }

      }

      private static Item addEnchantment(Item item, Enchantment ench) {
            ench.setLevel(1);
            item.addEnchantment(new Enchantment[]{ench});
            return item;
      }
}
