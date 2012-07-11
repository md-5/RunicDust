package net.minecraft.server.dustmod;

import forge.Configuration;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.DustShape;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.TileEntityDust;
import net.minecraft.server.dustmod.events.DEBait;
import net.minecraft.server.dustmod.events.DEBomb;
import net.minecraft.server.dustmod.events.DEBounce;
import net.minecraft.server.dustmod.events.DECage;
import net.minecraft.server.dustmod.events.DECampFire;
import net.minecraft.server.dustmod.events.DECompression;
import net.minecraft.server.dustmod.events.DEDawn;
import net.minecraft.server.dustmod.events.DEEarthSprite;
import net.minecraft.server.dustmod.events.DEEggifier;
import net.minecraft.server.dustmod.events.DEFarm;
import net.minecraft.server.dustmod.events.DEFireBowEnch;
import net.minecraft.server.dustmod.events.DEFireRain;
import net.minecraft.server.dustmod.events.DEFireSprite;
import net.minecraft.server.dustmod.events.DEFireTrap;
import net.minecraft.server.dustmod.events.DEFlatten;
import net.minecraft.server.dustmod.events.DEFog;
import net.minecraft.server.dustmod.events.DEForcefield;
import net.minecraft.server.dustmod.events.DEFortuneEnch;
import net.minecraft.server.dustmod.events.DEHeal;
import net.minecraft.server.dustmod.events.DEHideout;
import net.minecraft.server.dustmod.events.DEHunterVision;
import net.minecraft.server.dustmod.events.DELiftTerrain;
import net.minecraft.server.dustmod.events.DELightning;
import net.minecraft.server.dustmod.events.DELillyBridge;
import net.minecraft.server.dustmod.events.DELoyaltySprite;
import net.minecraft.server.dustmod.events.DELumberjack;
import net.minecraft.server.dustmod.events.DELunar;
import net.minecraft.server.dustmod.events.DEMiniTele;
import net.minecraft.server.dustmod.events.DEObelisk;
import net.minecraft.server.dustmod.events.DEPit;
import net.minecraft.server.dustmod.events.DEPoisonTrap;
import net.minecraft.server.dustmod.events.DEPowerRelay;
import net.minecraft.server.dustmod.events.DEResurrection;
import net.minecraft.server.dustmod.events.DESilkTouchEnch;
import net.minecraft.server.dustmod.events.DESpawnRecord;
import net.minecraft.server.dustmod.events.DESpawnTorch;
import net.minecraft.server.dustmod.events.DESpawnerCollector;
import net.minecraft.server.dustmod.events.DESpawnerReprog;
import net.minecraft.server.dustmod.events.DESpeed;
import net.minecraft.server.dustmod.events.DESpiritTool;
import net.minecraft.server.dustmod.events.DETeleportation;
import net.minecraft.server.dustmod.events.DETimeLock;
import net.minecraft.server.dustmod.events.DEVoid;
import net.minecraft.server.dustmod.events.DEWall;
import net.minecraft.server.dustmod.events.DEXP;
import net.minecraft.server.dustmod.events.DEXPStore;

public class DustManager {

   public static HashMap events = new HashMap();
   public static List names = new ArrayList();
   public static ArrayList shapes = new ArrayList();
   public static Configuration config;


   public static EntityDust initiate(DustShape var0, String var1, double var2, double var4, double var6, World var8, List var9, int[][] var10, String var11) {
      DustEvent var12 = (DustEvent)events.get(var1);
      if(var12 == null) {
         return null;
      } else {
         EntityDust var13 = new EntityDust(var8);
         var13.entityDustID = mod_DustMod.getNextDustEntityID().longValue();
         mod_DustMod.registerEntityDust(var13, var13.entityDustID);
         var13.setPosition(var2, var4 - 0.8D, var6);
         var13.dustPoints = var9;
         var13.dusts = var10;
         var13.summonerUN = var11 == null?"":var11;
         Iterator var14 = var9.iterator();

         while(var14.hasNext()) {
            Integer[] var15 = (Integer[])var14.next();
            TileEntityDust var16 = (TileEntityDust)var8.getTileEntity(var15[0].intValue(), var15[1].intValue(), var15[2].intValue());
            var16.setEntityDust(var13);
         }

         if(var0.solid) {
            boolean var20 = false;
            var13.dustID = -1;

            for(int var19 = 0; var19 < var10.length && !var20; ++var19) {
               for(int var21 = 0; var21 < var10[0].length && !var20; ++var21) {
                  int var17 = var10[var19][var21];
                  if(var17 != 0) {
                     var13.dustID = var17;
                     var20 = true;
                  }
               }
            }
         }

         var13.setEvent(var12, var1);
         var8.addEntity(var13);
         if((var12.allowed || ModLoader.getMinecraftServerInstance().serverConfigurationManager.isOp(var11)) && var12.permaAllowed) {
            var12.init(var13);
         } else {
            EntityHuman var18 = var8.a(var11);
            var13.reanimate = true;
            if(var18 != null) {
               var18.a("This rune is disabled on this server.");
            }
         }

         var13.dusts = (int[][])null;
         return var13;
      }
   }

   public static DustEvent get(String var0) {
      return (DustEvent)events.get(var0);
   }

   public static void add(String var0, DustEvent var1) {
      events.put(var0, var1);
      names.add(var0);
      var1.name = var0;
   }

   public static DustShape getShape(int var0) {
      return (DustShape)shapes.get(var0);
   }

   public static DustShape getShapeFromID(int var0) {
      Iterator var1 = shapes.iterator();

      DustShape var2;
      do {
         if(!var1.hasNext()) {
            return null;
         }

         var2 = (DustShape)var1.next();
      } while(var2.id != var0);

      return var2;
   }

   public static void registerAllShapes() {
      byte var0 = -1;
      byte var1 = 1;
      byte var2 = 2;
      byte var3 = 3;
      byte var4 = 4;
      DustShape var5 = new DustShape(4, 4, 1, "torch2", false, 0, 0, 0, 0, 0);
      add(var5.name, new DESpawnTorch());
      int[][][] var6 = new int[][][]{{{0, 0, 0, 0}, {0, var0, var0, 0}, {0, var0, var0, 0}, {0, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Torch Rune");
      var5.setNotes("Sacrifice:\n\n-None: Normal torch spawn.\n-1xFlint: Beacon rune.");
      var5.setDesc("Description:\n\nSpawns a torch or, if a piece of flint is sacrficed, a beacon.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(4, 4, 1, "rabbit", true, 0, 0, 0, 0, 44);
      add(var5.name, new DEHideout());
      var6 = new int[][][]{{{0, 0, var0, var0}, {0, 0, 0, var0}, {var0, 0, 0, 0}, {var0, var0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of the Rabbit Hole");
      var5.setNotes("Sacrifice:\n\n-2xHunger.\n\nNotes:\n\n-Variable determines the volume of the den.\n-Stepping on the rune will send you down.\n-Standing directly below and crouching will send you back up.");
      var5.setDesc("Description:\n\nCreates a small hole beneath the rune and allows you to jump inside for safety. Walking over the top will send you down to the next solid block below the rune. Pressing [crouch] while directly beneath the rune will bring you back up.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(8, 8, 1, "pit", true, 2, 2, 2, 2, 2);
      add(var5.name, new DEPit());
      var6 = new int[][][]{{{0, 0, var0, 0, 0, var0, 0, 0}, {0, var0, var0, 0, 0, var0, var0, 0}, {var0, var0, 0, 0, 0, 0, var0, var0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {var0, var0, 0, 0, 0, 0, var0, var0}, {0, var0, var0, 0, 0, var0, var0, 0}, {0, 0, var0, 0, 0, var0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Depths");
      var5.setNotes("Sacrifice:\n\n-2xLog at Plant & Gunpowder levels\n-2xIronIngot at Lapis & Blaze levels.\n\nNotes:\n\n-Requires hole in the center.");
      var5.setDesc("Description:\n\nDigs a pit down into the earth. Requires a hole at the center of the rune (1 block down).");
      shapes.add(var5);
      var5 = new DustShape(8, 8, 1, "lumberjack", true, 2, 2, 0, 0, 4);
      add(var5.name, new DELumberjack());
      var6 = new int[][][]{{{0, 0, var0, 0, 0, var0, 0, 0}, {0, 0, var0, var0, var0, var0, 0, 0}, {var0, var0, 0, 0, 0, 0, var0, var0}, {0, var0, 0, 0, 0, 0, var0, 0}, {0, var0, 0, 0, 0, 0, var0, 0}, {var0, var0, 0, 0, 0, 0, var0, var0}, {0, 0, var0, var0, var0, var0, 0, 0}, {0, 0, var0, 0, 0, var0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Lumber");
      var5.setNotes("Sacrifice:\n\n-3xLog + 2xStick");
      var5.setDesc("Description:\n\nChops down all adjacent trees. Has a chance to drop more than 1 log and some sticks.");
      var5.setManualRotationDerp(new int[]{-2, -1, -1, 0, 0, -1, -1, -2});
      shapes.add(var5);
      var5 = new DustShape(8, 8, 1, "campfire6", true, 2, 2, 2, 2, 40);
      add(var5.name, new DECampFire());
      var6 = new int[][][]{{{0, var1, 0, var1, var1, 0, var1, 0}, {var1, 0, 0, var1, var1, 0, 0, var1}, {0, 0, 0, 0, 0, 0, 0, 0}, {var1, var1, 0, 0, 0, 0, var1, var1}, {var1, var1, 0, 0, 0, 0, var1, var1}, {0, 0, 0, 0, 0, 0, 0, 0}, {var1, 0, 0, var1, var1, 0, 0, var1}, {0, var1, 0, var1, var1, 0, var1, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Fire");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      var5.setNotes("Sacrifice:\n\n-8xLog + 1xZombieFlesh\n\nNotes:\n\n-Lasts a fourth of a day unless fueled.\n");
      var5.setDesc("Description:\n\nCreates a flame that allows you to smelt items. Items thrown in 1 at a time have a 90% chance of popping back out smelted.");
      shapes.add(var5);
      var5 = new DustShape(4, 4, 1, "firetrap", true, 0, 0, 0, 0, 10);
      add(var5.name, new DEFireTrap());
      var6 = new int[][][]{{{0, 0, 0, var0}, {var0, 0, var0, var0}, {var0, var0, 0, 0}, {var0, var0, var0, 0}}};
      var5.setData(var6);
      var5.setName("Fire Trap Rune");
      var5.setNotes("Sacrifice:\n\n-3xFlint\n\nNotes:\n\n-Dust must be gunpowder or better.");
      var5.setDesc("Description:\n\nSets entities and landscape on fire when an entity comes near.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(4, 4, 1, "lightning", true, 0, 0, 0, 0, 12);
      add(var5.name, new DELightning());
      var6 = new int[][][]{{{0, var0, var0, var0}, {0, var0, 0, 0}, {0, 0, var0, 0}, {var0, var0, var0, 0}}};
      var5.setData(var6);
      var5.setName("Lightning Trap Rune");
      var5.setNotes("Sacrifice:\n\n-3xIronIngot\n\nNotes:\n\n-Dust must be gunpowder or better.");
      var5.setDesc("Description:\n\nZaps entities with lightning when an entity comes near.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(6, 6, 1, "poison", true, 3, 3, 1, 1, 11);
      add(var5.name, new DEPoisonTrap());
      var6 = new int[][][]{{{0, 0, var0, 0, var0, 0}, {0, var0, var0, var0, var0, var0}, {var0, var0, var0, 0, var0, 0}, {var0, 0, var0, var0, var0, var0}, {0, var0, 0, var0, var0, 0}, {0, 0, var0, var0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Poison Trap Rune");
      var5.setNotes("Sacrifice:\n\n-3xSpiderEye\n\nNotes:\n\n-Dust must be gunpowder or better.");
      var5.setDesc("Description:\n\nPoisons entities when an entity comes near.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(5, 6, 1, "bomb", false, 0, 2, 0, 2, 13);
      add(var5.name, new DEBomb());
      var6 = new int[][][]{{{0, 0, 0, 0, var0}, {0, 0, 0, var0, var0}, {var2, var2, var2, var0, 0}, {var2, var0, var0, var2, 0}, {var2, var0, var0, var2, 0}, {var2, var2, var2, var2, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Detonation");
      var5.setNotes("Sacrifice:\n\n-5xGunpowder\nOR\n-15xGunpowder Runic Dust\n\nNotes:\n\n-Center determines strength.\n-Fuse determines triggering.\n-If fuse is made out of plant runic dust, it will wait for a mob to trigger.\n-Otherwise, the time until deonation will depend on the dust strength.");
      var5.setDesc("Description:\n\nCreates a variable-sized explosion when triggered by an entity or by time. Center determines the strength, fuse deteremines the triggering. If fuse is made out of plant runic dust, it will wait for a mob to trigger. Otherwise, the time until deonation will depend on the dust strength.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, -1, 0, -1});
      shapes.add(var5);
      var5 = new DustShape(6, 9, 1, "lilypad", false, 3, 0, 1, 4, 7);
      add(var5.name, new DELillyBridge());
      var6 = new int[][][]{{{0, 0, var1, var2, 0, 0}, {0, 0, var1, var2, 0, 0}, {0, var1, var1, var2, var2, 0}, {0, var1, var1, var2, var2, 0}, {var1, var1, 0, 0, var2, var2}, {var1, var1, var1, var2, var2, var2}, {var1, var1, var2, var1, var2, var2}, {var1, var1, var1, var2, var2, var2}, {0, 0, var1, var2, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of the Leaping Frog");
      var5.setNotes("Sacrifice:\n\n-4xLeaves");
      var5.setDesc("Description:\n\nSpawns a bridge of lily pads over a body of water in front of it.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, -1});
      shapes.add(var5);
      var5 = new DustShape(8, 6, 1, "void", true, 2, 3, 2, 1, 29);
      add(var5.name, new DEVoid());
      var6 = new int[][][]{{{0, var3, 0, var3, var3, 0, var3, 0}, {var3, var3, var3, 0, 0, var3, var3, var3}, {0, var3, 0, var3, var3, 0, var3, 0}, {0, var3, 0, var3, var3, 0, var3, 0}, {var3, var3, var3, 0, 0, var3, var3, var3}, {0, var3, 0, var3, var3, 0, var3, 0}}};
      var5.setData(var6);
      var5.setName("Rune of the Void");
      var5.setNotes("Sacrifice:\n\n-3XP + Items to store\n\nNotes:\n\n-If there is a sacrifice, it will be stored.\n-If there is not, all items in storage will be dropped.\n-5XP will be taken either way.\n-Inventories are separated by user. So you will not get yours mixed with someone elses.");
      var5.setDesc("Description:\n\nStores sacrificed items in a void. When activated without a sacrifice, the items are returned.");
      var5.setManualRotationDerp(new int[]{0, 0, -1, 0, 0, 0, -1, 0});
      shapes.add(var5);
      var5 = new DustShape(8, 8, 1, "obelisk", true, 2, 2, 2, 2, 15);
      add(var5.name, new DEObelisk());
      var6 = new int[][][]{{{0, 0, 0, var1, var1, 0, 0, 0}, {0, var1, var1, var1, var1, var1, var1, 0}, {0, var1, 0, 0, 0, 0, var1, 0}, {var1, var1, 0, 0, 0, 0, var1, var1}, {var1, var1, 0, 0, 0, 0, var1, var1}, {0, var1, 0, 0, 0, 0, var1, 0}, {0, var1, var1, var1, var1, var1, var1, 0}, {0, 0, 0, var1, var1, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Heights");
      var5.setNotes("Sacrifice:\n\n-2xIronOre\n\nNotes:\n\n-The obelisk will stay standing for one day waiting for you.\n-Destroying the top block will cause it to go back down.\n-If you do not destroy the top block within a day, it will simply remain standing.");
      var5.setDesc("Description:\n\n");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(4, 4, 1, "heal", true, 0, 0, 0, 0, 1);
      add(var5.name, new DEHeal());
      var6 = new int[][][]{{{0, var0, var0, 0}, {var0, var0, var0, var0}, {var0, var0, var0, var0}, {0, var0, var0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Healing");
      var5.setNotes("Sacrifice:\n\n-2xCoal + 2XP");
      var5.setDesc("Description:\n\nHeals any nearby entities\' hearts with regeneration.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(14, 6, 1, "wall", false, 3, 3, 5, 1, 16);
      add(var5.name, new DEWall());
      var6 = new int[][][]{{{0, 0, 0, 0, 0, 0, var2, var2, var2, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, var2, var1, var2, var2, var2, 0, 0, 0}, {0, var2, var2, var2, var2, var2, var2, var2, var1, var1, var2, var2, var2, var2}, {var1, var1, var1, var1, var2, var2, var1, var1, var1, var1, var1, var1, var1, 0}, {0, 0, 0, var1, var1, var1, var2, var1, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, var1, var1, var1, 0, 0, 0, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of the Barrier");
      var5.setNotes("Sacrifice:\n\n-5xIronOre + 3XP");
      var5.setDesc("Description:\n\nLifts a wall out of the earth.");
      var5.setManualRotationDerp(new int[]{0, 0, -1, 1, 0, 0, -1, 1, 18});
      shapes.add(var5);
      var5 = new DustShape(8, 8, 1, "dawn", true, 2, 2, 2, 2, 8);
      add(var5.name, new DEDawn());
      var6 = new int[][][]{{{0, 0, var2, 0, 0, var2, 0, 0}, {0, 0, 0, var2, var2, 0, 0, 0}, {var2, 0, var2, 0, 0, var2, 0, var2}, {0, var2, 0, var2, var2, 0, var2, 0}, {0, var2, 0, var2, var2, 0, var2, 0}, {var2, 0, var2, 0, 0, var2, 0, var2}, {0, 0, 0, var2, var2, 0, 0, 0}, {0, 0, var2, 0, 0, var2, 0, 0}}};
      var5.setName("Rune of Dawn");
      var5.setData(var6);
      var5.setNotes("Sacrifice:\n\n-4xRedstoneDust + 1LapisLazuli\n\nNotes:\n-If it is already day, it will last through to the next night night to do anything.");
      var5.setDesc("Description:\n\nTurns night into day. If it is already day, it will wait until the next night to activate.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(4, 4, 1, "lunar", true, 0, 0, 0, 0, 9);
      add(var5.name, new DELunar());
      var6 = new int[][][]{{{var2, var2, var2, var2}, {var2, 0, 0, var2}, {var2, 0, 0, 0}, {var2, var2, 0, var2}}};
      var5.setName("Rune of Dusk");
      var5.setData(var6);
      var5.setNotes("Sacrifice:\n\n-4xNetherwart + 1xLapisLazuli");
      var5.setDesc("Description:\n\nTurns day into night. If it is already night, it will wait until the next day to activate.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(8, 6, 1, "farm", false, 2, 3, 2, 1, 3);
      add(var5.name, new DEFarm());
      var6 = new int[][][]{{{0, var1, var1, 0, 0, var1, var1, 0}, {var1, 0, var1, var0, var0, var1, 0, var1}, {var1, 0, var1, var0, var0, var1, 0, var1}, {var1, 0, var1, var0, var0, var1, 0, var1}, {var1, 0, var1, var0, var0, var1, 0, var1}, {0, var1, var1, 0, 0, var1, var1, 0}}};
      var5.setData(var6);
      var5.setName("Rune of the Farm");
      var5.setManualRotationDerp(new int[]{0, 0, -1, 0, 0, 0, -1, 0});
      var5.setNotes("Sacrifice:\n\n-8xIronIngot + 4XP");
      var5.setDesc("Description:\n\nInstantly spawns a farm.");
      shapes.add(var5);
      var5 = new DustShape(6, 6, 1, "xpstore", false, 3, 3, 1, 1, 37);
      add(var5.name, new DEXPStore());
      var6 = new int[][][]{{{0, 0, 0, var1, var1, 0}, {0, var2, var2, 0, var1, var1}, {0, var2, var3, var3, 0, var1}, {var1, 0, var3, var3, var2, 0}, {var1, var1, 0, var2, var2, 0}, {0, var1, var1, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Wisdom");
      var5.setNotes("Sacrifice:\n\n-16xIronIngot + 6XP\n\nNotes:\n\n-Right-clicking will give you back your XP and pause the rune for a short time.\n-While the rune is paused, it will glow yellow and not absorb any XP.\n-Not accessible by other players.");
      var5.setDesc("Description:\n\nStores all XP. When you walk over it it will take all your levels. It will also store any XP orbs dropped onto it. Not useable by other players.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(5, 4, 1, "speed", false, 0, 0, 0, 0, 6);
      add(var5.name, new DESpeed());
      var6 = new int[][][]{{{var1, var1, var1, var1, var0}, {0, var3, var3, var3, var0}, {0, 0, var1, var1, var0}, {0, 0, 0, var3, var0}}};
      var5.setData(var6);
      var5.setName("Rune of Speed");
      var5.setNotes("Sacrifice:\n\n-4xSugar + 2xBlazePowder");
      var5.setDesc("Description:\n\nGives you a variable speed boost.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, -1, -1, 0});
      shapes.add(var5);
      var5 = new DustShape(6, 6, 1, "cage", false, 3, 3, 1, 1, 18);
      add(var5.name, new DECage());
      var6 = new int[][][]{{{0, var2, var2, var2, var2, 0}, {var2, var2, 0, 0, var2, var2}, {var2, var3, var3, var3, var3, var2}, {var2, var3, var3, var3, var3, var2}, {var2, var2, 0, 0, var2, var2}, {0, var2, var2, var2, var2, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Entrapment");
      var5.setNotes("Sacrifice:\n\n-6xIron + 8xLapisLazuli");
      var5.setDesc("Description:\n\nEntraps a single mob that walks nearby. Will not entrap the summoner.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(8, 6, 1, "fog", false, 2, 3, 2, 1, 41);
      add(var5.name, new DEFog());
      var6 = new int[][][]{{{0, 0, var2, var2, var2, var2, var2, 0}, {0, var3, var2, var3, var3, var3, var2, var2}, {var3, 0, var2, var2, var3, var3, var2, var2}, {var3, var3, var2, var2, var3, var3, 0, var2}, {var3, var3, var2, var2, var2, var3, var2, 0}, {0, var3, var3, var3, var3, var3, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of the Blinding Fog");
      var5.setNotes("Sacrifice:\n\n-1xWaterBucket + 1xRedshroom + 6XP\n\nNotes:\n\n-Will last a day unless fueled.");
      var5.setDesc("Description:\n\nCreates a fog that blinds and confuses anyone inside.");
      var5.setManualRotationDerp(new int[]{0, 0, -1, 0, 0, 0, -1, 0});
      shapes.add(var5);
      var5 = new DustShape(6, 6, 1, "compression", false, 3, 3, 1, 1, 24);
      add(var5.name, new DECompression());
      var6 = new int[][][]{{{0, var2, var4, var2, var4, 0}, {var2, var2, var2, var4, var4, var4}, {var4, var4, var2, var4, var2, var2}, {var2, var2, var4, var2, var4, var4}, {var4, var4, var4, var2, var2, var2}, {0, var4, var2, var4, var2, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Compression");
      var5.setNotes("Sacrifice:\n\n-NxCoal + 1xIronBlock\n\nNotes:\n\n-Every 32Coal will yield a diamond.");
      var5.setDesc("Description:\n\nTurns coal into diamond at a ratio of 32Coal=1Diamond.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(6, 7, 1, "firerain6", false, 3, 3, 1, 1, 25);
      add(var5.name, new DEFireRain());
      var6 = new int[][][]{{{var4, var2, var2, var2, var2, var4}, {var4, var4, var2, var2, var4, var4}, {0, var2, var4, var4, var2, 0}, {var4, var2, var4, var4, var2, var4}, {var4, var4, var2, var2, var4, var4}, {0, var4, var2, var2, var4, 0}, {0, var2, var2, var2, var2, 0}}};
      var5.setData(var6);
      var5.setName("Rune of the Hellstorm");
      var5.setNotes("Sacrifice:\n\n-4xBlazeRod\n\nNotes:\n\n-Will last half a day unless fueled.-Will cause major lag.");
      var5.setDesc("Description:\n\nSummons a storm of ignited arrows for a duration of time. WARNING:Large chance of lag if left running. Break rune to stop.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, -1, -1, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(6, 7, 1, "egg", false, 3, 2, 5, 5, 45);
      add(var5.name, new DEEggifier());
      var6 = new int[][][]{{{0, 0, var1, var1, 0, 0}, {0, var1, var1, var1, var1, 0}, {var1, var1, var1, var1, var1, var1}, {var1, var1, var2, var2, var1, var1}, {var1, var1, var2, var2, var1, var1}, {var2, var2, var2, var2, var2, var2}, {0, var1, var1, var1, var1, 0}}};
      var5.setData(var6);
      var5.setName("Rune Rebirth");
      var5.setManualRotationDerp(new int[]{0, 1, 1, 1, 1, 1, 1, 0});
      var5.setNotes("Sacrifice:\n\n-1xLiveEntity + 1xEgg + 1xDiamond + 10XP\n\n");
      var5.setDesc("Description:\n\nDrops an egg containing the mob sacrificed.");
      shapes.add(var5);
      var5 = new DustShape(6, 6, 1, "bait6", false, 3, 3, 1, 1, 5);
      add(var5.name, new DEBait());
      var6 = new int[][][]{{{var1, var1, var1, 0, 0, 0}, {var1, var1, var1, var1, var1, 0}, {var1, var1, var3, var3, var1, 0}, {0, var1, var3, var3, var1, var1}, {0, var1, var1, var1, var1, var1}, {0, 0, 0, var1, var1, var1}}};
      var5.setData(var6);
      var5.setName("Rune of Baiting");
      var5.setNotes("Sacrifice:\n\n-1xMobEgg + 1xGoldBlock + 5XP\n\nNotes:\n\n-Will last seven days unless fueled.\n\nCurrent bug: Does not detect entities who do not utilize the new AI system.");
      var5.setDesc("Description:\n\nAttracts any mobs with the specified drop type.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(20, 20, 1, "flatten", false, 0, 0, 9, 9, 32);
      add(var5.name, new DEFlatten());
      var6 = new int[][][]{{{0, 0, 0, 0, 0, 0, 0, 0, var2, 0, 0, var2, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, var2, var2, var2, var2, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, var2, 0, 0, var2, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, var2, var2, var2, var2, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, var2, 0, 0, var2, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, var1, var1, var2, var2, var2, var2, var1, var1, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, var1, var1, var3, var2, 0, 0, var2, var3, var1, var1, 0, 0, 0, 0, 0}, {0, var2, var2, var2, 0, var1, var3, var2, var2, var3, var3, var2, var2, var3, var1, 0, var2, var2, var2, 0}, {var2, var2, var2, var2, var2, var2, var2, var2, 0, 0, 0, 0, var2, var2, var2, var2, var2, var2, var2, var2}, {0, var2, 0, var2, 0, var2, 0, var3, 0, var0, var0, 0, var3, 0, var2, 0, var2, 0, var2, 0}, {0, var2, 0, var2, 0, var2, 0, var3, 0, var0, var0, 0, var3, 0, var2, 0, var2, 0, var2, 0}, {var2, var2, var2, var2, var2, var2, var2, var2, 0, 0, 0, 0, var2, var2, var2, var2, var2, var2, var2, var2}, {0, var2, var2, var2, 0, var1, var3, var2, var2, var3, var3, var2, var2, var3, var1, 0, var2, var2, var2, 0}, {0, 0, 0, 0, 0, var1, var1, var3, var2, 0, 0, var2, var3, var1, var1, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, var1, var1, var2, var2, var2, var2, var1, var1, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, var2, 0, 0, var2, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, var2, var2, var2, var2, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, var2, 0, 0, var2, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, var2, var2, var2, var2, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, var2, 0, 0, var2, 0, 0, 0, 0, 0, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Level Earth");
      var5.setNotes("Sacrifice:\n\n-Plant Dust: 10XP + 20xIronOre\n-Gunpowder Dust: 12XP + 20xIronOre\n-Lapis Dust: 15XP + 20xIronOre\n-Blaze Dust: 20XP + 20xIronOre");
      var5.setDesc("Description:\n\nFlattens the terrain around it.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(10, 10, 1, "powerRelay", false, 1, 1, 5, 5, 42);
      add(var5.name, new DEPowerRelay());
      var6 = new int[][][]{{{0, var3, var3, var3, var3, 0, 0, 0, 0, 0}, {0, var3, var2, var2, var3, 0, var2, var2, 0, 0}, {0, var3, var2, var2, var3, var3, var2, var2, 0, 0}, {0, var3, var3, 0, var3, var3, var3, 0, 0, 0}, {var2, var2, 0, 0, var4, var4, var3, var3, var2, var2}, {var2, var2, var3, var3, var4, var4, 0, 0, var2, var2}, {0, 0, 0, var3, var3, var3, 0, var3, var3, 0}, {0, 0, var2, var2, var3, var3, var2, var2, var3, 0}, {0, 0, var2, var2, 0, var3, var2, var2, var3, 0}, {0, 0, 0, 0, 0, var3, var3, var3, var3, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Power Distribution");
      var5.setNotes("Sacrifice:\n\n-1xTestificateEgg + 15XP\n\nNotes:\n\n-Does not exhaust any fuel itself. All fuel is redirected to runes within a " + DEPowerRelay.distance + " block radius.\n" + "-Fueled runes will have a bigger spark than normal.\n" + "-Sprites cannot be powered by powering the location at which their runes are summoned, but by the sprites themselves.");
      var5.setDesc("Description:\n\nActs like a battery storing an infinite amount of fuel and distributing it to nearby runes who need it as they need it. Takes no fuel to sustain itself. Runes being powered will display a spark twice as big as normal.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(6, 6, 1, "resurrect", true, 3, 3, 1, 1, 17);
      add(var5.name, new DEResurrection());
      var6 = new int[][][]{{{var3, 0, 0, 0, var3, var3}, {var3, 0, var3, 0, 0, var3}, {var3, var3, var3, var3, 0, var3}, {var3, 0, var3, var3, var3, var3}, {var3, 0, 0, var3, 0, var3}, {var3, var3, 0, 0, 0, var3}}};
      var5.setData(var6);
      var5.setName("Rune of Resurrection");
      var5.setNotes("Sacrifice:\n\n-1xGhastTear + 4xSoulSand +2xMobDrop");
      var5.setDesc("Description:\n\nSpawns a mob of the specified drop type.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(12, 8, 1, "hunter6", false, 0, 2, 0, 0, 43);
      add(var5.name, (new DEHunterVision()).setAllowed(false).setPermaAllowed(false));
      var6 = new int[][][]{{{0, 0, var2, var2, var2, 0, 0, var2, var2, var2, 0, 0}, {var2, var2, var2, var4, var2, var2, var2, var2, var4, var2, var2, var2}, {0, 0, var4, var2, var2, var2, var2, var2, var2, var4, 0, 0}, {0, 0, var2, var2, var4, var4, var4, var4, var2, var2, 0, 0}, {0, 0, var2, var2, var4, var4, var4, var4, var2, var2, 0, 0}, {0, 0, var4, var2, var2, var2, var2, var2, var2, var4, 0, 0}, {var2, var2, var2, var4, var2, var2, var2, var2, var4, var2, var2, var2}, {0, 0, var2, var2, var2, 0, 0, var2, var2, var2, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of the Hunter");
      var5.setManualRotationDerp(new int[]{-1, -1, -1, -1, -1, -1, -1, -1});
      var5.setNotes("Sacrifice:\n\n-3xBlazePowder + 1xEnderEye + 12XP\n\nNotes:\n\n-Will last a day unless fueled.\n-Will cause lag: Right clicking will pause the rune (and its fuel consumption.)\n\nCurrent bug: Does not detect entities who do not utilize the new AI system.");
      var5.setDesc("Description:\n\nAllows you to see the location and health of any mob nearby. WARNING: locsiblitity for lag on lower-quality machines. Right-click the rune to disable. ");
      shapes.add(var5);
      var5 = new DustShape(6, 6, 1, "loyaltysprite6", false, 3, 3, 1, 1, 21);
      add(var5.name, (new DELoyaltySprite()).setAllowed(false).setPermaAllowed(false));
      var6 = new int[][][]{{{var3, var3, var2, var3, var2, var2}, {var2, var2, var2, var3, var3, var3}, {var3, var2, var3, var2, var3, var2}, {var3, var2, var3, var2, var3, var2}, {var2, var2, var2, var3, var3, var3}, {var3, var3, var2, var3, var2, var2}}};
      var5.setData(var6);
      var5.setName("Rune of the Loyalty Sprite");
      var5.setNotes("Sacrifice:\n\n-4xGhastTear + 10XP\n\nNotes:\n\n-Will last for three days unless fueled.\n-CURRENTLY BROKEN.");
      var5.setDesc("Description:\n\nTakes over the mind of a mob to have them fight for you. CURRENTLY BROKEN: Any attempt to summon this rune will automatically fail.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(9, 6, 1, "scollect", false, 2, 3, 0, 0, 35);
      add(var5.name, new DESpawnerCollector());
      var6 = new int[][][]{{{var3, var2, var2, var2, var2, var2, var2, var3}, {var3, var2, 0, 0, 0, 0, var2, var3}, {var2, var2, 0, 0, 0, 0, var2, var2}, {var3, var3, 0, 0, 0, 0, var3, var3}, {var2, var3, 0, 0, 0, 0, var3, var2}, {var2, var3, var3, var3, var3, var3, var3, var2}, {0, 0, var3, var3, var3, var3, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Spawner Collection");
      var5.setNotes("Sacrifice:\n\n-6xGoldOre + 13XP");
      var5.setDesc("Description:\n\nCollects a spawner that it is placed around.");
      var5.setManualRotationDerp(new int[]{-2, -1, -1, 0, 0, -1, -1, -2});
      shapes.add(var5);
      var5 = new DustShape(8, 10, 1, "reprog", true, 2, 2, 2, 2, 33);
      add(var5.name, new DESpawnerReprog());
      var6 = new int[][][]{{{0, 0, var3, 0, 0, var3, 0, 0}, {0, var3, var3, 0, 0, var3, var3, 0}, {0, var3, 0, 0, 0, 0, var3, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {var3, var3, 0, 0, 0, 0, var3, var3}, {var3, var3, 0, 0, 0, 0, var3, var3}, {0, var3, var3, var3, var3, var3, var3, 0}, {var3, var3, var3, var3, var3, var3, var3, var3}, {var3, var3, 0, var3, var3, 0, var3, var3}, {0, var3, 0, var3, var3, 0, var3, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Spawner Reassignment");
      var5.setNotes("Sacrifice:\n\n-1xMobEgg + 4xEnderPearl + 25XP");
      var5.setDesc("Description:\n\nReassigns a placed spawner to spawn mobs of the specified type.");
      var5.setManualRotationDerp(new int[]{-1, 0, 0, 0, 0, 0, 0, -1});
      shapes.add(var5);
      var5 = new DustShape(4, 4, 1, "spirittools", true, 0, 0, 0, 0, 14);
      add("spirittools", new DESpiritTool());
      var6 = new int[][][]{{{var3, var3, var3, var3}, {var3, var3, var3, var3}, {var3, var3, var3, var3}, {var3, var3, var3, var3}}};
      var5.setName("Rune of the Spirit Tools");
      var5.setNotes("Sacrifice:\n\n-Spirit Pickaxe: 1xGoldPickaxe + 4xTNT + 18XP\n-Spirit Sword: 1xGoldSword + 1xGlowstoneBlock + 18XP");
      var5.setDesc("Description:\n\nSpawns either a spirit sword or spirit pickaxe. Each with special abilities.");
      var5.setData(var6);
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(12, 10, 1, "tele6", false, 1, 0, 3, 8, 30);
      add(var5.name, new DETeleportation());
      var6 = new int[][][]{{{0, 0, 0, 0, 0, var4, 0, 0, 0, 0}, {0, 0, 0, 0, var4, var4, 0, 0, 0, 0}, {0, 0, 0, 0, var4, var2, 0, 0, 0, 0}, {0, 0, 0, 0, var2, var2, 0, 0, 0, 0}, {0, 0, 0, 0, var2, var4, 0, 0, 0, 0}, {0, 0, 0, 0, var4, var4, 0, 0, 0, 0}, {0, 0, 0, 0, var4, var2, 0, 0, 0, 0}, {0, 0, 0, 0, var2, var2, 0, 0, 0, 0}, {var2, var2, var2, var2, var2, var2, var2, var2, var2, var2}, {0, var4, var4, var2, var4, var4, var2, var4, var4, 0}, {0, 0, var2, var2, var4, var4, var2, var2, 0, 0}, {0, 0, 0, var2, 0, 0, var2, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Teleportation");
      var5.setNotes("Sacrifice:\n\n-1xEnderEye + 20XP\n\nNotes:\n\n-Takes away 3 hearts upon every teleportation.");
      var5.setDesc("Description:\n\nCreates a teleporation network location for other teleportation runes to teleport to. Will cost 4 hearts every teleportation. The teleportation network frequency on which to send you depends on the block beneath the blaze square in the rune design.");
      var5.setManualRotationDerp(new int[]{0, 0, 1, 0, 2, 0, 1, -2});
      shapes.add(var5);
      var5 = new DustShape(10, 4, 1, "minitele", false, 1, 0, 3, 0, 34);
      add(var5.name, new DEMiniTele());
      var6 = new int[][][]{{{var4, var4, var4, var4, var2, var2, var4, var4, var4, var4}, {0, var2, var2, var2, var4, var4, var2, var2, var2, 0}, {0, var4, var4, var2, var4, var4, var2, var4, var4, 0}, {0, 0, var2, var2, 0, 0, var2, var2, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Transport");
      var5.setNotes("Sacrifice:\n\n-5XP");
      var5.setDesc("Description:\n\nTeleports you to a teleporation network rune location. The teleportation network frequency on which to send you depends on the block beneath the blaze square in the rune design.");
      var5.setManualRotationDerp(new int[]{0, 0, -1, 0, 0, 0, -1, 0});
      shapes.add(var5);
      var5 = new DustShape(12, 12, 1, "Fire Sprite6", false, 0, 0, 4, 4, 20);
      add(var5.name, new DEFireSprite());
      var6 = new int[][][]{{{0, 0, 0, var2, var2, 0, 0, var2, var2, 0, 0, 0}, {0, 0, 0, 0, var2, 0, 0, var2, 0, 0, 0, 0}, {0, 0, 0, var2, var4, var2, var2, var4, var2, 0, 0, 0}, {var2, 0, var2, var4, var4, var2, var2, var4, var4, var2, 0, var2}, {var2, var2, var4, var4, 0, 0, 0, 0, var4, var4, var2, var2}, {0, 0, var2, var2, 0, var4, var4, 0, var2, var2, 0, 0}, {0, 0, var2, var2, 0, var4, var4, 0, var2, var2, 0, 0}, {var2, var2, var4, var4, 0, 0, 0, 0, var4, var4, var2, var2}, {var2, 0, var2, var4, var4, var2, var2, var4, var4, var2, 0, var2}, {0, 0, 0, var2, var4, var2, var2, var4, var2, 0, 0, 0}, {0, 0, 0, 0, var2, 0, 0, var2, 0, 0, 0, 0}, {0, 0, 0, var2, var2, 0, 0, var2, var2, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of the Fire Sprite");
      var5.setNotes("Sacrifice:\n\n-1xGhastTear + 2xFireCharge + 22XP\n\n-Notes:\n\n-Will last for three days unless fueled.");
      var5.setDesc("Description:\n\nCreates a sprite that will follow you and set the world and mobs on fire. ");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(10, 8, 1, "earthsprite6", false, 1, 2, 3, 2, 39);
      add(var5.name, new DEEarthSprite());
      var6 = new int[][][]{{{0, 0, 0, 0, var2, var2, 0, 0, 0, 0}, {0, 0, var2, var1, var4, var2, var1, var2, 0, 0}, {0, var4, var4, var1, var4, var4, var1, var4, var4, 0}, {var2, var2, var2, var1, var2, var4, var1, var2, var2, var2}, {var2, var2, var2, var1, var4, var2, var1, var2, var2, var2}, {0, var4, var4, var1, var4, var4, var1, var4, var4, 0}, {0, 0, var2, var1, var2, var4, var1, var2, 0, 0}, {0, 0, 0, 0, var2, var2, 0, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of the Earth Sprite");
      var5.setNotes("Sacrifice:\n\n-1xGhastTear + 20xIronOre + 16xGlass + 25XP\n\nNotes:\n\n-Stop and crouch to call the sprite to protect you.\n-Will last for three days unless fueled.\n");
      var5.setDesc("Description:\n\nSummons a sprite that will encircle you with earth. Pressing [crouch] while standing still will call the sprite to protect you.");
      var5.setManualRotationDerp(new int[]{0, 0, -1, 0, 0, 0, -1, 0});
      shapes.add(var5);
      var5 = new DustShape(6, 6, 1, "timelock6", false, 3, 2, 1, 2, 19);
      add(var5.name, new DETimeLock());
      var6 = new int[][][]{{{0, var2, var2, var2, var2, 0}, {0, var2, 0, 0, var2, 0}, {var2, var2, var2, var2, var2, var2}, {var2, var2, var3, var3, var2, var2}, {var2, var2, var3, var3, var2, var2}, {var2, var2, var2, var2, var2, var2}}};
      var5.setData(var6);
      var5.setName("Rune of Locked Time");
      var5.setNotes("Sacrifice:\n\n-8xObsidian + 4xSlime + 1xLapisLaz\n\nNotes:\n\n-Expect bugs.\n-Lasts for a day\'s time unless fueled.");
      var5.setDesc("Description:\n\nLocks day/night time, sand falling, and water flowing for as long as it is fueled. BEWARE: Very high chance of bugs especially with other mods.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, -1});
      shapes.add(var5);
      var5 = new DustShape(4, 4, 1, "bounce", false, 0, 0, 0, 0, 22);
      add(var5.name, new DEBounce());
      var6 = new int[][][]{{{var2, var2, var1, var1}, {var2, var1, var2, var1}, {var1, var2, var1, var2}, {var1, var1, var2, var2}}};
      var5.setData(var6);
      var5.setName("Rune of Bouncing");
      var5.setNotes("Sacrifice:\n\n-4xSlimeBall");
      var5.setDesc("Description:\n\nCreates a rune that will help you jump much higher.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(4, 4, 1, "record", false, 0, 0, 0, 0, 23);
      add(var5.name, new DESpawnRecord());
      var6 = new int[][][]{{{2, 2, 2, 2}, {2, 3, 2, 2}, {2, 2, 3, 2}, {2, 2, 2, 2}}};
      var5.setData(var6);
      var5.setName("Rune of Music");
      var5.setNotes("Sacrifice:\n\n-1xDiamond");
      var5.setDesc("Description:\n\nSpawns a random record.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(8, 8, 1, "ench.silktouch", false, 2, 2, 2, 2, 26);
      add(var5.name, new DESilkTouchEnch());
      var6 = new int[][][]{{{0, 0, 0, var2, 0, 0, 0, 0}, {0, var2, var2, var2, var1, var1, var1, 0}, {0, var2, var2, var4, var2, var1, var1, 0}, {var2, var2, var4, var2, var4, var2, var1, 0}, {0, var1, var2, var4, var2, var4, var2, var2}, {0, var1, var1, var2, var4, var2, var2, 0}, {0, var1, var1, var1, var2, var2, var2, 0}, {0, 0, 0, 0, var2, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Enchanting Rune of Silk Touch");
      var5.setNotes("Sacrifice:\n\n-1xLiveTestificate + 1xDiamondPickaxe + 1xGoldBlock + 20XP\nOR\n-1xLiveTestificate + 1xDiamondSword + 1xGoldBlock + 20XP");
      var5.setDesc("Description:\n\nEnchants and repairs your pick or shovel with Silk Touch I.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(6, 6, 1, "ench.firebow", false, 3, 3, 1, 1, 28);
      add(var5.name, new DEFireBowEnch());
      var6 = new int[][][]{{{0, var4, var2, var2, 0, 0}, {var4, var4, var2, var4, var4, 0}, {var2, var2, var2, var2, var4, var2}, {var2, var4, var2, var2, var2, var2}, {0, var4, var4, var2, var4, var4}, {0, 0, var2, var2, var4, 0}}};
      var5.setData(var6);
      var5.setName("Enchanting Rune of the Fire Bow");
      var5.setNotes("Sacrifice:\n\n-9xFireCharge + 1xBow + 1xGoldBlock + 30XP");
      var5.setDesc("Description:\n\nEnchants and repairs your bow with Fire Aspect I");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(10, 10, 1, "ench.fortune", false, 1, 1, 5, 5, 27);
      add(var5.name, new DEFortuneEnch());
      var6 = new int[][][]{{{0, 0, var2, var2, var2, var2, var2, var2, 0, 0}, {0, 0, var2, var3, var2, var2, var3, var2, 0, 0}, {var2, var2, var3, var3, var1, var1, var3, var3, var2, var2}, {var2, var3, var3, var1, 0, 0, var1, var3, var3, var2}, {var2, var2, var1, 0, 0, 0, 0, var1, var2, var2}, {var2, var2, var1, 0, 0, 0, 0, var1, var2, var2}, {var2, var3, var3, var1, 0, 0, var1, var3, var3, var2}, {var2, var2, var3, var3, var1, var1, var3, var3, var2, var2}, {0, 0, var2, var3, var2, var2, var3, var2, 0, 0}, {0, 0, var2, var2, var2, var2, var2, var2, 0, 0}}};
      var5.setData(var6);
      var5.setName("Enchanting Rune of Fortune");
      var5.setNotes("Sacrifice:\n\n-1xDiamondOre + 1xLapisOre + 1xRedstoneOre + 1xGoldBlock + 1xDiamondPickaxe + 40XP\nOR\n-1xDiamondOre + 1xLapisOre + 1xRedstoneOre + 1xGoldBlock + 1xDiamondSword + 40XP\n");
      var5.setDesc("Description:\n\nEnchants and repairs your pick or sword with Fortune IV or Looting IV respectively.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(12, 12, 1, "forcefield6", false, 0, 0, 4, 4, 36);
      add(var5.name, new DEForcefield());
      var6 = new int[][][]{{{0, 0, 0, 0, var3, 0, 0, var3, 0, 0, 0, 0}, {0, 0, var3, var3, 0, var3, var3, 0, var3, var3, 0, 0}, {0, var3, var3, var2, var3, var4, var4, var3, var2, var3, var3, 0}, {0, var3, var2, var2, var4, var2, var2, var4, var2, var2, var3, 0}, {var3, 0, var3, var4, var4, 0, 0, var4, var4, var3, 0, var3}, {0, var3, var4, var2, 0, var0, var0, 0, var2, var4, var3, 0}, {0, var3, var4, var2, 0, var0, var0, 0, var2, var4, var3, 0}, {var3, 0, var3, var4, var4, 0, 0, var4, var4, var3, 0, var3}, {0, var3, var2, var2, var4, var2, var2, var4, var2, var2, var3, 0}, {0, var3, var3, var2, var3, var4, var4, var3, var2, var3, var3, 0}, {0, 0, var3, var3, 0, var3, var3, 0, var3, var3, 0, 0}, {0, 0, 0, 0, var3, 0, 0, var3, 0, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Protection");
      var5.setNotes("Sacrifice:\n\n-2xLiveIronGolem + 50XP\n\nNotes:\n\n-Lasts for a day unless fueled.");
      var5.setDesc("Description:\n\nCreates a forcefield that will push away all hostile mobs.");
      var5.setManualRotationDerp(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
      shapes.add(var5);
      var5 = new DustShape(16, 8, 1, "liftterrain", false, 2, 2, 6, 2, 38);
      add(var5.name, new DELiftTerrain());
      var6 = new int[][][]{{{0, 0, 0, 0, 0, var3, var2, var2, var2, var2, var3, 0, 0, 0, 0, 0}, {var3, var3, var3, var3, var3, var3, var2, var1, var1, var2, var3, var3, var3, var3, var3, var3}, {0, 0, var2, var2, var2, var2, var1, var1, var1, var1, var2, var2, var2, var2, 0, 0}, {0, 0, 0, 0, var2, var1, var1, var0, var0, var1, var1, var2, 0, 0, 0, 0}, {0, 0, 0, 0, var2, var1, var1, var0, var0, var1, var1, var2, 0, 0, 0, 0}, {0, 0, var2, var2, var2, var2, var1, var1, var1, var1, var2, var2, var2, var2, 0, 0}, {var3, var3, var3, var3, var3, var3, var2, var1, var1, var2, var3, var3, var3, var3, var3, var3}, {0, 0, 0, 0, 0, var3, var2, var2, var2, var2, var3, 0, 0, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of the Mountain");
      var5.setNotes("Sacrifice:\n\n-1xLiveIronGolem + 1xRose + 60XP\n\nNotes:\n\n-The area you want to lift should be outline with etchings filled with clay blocks.");
      var5.setDesc("Description:\n\nLifts the earth specified by the clay-filled etchings up high into the sky.");
      var5.setManualRotationDerp(new int[]{0, 0, -1, 1, 0, 0, -1, 1});
      shapes.add(var5);
      var5 = new DustShape(22, 32, 1, "xp6", false, 2, 3, 15, 10, 31);
      add(var5.name, new DEXP());
      var6 = new int[][][]{{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, var2, 0, 0, var2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, var2, 0, 0, 0, var2, var2, var2, var2, 0, 0, 0, var2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, var2, var2, var2, var2, 0, 0, 0, 0, var2, var2, 0, 0, 0, 0, var2, var2, var2, var2, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, var3, var3, 0, 0, 0, 0, 0, 0, var3, 0, var3, 0, 0, var3, 0, var3, 0, 0, 0, 0, 0, 0, var3, var3, 0, 0, 0, 0}, {0, 0, 0, 0, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, 0, 0, 0, 0}, {0, 0, var2, var2, 0, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, 0, var2, var2, 0, 0}, {0, 0, var2, 0, 0, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, 0, 0, var2, 0, 0}, {0, 0, 0, 0, 0, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, 0, 0, 0, 0, 0}, {0, 0, 0, var3, var3, var3, 0, 0, 0, 0, 0, 0, 0, 0, var3, var3, var3, var3, 0, 0, 0, 0, 0, 0, 0, 0, var3, var3, var3, 0, 0, 0}, {0, var3, var3, var3, 0, var3, 0, 0, 0, 0, var2, var2, var2, var2, var3, var2, var2, var3, var2, var2, var2, var2, 0, 0, 0, 0, var3, 0, var3, var3, var3, 0}, {var3, var3, var3, 0, var3, var3, 0, 0, 0, 0, 0, var3, var3, var2, var2, var3, var3, var2, var2, var3, var3, 0, 0, 0, 0, 0, var3, var3, 0, var3, var3, var3}, {var3, var3, var3, 0, var3, var3, 0, 0, 0, 0, 0, var3, var3, var2, var2, var3, var3, var2, var2, var3, var3, 0, 0, 0, 0, 0, var3, var3, 0, var3, var3, var3}, {0, var3, var3, var3, 0, var3, 0, 0, 0, 0, var2, var2, var2, var2, var3, var2, var2, var3, var2, var2, var2, var2, 0, 0, 0, 0, var3, 0, var3, var3, var3, 0}, {0, 0, 0, var3, var3, var3, 0, 0, 0, 0, 0, 0, 0, 0, var3, var3, var3, var3, 0, 0, 0, 0, 0, 0, 0, 0, var3, var3, var3, 0, 0, 0}, {0, 0, 0, 0, 0, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, 0, 0, 0, 0, 0}, {0, 0, var2, 0, 0, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, 0, 0, var2, 0, 0}, {0, 0, var2, var2, 0, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, var3, 0, 0, 0, 0, 0, 0, 0, 0, 0, var3, 0, var2, var2, 0, 0}, {0, 0, 0, 0, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, var3, 0, 0, 0, 0}, {0, 0, 0, 0, var3, var3, 0, 0, 0, 0, 0, 0, var3, 0, var3, 0, 0, var3, 0, var3, 0, 0, 0, 0, 0, 0, var3, var3, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, var2, var2, var2, var2, 0, 0, 0, 0, var2, var2, 0, 0, 0, 0, var2, var2, var2, var2, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, var2, 0, 0, 0, var2, var2, var2, var2, 0, 0, 0, var2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, var2, 0, 0, var2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}}};
      var5.setData(var6);
      var5.setName("Rune of Sarlacc");
      var5.setNotes("Sacrifice:\n\n-1xPassiveMobEgg + 1xHostileMobEgg + 80XP\n\nNotes:\n\n-Lasts for a day unless fueled. \n-Every mob sacrificed will prolonge it\' life for an eighth of a day.\n-Every item (smeltable or otherwise) will prolonge it\'s life for (1/2 a second)*stackSize.");
      var5.setDesc("Description:\n\nKills any mobs dropped onto it and destroys their drops. However, will drop 2 times as much XP into the holes around it. Will not damage anyone underneath.");
      var5.setManualRotationDerp(new int[]{0, 0, -1, 1, 0, 0, -1, 1});
      shapes.add(var5);
      config = new Configuration(new File("./DustModConfig.cfg"));
      config.load();

      for(int var7 = 0; var7 < names.size(); ++var7) {
         DustShape var8 = getShape(var7);
         String var9 = var8.name;
         DustEvent var10 = get(var9);
         if(!var10.secret) {
            var10.allowed = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Allow " + var8.pName, "general", var10.allowed).value);
         }
      }

      config.save();
   }

}
