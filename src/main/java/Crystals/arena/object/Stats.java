package Crystals.arena.object;

import java.util.Map;

public class Stats {
      public int kills = 0;
      public int deaths = 0;
      public int wins = 0;
      public int losses = 0;
      public int crystals = 0;
      public int crystalRevive = 0;
      public int killsDelta = 0;
      public int deathsDelta = 0;
      public int winsDelta = 0;
      public int lossesDelta = 0;
      public int crystalsDelta = 0;
      public int crystalReviveDelta = 0;

      public void init(Map data) {
            this.kills = (Integer)data.get("kills");
            this.deaths = (Integer)data.get("deaths");
            this.wins = (Integer)data.get("wins");
            this.losses = (Integer)data.get("losses");
            this.crystals = (Integer)data.get("crystals");
            this.crystalRevive = (Integer)data.get("crystalsrevived");
            this.killsDelta = 0;
            this.deathsDelta = 0;
            this.winsDelta = 0;
            this.lossesDelta = 0;
            this.crystalsDelta = 0;
            this.crystalReviveDelta = 0;
      }

      public int getDeaths() {
            return this.deaths + this.deathsDelta;
      }

      public int getKills() {
            return this.kills + this.killsDelta;
      }

      public int getWins() {
            return this.wins + this.winsDelta;
      }

      public int getLosses() {
            return this.losses + this.lossesDelta;
      }

      public int getCrystalRevive() {
            return this.crystalRevive + this.crystalReviveDelta;
      }

      public int getCrystals() {
            return this.crystals + this.crystalsDelta;
      }
}
