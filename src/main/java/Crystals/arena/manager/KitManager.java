package Crystals.arena.manager;

import Crystals.Crystals;
import Crystals.arena.Arena;
import Crystals.arena.kit.Kit;
import Crystals.arena.object.PlayerData;
import Crystals.lang.Language;
import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSwordDiamond;
import cn.nukkit.utils.TextFormat;

public class KitManager {
      public Arena plugin;

      public KitManager(Arena plugin) {
            this.plugin = plugin;
      }

      public void onKitChange(Player p, Kit kit) {
            if (kit == Kit.WARRIOR) {
                  p.sendMessage(TextFormat.RED + "Tento kit je prozatim mimo provoz");
            } else {
                  PlayerData data = this.plugin.getPlayerData(p);
                  if (!data.hasKit(kit) && !p.hasPermission("gameteam.vip")) {
                        p.sendMessage(Crystals.getPrefix() + Language.translate("has_not_kit", data.getBaseData()));
                  } else {
                        data.setNewKit(kit);
                        if (kit == Kit.BERSERKER) {
                              p.setMaxHealth(14);
                        } else {
                              p.setMaxHealth(20);
                        }

                        p.sendMessage(Crystals.getPrefix() + Language.translate("kit_select", data.getBaseData(), kit.getName()));
                        p.attack(new EntityDamageEvent(p, DamageCause.CUSTOM, 99999.0F));
                  }
            }
      }

      public void addKitSelectItem(Player p) {
            p.getInventory().setItem(1, (new ItemSwordDiamond()).setCustomName(Language.translate("kit_title", p)));
            p.getInventory().setItem(2, Item.get(35).setCustomName(Language.translate("team_select", p)));
      }

      public void giveKit(Player p) {
            PlayerData data = this.plugin.getPlayerData(p);
            Kit kit = data.getNewKit();
            data.setKit(kit);
            PlayerInventory inv = p.getInventory();
            inv.clearAll();
            kit.give(p);
            Item[] armor = data.getTeam().getArmor();
            inv.setHelmet(armor[0]);
            inv.setChestplate(armor[1]);
            inv.setLeggings(armor[2]);
            inv.setBoots(armor[3]);
            inv.sendContents(p);
            inv.sendArmorContents(p);
      }

      public void itemHeld(Player p, int slot) {
            if (slot < Kit.values().length && slot >= 0) {
                  this.onKitChange(p, Kit.values()[slot]);
            }

      }

      public void buyKit(Player p, Kit kit) {
            PlayerData data = this.plugin.getPlayerData(p);
            if (data.hasPurchasedKit(kit)) {
                  p.sendMessage(Language.translate("kit_already", data.getBaseData()));
            } else if (data.getBaseData().getMoney() < kit.getCost()) {
                  p.sendMessage(Language.translate("low_money", data.getBaseData()));
            } else {
                  p.sendMessage(Language.translate("buy_kit", data.getBaseData(), kit.getName()));
                  data.getKits().add(kit);
                  data.getBaseData().addMoney(-kit.getCost());
            }

      }
}
