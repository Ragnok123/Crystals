package Crystals.entity;

import Crystals.Crystals;
import Crystals.arena.Arena;
import Crystals.arena.object.PlayerData;
import Crystals.entity.finder.PlayerFinder;
import Crystals.entity.hook.AdvancedAttackHook;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.entity.EntityDamageEvent.DamageModifier;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.hook.WanderHook;

public class CombatLoggerNPC extends MovingEntity implements Attackable {
      public Item[] armor = null;
      public List items = null;
      public Item itemInHand = null;
      public int team = -1;
      public int timer = 0;
      private String name = null;
      private UUID uuid = null;

      public CombatLoggerNPC(FullChunk chunk, CompoundTag nbt) {
            super(chunk, nbt);
            this.setTargetFinder(new PlayerFinder(this, 500L, 16));
            this.addHook("attack", new AdvancedAttackHook(this, 2.0D, this::getDamage, (i) -> {
                  if (!(i instanceof Player)) {
                        return false;
                  } else {
                        Player target = (Player)i;
                        Arena arena = this.getArena();
                        PlayerData data = arena.getPlayerData(target);
                        if (data != null && data.getTeam() != null) {
                              return this.getTeam() != data.getTeam().getIndex();
                        } else {
                              return false;
                        }
                  }
            }, 1000L, 10, 180.0D));
            this.addHook("wander", new WanderHook(this, 80));
      }

      public int getNetworkId() {
            return 32;
      }

      public float getWidth() {
            return 0.6F;
      }

      public float getLength() {
            return 0.6F;
      }

      public float getHeight() {
            return 1.62F;
      }

      public Item[] getDrops() {
            return new Item[0];
      }

      protected void initEntity() {
            super.initEntity();
            this.setMaxHealth(20);
      }

      public boolean onUpdate(int currentTick) {
            if (this.timer > 800) {
                  this.close();
                  return false;
            } else {
                  ++this.timer;
                  return super.onUpdate(currentTick);
            }
      }

      public boolean attack(EntityDamageEvent source) {
            if (source.getCause() != DamageCause.VOID && source.getCause() != DamageCause.CUSTOM && source.getCause() != DamageCause.MAGIC) {
                  if (source instanceof EntityDamageByEntityEvent) {
                        Entity damager = ((EntityDamageByEntityEvent)source).getDamager();
                        if (damager instanceof Player) {
                              PlayerData data = this.getArena().getPlayerData((Player)damager);
                              if (data == null || data.getTeam() == null || data.getTeam().getIndex() == this.getTeam()) {
                                    source.setCancelled();
                                    return false;
                              }
                        }
                  }

                  int points = 0;
                  int epf = 0;
                  int toughness = 0;
                  Item[] var5 = this.getArmor();
                  int var6 = var5.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                        Item armor = var5[var7];
                        points += armor.getArmorPoints();
                        epf = (int)((double)epf + this.calculateEnchantmentReduction(armor, source));
                        toughness += armor.getToughness();
                  }

                  float originalDamage = source.getDamage();
                  float finalDamage = (float)((double)(originalDamage * (1.0F - Math.max((float)(points / 5), (float)points - originalDamage / (float)(2 + toughness / 4)) / 25.0F)) * (1.0D - (double)epf * 0.04D));
                  source.setDamage(finalDamage - originalDamage, DamageModifier.ARMOR);
                  return super.attack(source);
            } else {
                  return false;
            }
      }

      public boolean targetOption(EntityCreature creature, double distance) {
            if (!(creature instanceof Player)) {
                  return false;
            } else if (creature.distanceSquared(this) > 256.0D) {
                  return false;
            } else {
                  Player target = (Player)creature;
                  Arena arena = this.getArena();
                  PlayerData data = arena.getPlayerData(target);
                  if (data != null && data.getTeam() != null) {
                        return this.getTeam() != data.getTeam().getIndex();
                  } else {
                        return false;
                  }
            }
      }

      public void spawnTo(Player player) {
            AddEntityPacket pk = new AddEntityPacket();
            pk.entityUniqueId = this.getId();
            pk.entityRuntimeId = this.getId();
            pk.type = this.getNetworkId();
            pk.x = (float)this.x;
            pk.y = (float)this.y;
            pk.z = (float)this.z;
            pk.speedX = (float)this.motionX;
            pk.speedY = (float)this.motionY;
            pk.speedZ = (float)this.motionZ;
            pk.metadata = this.dataProperties;
            player.dataPacket(pk);
            super.spawnTo(player);
            MobEquipmentPacket packet = new MobEquipmentPacket();
            packet.eid = this.getId();
            packet.inventorySlot = 0;
            packet.hotbarSlot = 0;
            packet.item = this.getItemInHand();
            player.dataPacket(packet);
            MobArmorEquipmentPacket packet1 = new MobArmorEquipmentPacket();
            packet1.eid = this.getId();
            packet1.slots = this.getArmor();
            player.dataPacket(packet1);
      }

      public int getTeam() {
            if (this.team == -1) {
                  this.team = this.namedTag.getInt("playerTeam");
            }

            return this.team;
      }

      public Item getItemInHand() {
            if (this.itemInHand == null) {
                  this.itemInHand = NBTIO.getItemHelper(this.namedTag.getCompound("playerItem"));
            }

            return this.itemInHand;
      }

      public Item[] getArmor() {
            if (this.armor == null) {
                  this.armor = new Item[4];
                  ListTag armorList = this.namedTag.getList("playerArmor", CompoundTag.class);

                  for(int i = 0; i < armorList.size(); ++i) {
                        this.armor[i] = NBTIO.getItemHelper((CompoundTag)armorList.get(i));
                  }
            }

            return this.armor;
      }

      public Item[] getItems() {
            if (this.items == null) {
                  this.items = new ArrayList();
                  ListTag armorList = this.namedTag.getList("playerItems", CompoundTag.class);

                  for(int i = 0; i < armorList.size(); ++i) {
                        this.items.add(NBTIO.getItemHelper((CompoundTag)armorList.get(i)));
                  }
            }

            return (Item[])this.items.stream().toArray((x$0) -> {
                  return new Item[x$0];
            });
      }

      public float getDamage(Entity entity) {
            Item item = this.getItemInHand();
            float damage = (float)item.getAttackDamage();
            Enchantment[] var4 = item.getEnchantments();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                  Enchantment enchantment = var4[var6];
                  damage = (float)((double)damage + enchantment.getDamageBonus(entity));
            }

            return damage;
      }

      protected double calculateEnchantmentReduction(Item item, EntityDamageEvent source) {
            if (!item.hasEnchantments()) {
                  return 0.0D;
            } else {
                  double reduction = 0.0D;
                  Enchantment[] var5 = item.getEnchantments();
                  int var6 = var5.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                        Enchantment ench = var5[var7];
                        reduction += (double)ench.getDamageProtection(source);
                  }

                  return reduction;
            }
      }

      public Arena getArena() {
            return ((Arena[])Crystals.getInstance().ins.values().stream().toArray((x$0) -> {
                  return new Arena[x$0];
            }))[0];
      }

      public String getPlayerName() {
            if (this.name == null) {
                  this.name = this.namedTag.getString("playerName");
            }

            return this.name;
      }

      public void attackEntity(Entity entity) {
            Enchantment[] var2 = this.getItemInHand().getEnchantments();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                  Enchantment enchantment = var2[var4];
                  enchantment.doPostAttack(this, entity);
            }

      }
}
