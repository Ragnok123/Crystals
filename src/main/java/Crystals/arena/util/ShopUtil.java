package Crystals.arena.util;

import Crystals.lang.Language;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.TextFormat;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShopUtil {
      private static final List shopItems = new ArrayList();
      private static final List potionShopItems = new ArrayList();
      private static final List shopEntries = new ArrayList();
      private static final List potionShopEntries = new ArrayList();
      private static final List shopWindows = new ArrayList();
      private static final List potionShopWindows = new ArrayList();

      private static Item enchantBook(int id, int level) {
            Item item = Item.get(403);
            item.addEnchantment(new Enchantment[]{Enchantment.getEnchantment(id).setLevel(level)});
            return item;
      }

      public static ShopUtil.ShopEntry getItem(int id) {
            return id >= 0 && id < shopEntries.size() ? (ShopUtil.ShopEntry)shopEntries.get(id) : null;
      }

      public static ShopUtil.ShopEntry getPotionItem(int id) {
            return id >= 0 && id < shopEntries.size() ? (ShopUtil.ShopEntry)shopEntries.get(id) : null;
      }

      public static void processPurchase(Player p, ShopUtil.ShopEntry entry, int buttonId) {
            if (entry == null) {
                  MainLogger.getLogger().warning("Crystals base shop button index out of range: " + buttonId);
            } else {
                  int cost = entry.getCost();
                  if (p.getExperienceLevel() < cost) {
                        p.sendMessage(Language.translate("not_enough_xp", p));
                  } else if (!p.getInventory().canAddItem(entry.getItem())) {
                        p.sendMessage(Language.translate("full_inv", p));
                  } else {
                        p.setExperience(0, p.getExperienceLevel() - cost);
                        p.getInventory().addItem(new Item[]{entry.getItem()});
                        p.sendMessage(Language.translate("buy_success", p, entry.getItem().getName()));
                  }
            }
      }

      public static List getShopItems() {
            return shopItems;
      }

      public static List getPotionShopItems() {
            return potionShopItems;
      }

      static {
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(0, 1), 10, "Protection I", "https://i.imgur.com/dW9YdKM.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(0, 2), 20, "Protection II", "https://i.imgur.com/dW9YdKM.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(0, 3), 30, "Protection III", "https://i.imgur.com/dW9YdKM.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(1, 1), 5, "Fire Protection I", "https://i.imgur.com/dW9YdKM.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(1, 2), 15, "Fire Protection II", "https://i.imgur.com/dW9YdKM.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(1, 3), 25, "Fire Protection III", "https://i.imgur.com/dW9YdKM.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(4, 1), 10, "Projectile Protection I", "https://i.imgur.com/dW9YdKM.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(4, 3), 30, "Projectile Protection III", "https://i.imgur.com/dW9YdKM.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(9, 1), 5, "Sharpness I", "https://i.imgur.com/nnX2h23.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(9, 2), 10, "Sharpness II", "https://i.imgur.com/nnX2h23.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(9, 3), 20, "Sharpness III", "https://i.imgur.com/nnX2h23.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(13, 1), 15, "Fire Aspect I", "https://i.imgur.com/nnX2h23.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(13, 2), 20, "Fire Aspect II", "https://i.imgur.com/nnX2h23.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(12, 1), 5, "Knockback I", "https://i.imgur.com/nnX2h23.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(12, 2), 10, "Knockback II", "https://i.imgur.com/nnX2h23.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(19, 1), 5, "Power I", "https://d1u5p3l4wpay3k.cloudfront.net/minecraft_gamepedia/6/65/Bow.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(19, 2), 10, "Power II", "https://d1u5p3l4wpay3k.cloudfront.net/minecraft_gamepedia/6/65/Bow.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(19, 3), 25, "Power III", "https://d1u5p3l4wpay3k.cloudfront.net/minecraft_gamepedia/6/65/Bow.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(19, 4), 40, "Power IV", "https://d1u5p3l4wpay3k.cloudfront.net/minecraft_gamepedia/6/65/Bow.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(20, 1), 15, "Punch I", "https://d1u5p3l4wpay3k.cloudfront.net/minecraft_gamepedia/6/65/Bow.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(20, 2), 30, "Punch II", "https://d1u5p3l4wpay3k.cloudfront.net/minecraft_gamepedia/6/65/Bow.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(22, 1), 20, "Infinity I", "https://d1u5p3l4wpay3k.cloudfront.net/minecraft_gamepedia/6/65/Bow.png"));
            shopEntries.add(new ShopUtil.ShopEntry(enchantBook(21, 1), 20, "Flame I", "https://d1u5p3l4wpay3k.cloudfront.net/minecraft_gamepedia/6/65/Bow.png"));
            shopEntries.add(new ShopUtil.ShopEntry(Item.get(336), 10, "Bricks", "https://d1u5p3l4wpay3k.cloudfront.net/minecraft_gamepedia/f/ff/Bricks.png"));
            shopEntries.add(new ShopUtil.ShopEntry(Item.get(261), 40, "Teleport Bow", "https://d1u5p3l4wpay3k.cloudfront.net/minecraft_gamepedia/6/65/Bow.png"));
            shopEntries.add(new ShopUtil.ShopEntry(Item.get(130), 20, "Team Chest", "https://vignette.wikia.nocookie.net/minecraftuniverse/images/f/f0/Ender_Chest.png/revision/latest?cb=20120904164739"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(373, 29), 40, "Regeneration", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/p/o/potion_blue_1.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(373, 30), 50, "REgeneration II", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/p/o/potion_blue_1.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(373, 15), 20, "Speed", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/p/o/potion_blue_1.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(373, 32), 40, "Strength", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/p/o/potion_blue_1.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(373, 33), 50, "Strength II", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/p/o/potion_blue_1.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 28), 40, "Regeneration", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 30), 50, "Regeneration II", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 14), 30, "Speed", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 16), 35, "Speed II", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 21), 25, "Instant Health", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 22), 35, "Instant Health II", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 31), 50, "Strength", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 33), 60, "Strength II", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 34), 25, "Weakness", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 35), 35, "Weakness II", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 25), 25, "Poison", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 27), 35, "Poison", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 23), 40, "Harming", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            potionShopEntries.add(new ShopUtil.ShopEntry(Item.get(438, 24), 50, "Harming II", "http://www.blocksandgold.com//media/catalog/product/cache/3/image/200x/6cffa5908a86143a54dc6ad9b8a7c38e/s/p/splash_red.png"));
            Iterator var0 = shopEntries.iterator();

            ShopUtil.ShopEntry entry;
            while(var0.hasNext()) {
                  entry = (ShopUtil.ShopEntry)var0.next();
                  shopItems.add(new ElementButton(entry.getName() + "\n" + TextFormat.YELLOW + "Cost: " + TextFormat.AQUA + entry.getCost() + " XP", new ElementButtonImageData("url", entry.getIcon())));
            }

            var0 = potionShopEntries.iterator();

            while(var0.hasNext()) {
                  entry = (ShopUtil.ShopEntry)var0.next();
                  potionShopItems.add(new ElementButton(entry.getName() + "\n" + TextFormat.YELLOW + "Cost: " + TextFormat.AQUA + entry.getCost() + " XP", new ElementButtonImageData("url", entry.getIcon())));
            }

      }

      public static class ShopEntry {
            private final Item item;
            private final int cost;
            private final String name;
            private final String icon;

            @ConstructorProperties({"item", "cost", "name", "icon"})
            public ShopEntry(Item item, int cost, String name, String icon) {
                  this.item = item;
                  this.cost = cost;
                  this.name = name;
                  this.icon = icon;
            }

            public Item getItem() {
                  return this.item;
            }

            public int getCost() {
                  return this.cost;
            }

            public String getName() {
                  return this.name;
            }

            public String getIcon() {
                  return this.icon;
            }
      }
}
