package Crystals.arena.object;

import Crystals.arena.VirtualInventory;
import Crystals.arena.kit.Kit;
import Crystals.entity.CombatLoggerNPC;
import GTCore.Object.Rank;
import cn.nukkit.Player;
import java.util.HashSet;
import java.util.Set;

public class PlayerData {
      private String name;
      private Kit kit;
      private Team team;
      private boolean inLobby;
      private Kit newKit;
      private PlayerData killer;
      private long time;
      private boolean wasInGame;
      public VirtualInventory inventory;
      private GTCore.Object.PlayerData baseData;
      private Set kits;
      public Stats stats;
      public int currentCompassTeam;
      public CombatLoggerNPC npc;
      public boolean npcKilled;

      public PlayerData(String name, GTCore.Object.PlayerData pdata) {
            this.kit = Kit.CIVILIAN;
            this.team = null;
            this.inLobby = true;
            this.newKit = Kit.CIVILIAN;
            this.time = 0L;
            this.wasInGame = false;
            this.inventory = null;
            this.kits = new HashSet();
            this.stats = null;
            this.currentCompassTeam = 0;
            this.npc = null;
            this.npcKilled = false;
            this.name = name;
            this.newKit = Kit.CIVILIAN;
            this.baseData = pdata;
      }

      public boolean wasInGame() {
            return this.wasInGame && this.team != null;
      }

      public void setInGame() {
            this.wasInGame = true;
      }

      public PlayerData wasKilled() {
            return System.currentTimeMillis() - this.time <= 10000L && this.killer != null ? this.killer : null;
      }

      public VirtualInventory getSavedInventory() {
            return this.inventory;
      }

      public void saveInventory(Player p) {
            this.inventory = new VirtualInventory(p);
            if (this.kit == Kit.BERSERKER) {
                  this.inventory.maxHealth = p.getMaxHealth();
            }

      }

      public void removeInventory() {
            this.inventory = null;
      }

      public void setKiller(PlayerData p) {
            this.killer = p;
            this.time = System.currentTimeMillis();
      }

      public boolean hasKit(Kit kit) {
            return this.getBaseData().getRank() != Rank.PLAYER || this.kits.contains(kit);
      }

      public boolean hasPurchasedKit(Kit kit) {
            return this.kits.contains(kit);
      }

      public Player getPlayer() {
            return this.baseData.getPlayer();
      }

      public String getName() {
            return this.name;
      }

      public Kit getKit() {
            return this.kit;
      }

      public void setKit(Kit kit) {
            this.kit = kit;
      }

      public Team getTeam() {
            return this.team;
      }

      public void setTeam(Team team) {
            this.team = team;
      }

      public void setInLobby(boolean inLobby) {
            this.inLobby = inLobby;
      }

      public boolean isInLobby() {
            return this.inLobby;
      }

      public Kit getNewKit() {
            return this.newKit;
      }

      public void setNewKit(Kit newKit) {
            this.newKit = newKit;
      }

      public long getTime() {
            return this.time;
      }

      public void setTime(long time) {
            this.time = time;
      }

      public GTCore.Object.PlayerData getBaseData() {
            return this.baseData;
      }

      public void setBaseData(GTCore.Object.PlayerData baseData) {
            this.baseData = baseData;
      }

      public Set getKits() {
            return this.kits;
      }
}
