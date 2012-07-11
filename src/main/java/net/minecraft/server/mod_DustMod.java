package net.minecraft.server;

import forge.IGuiHandler;
import forge.MinecraftForge;
import forge.NetworkMod;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySenses;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EnumToolMaterial;
import net.minecraft.server.IMonster;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MLProp;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ModLoader;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalSelector;
import net.minecraft.server.PathfinderGoalSelectorItem;
import net.minecraft.server.World;
import net.minecraft.server.dustmod.BlockDust;
import net.minecraft.server.dustmod.BlockDustTable;
import net.minecraft.server.dustmod.BlockRut;
import net.minecraft.server.dustmod.DustManager;
import net.minecraft.server.dustmod.DustShape;
import net.minecraft.server.dustmod.EntityBlock;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.ItemChisel;
import net.minecraft.server.dustmod.ItemDust;
import net.minecraft.server.dustmod.ItemPlaceScroll;
import net.minecraft.server.dustmod.ItemRunicTome;
import net.minecraft.server.dustmod.ItemSpiritPickaxe;
import net.minecraft.server.dustmod.ItemSpiritSword;
import net.minecraft.server.dustmod.TileEntityDust;
import net.minecraft.server.dustmod.TileEntityDustTable;
import net.minecraft.server.dustmod.TileEntityRut;
import net.minecraft.server.dustmod.network.PacketHandler;

public class mod_DustMod extends NetworkMod implements IGuiHandler {

   public static final String version = "v0.6.1";
   public static final int warpVer = 1;
   public static boolean debug = false;
   public static String path = "/dust";
   public static int[] tex;
   public static int groundTex;
   public static boolean allTex = true;
   public static int DustMetaDefault = 0;
   public static int DustMetaUsing = 1;
   public static int DustMetaUsed = 2;
   @MLProp
   public static int BLOCK_RutID = 222;
   @MLProp
   public static int BLOCK_DustTableID = 221;
   @MLProp
   public static int BLOCK_DustID = 220;
   @MLProp
   public static int ITEM_DustID = 1240;
   @MLProp
   public static int ITEM_RunicTomeID = 1220;
   @MLProp
   public static int ITEM_DustScrollID = 1221;
   @MLProp
   public static int ITEM_SpiritSwordID = 1230;
   @MLProp
   public static int ITEM_SpiritPickID = 1231;
   @MLProp
   public static int ITEM_ChiselID = 1232;
   @MLProp
   public static int ITEM_SacrificeNegationID = 1233;
   @MLProp
   public static int ITEM_RunicPaperID = 1234;
   @MLProp
   public static int ENTITY_FireSpriteID = 149;
   @MLProp
   public static int ENTITY_BlockEntityID = 150;
   @MLProp
   public static boolean Enable_Render_Flames_On_Dust = true;
   @MLProp
   public static boolean Enable_Render_Flames_On_Ruts = true;
   private static HashMap entMap;
   private static long nextDustEntID;
   public static int plantDID = 1;
   public static int gunDID = 2;
   public static int lapisDID = 3;
   public static int blazeDID = 4;
   public static Block dust;
   public static Item idust;
   public static Block dustTable;
   public static Block rutBlock;
   public static Item tome;
   public static Item dustScroll;
   public static Item spiritPickaxe;
   public static Item spiritSword;
   public static int dustModelID;
   public static int dustEntityID;
   public static int rutModelID;
   public static Item chisel;
   public static Item negateSacrifice;
   public static Item runicPaper;
   public static String savePath = "";
   public static String worldName = "";
   public static Properties propInv;
   public static Properties propNet;
   public static Properties propGeneral;
   public static File invFS;
   public static File netFS;
   public static File generalFS;
   public static int prevVoidSize;
   public static HashMap voidInventory;
   public static ArrayList voidNetwork;
   public static int skipWarpTick = 0;
   public static boolean hunterVisionActive = false;
   World tempWorld = null;
   public static final int numSec = 0;
   public static HashMap entdrops = new HashMap();


   public static void killEntity(Entity var0) {
      var0.aI();
   }

   public static void setEntityToAttack(EntityCreature var0, Entity var1) {
      var0.target = var1;
   }

   public static Entity getEntityToAttack(EntityCreature var0) {
      return var0.target;
   }

   public static PathfinderGoalSelector getTasks(EntityLiving var0) {
      return var0.goalSelector;
   }

   public static PathfinderGoalSelector getTargetTasks(EntityLiving var0) {
      return var0.targetSelector;
   }

   public static void setCantSee(EntityLiving var0, EntityLiving var1) {
      EntitySenses var2 = var0.am();
      if(var2.canSee(var1)) {
         var2.seenEntities.remove(var1);
         var2.unseenEntities.add(var1);
      }

   }

   public static void setCanSee(EntityLiving var0, EntityLiving var1) {
      EntitySenses var2 = var0.am();
      if(!var2.canSee(var1)) {
         var2.unseenEntities.remove(var1);
         var2.seenEntities.add(var1);
      }

   }

   public static void addAITask(EntityLiving var0, PathfinderGoal var1, int var2) {
      var0.goalSelector.a(var2, var1);
   }

   public static boolean hasAITask(EntityLiving var0, PathfinderGoal var1) {
      try {
         ArrayList var2 = (ArrayList)ModLoader.getPrivateValue(PathfinderGoalSelector.class, var0.goalSelector, 0);
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            PathfinderGoalSelectorItem var5 = (PathfinderGoalSelectorItem)var4;
            PathfinderGoal var6 = var5.a;
            if(var6.getClass() == var1.getClass()) {
               return true;
            }
         }
      } catch (Exception var7) {
         Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var7);
      }

      return false;
   }

   public static int getExperiencePoints(EntityLiving var0, EntityHuman var1) {
      return var0.getExpValue(var1);
   }

   public static boolean getHasAttacked(EntityCreature var0) {
      return var0.e;
   }

   public static void updateState(EntityCreature var0) {
      var0.d_();
   }

   public static void notifyBlockChange(World var0, int var1, int var2, int var3, int var4) {
      var0.update(var1, var2, var3, var4);
   }

   public static int damageDropped(Block var0, int var1) {
      return var0.getDropData(var1);
   }

   public static float getMoveForward(EntityLiving var0) {
      return var0.aX;
   }

   public static float getMoveSpeed(EntityLiving var0) {
      return var0.bb;
   }

   public static boolean isJumping(EntityLiving var0) {
      return var0.aZ;
   }

   public static boolean isAIEnabled(EntityCreature var0) {
      return var0.isAIEnabled();
   }

   public static void updateActionState(EntityLiving var0) {
      var0.d_();
   }

   public static void setHasAttacked(EntityCreature var0, boolean var1) {
      var0.e = var1;
   }

   public mod_DustMod() {
      voidInventory = new HashMap();
      voidNetwork = new ArrayList();
   }

   public void loadMap(MinecraftServer var1) {
      boolean var2 = true;
      savePath = "./" + var1.getWorldName() + "/data/";
      if(var2) {
         voidInventory = new HashMap();
         propInv = new Properties();
         propNet = new Properties();
         propGeneral = new Properties();
         if(invFS == null || !invFS.exists()) {
            try {
               invFS = new File(savePath + "dustmodvoidinventory.dat");
               if(invFS.createNewFile()) {
                  if(propInv == null) {
                     propInv = new Properties();
                  }

                  propInv.store(new FileOutputStream(invFS), (String)null);
               }
            } catch (IOException var23) {
               Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var23);
            }

            try {
               netFS = new File(savePath + "dustmodvoidnetwork.dat");
               if(netFS.createNewFile()) {
                  if(propNet == null) {
                     propNet = new Properties();
                  }

                  propNet.store(new FileOutputStream(netFS), (String)null);
               }
            } catch (IOException var22) {
               Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var22);
            }
         }

         if(generalFS == null || !generalFS.exists()) {
            try {
               generalFS = new File(savePath + "dustmodgeneral.dat");
               if(generalFS.createNewFile()) {
                  if(propGeneral == null) {
                     propGeneral = new Properties();
                  }

                  propGeneral.store(new FileOutputStream(generalFS), (String)null);
               }
            } catch (IOException var21) {
               Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var21);
            }
         }

         try {
            propInv.load(new FileInputStream(invFS));
            Iterator var3 = propInv.keySet().iterator();

            Object var4;
            String var5;
            int var6;
            int var8;
            int var9;
            int var10;
            while(var3.hasNext()) {
               var4 = var3.next();
               var5 = (String)var4;

               try {
                  var6 = Integer.valueOf(var5).intValue();
                  if(propInv.size() == 1) {
                     ;
                  }
               } catch (Exception var20) {
                  ;
               }

               String var25 = (String)propInv.get(var4);
               byte var7 = 0;
               if(var25.length() > 0 && var25.charAt(var7) == 91) {
                  int var26 = var7 + 1;
                  var8 = Integer.valueOf(var25.substring(var26, var25.indexOf(44, var26))).intValue();
                  var26 = var25.indexOf(44, var26) + 1;
                  var9 = Integer.valueOf(var25.substring(var26, var25.indexOf(44, var26))).intValue();
                  var26 = var25.indexOf(44, var26) + 1;
                  var10 = Integer.valueOf(var25.substring(var26, var25.indexOf(93, var26))).intValue();
                  var25.indexOf(93, var26);
                  if(voidInventory.get(var5) == null) {
                     voidInventory.put(var5, new ArrayList());
                  }

                  ((ArrayList)voidInventory.get(var5)).add(new ItemStack(var8, var9, var10));
                  System.out.println("Void Inventory loading: " + var5 + "[" + var8 + "," + var9 + "," + var10 + "]");
               }
            }

            propNet.load(new FileInputStream(netFS));
            var3 = propNet.keySet().iterator();

            while(var3.hasNext()) {
               var4 = var3.next();

               try {
                  var5 = (String)var4;
                  var6 = Integer.valueOf(var5).intValue();
                  String var28 = (String)propNet.get(var4);
                  byte var27 = 0;
                  if(var28.length() > 0 && var28.charAt(var27) == 91) {
                     var8 = var27 + 1;
                     var9 = Integer.valueOf(var28.substring(var8, var28.indexOf(44, var8))).intValue();
                     var8 = var28.indexOf(44, var8) + 1;
                     var10 = Integer.valueOf(var28.substring(var8, var28.indexOf(44, var8))).intValue();
                     var8 = var28.indexOf(44, var8) + 1;
                     int var11 = Integer.valueOf(var28.substring(var8, var28.indexOf(44, var8))).intValue();
                     var8 = var28.indexOf(44, var8) + 1;
                     int var12 = Integer.valueOf(var28.substring(var8, var28.indexOf(44, var8))).intValue();
                     var8 = var28.indexOf(44, var8) + 1;
                     int var13 = Integer.valueOf(var28.substring(var8, var28.indexOf(44, var8))).intValue();
                     var8 = var28.indexOf(44, var8) + 1;
                     int var14 = Integer.valueOf(var28.substring(var8, var28.indexOf(44, var8))).intValue();
                     var8 = var28.indexOf(44, var8) + 1;
                     int var15 = Integer.valueOf(var28.substring(var8, var28.indexOf(44, var8))).intValue();
                     var8 = var28.indexOf(44, var8) + 1;
                     int var16 = Integer.valueOf(var28.substring(var8, var28.indexOf(93, var8))).intValue();
                     var28.indexOf(93, var8);
                     voidNetwork.add(new int[]{var9, var10, var11, var12, var13, var14, var15, var16});
                     System.out.println("Void Network loading: [" + var9 + "," + var10 + "," + var11 + "] " + var12 + ":" + var13 + " dim:" + var15 + " ver:" + var16);
                  }
               } catch (Exception var19) {
                  ;
               }
            }
         } catch (IOException var24) {
            Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var24);
         }

         try {
            propGeneral.load(new FileInputStream(generalFS));
            entMap = new HashMap();
            nextDustEntID = Long.valueOf(propGeneral.getProperty("entDustNID")).longValue();
         } catch (IOException var17) {
            Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var17);
         } catch (NumberFormatException var18) {
            nextDustEntID = 0L;
            propGeneral.setProperty("entDustNID", "" + nextDustEntID);
         }
      }

   }

   public static void updateVoidInventory() {
      try {
         propInv.load(new FileInputStream(invFS));
         if(propInv != null) {
            Iterator var0 = propInv.keySet().iterator();

            while(var0.hasNext()) {
               Object var1 = var0.next();
               String var2 = (String)var1;
               propInv.setProperty(var2, "");
            }

            var0 = voidInventory.keySet().iterator();

            while(var0.hasNext()) {
               String var7 = (String)var0.next();
               ArrayList var8 = (ArrayList)voidInventory.get(var7);
               if(var8 != null) {
                  Iterator var3 = var8.iterator();

                  while(var3.hasNext()) {
                     ItemStack var4 = (ItemStack)var3.next();
                     propInv.setProperty(var7, "[" + var4.id + "," + var4.count + "," + var4.getData() + "]");
                  }
               }
            }

            try {
               propInv.store(new FileOutputStream(invFS), (String)null);
            } catch (IOException var5) {
               Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var5);
            }
         }
      } catch (IOException var6) {
         Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var6);
      }

   }

   public static void addItemToVoidInventory(EntityDust var0, ItemStack var1) {
      if(voidInventory.get(var0.summonerUN) == null) {
         voidInventory.put(var0.summonerUN, new ArrayList());
      }

      ((ArrayList)voidInventory.get(var0.summonerUN)).add(var1);
   }

   public static ArrayList getVoidInventory(EntityDust var0) {
      return (ArrayList)voidInventory.get(var0.summonerUN);
   }

   public static void clearVoidInventory(EntityDust var0) {
      voidInventory.put(var0.summonerUN, (Object)null);
   }

   public static boolean containsWarp(int[] var0) {
      Iterator var1 = voidNetwork.iterator();

      int[] var2;
      do {
         if(!var1.hasNext()) {
            return false;
         }

         var2 = (int[])var1.next();
      } while(var2[0] != var0[0] || var2[1] != var0[1] || var2[2] != var0[2]);

      return true;
   }

   public static void addWarp(int[] var0) {
      if(!containsWarp(var0)) {
         voidNetwork.add(var0);
         updateVoidNetwork();
      }

   }

   public static void removeWarp(int[] var0) {
      for(int var1 = 0; var1 < voidNetwork.size(); ++var1) {
         int[] var2 = (int[])voidNetwork.get(var1);
         if(var2[0] == var0[0] && var2[1] == var0[1] && var2[2] == var0[2]) {
            voidNetwork.remove(var1);
            updateVoidNetwork();
            return;
         }
      }

   }

   public static int getVoidNetworkIndex(int[] var0) {
      for(int var1 = 0; var1 < voidNetwork.size(); ++var1) {
         int[] var2 = (int[])voidNetwork.get(var1);
         if(var2[0] == var0[0] && var2[1] == var0[1] && var2[2] == var0[2]) {
            return var1;
         }
      }

      return -1;
   }

   public static int[] toWarp(EntityDust var0) {
      return new int[]{var0.getX(), ((Integer[])var0.dustPoints.get(0))[1].intValue(), var0.getZ(), var0.data, var0.worldObj.getData(var0.getX(), ((Integer[])var0.dustPoints.get(0))[1].intValue() - 1, var0.getZ()), (int)var0.rotationYaw, var0.worldObj.worldProvider.dimension, 1};
   }

   public static EntityDust getWarpEntity(int[] var0, World var1) {
      double var2 = (double)var0[0];
      double var4 = (double)var0[1];
      double var6 = (double)var0[2];
      double var8 = 2.0D;
      List var10 = var1.getEntities((Entity)null, AxisAlignedBB.b(var2, var4, var6, var2 + 1.0D, var4 + 1.0D, var6 + 1.0D).grow(var8, var8, var8));
      Iterator var11 = var10.iterator();

      Entity var12;
      do {
         if(!var11.hasNext()) {
            return null;
         }

         var12 = (Entity)var11.next();
      } while(!(var12 instanceof EntityDust));

      EntityDust var13 = (EntityDust)var12;
      return var13;
   }

   public static synchronized void updateVoidNetwork() {
      if(propNet != null) {
         try {
            propNet.load(new FileInputStream(netFS));
            Object[] var0 = propNet.keySet().toArray();
            Object[] var1 = var0;
            int var2 = var0.length;

            int var6;
            for(int var3 = 0; var3 < var2; ++var3) {
               Object var4 = var1[var3];
               String var5 = (String)var4;
               var6 = Integer.valueOf(var5).intValue();
               String var7 = (String)propNet.get(var4);
               propNet.setProperty(var5, "");
            }

            ArrayList var11 = (ArrayList)voidNetwork.clone();

            for(var2 = 0; var2 < var11.size(); ++var2) {
               int[] var12 = (int[])var11.get(var2);
               String var13 = "[";
               int[] var14 = var12;
               var6 = var12.length;

               for(int var15 = 0; var15 < var6; ++var15) {
                  int var8 = var14[var15];
                  var13 = var13 + var8 + ",";
               }

               var13 = var13.substring(0, var13.length() - 1) + "]";
               propNet.setProperty(var2 + "", "[" + var12[0] + "," + var12[1] + "," + var12[2] + "," + var12[3] + "," + var12[4] + "," + var12[5] + "," + var12[6] + "," + var12[7] + "]");
            }

            try {
               propNet.store(new FileOutputStream(netFS), (String)null);
            } catch (IOException var9) {
               Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var9);
            }
         } catch (IOException var10) {
            Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var10);
         }
      }

   }

   public String getVersion() {
      return "v0.6.1";
   }

   public void load() {
      MinecraftForge.registerConnectionHandler(new PacketHandler());
      nextDustEntID = 0L;
      entMap = new HashMap();
      groundTex = ModLoader.addOverride("/terrain.png", path + "/dust.png");
      dust = (new BlockDust(BLOCK_DustID, 164)).setHardness(0.2F).a(0.45F).a(Block.g).a("dustblock").j();
      idust = (new ItemDust(ITEM_DustID, dust)).setItemName("idust");
      dustTable = (new BlockDustTable(BLOCK_DustTableID)).c(2.5F).a(Block.e).a("dustTable");
      tome = (new ItemRunicTome(ITEM_RunicTomeID)).setItemName("dustlibrary").d(ModLoader.addOverride("/gui/items.png", path + "/notebook.png"));
      negateSacrifice = (new Item(ITEM_SacrificeNegationID)).a("negateSacrifice").d(ModLoader.addOverride("/gui/items.png", path + "/cancel.png"));
      runicPaper = (new Item(ITEM_RunicPaperID)).a("runicPaper").d(ModLoader.addOverride("/gui/items.png", path + "/runicPaper.png"));
      dustScroll = (new ItemPlaceScroll(ITEM_DustScrollID)).setItemName("dustscroll");
      rutBlock = (new BlockRut(BLOCK_RutID)).setBlockName("dustrutblock").c(3.0F);
      ModLoader.registerBlock(rutBlock);
      chisel = (new ItemChisel(ITEM_ChiselID)).setItemName("itemdustchisel");
      spiritPickaxe = (new ItemSpiritPickaxe(ITEM_SpiritPickID, EnumToolMaterial.DIAMOND)).setIconIndex(ModLoader.addOverride("/gui/items.png", path + "/spiritpick.png")).a("dustpickaxeSpirit");
      spiritSword = (new ItemSpiritSword(ITEM_SpiritSwordID)).setIconIndex(ModLoader.addOverride("/gui/items.png", path + "/spiritsword.png")).a("dustswordSpirit");
      ModLoader.registerBlock(dust);
      ModLoader.registerBlock(dustTable);
      ModLoader.addLocalization("tile.plantdust.name", "Plant Runic Dust");
      ModLoader.addLocalization("tile.gundust.name", "Gunpowder Runic Dust");
      ModLoader.addLocalization("tile.lapisdust.name", "Lapis Runic Dust");
      ModLoader.addLocalization("tile.blazedust.name", "Blaze Runic Dust");
      ModLoader.addLocalization("tile.dust.name", "Do not use this.");
      ModLoader.addName(dustTable, "Runic Lexicon");
      ModLoader.addName(tome, "Runic Tome");
      ModLoader.addName(negateSacrifice, "Negate Rune Sacrifice");
      ModLoader.addName(runicPaper, "Scroll Paper");
      ModLoader.addName(spiritPickaxe, "Spirit Pickaxe");
      ModLoader.addName(spiritSword, "Spirit Sword");
      ModLoader.addName(chisel, "Hammer&Chisel");
      ModLoader.registerTileEntity(TileEntityDust.class, "dusttileentity");
      ModLoader.registerTileEntity(TileEntityDustTable.class, "dusttabletileentity");
      ModLoader.registerTileEntity(TileEntityRut.class, "dustruttileentity");
      ModLoader.setInGUIHook(this, true, false);
      ModLoader.setInGameHook(this, true, false);
      dustModelID = ModLoader.getUniqueBlockModelID(this, true);
      rutModelID = ModLoader.getUniqueBlockModelID(this, true);
      if(debug) {
         ModLoader.addShapelessRecipe(new ItemStack(Block.DIRT, 64), new Object[]{Block.DIRT});
         ModLoader.addShapelessRecipe(new ItemStack(Item.MONSTER_EGG, 64, 90), new Object[]{Block.WOOD});
         ModLoader.addShapelessRecipe(new ItemStack(Block.LONG_GRASS, 64, 1), new Object[]{Block.DIRT, Block.DIRT});
         ModLoader.addShapelessRecipe(new ItemStack(Item.COAL, 64, 0), new Object[]{new ItemStack(Block.LONG_GRASS, 4, 1), Block.DIRT});
         ModLoader.addShapelessRecipe(new ItemStack(Item.SULPHUR, 64, 0), new Object[]{new ItemStack(Block.LONG_GRASS, 4, 1), Block.DIRT, Block.DIRT});
         ModLoader.addShapelessRecipe(new ItemStack(Item.INK_SACK, 64, 4), new Object[]{new ItemStack(Item.COAL, 4, 0), Block.DIRT});
         ModLoader.addShapelessRecipe(new ItemStack(Item.BLAZE_POWDER, 64, 0), new Object[]{new ItemStack(Item.INK_SACK, 4, 4), Block.DIRT});
         ModLoader.addShapelessRecipe(new ItemStack(tome, 1), new Object[]{Block.DIRT, Block.DIRT, Block.DIRT});
         ModLoader.addShapelessRecipe(new ItemStack(Item.PAPER, 64), new Object[]{Block.DIRT, Block.DIRT, Block.DIRT, Block.DIRT});
      }

      ModLoader.registerEntityID(EntityDust.class, "dustentity", ENTITY_FireSpriteID);
      ModLoader.registerEntityID(EntityBlock.class, "dustblockentity", ENTITY_BlockEntityID);
      MinecraftForge.registerEntity(EntityDust.class, this, ENTITY_FireSpriteID, 128, 5, false);
      MinecraftForge.registerEntity(EntityBlock.class, this, ENTITY_BlockEntityID, 64, 1, false);
      ModLoader.addRecipe(new ItemStack(dustTable, 1), new Object[]{"dwd", "wbw", "dwd", Character.valueOf('d'), new ItemStack(idust, 1, -1), Character.valueOf('w'), new ItemStack(Block.WOOD, 1, -1), Character.valueOf('b'), new ItemStack(tome, 1)});
      ModLoader.addRecipe(new ItemStack(dustTable, 1), new Object[]{"wdw", "dbd", "wdw", Character.valueOf('d'), new ItemStack(idust, 1, -1), Character.valueOf('w'), new ItemStack(Block.WOOD, 1, -1), Character.valueOf('b'), new ItemStack(tome, 1)});
      ModLoader.addRecipe(new ItemStack(chisel, 1), new Object[]{"st", "i ", Character.valueOf('s'), new ItemStack(Block.COBBLESTONE, 1), Character.valueOf('t'), new ItemStack(Item.STICK, 1), Character.valueOf('i'), new ItemStack(Item.IRON_INGOT, 1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), new ItemStack(Block.LONG_GRASS, 1, -1), new ItemStack(Block.LONG_GRASS, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), new ItemStack(Block.LEAVES, 1, -1), new ItemStack(Block.LEAVES, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), new ItemStack(Block.SAPLING, 1, -1), new ItemStack(Block.SAPLING, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), Item.SEEDS, Item.SEEDS});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), Block.CACTUS, Block.CACTUS});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), Block.CACTUS, Item.SEEDS});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), Block.CACTUS, new ItemStack(Block.SAPLING, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), Block.CACTUS, new ItemStack(Block.LEAVES, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), Block.CACTUS, new ItemStack(Block.LONG_GRASS, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), Item.SEEDS, new ItemStack(Block.SAPLING, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), Item.SEEDS, new ItemStack(Block.LEAVES, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), Item.SEEDS, new ItemStack(Block.LONG_GRASS, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), new ItemStack(Block.SAPLING, 1, -1), new ItemStack(Block.LEAVES, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), new ItemStack(Block.SAPLING, 1, -1), new ItemStack(Block.LONG_GRASS, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 1), new Object[]{new ItemStack(Item.COAL.id, 1, -1), new ItemStack(Block.LEAVES, 1, -1), new ItemStack(Block.LONG_GRASS, 1, -1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 3, 2), new Object[]{Item.SULPHUR, new ItemStack(idust, 1, 1), new ItemStack(idust, 1, 1)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 4, 3), new Object[]{new ItemStack(Item.COAL.id, 1, -1), new ItemStack(Item.INK_SACK, 2, 4), new ItemStack(Item.INK_SACK, 2, 4), new ItemStack(Item.INK_SACK, 2, 4)});
      ModLoader.addShapelessRecipe(new ItemStack(idust, 3, 4), new Object[]{Item.BLAZE_POWDER, new ItemStack(idust, 1, 3), new ItemStack(idust, 1, 3), new ItemStack(idust, 1, 3)});
      ModLoader.addShapelessRecipe(new ItemStack(tome, 1), new Object[]{new ItemStack(idust, 1, 1), Item.BOOK});
      ModLoader.addShapelessRecipe(new ItemStack(tome, 1), new Object[]{new ItemStack(idust, 1, 2), Item.BOOK});
      ModLoader.addShapelessRecipe(new ItemStack(tome, 1), new Object[]{new ItemStack(idust, 1, 3), Item.BOOK});
      ModLoader.addShapelessRecipe(new ItemStack(tome, 1), new Object[]{new ItemStack(idust, 1, 4), Item.BOOK});
      ModLoader.addShapelessRecipe(new ItemStack(runicPaper, 1), new Object[]{Item.PAPER, Item.GOLD_NUGGET, Item.GOLD_NUGGET});
      DustManager.registerAllShapes();

      for(int var1 = 0; var1 < DustManager.shapes.size(); ++var1) {
         ModLoader.addLocalization("tile.scroll" + DustManager.getShape(var1).name + ".name", DustManager.getShape(var1).pName + " Placing Scroll");
      }

      this.loadMap(ModLoader.getMinecraftServerInstance());
   }

   public static Long getNextDustEntityID() {
      ++nextDustEntID;
      propGeneral.setProperty("entDustNID", "" + nextDustEntID);

      try {
         propGeneral.store(new FileOutputStream(generalFS), (String)null);
      } catch (FileNotFoundException var1) {
         Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var1);
      } catch (IOException var2) {
         Logger.getLogger(mod_DustMod.class.getName()).log(Level.SEVERE, (String)null, var2);
      }

      return Long.valueOf(nextDustEntID - 1L);
   }

   public static void registerEntityDust(EntityDust var0, long var1) {
      entMap.put(Long.valueOf(var1), var0);
   }

   public static EntityDust getDustAtID(long var0) {
      return entMap.containsKey(Long.valueOf(var0))?(EntityDust)entMap.get(Long.valueOf(var0)):null;
   }

   public void addRenderer(Map var1) {}

   public static boolean isDust(int var0) {
      return var0 == dust.id;
   }

   public void loadTex() {
      if(tex == null) {
         tex = new int[16];
         if(allTex) {
            for(int var1 = 0; var1 < 16; ++var1) {
               tex[var1] = ModLoader.addOverride("/terrain.png", path + "/dust" + (var1 + 1) + ".png");
            }
         } else {
            tex[0] = ModLoader.addOverride("/terrain.png", path + "/dust0.png");
            tex[1] = ModLoader.addOverride("/terrain.png", path + "/dust1.png");
            tex[2] = ModLoader.addOverride("/terrain.png", path + "/dust2.png");
            tex[3] = ModLoader.addOverride("/terrain.png", path + "/dust3.png");
            tex[4] = ModLoader.addOverride("/terrain.png", path + "/dust4.png");
         }

      }
   }

   public static void callShape(World var0, double var1, double var3, double var5, int[][] var7, List var8, String var9) {
      DustShape var10 = null;
      new ArrayList();
      int var12 = var7.length;
      int var13 = var7[0].length;
      int var14 = 0;
      int var15 = 0;
      boolean var16 = false;

      int var17;
      int var18;
      for(var17 = 0; var17 < var7.length; ++var17) {
         for(var18 = 0; var18 < var7[0].length; ++var18) {
            if((var17 < var12 || var18 < var13) && var7[var17][var18] != 0) {
               if(var17 < var12) {
                  var12 = var17;
               }

               if(var18 < var13) {
                  var13 = var18;
               }

               var16 = true;
            }

            if(var7[var17][var18] != 0) {
               if(var17 > var14) {
                  var14 = var17;
               }

               if(var18 > var15) {
                  var15 = var18;
               }
            }
         }
      }

      var17 = Math.abs(var14 - var12) + 1;
      var18 = Math.abs(var15 - var13) + 1;
      if(var17 < 4) {
         var12 = 0;
         var14 = 3;
         var17 = 4;
      }

      if(var18 < 4) {
         var13 = 0;
         var15 = 3;
         var18 = 4;
      }

      int[][] var19 = new int[var17][var18];

      int var21;
      int var20;
      for(var20 = var12; var20 <= var14; ++var20) {
         for(var21 = var13; var21 <= var15; ++var21) {
            var19[var20 - var12][var21 - var13] = 0;
            var19[var20 - var12][var21 - var13] = var7[var20][var21];
         }
      }

      int[][] var31 = var19;
      var21 = var19.length;

      int var22;
      for(var22 = 0; var22 < var21; ++var22) {
         int[] var23 = var31[var22];
         int[] var24 = var23;
         int var25 = var23.length;

         for(int var26 = 0; var26 < var25; ++var26) {
            int var27 = var24[var26];
            if(var27 == -2) {
               Iterator var28 = var8.iterator();

               while(var28.hasNext()) {
                  Integer[] var29 = (Integer[])var28.next();
                  int var30 = var0.getTypeId(var29[0].intValue(), var29[1].intValue(), var29[2].intValue());
                  if(var30 == dust.id) {
                     var0.setData(var29[0].intValue(), var29[1].intValue(), var29[2].intValue(), 2);
                  }
               }

               return;
            }
         }
      }

      for(var20 = 0; var20 < DustManager.shapes.size(); ++var20) {
         DustShape var35 = (DustShape)DustManager.shapes.get(var20);
         int[][] var33;
         if((var33 = var35.compareData(var19)) != null) {
            var19 = var33;
            var10 = var35;
            break;
         }
      }

      if(var10 != null) {
         System.out.println("Found: " + var10.name);
         DustManager.initiate(var10, var10.name, var1, var3, var5, var0, var8, var19, var9);
      } else {
         Iterator var34 = var8.iterator();

         while(var34.hasNext()) {
            Integer[] var32 = (Integer[])var34.next();
            var22 = var0.getTypeId(var32[0].intValue(), var32[1].intValue(), var32[2].intValue());
            if(var22 == dust.id) {
               var0.setData(var32[0].intValue(), var32[1].intValue(), var32[2].intValue(), 2);
            }
         }

         System.out.println("nothing found.");
      }

   }

   public static int compareDust(int var0, int var1) {
      int var2 = dustValue(var1);
      int var3 = dustValue(var0);
      if(var2 != -1 && var3 != -1) {
         return var2 == var3?0:(var2 > var3?1:(var3 < var2?-1:-1));
      } else {
         throw new IllegalArgumentException("Invalid dust ID.");
      }
   }

   public static int dustValue(int var0) {
      int var1 = -1;
      if(var0 <= 5) {
         var1 = var0;
      }

      return var1;
   }

   public static int[] getColor(int var0) {
      switch(var0) {
      case -2:
         return new int[]{206, 0, 224};
      case -1:
      case 0:
      default:
         System.out.println("COLOR GET WRONG " + var0);
         return new int[]{255, 0, 0};
      case 1:
         return new int[]{194, 227, 0};
      case 2:
         return new int[]{102, 100, 100};
      case 3:
         return new int[]{0, 135, 255};
      case 4:
         return new int[]{255, 110, 30};
      }
   }

   public static ItemStack getDrop(Entity var0) {
      return null;
   }

   public static int getEntityIDFromDrop(ItemStack var0, int var1) {
      Iterator var2 = entdrops.keySet().iterator();

      ItemStack var3;
      do {
         do {
            do {
               if(!var2.hasNext()) {
                  return -1;
               }

               var3 = (ItemStack)var2.next();
            } while(var3.id != var0.id);
         } while(var0.count < var3.id * var1 && var0.count != -1);
      } while(var3.getData() != var0.getData() && var3.getData() != -1);

      return ((Integer)entdrops.get(var3)).intValue();
   }

   public static int getStackSizeForEntityDrop(int var0, int var1) {
      Iterator var2 = entdrops.keySet().iterator();

      ItemStack var3;
      do {
         do {
            if(!var2.hasNext()) {
               return -1;
            }

            var3 = (ItemStack)var2.next();
         } while(var3.id != var0);
      } while(var3.getData() != var1 && var1 != -1);

      return var3.count;
   }

   public static boolean isMobIDHostile(int var0) {
      Entity var1 = EntityTypes.a(var0, ModLoader.getMinecraftServerInstance().getWorldServer(0));
      return var1 instanceof IMonster;
   }

   public Object getGuiElement(int var1, EntityHuman var2, World var3, int var4, int var5, int var6) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public boolean clientSideRequired() {
      return true;
   }

   public boolean serverSideRequired() {
      return false;
   }

   static {
      entdrops.put(new ItemStack(Item.RAW_CHICKEN.id, 4, 0), Integer.valueOf(93));
      entdrops.put(new ItemStack(Item.RAW_BEEF.id, 4, 0), Integer.valueOf(92));
      entdrops.put(new ItemStack(Block.BIG_MUSHROOM_2, 16, -1), Integer.valueOf(96));
      entdrops.put(new ItemStack(Item.RAW_FISH.id, 8, 0), Integer.valueOf(98));
      entdrops.put(new ItemStack(Item.PORK.id, 4, 0), Integer.valueOf(90));
      entdrops.put(new ItemStack(Block.WOOL.id, 8, -1), Integer.valueOf(91));
      entdrops.put(new ItemStack(Item.INK_SACK.id, 4, 0), Integer.valueOf(94));
      entdrops.put(new ItemStack(Block.BRICK.id, 8, 0), Integer.valueOf(120));
      entdrops.put(new ItemStack(Item.ENDER_PEARL.id, 8, 0), Integer.valueOf(58));
      entdrops.put(new ItemStack(Item.LEATHER.id, 16, 0), Integer.valueOf(95));
      entdrops.put(new ItemStack(Item.GOLD_NUGGET.id, 16, 0), Integer.valueOf(57));
      entdrops.put(new ItemStack(Item.BLAZE_ROD.id, 16, 0), Integer.valueOf(61));
      entdrops.put(new ItemStack(Item.SPIDER_EYE.id, 8, 0), Integer.valueOf(59));
      entdrops.put(new ItemStack(Item.SULPHUR.id, 8, 0), Integer.valueOf(50));
      entdrops.put(new ItemStack(Item.GHAST_TEAR.id, 8, 0), Integer.valueOf(56));
      entdrops.put(new ItemStack(Item.MAGMA_CREAM.id, 8, 0), Integer.valueOf(62));
      entdrops.put(new ItemStack(Block.SMOOTH_BRICK.id, 16, 1), Integer.valueOf(60));
      entdrops.put(new ItemStack(Item.BONE.id, 16, 0), Integer.valueOf(51));
      entdrops.put(new ItemStack(Item.SLIME_BALL.id, 16, 0), Integer.valueOf(55));
      entdrops.put(new ItemStack(Item.STRING.id, 16, 0), Integer.valueOf(52));
      entdrops.put(new ItemStack(Item.ROTTEN_FLESH.id, 8, 0), Integer.valueOf(54));
      entdrops.put(new ItemStack(Block.SNOW.id, 8, 0), Integer.valueOf(97));
      entdrops.put(new ItemStack(Block.IRON_BLOCK.id, 8, 0), Integer.valueOf(99));
      entdrops.put(new ItemStack(Block.DRAGON_EGG.id, 64, 0), Integer.valueOf(63));
      entdrops.put(new ItemStack(Block.DIAMOND_BLOCK.id, 64, 0), Integer.valueOf(53));
   }
}
