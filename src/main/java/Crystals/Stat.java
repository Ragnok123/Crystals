package Crystals;

public enum Stat {
      KILLS(50, 3),
      DEATHS(0, 0),
      WINS(1000, 60),
      LOSSES(0, 0),
      CRYSTALS(200, 30),
      CRYSTALS_REVIVE(150, 20);

      private String name;
      private int xp;
      private int tokens;

      private Stat(int xp, int tokens) {
            this.xp = xp;
            this.tokens = tokens;
            this.name = this.name().toLowerCase().trim();
      }

      public String getName() {
            return this.name;
      }

      public int getXp() {
            return this.xp;
      }

      public int getTokens() {
            return this.tokens;
      }
}
