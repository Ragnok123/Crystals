package Crystals.arena.util;

import Crystals.arena.object.Team;
import cn.nukkit.item.ItemFireworks;
import cn.nukkit.item.ItemFireworks.FireworkExplosion;
import cn.nukkit.item.ItemFireworks.FireworkExplosion.ExplosionType;
import cn.nukkit.utils.DyeColor;
import java.util.ArrayList;
import java.util.List;

public class FireworkUtils {
      public static List BLUE = new ArrayList();
      public static List RED = new ArrayList();
      public static List GREEN = new ArrayList();
      public static List YELLOW = new ArrayList();

      public static void init() {
            ItemFireworks blue = new ItemFireworks();
            new ItemFireworks();
            new ItemFireworks();
            new ItemFireworks();
            blue.addExplosion((new FireworkExplosion()).addColor(DyeColor.BLUE).addColor(DyeColor.LIGHT_BLUE).addFade(DyeColor.GREEN).trail(true).flicker(true).type(ExplosionType.LARGE_BALL));
            BLUE.add(blue);
            blue = new ItemFireworks();
            blue.addExplosion((new FireworkExplosion()).addColor(DyeColor.BLUE).addColor(DyeColor.LIGHT_BLUE).addFade(DyeColor.GREEN).trail(true).flicker(true).type(ExplosionType.BURST));
            BLUE.add(blue);
            blue = new ItemFireworks();
            blue.addExplosion((new FireworkExplosion()).addColor(DyeColor.BLUE).addColor(DyeColor.LIGHT_BLUE).addFade(DyeColor.GREEN).trail(true).flicker(true).type(ExplosionType.STAR_SHAPED));
            BLUE.add(blue);
            blue = new ItemFireworks();
            blue.addExplosion((new FireworkExplosion()).addColor(DyeColor.BLUE).addColor(DyeColor.CYAN).addFade(DyeColor.LIGHT_BLUE).addFade(DyeColor.LIME).trail(true).flicker(true).type(ExplosionType.LARGE_BALL));
            BLUE.add(blue);
            ItemFireworks red = new ItemFireworks();
            red.addExplosion((new FireworkExplosion()).addColor(DyeColor.RED).addColor(DyeColor.ORANGE).addFade(DyeColor.YELLOW).trail(true).flicker(true).type(ExplosionType.LARGE_BALL));
            RED.add(red);
            red = new ItemFireworks();
            red.addExplosion((new FireworkExplosion()).addColor(DyeColor.RED).addColor(DyeColor.ORANGE).addFade(DyeColor.YELLOW).trail(true).flicker(true).type(ExplosionType.BURST));
            RED.add(red);
            red = new ItemFireworks();
            red.addExplosion((new FireworkExplosion()).addColor(DyeColor.RED).addColor(DyeColor.ORANGE).addFade(DyeColor.YELLOW).trail(true).flicker(true).type(ExplosionType.STAR_SHAPED));
            RED.add(red);
            red = new ItemFireworks();
            red.addExplosion((new FireworkExplosion()).addColor(DyeColor.RED).addColor(DyeColor.PURPLE).addFade(DyeColor.ORANGE).addFade(DyeColor.YELLOW).trail(true).flicker(true).type(ExplosionType.LARGE_BALL));
            RED.add(red);
            ItemFireworks yellow = new ItemFireworks();
            yellow.addExplosion((new FireworkExplosion()).addColor(DyeColor.YELLOW).addColor(DyeColor.LIME).addFade(DyeColor.PINK).trail(true).flicker(true).type(ExplosionType.LARGE_BALL));
            YELLOW.add(yellow);
            yellow = new ItemFireworks();
            yellow.addExplosion((new FireworkExplosion()).addColor(DyeColor.YELLOW).addColor(DyeColor.LIME).addFade(DyeColor.PINK).trail(true).flicker(true).type(ExplosionType.BURST));
            YELLOW.add(yellow);
            yellow = new ItemFireworks();
            yellow.addExplosion((new FireworkExplosion()).addColor(DyeColor.YELLOW).addColor(DyeColor.LIME).addFade(DyeColor.PINK).trail(true).flicker(true).type(ExplosionType.STAR_SHAPED));
            YELLOW.add(yellow);
            yellow = new ItemFireworks();
            yellow.addExplosion((new FireworkExplosion()).addColor(DyeColor.YELLOW).addColor(DyeColor.PINK).addFade(DyeColor.ORANGE).addFade(DyeColor.LIME).trail(true).flicker(true).type(ExplosionType.LARGE_BALL));
            YELLOW.add(yellow);
            ItemFireworks green = new ItemFireworks();
            green.addExplosion((new FireworkExplosion()).addColor(DyeColor.GREEN).addColor(DyeColor.LIME).addFade(DyeColor.YELLOW).trail(true).flicker(true).type(ExplosionType.LARGE_BALL));
            GREEN.add(green);
            green = new ItemFireworks();
            green.addExplosion((new FireworkExplosion()).addColor(DyeColor.GREEN).addColor(DyeColor.LIME).addFade(DyeColor.YELLOW).trail(true).flicker(true).type(ExplosionType.BURST));
            GREEN.add(green);
            green = new ItemFireworks();
            green.addExplosion((new FireworkExplosion()).addColor(DyeColor.GREEN).addColor(DyeColor.LIME).addFade(DyeColor.YELLOW).trail(true).flicker(true).type(ExplosionType.STAR_SHAPED));
            GREEN.add(green);
            green = new ItemFireworks();
            green.addExplosion((new FireworkExplosion()).addColor(DyeColor.LIME).addColor(DyeColor.GREEN).addFade(DyeColor.LIGHT_BLUE).addFade(DyeColor.PINK).trail(true).flicker(true).type(ExplosionType.LARGE_BALL));
            GREEN.add(green);
      }

      public static List of(Team team) {
            switch(team.getIndex()) {
            case 0:
                  return BLUE;
            case 1:
                  return RED;
            case 2:
                  return YELLOW;
            case 3:
                  return GREEN;
            default:
                  return null;
            }
      }
}
