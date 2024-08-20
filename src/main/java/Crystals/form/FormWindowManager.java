package Crystals.form;

import Crystals.arena.Arena;
import Crystals.arena.kit.Kit;
import Crystals.arena.object.PlayerData;
import Crystals.arena.object.Team;
import Crystals.arena.util.ShopUtil;
import Crystals.lang.Language;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.TextFormat;
import java.util.Iterator;

public class FormWindowManager {
      private static final int KIT_SELECT_ID = 92846738;
      private static final int KIT_BUY_ID = 92846739;
      private static final int KIT_CONFIRM_ID = 92846740;
      private static final int ENCHANT_ID = 92846741;
      private static final int POTION_ID = 92846742;
      private static final int TEAM_SELECT_ID = 92846743;
      private Arena plugin;

      public FormWindowManager(Arena plugin) {
            this.plugin = plugin;
      }

      public void addKitWindow(Player p) {
            PlayerData data = this.plugin.getPlayerData(p);
            FormWindowSimple formWindowCustom = new FormWindowSimple(Language.translate("kit_title", p), Language.translate("kit_desc", p));
            Kit[] var4 = Kit.values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                  Kit kit = var4[var6];
                  formWindowCustom.addButton(new ElementButton((data.hasKit(kit) ? TextFormat.DARK_GREEN : TextFormat.DARK_RED) + kit.getName(), new ElementButtonImageData("url", kit.getImageUrl())));
            }

            p.showFormWindow(formWindowCustom, 92846738);
      }

      public void addTeamSelectWindow(Player p) {
            FormWindowSimple window = new FormWindowSimple(Language.translate("team_select", p), "");
            Team[] var3 = this.plugin.teams;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                  Team team = var3[var5];
                  window.addButton(new ElementButton(team.getColor() + team.getName()));
            }

            p.showFormWindow(window, 92846743);
      }

      public void addEnchantShopWindow(Player p) {
            FormWindowSimple window = new FormWindowSimple(Language.translate("merchant", p), "");
            Iterator var3 = ShopUtil.getShopItems().iterator();

            while(var3.hasNext()) {
                  ElementButton btn = (ElementButton)var3.next();
                  window.addButton(btn);
            }

            p.showFormWindow(window, 92846741);
      }

      public void addPotionShopWindow(Player p) {
            FormWindowSimple window = new FormWindowSimple(Language.translate("merchant_pot", p), "");
            Iterator var3 = ShopUtil.getPotionShopItems().iterator();

            while(var3.hasNext()) {
                  ElementButton btn = (ElementButton)var3.next();
                  window.addButton(btn);
            }

            p.showFormWindow(window, 92846741);
      }

      public void handleResponse(Player p, int id, FormResponse formResponse) {
            if (formResponse instanceof FormResponseSimple) {
                  FormResponseSimple response = (FormResponseSimple)formResponse;
                  int buttonId = response.getClickedButtonId();
                  ShopUtil.ShopEntry entry;
                  switch(id) {
                  case 92846738:
                        Kit kit;
                        try {
                              kit = Kit.values()[response.getClickedButtonId()];
                        } catch (Exception var9) {
                              MainLogger.getLogger().logException(var9);
                              p.sendMessage(var9.getMessage());
                              return;
                        }

                        this.plugin.kitManager.onKitChange(p, kit);
                  case 92846739:
                  case 92846740:
                  default:
                        break;
                  case 92846741:
                        entry = ShopUtil.getItem(buttonId);
                        ShopUtil.processPurchase(p, entry, buttonId);
                        break;
                  case 92846742:
                        entry = ShopUtil.getItem(buttonId);
                        ShopUtil.processPurchase(p, entry, buttonId);
                        break;
                  case 92846743:
                        this.plugin.joinTeam(p, buttonId + 1);
                  }

            }
      }
}
