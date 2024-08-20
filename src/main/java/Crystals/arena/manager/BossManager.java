package Crystals.arena.manager;

import Crystals.arena.Arena;
import Crystals.arena.object.Team;
import Crystals.arena.util.Utils;
import Crystals.entity.EntityWitherBoss;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.potion.Effect;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class BossManager {
      public Arena plugin;
      private final List rewards = new ArrayList();

      public BossManager(Arena arena) {
            this.plugin = arena;
            this.rewards.add(new BossManager.BossReward(true, "br_xp", (p, t) -> {
                  p.setExperience(p.getExperience(), p.getExperienceLevel() + 400);
            }));
            this.rewards.add(new BossManager.BossReward(true, "br_speed", (p, t) -> {
                  p.addEffect(Effect.getEffect(1).setAmplifier(1).setDuration(6000));
            }));
            this.rewards.add(new BossManager.BossReward(true, "br_regen", (p, t) -> {
                  p.addEffect(Effect.getEffect(10).setAmplifier(1).setDuration(6000));
            }));
            this.rewards.add(new BossManager.BossReward(true, "br_haste", (p, t) -> {
                  p.addEffect(Effect.getEffect(3).setAmplifier(1).setDuration(6000));
            }));
            this.rewards.add(new BossManager.BossReward(false, "br_dxp", (p, t) -> {
                  t.setDoubleXp(6000);
            }));
            this.rewards.add(new BossManager.BossReward(true, "br_str", (p, t) -> {
                  p.addEffect(Effect.getEffect(5).setAmplifier(1).setDuration(1200));
            }));
            this.rewards.add(new BossManager.BossReward(true, "br_diamonds", (p, t) -> {
                  p.getInventory().addItem(new Item[]{Item.get(264, 0, 5)});
            }));
            this.rewards.add(new BossManager.BossReward(false, "br_immune", (p, t) -> {
                  t.getCrystal().setImmune(1800);
            }));
      }

      public void spawnBosses() {
            this.plugin.messageAllPlayers("boss_respawn", false, new String[0]);
            Iterator var1 = this.plugin.data.bosses.entrySet().iterator();

            while(var1.hasNext()) {
                  Entry bossEntry = (Entry)var1.next();
                  Vector3 pos = (Vector3)bossEntry.getValue();
                  String team = (String)bossEntry.getKey();
                  int chunkX = pos.getFloorX() >> 4;
                  int chunkZ = pos.getFloorZ() >> 4;
                  if (!this.plugin.level.isChunkLoaded(chunkX, chunkZ)) {
                        this.plugin.level.loadChunk(chunkX, chunkZ);
                  }

                  BaseFullChunk chunk = this.plugin.level.getChunk(chunkX, chunkZ);
                  Iterator var8 = chunk.getEntities().values().iterator();

                  while(var8.hasNext()) {
                        Entity entity = (Entity)var8.next();
                        if (entity instanceof EntityWitherBoss) {
                              entity.close();
                        }
                  }

                  Entity boss = new EntityWitherBoss(chunk, Utils.getNbt(pos).putInt("CTeam", this.plugin.getTeam(team).getIndex()));
                  boss.setPosition(pos);
                  boss.spawnToAll();
                  GTCore.utils.Utils.addSound("mob.wither.spawn", boss, this.plugin.level.getPlayers().values());
            }

      }

      public void applyReward(Team team) {
            BossManager.BossReward reward = (BossManager.BossReward)this.rewards.get((new Random()).nextInt(this.rewards.size()));
            if (reward.isPlayers()) {
                  team.getPlayers().values().forEach((p) -> {
                        reward.reward.accept(p, team);
                  });
            } else {
                  reward.getReward().accept((Object)null, team);
            }

            this.plugin.messageAllPlayers(reward.getTransaltion(), new String[]{team.getColor() + team.getName()});
      }

      private static class BossReward {
            private final boolean players;
            private final String transaltion;
            private final BiConsumer reward;

            @ConstructorProperties({"players", "transaltion", "reward"})
            public BossReward(boolean players, String transaltion, BiConsumer reward) {
                  this.players = players;
                  this.transaltion = transaltion;
                  this.reward = reward;
            }

            public boolean isPlayers() {
                  return this.players;
            }

            public String getTransaltion() {
                  return this.transaltion;
            }

            public BiConsumer getReward() {
                  return this.reward;
            }
      }
}
