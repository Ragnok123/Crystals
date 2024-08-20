package Crystals.arena.manager;

import Crystals.Crystals;
import Crystals.arena.Arena;
import Crystals.lang.Language;
import cn.nukkit.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.apache.commons.lang.math.NumberUtils;

public class VotingManager {
      public Arena plugin;
      public HashMap players = new HashMap();
      public String[] currentTable = new String[4];
      public ArrayList allVotes;
      public HashMap stats;

      public VotingManager(Arena plugin) {
            this.allVotes = new ArrayList(Crystals.getInstance().maps.keySet());
            this.plugin = plugin;
      }

      public void createVoteTable() {
            ArrayList all = new ArrayList(this.allVotes);

            for(int i = 0; i < 3; ++i) {
                  int key = (new Random()).nextInt(all.size());
                  this.currentTable[i] = (String)all.get(key);
                  all.remove(key);
            }

            this.stats = new HashMap();
            this.stats.put(this.currentTable[0], 0);
            this.stats.put(this.currentTable[1], 0);
            this.stats.put(this.currentTable[2], 0);
            this.players.clear();
      }

      public void onVote(Player p, String vote) {
            if (this.plugin.phase != Arena.GamePhase.GAME && this.plugin.inArena(p)) {
                  if (NumberUtils.isNumber(vote)) {
                        int voteInt = Integer.valueOf(vote);
                        if (voteInt < 1 || Integer.valueOf(vote) > this.currentTable.length) {
                              p.sendMessage(Crystals.getPrefix() + Language.translate("use_vote", p));
                              return;
                        }

                        if (this.players.containsKey(p.getName().toLowerCase())) {
                              this.stats.put(this.players.get(p.getName().toLowerCase()), (Integer)this.stats.get(this.players.get(p.getName().toLowerCase())) - 1);
                        }

                        this.stats.put(this.currentTable[Integer.valueOf(vote) - 1], (Integer)this.stats.get(this.currentTable[Integer.valueOf(vote) - 1]) + 1);
                        this.players.put(p.getName().toLowerCase(), this.currentTable[Integer.valueOf(vote) - 1]);
                        p.sendMessage(Crystals.getPrefix() + Language.translate("vote", p, this.currentTable[Integer.valueOf(vote) - 1]));
                        this.plugin.barUtil.updateVotes();
                  } else {
                        if (!vote.toLowerCase().equals(this.currentTable[0].toLowerCase()) && !vote.toLowerCase().equals(this.currentTable[1].toLowerCase()) && !vote.toLowerCase().equals(this.currentTable[2].toLowerCase())) {
                              p.sendMessage(Crystals.getPrefix() + Language.translate("use_vote", p));
                              return;
                        }

                        if (this.players.containsKey(p.getName().toLowerCase())) {
                              this.stats.put(this.players.get(p.getName().toLowerCase()), (Integer)this.stats.get(this.players.get(p.getName().toLowerCase())) - 1);
                        }

                        String finall = Character.toUpperCase(vote.charAt(0)) + vote.substring(1).toLowerCase();
                        this.stats.put(finall, (Integer)this.stats.get(finall) + 1);
                        this.players.put(p.getName().toLowerCase(), finall);
                        p.sendMessage(Crystals.getPrefix() + Language.translate("vote", p, vote));
                        this.plugin.barUtil.updateVotes();
                  }

            } else {
                  p.sendMessage(Crystals.getPrefix() + Language.translate("can_not_vote", p));
            }
      }
}
