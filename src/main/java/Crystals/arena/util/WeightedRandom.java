package Crystals.arena.util;

import java.util.Iterator;
import java.util.Random;

public final class WeightedRandom {
      public static WeightedRandom.Choice getRandom(Random random, Iterable possibilities) {
            int weights = 0;

            WeightedRandom.Choice possibility;
            for(Iterator var3 = possibilities.iterator(); var3.hasNext(); weights += possibility.getWeight()) {
                  possibility = (WeightedRandom.Choice)var3.next();
            }

            int restWeight = random.nextInt(weights);
            Iterator var7 = possibilities.iterator();

            WeightedRandom.Choice t;
            do {
                  if (!var7.hasNext()) {
                        return null;
                  }

                  t = (WeightedRandom.Choice)var7.next();
                  restWeight -= t.getWeight();
            } while(restWeight >= 0);

            return t;
      }

      public interface Choice {
            int getWeight();
      }
}
