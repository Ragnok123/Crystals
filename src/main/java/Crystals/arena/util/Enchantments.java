package Crystals.arena.util;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Enchantments {
      private static HashMap armorValues = new HashMap() {
            {
                  this.put(298, 1.0F);
                  this.put(299, 3.0F);
                  this.put(300, 2.0F);
                  this.put(301, 1.0F);
                  this.put(302, 1.0F);
                  this.put(303, 5.0F);
                  this.put(304, 4.0F);
                  this.put(305, 1.0F);
                  this.put(314, 1.0F);
                  this.put(315, 5.0F);
                  this.put(316, 3.0F);
                  this.put(317, 1.0F);
                  this.put(306, 2.0F);
                  this.put(307, 6.0F);
                  this.put(308, 5.0F);
                  this.put(309, 2.0F);
                  this.put(310, 3.0F);
                  this.put(311, 8.0F);
                  this.put(312, 6.0F);
                  this.put(313, 3.0F);
            }
      };
      private static int[] toolBase = new int[]{15};
      private static int[] tool = new int[]{18, 16};
      private static int[] swordBase = new int[]{9, 9, 11, 10};
      private static int[] sword = new int[]{12, 13, 14};
      private static int[] bow = new int[]{22, 21, 20};
      private static int[] bowBase = new int[]{19, 19, 19, 21, 20};
      private static int[] armorBase = new int[]{0, 0, 0, 3, 2, 1, 4};
      private static int[] armor = new int[]{5};
      private static int[] helmet = new int[]{6};
      private static int[] boots = new int[]{7};
      private static int[] all = new int[]{17};

      public static boolean enchantItem(Item item, int level) {
            ArrayList enchantments = new ArrayList();
            if (item.hasEnchantments()) {
                  return false;
            } else {
                  Enchantment base;
                  int count;
                  int i;
                  Enchantment additional;
                  boolean same;
                  Iterator var8;
                  Enchantment id;
                  if (item.isTool()) {
                        if (item.isSword()) {
                              base = Enchantment.get(randEnchant(swordBase));
                              base.setLevel(1);
                              if (level > 10 && level < 15) {
                                    base.setLevel(2);
                              } else if (level > 15 && level < 25) {
                                    base.setLevel(3);
                              } else if (level >= 30) {
                                    base.setLevel(4);
                              }

                              enchantments.add(base);
                              if (level >= 15) {
                                    count = 1;
                                    if (level >= 30) {
                                          count = Utils.rand(1, 2);
                                    }

                                    for(i = 0; i < count; ++i) {
                                          additional = Enchantment.get(randEnchant(sword));
                                          same = false;
                                          var8 = enchantments.iterator();

                                          while(var8.hasNext()) {
                                                id = (Enchantment)var8.next();
                                                if (id.getId() == additional.getId()) {
                                                      same = true;
                                                      break;
                                                }
                                          }

                                          if (!same) {
                                                if (level > 20) {
                                                      additional.setLevel(additional.getMaxLevel());
                                                } else {
                                                      additional.setLevel((int)Math.ceil((double)(additional.getMaxLevel() / 2)));
                                                }

                                                enchantments.add(additional);
                                          }
                                    }

                                    if (Utils.rand(0, 1) == 0) {
                                          enchantments.add(Enchantment.get(randEnchant(all)));
                                    }
                              } else if (Utils.rand(0, 2) == 0) {
                                    enchantments.add(Enchantment.get(randEnchant(all)));
                              }
                        } else if (item.getId() == 261) {
                              base = Enchantment.get(randEnchant(bowBase));
                              base.setLevel(1);
                              if (level > 10 && level < 15) {
                                    base.setLevel(2);
                              } else if (level > 15 && level < 25) {
                                    base.setLevel(Math.min(3, base.getMaxEnchantableLevel()));
                              } else if (level >= 30) {
                                    base.setLevel(Math.min(4, base.getMaxEnchantableLevel()));
                              }

                              enchantments.add(base);
                              if (level >= 15) {
                                    count = Utils.rand(1, level >= 30 ? 3 : 2);

                                    for(i = 0; i < count; ++i) {
                                          additional = Enchantment.get(randEnchant(bow));
                                          same = false;
                                          var8 = enchantments.iterator();

                                          while(var8.hasNext()) {
                                                id = (Enchantment)var8.next();
                                                if (id.getId() == additional.getId()) {
                                                      same = true;
                                                      break;
                                                }
                                          }

                                          if (!same) {
                                                if (level > 20 && Utils.rand(0, 1) == 0) {
                                                      additional.setLevel(additional.getMaxLevel());
                                                } else {
                                                      additional.setLevel((int)Math.ceil((double)(additional.getMaxLevel() / 2)));
                                                }

                                                enchantments.add(additional);
                                          }
                                    }

                                    if (Utils.rand(0, 1) == 0) {
                                          enchantments.add(Enchantment.get(randEnchant(all)));
                                    }
                              } else if (Utils.rand(0, 2) == 0) {
                                    enchantments.add(Enchantment.get(randEnchant(all)));
                              }
                        } else {
                              base = Enchantment.get(randEnchant(toolBase));
                              base.setLevel(1);
                              if (level > 10 && level < 15) {
                                    base.setLevel(2);
                              } else if (level > 15 && level < 25) {
                                    base.setLevel(3);
                              } else if (level >= 30) {
                                    base.setLevel(4);
                              }

                              enchantments.add(base);
                              if (level < 15) {
                                    if (Utils.rand(0, 2) == 0) {
                                          enchantments.add(Enchantment.get(randEnchant(all)));
                                    }
                              } else {
                                    count = 1;
                                    if (level >= 30) {
                                          count = Utils.rand(1, 2);
                                    }

                                    i = 0;

                                    while(true) {
                                          if (i >= count) {
                                                if (Utils.rand(0, 1) == 0) {
                                                      enchantments.add(Enchantment.get(randEnchant(all)));
                                                }
                                                break;
                                          }

                                          additional = Enchantment.get(randEnchant(tool));
                                          same = false;
                                          var8 = enchantments.iterator();

                                          while(var8.hasNext()) {
                                                id = (Enchantment)var8.next();
                                                if (id.getId() == additional.getId()) {
                                                      same = true;
                                                      break;
                                                }
                                          }

                                          if (!same) {
                                                if (level > 20) {
                                                      additional.setLevel(additional.getMaxLevel());
                                                } else {
                                                      additional.setLevel((int)Math.ceil((double)(additional.getMaxLevel() / 2)));
                                                }

                                                enchantments.add(additional);
                                          }

                                          ++i;
                                    }
                              }

                              if (Utils.rand(0, 1) == 0) {
                                    Enchantment special;
                                    if (item.isHelmet()) {
                                          special = Enchantment.get(randEnchant(helmet));
                                          special.setLevel(1);
                                          enchantments.add(special);
                                    } else if (item.isBoots()) {
                                          special = Enchantment.get(randEnchant(boots));
                                          special.setLevel(1);
                                          if (level >= 20) {
                                                special.setLevel(Utils.rand(1, special.getMaxLevel()));
                                          } else if (level >= 13 && Utils.rand(0, 3) == 0) {
                                                special.setLevel(special.getMaxLevel());
                                          }

                                          enchantments.add(special);
                                    }
                              }
                        }
                  } else {
                        if (!item.isArmor()) {
                              return false;
                        }

                        base = Enchantment.get(randEnchant(armorBase));
                        base.setLevel(1);
                        if (level > 10 && level < 15) {
                              base.setLevel(2);
                        } else if (level > 15 && level < 25) {
                              base.setLevel(3);
                        } else if (level >= 30) {
                              base.setLevel(4);
                        }

                        enchantments.add(base);
                        if (level >= 15) {
                              count = 1;
                              if (level >= 30) {
                                    count = Utils.rand(1, 2);
                              }

                              for(i = 0; i < count; ++i) {
                                    additional = Enchantment.get(randEnchant(armor));
                                    same = false;
                                    var8 = enchantments.iterator();

                                    while(var8.hasNext()) {
                                          id = (Enchantment)var8.next();
                                          if (id.getId() == additional.getId()) {
                                                same = true;
                                                break;
                                          }
                                    }

                                    if (!same) {
                                          if (level > 20) {
                                                additional.setLevel(additional.getMaxLevel());
                                          } else {
                                                additional.setLevel((int)Math.ceil((double)(additional.getMaxLevel() / 2)));
                                          }

                                          enchantments.add(additional);
                                    }
                              }

                              if (Utils.rand(0, 1) == 0) {
                                    enchantments.add(Enchantment.get(randEnchant(all)));
                              }
                        } else if (Utils.rand(0, 2) == 0) {
                              enchantments.add(Enchantment.get(randEnchant(all)));
                        }
                  }

                  item.addEnchantment((Enchantment[])enchantments.stream().toArray((x$0) -> {
                        return new Enchantment[x$0];
                  }));
                  return true;
            }
      }

      private static int randEnchant(int[] a) {
            return a.length == 0 ? 0 : a[(new Random()).nextInt(a.length)];
      }
}
