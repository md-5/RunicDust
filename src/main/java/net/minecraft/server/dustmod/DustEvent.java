package net.minecraft.server.dustmod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.Sacrifice;
import net.minecraft.server.dustmod.TileEntityDust;
import net.minecraft.server.dustmod.TileEntityRut;

public abstract class DustEvent {

   protected ArrayList waitingSacrifices = new ArrayList();
   private int sacID = 0;
   public String name;
   public boolean secret = false;
   public boolean allowed = true;
   public boolean permaAllowed = true;
   public boolean overrideRemote = false;


   public DustEvent addSacr(Sacrifice var1) {
      if(this.sacID >= this.waitingSacrifices.size()) {
         this.waitingSacrifices.add(new ArrayList());
      }

      ((ArrayList)this.waitingSacrifices.get(this.sacID)).add(var1);
      return this;
   }

   public DustEvent addSacrificeList(Sacrifice ... var1) {
      List var2 = Arrays.asList(var1);
      if(this.sacID >= this.waitingSacrifices.size()) {
         this.waitingSacrifices.add(new ArrayList());
      }

      ArrayList var3 = (ArrayList)this.waitingSacrifices.get(this.sacID);
      var3.addAll(var2);
      ++this.sacID;
      return this;
   }

   public DustEvent shiftSacr() {
      ++this.sacID;
      return this;
   }

   public final void init(EntityDust var1) {
      this.onInit(var1);
   }

   protected void onInit(EntityDust var1) {}

   public final void tick(EntityDust var1) {
      if(var1.sacrificeWaiting > 0) {
         --var1.sacrificeWaiting;
         Iterator var2 = this.waitingSacrifices.iterator();

         while(var2.hasNext()) {
            ArrayList var3 = (ArrayList)var2.next();
            Sacrifice[] var4 = this.sacrifice(var1, var3);
            boolean var5 = true;
            Sacrifice[] var6 = var4;
            int var7 = var4.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Sacrifice var9 = var6[var8];
               if(!var9.isComplete) {
                  var5 = false;
                  break;
               }
            }

            if(var5) {
               this.handle(var1, var4);
               var1.sacrificeWaiting = -1;
            }
         }
      } else {
         if(var1.sacrificeWaiting == 0) {
            System.out.println("Waiting sacrifice = 0 death");
            var1.fizzle();
            return;
         }

         this.onTick(var1);
      }

   }

   protected void onTick(EntityDust var1) {}

   public void onRightClick(EntityDust var1, TileEntityDust var2, EntityHuman var3) {}

   public final void unload(EntityDust var1) {
      this.onUnload(var1);
   }

   protected void onUnload(EntityDust var1) {
      if(var1.rutPoints != null) {
         Iterator var2 = var1.rutPoints.iterator();

         while(var2.hasNext()) {
            Integer[] var3 = (Integer[])var2.next();
            TileEntityRut var4 = (TileEntityRut)var1.world.getTileEntity(var3[0].intValue(), var3[1].intValue(), var3[2].intValue());
            if(var4 != null) {
               var4.isBeingUsed = false;
            }
         }
      }

   }

   protected boolean takeXP(EntityDust var1, int var2) {
      EntityHuman var3 = var1.world.findNearbyPlayer(var1, 12.0D);
      if(var3 != null && var3.abilities.canInstantlyBuild) {
         return true;
      } else if(var3 != null && var3.expLevel >= var2) {
         var3.expLevel -= var2;
         return true;
      } else {
         return false;
      }
   }

   protected boolean takeHunger(EntityDust var1, int var2) {
      EntityHuman var3 = var1.world.findNearbyPlayer(var1, 12.0D);
      if(var3 != null && var3.abilities.canInstantlyBuild) {
         return true;
      } else if(var3 != null && var3.getFoodData().a() >= var2) {
         var3.getFoodData().eat(-var2, 0.0F);
         return true;
      } else {
         return false;
      }
   }

   protected boolean checkSacrifice(ItemStack[] var1) {
      ItemStack[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack var5 = var2[var4];
         if(var5.count > 0) {
            return false;
         }
      }

      return true;
   }

   protected List getEntities(World var1, double var2, double var4, double var6) {
      return this.getEntities(var1, var2, var4, var6, 1.0D);
   }

   protected List getEntities(Entity var1) {
      return this.getEntities(var1.world, var1.locX, var1.locY - (double)var1.height, var1.locZ, 1.0D);
   }

   protected List getEntities(Entity var1, double var2) {
      return this.getEntities(var1.world, var1.locX, var1.locY - (double)var1.height, var1.locZ, var2);
   }

   protected List getEntities(World var1, double var2, double var4, double var6, double var8) {
      List var10 = var1.getEntities((Entity)null, AxisAlignedBB.b(var2, var4, var6, var2 + 1.0D, var4 + 1.0D, var6 + 1.0D).grow(var8, var8, var8));
      return var10;
   }

   protected List getEntities(World var1, Class var2, double var3, double var5, double var7, double var9) {
      List var11 = var1.a(var2, AxisAlignedBB.b(var3, var5, var7, var3 + 1.0D, var5 + 1.0D, var7 + 1.0D).grow(var9, var9, var9));
      return var11;
   }

   protected List getEntitiesExcluding(World var1, Entity var2, double var3, double var5, double var7, double var9) {
      List var11 = var1.getEntities(var2, AxisAlignedBB.b(var3, var5, var7, var3 + 1.0D, var5 + 1.0D, var7 + 1.0D).grow(var9, var9, var9));
      return var11;
   }

   protected final List getSacrifice(EntityDust var1) {
      return this.getSacrifice(var1, 1.0D);
   }

   protected final List getSacrifice(EntityDust var1, double var2) {
      ArrayList var4 = new ArrayList();
      List var5 = this.getEntities(var1.world, var1.locX, var1.locY - 0.0D, var1.locZ, var2);
      Iterator var6 = var5.iterator();

      while(var6.hasNext()) {
         Object var7 = var6.next();
         if(var7 instanceof EntityItem) {
            EntityItem var8 = (EntityItem)var7;
            var4.add(var8);
         }
      }

      return var4;
   }

   protected final boolean takeSacrifice(EntityDust var1, ItemStack var2) {
      List var3 = this.getSacrifice(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         EntityItem var5 = (EntityItem)var4.next();
         if(var5.itemStack.id == mod_DustMod.negateSacrifice.id) {
            return true;
         }

         if(var5.itemStack.id == var2.id && (var2.getData() == -1 || var5.itemStack.getData() == var2.getData())) {
            if(var5.itemStack.count > var2.count || var2.count <= 0) {
               var5.itemStack.count -= var2.count;
               break;
            }

            var2.count -= var5.itemStack.count;
            mod_DustMod.killEntity(var5);
         }
      }

      return var2.count <= 0;
   }

   protected final void fizzle() {}

   protected final void beam() {}

   protected final void star() {}

   public void registerFollower(EntityDust var1, Object var2) {}

   protected ItemStack[] sacrifice(EntityDust var1, ItemStack[] var2) {
      List var3 = this.getSacrifice(var1);
      boolean var4 = false;
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         EntityItem var6 = (EntityItem)var5.next();
         ItemStack var7 = var6.itemStack;
         if(var7.id == mod_DustMod.negateSacrifice.id) {
            var4 = true;
            break;
         }

         ItemStack[] var8 = var2;
         int var9 = var2.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            ItemStack var11 = var8[var10];
            if(var11.id == var7.id && (var11.getData() == -1 || var11.getData() == var7.getData())) {
               while(var11.count > 0 && var7.count > 0) {
                  --var7.count;
                  --var11.count;
               }
            }
         }

         if(var7.count <= 0) {
            mod_DustMod.killEntity(var6);
         }
      }

      if(var4) {
         ItemStack[] var12 = var2;
         int var13 = var2.length;

         for(int var14 = 0; var14 < var13; ++var14) {
            ItemStack var15 = var12[var14];
            var15.count = 0;
         }
      }

      return var2;
   }

   protected Sacrifice[] sacrifice(EntityDust var1, ArrayList var2) {
      List var3 = this.getEntities(var1, 3.0D);
      Sacrifice[] var4 = new Sacrifice[var2.size()];

      for(int var5 = 0; var5 < var2.size(); ++var5) {
         Sacrifice var6 = (Sacrifice)var2.get(var5);
         var4[var5] = var6.clone();
      }

      boolean var13 = false;
      Iterator var15 = var3.iterator();

      while(var15.hasNext()) {
         Entity var7 = (Entity)var15.next();
         EntityItem var8 = null;
         if(var7 instanceof EntityItem) {
            var8 = (EntityItem)var7;
         }

         if(var8 != null && var8.itemStack.id == mod_DustMod.negateSacrifice.id) {
            var13 = true;
            break;
         }

         Sacrifice[] var9 = var4;
         int var10 = var4.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            Sacrifice var12 = var9[var11];
            if(!var12.isComplete && !(var7 instanceof EntityHuman) && !(var7 instanceof EntityDust) && var12.matchObject(var7)) {
               if(var8 != null) {
                  var12.itemType.count -= var8.itemStack.count;
                  if(var12.itemType.count == 0) {
                     var12.isComplete = true;
                  }
               } else {
                  var12.isComplete = true;
               }

               System.out.println("Sacrifice matched to " + var7 + " " + var12.isComplete);
               var12.entity = var7;
               break;
            }
         }
      }

      if(var13) {
         Sacrifice[] var14 = var4;
         int var16 = var4.length;

         for(int var18 = 0; var18 < var16; ++var18) {
            Sacrifice var17 = var14[var18];
            var17.isComplete = true;
         }
      }

      return var4;
   }

   public void handle(EntityDust var1, Sacrifice[] var2) {
      Sacrifice[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Sacrifice var6 = var3[var5];
         var6.handleObject(var1, var6.entity);
      }

   }

   protected final void findRuts(EntityDust var1) {
      World var2 = var1.world;
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.dustPoints.iterator();

      while(var4.hasNext()) {
         Integer[] var5 = (Integer[])var4.next();
         var3.add(new Integer[]{var5[0], Integer.valueOf(var5[1].intValue() - 1), var5[2]});
         this.checkNeighbors(var2, var3, var5[0].intValue(), var5[1].intValue() - 1, var5[2].intValue());
      }

      var1.rutPoints = var3;
   }

   protected final void findRutsWithDistance(EntityDust var1, int var2) {
      World var3 = var1.world;
      ArrayList var4 = new ArrayList();
      Iterator var5 = var1.dustPoints.iterator();

      while(var5.hasNext()) {
         Integer[] var6 = (Integer[])var5.next();
         var4.add(new Integer[]{var6[0], Integer.valueOf(var6[1].intValue() - 1), var6[2]});
         this.checkNeighborsWithDistance(var3, var4, var6[0].intValue(), var6[1].intValue() - 1, var6[2].intValue(), var2 - 1);
      }

      var1.rutPoints = var4;
   }

   protected final void findRuts(EntityDust var1, int var2) {
      World var3 = var1.world;
      ArrayList var4 = new ArrayList();
      Iterator var5 = var1.dustPoints.iterator();

      while(var5.hasNext()) {
         Integer[] var6 = (Integer[])var5.next();
         var4.add(new Integer[]{var6[0], Integer.valueOf(var6[1].intValue() - 1), var6[2]});
         this.checkNeighbors(var3, var4, var6[0].intValue(), var6[1].intValue() - 1, var6[2].intValue(), var2);
      }

      var1.rutPoints = var4;
   }

   protected final boolean findRutArea(EntityDust var1) {
      World var2 = var1.world;
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      List var7 = var1.rutPoints;
      if(var7 == null) {
         this.findRuts(var1);
         var7 = var1.rutPoints;
         if(var7 == null) {
            return false;
         }
      }

      int var8 = var7.size();

      Integer[] var10;
      int var11;
      int var12;
      int var13;
      int var14;
      int var17;
      int var16;
      int var19;
      int var18;
      int var20;
      for(int var9 = 0; var9 < var8; ++var9) {
         var10 = (Integer[])var7.get(var9);
         var11 = var10[0].intValue();
         var12 = var10[1].intValue();
         var13 = var10[2].intValue();

         for(var14 = 0; var14 < var8; ++var14) {
            if(var9 != var14) {
               Integer[] var15 = (Integer[])var7.get(var14);
               var16 = var15[0].intValue();
               var17 = var15[1].intValue();
               var18 = var15[2].intValue();
               Integer[] var21;
               if(var12 == var17 && var11 == var16 && var18 > var13) {
                  var19 = var18 - var13;

                  for(var20 = 1; var20 < var19; ++var20) {
                     var21 = new Integer[]{Integer.valueOf(var16), Integer.valueOf(var17), Integer.valueOf(var13 + var20)};
                     var4.add(var21);
                  }
               }

               if(var12 == var17 && var13 == var18 && var16 > var11) {
                  var19 = var16 - var11;

                  for(var20 = 1; var20 < var19; ++var20) {
                     var21 = new Integer[]{Integer.valueOf(var11 + var20), Integer.valueOf(var17), Integer.valueOf(var18)};
                     var3.add(var21);
                  }
               }

               if(var11 == var16 && var13 == var18 && var17 > var12) {
                  var19 = var17 - var12;

                  for(var20 = 1; var20 < var19; ++var20) {
                     var21 = new Integer[]{Integer.valueOf(var16), Integer.valueOf(var12 + var20), Integer.valueOf(var18)};
                     var5.add(var21);
                  }
               }
            }
         }
      }

      Iterator var22 = var3.iterator();

      Iterator var28;
      Integer[] var31;
      boolean var30;
      label225:
      while(var22.hasNext()) {
         var10 = (Integer[])var22.next();
         var11 = var10[0].intValue();
         var12 = var10[1].intValue();
         var13 = var10[2].intValue();
         var30 = false;
         var28 = var5.iterator();

         while(var28.hasNext()) {
            var31 = (Integer[])var28.next();
            var17 = var31[0].intValue();
            var18 = var31[1].intValue();
            var19 = var31[2].intValue();
            if(var11 == var17 && var12 == var18 && var13 == var19) {
               var6.add(new Integer[]{Integer.valueOf(var17), Integer.valueOf(var18), Integer.valueOf(var19)});
               var30 = true;
            }
         }

         if(!var30) {
            var28 = var4.iterator();

            do {
               if(!var28.hasNext()) {
                  continue label225;
               }

               var31 = (Integer[])var28.next();
               var17 = var31[0].intValue();
               var18 = var31[1].intValue();
               var19 = var31[2].intValue();
            } while(var11 != var17 || var12 != var18 || var13 != var19);

            var6.add(new Integer[]{Integer.valueOf(var17), Integer.valueOf(var18), Integer.valueOf(var19)});
            var30 = true;
         }
      }

      var22 = var5.iterator();

      while(var22.hasNext()) {
         var10 = (Integer[])var22.next();
         var11 = var10[0].intValue();
         var12 = var10[1].intValue();
         var13 = var10[2].intValue();
         var30 = false;
         var28 = var3.iterator();

         while(var28.hasNext()) {
            var31 = (Integer[])var28.next();
            var17 = var31[0].intValue();
            var18 = var31[1].intValue();
            var19 = var31[2].intValue();
            if(var17 == var17 && var18 == var18 && var19 == var19) {
               var6.add(new Integer[]{Integer.valueOf(var17), Integer.valueOf(var18), Integer.valueOf(var19)});
               var30 = true;
            }
         }

         if(!var30) {
            var28 = var4.iterator();

            while(var28.hasNext()) {
               var31 = (Integer[])var28.next();
               var17 = var31[0].intValue();
               var18 = var31[1].intValue();
               var19 = var31[2].intValue();
               if(var11 == var17 && var12 == var18 && var13 == var19) {
                  var6.add(new Integer[]{Integer.valueOf(var17), Integer.valueOf(var18), Integer.valueOf(var19)});
                  var30 = true;
               }
            }
         }
      }

      var22 = var4.iterator();

      while(var22.hasNext()) {
         var10 = (Integer[])var22.next();
         var11 = var10[0].intValue();
         var12 = var10[1].intValue();
         var13 = var10[2].intValue();
         var30 = false;
         var28 = var5.iterator();

         while(var28.hasNext()) {
            var31 = (Integer[])var28.next();
            var17 = var31[0].intValue();
            var18 = var31[1].intValue();
            var19 = var31[2].intValue();
            if(var17 == var11 && var18 == var12 && var19 == var13) {
               var6.add(new Integer[]{Integer.valueOf(var17), Integer.valueOf(var18), Integer.valueOf(var19)});
               var30 = true;
            }
         }

         if(!var30) {
            var28 = var3.iterator();

            while(var28.hasNext()) {
               var31 = (Integer[])var28.next();
               var17 = var31[0].intValue();
               var18 = var31[1].intValue();
               var19 = var31[2].intValue();
               if(var17 == var17 && var18 == var18 && var19 == var19) {
                  var6.add(new Integer[]{Integer.valueOf(var17), Integer.valueOf(var18), Integer.valueOf(var19)});
                  var30 = true;
               }
            }
         }
      }

      ArrayList var24 = new ArrayList();
      int var23 = 0;

      while(var23 < var6.size()) {
         Integer[] var25 = (Integer[])var6.get(var23);
         var12 = var25[0].intValue();
         var13 = var25[1].intValue();
         var14 = var25[2].intValue();
         var28 = var24.iterator();

         while(true) {
            if(var28.hasNext()) {
               Integer var35 = (Integer)var28.next();
               if(var35.intValue() != var23) {
                  continue;
               }
            } else {
               boolean var32 = false;

               Integer[] var33;
               for(var16 = 0; var16 < var6.size(); ++var16) {
                  if(var23 != var16) {
                     var33 = (Integer[])var6.get(var16);
                     var18 = var33[0].intValue();
                     var19 = var33[1].intValue();
                     var20 = var33[2].intValue();
                     if(var12 == var18 && var13 == var19 && var14 == var20) {
                        var24.add(Integer.valueOf(var16));
                        var32 = true;
                     }
                  }
               }

               Iterator var34 = var7.iterator();

               while(var34.hasNext()) {
                  var33 = (Integer[])var34.next();
                  var18 = var33[0].intValue();
                  var19 = var33[1].intValue();
                  var20 = var33[2].intValue();
                  if(var18 == var12 && var19 == var13 && var20 == var14) {
                     var24.add(Integer.valueOf(var23));
                     break;
                  }
               }
            }

            ++var23;
            break;
         }
      }

      ArrayList var26 = new ArrayList();
      var11 = 0;

      while(var11 < var6.size()) {
         Iterator var27 = var24.iterator();

         while(true) {
            if(var27.hasNext()) {
               Integer var29 = (Integer)var27.next();
               if(!var29.equals(Integer.valueOf(var11))) {
                  continue;
               }
            } else {
               var26.add(var6.get(var11));
            }

            ++var11;
            break;
         }
      }

      System.out.println("Area : " + var6.size() + " " + var26.size() + " " + var24.size());
      var1.rutAreaPoints = var26;
      return true;
   }

   protected final boolean findRutArea(EntityDust var1, int var2) {
      World var3 = var1.world;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();
      List var8 = var1.rutPoints;
      if(var8 == null) {
         this.findRuts(var1, var2);
         var8 = var1.rutPoints;
         if(var8 == null) {
            return false;
         }
      }

      int var9 = var8.size();

      Integer[] var11;
      int var12;
      int var13;
      int var14;
      int var15;
      int var17;
      int var19;
      int var18;
      int var21;
      int var20;
      for(int var10 = 0; var10 < var9; ++var10) {
         var11 = (Integer[])var8.get(var10);
         var12 = var11[0].intValue();
         var13 = var11[1].intValue();
         var14 = var11[2].intValue();

         for(var15 = 0; var15 < var9; ++var15) {
            if(var10 != var15) {
               Integer[] var16 = (Integer[])var8.get(var15);
               var17 = var16[0].intValue();
               var18 = var16[1].intValue();
               var19 = var16[2].intValue();
               Integer[] var22;
               if(var13 == var18 && var12 == var17 && var19 > var14) {
                  var20 = var19 - var14;

                  for(var21 = 1; var21 < var20; ++var21) {
                     var22 = new Integer[]{Integer.valueOf(var17), Integer.valueOf(var18), Integer.valueOf(var14 + var21)};
                     var5.add(var22);
                  }
               } else if(var13 == var18 && var14 == var19 && var17 > var12) {
                  var20 = var17 - var12;

                  for(var21 = 1; var21 < var20; ++var21) {
                     var22 = new Integer[]{Integer.valueOf(var12 + var21), Integer.valueOf(var18), Integer.valueOf(var19)};
                     var4.add(var22);
                  }
               } else if(var12 == var17 && var14 == var19 && var18 > var13) {
                  var20 = var18 - var13;

                  for(var21 = 1; var21 < var20; ++var21) {
                     var22 = new Integer[]{Integer.valueOf(var17), Integer.valueOf(var13 + var21), Integer.valueOf(var19)};
                     var6.add(var22);
                  }
               }
            }
         }
      }

      Iterator var23 = var4.iterator();

      Iterator var29;
      boolean var31;
      Integer[] var32;
      while(var23.hasNext()) {
         var11 = (Integer[])var23.next();
         var12 = var11[0].intValue();
         var13 = var11[1].intValue();
         var14 = var11[2].intValue();
         var31 = false;
         var29 = var6.iterator();

         while(var29.hasNext()) {
            var32 = (Integer[])var29.next();
            var18 = var32[0].intValue();
            var19 = var32[1].intValue();
            var20 = var32[2].intValue();
            if(var12 == var18 && var13 == var19 && var14 == var20) {
               var7.add(var32);
               var31 = true;
            }
         }

         if(!var31) {
            var29 = var5.iterator();

            while(var29.hasNext()) {
               var32 = (Integer[])var29.next();
               var18 = var32[0].intValue();
               var19 = var32[1].intValue();
               var20 = var32[2].intValue();
               if(var12 == var18 && var13 == var19 && var14 == var20) {
                  var7.add(var32);
                  var31 = true;
               }
            }
         }
      }

      var23 = var6.iterator();

      while(var23.hasNext()) {
         var11 = (Integer[])var23.next();
         var12 = var11[0].intValue();
         var13 = var11[1].intValue();
         var14 = var11[2].intValue();
         var31 = false;
         var29 = var4.iterator();

         while(var29.hasNext()) {
            var32 = (Integer[])var29.next();
            var18 = var32[0].intValue();
            var19 = var32[1].intValue();
            var20 = var32[2].intValue();
            if(var18 == var18 && var19 == var19 && var20 == var20) {
               var7.add(var32);
               var31 = true;
            }
         }

         if(!var31) {
            var29 = var5.iterator();

            while(var29.hasNext()) {
               var32 = (Integer[])var29.next();
               var18 = var32[0].intValue();
               var19 = var32[1].intValue();
               var20 = var32[2].intValue();
               if(var12 == var18 && var13 == var19 && var14 == var20) {
                  var7.add(var32);
                  var31 = true;
               }
            }
         }
      }

      var23 = var5.iterator();

      while(var23.hasNext()) {
         var11 = (Integer[])var23.next();
         var12 = var11[0].intValue();
         var13 = var11[1].intValue();
         var14 = var11[2].intValue();
         var31 = false;
         var29 = var6.iterator();

         while(var29.hasNext()) {
            var32 = (Integer[])var29.next();
            var18 = var32[0].intValue();
            var19 = var32[1].intValue();
            var20 = var32[2].intValue();
            if(var18 == var12 && var19 == var13 && var20 == var14) {
               var7.add(var32);
               var31 = true;
            }
         }

         if(!var31) {
            var29 = var4.iterator();

            while(var29.hasNext()) {
               var32 = (Integer[])var29.next();
               var18 = var32[0].intValue();
               var19 = var32[1].intValue();
               var20 = var32[2].intValue();
               if(var18 == var18 && var19 == var19 && var20 == var20) {
                  var7.add(var32);
                  var31 = true;
               }
            }
         }
      }

      ArrayList var25 = new ArrayList();
      int var24 = 0;

      while(var24 < var7.size()) {
         Integer[] var27 = (Integer[])var7.get(var24);
         var13 = var27[0].intValue();
         var14 = var27[1].intValue();
         var15 = var27[2].intValue();
         var29 = var25.iterator();

         while(true) {
            if(var29.hasNext()) {
               Integer var33 = (Integer)var29.next();
               if(var33.intValue() != var24) {
                  continue;
               }
            } else {
               boolean var34 = false;

               Integer[] var35;
               for(var17 = 0; var17 < var7.size(); ++var17) {
                  if(var24 != var17) {
                     var35 = (Integer[])var7.get(var17);
                     var19 = var35[0].intValue();
                     var20 = var35[1].intValue();
                     var21 = var35[2].intValue();
                     if(var13 == var19 && var14 == var20 && var15 == var21) {
                        var25.add(Integer.valueOf(var17));
                        var34 = true;
                     }
                  }
               }

               Iterator var36 = var8.iterator();

               while(var36.hasNext()) {
                  var35 = (Integer[])var36.next();
                  var19 = var35[0].intValue();
                  var20 = var35[1].intValue();
                  var21 = var35[2].intValue();
                  if(var19 == var13 && var20 == var14 && var21 == var15) {
                     var25.add(Integer.valueOf(var24));
                     break;
                  }
               }
            }

            ++var24;
            break;
         }
      }

      ArrayList var26 = new ArrayList();
      var12 = 0;

      while(var12 < var7.size()) {
         Iterator var28 = var25.iterator();

         while(true) {
            if(var28.hasNext()) {
               Integer var30 = (Integer)var28.next();
               if(!var30.equals(Integer.valueOf(var12))) {
                  continue;
               }
            } else {
               var26.add(var7.get(var12));
            }

            ++var12;
            break;
         }
      }

      System.out.println("Area : " + var7.size() + " " + var26.size() + " " + var25.size());
      var1.rutAreaPoints = var26;
      return true;
   }

   protected final boolean findRutAreaFlat(EntityDust var1, int var2) {
      World var3 = var1.world;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      new ArrayList();
      ArrayList var7 = new ArrayList();
      List var8 = var1.rutPoints;
      if(var8 == null) {
         this.findRuts(var1, var2);
         var8 = var1.rutPoints;
         if(var8 == null) {
            return false;
         }
      }

      int var9 = var8.size();

      Integer[] var11;
      int var12;
      int var13;
      int var14;
      int var17;
      int var16;
      int var19;
      int var18;
      for(int var10 = 0; var10 < var9; ++var10) {
         var11 = (Integer[])var8.get(var10);
         var12 = var11[0].intValue();
         var13 = var11[2].intValue();

         for(var14 = 0; var14 < var9; ++var14) {
            if(var10 != var14) {
               Integer[] var15 = (Integer[])var8.get(var14);
               var16 = var15[0].intValue();
               var17 = var15[2].intValue();
               Integer[] var20;
               if(var12 == var16 && var17 > var13) {
                  var18 = var17 - var13;

                  for(var19 = 1; var19 < var18; ++var19) {
                     var20 = new Integer[]{Integer.valueOf(var16), Integer.valueOf(var13 + var19)};
                     var5.add(var20);
                  }
               } else if(var13 == var17 && var16 > var12) {
                  var18 = var16 - var12;

                  for(var19 = 1; var19 < var18; ++var19) {
                     var20 = new Integer[]{Integer.valueOf(var12 + var19), Integer.valueOf(var17)};
                     var4.add(var20);
                  }
               }
            }
         }
      }

      Iterator var22 = var4.iterator();

      Iterator var27;
      while(var22.hasNext()) {
         var11 = (Integer[])var22.next();
         var12 = var11[0].intValue();
         var13 = var11[1].intValue();
         boolean var28 = false;
         var27 = var5.iterator();

         while(var27.hasNext()) {
            Integer[] var30 = (Integer[])var27.next();
            var17 = var30[0].intValue();
            var18 = var30[1].intValue();
            if(var12 == var17 && var13 == var18) {
               var7.add(var30);
               var28 = true;
            }
         }
      }

      ArrayList var21 = new ArrayList();
      int var25 = 0;

      while(var25 < var7.size()) {
         Integer[] var24 = (Integer[])var7.get(var25);
         var13 = var24[0].intValue();
         var14 = var24[1].intValue();
         var27 = var21.iterator();

         while(true) {
            if(var27.hasNext()) {
               Integer var29 = (Integer)var27.next();
               if(var29.intValue() != var25) {
                  continue;
               }
            } else {
               boolean var31 = false;

               Integer[] var34;
               for(var16 = 0; var16 < var7.size(); ++var16) {
                  if(var25 != var16) {
                     var34 = (Integer[])var7.get(var16);
                     var18 = var34[0].intValue();
                     var19 = var34[1].intValue();
                     if(var13 == var18 && var14 == var19) {
                        var21.add(Integer.valueOf(var16));
                        var31 = true;
                     }
                  }
               }

               Iterator var33 = var8.iterator();

               while(var33.hasNext()) {
                  var34 = (Integer[])var33.next();
                  var18 = var34[0].intValue();
                  var19 = var34[2].intValue();
                  if(var18 == var13 && var19 == var14) {
                     var21.add(Integer.valueOf(var25));
                     break;
                  }
               }
            }

            ++var25;
            break;
         }
      }

      ArrayList var23 = new ArrayList();
      var12 = 0;

      while(var12 < var7.size()) {
         Iterator var26 = var21.iterator();

         while(true) {
            if(var26.hasNext()) {
               Integer var32 = (Integer)var26.next();
               if(!var32.equals(Integer.valueOf(var12))) {
                  continue;
               }
            } else {
               var23.add(var7.get(var12));
            }

            ++var12;
            break;
         }
      }

      System.out.println("Area : " + var7.size() + " " + var23.size() + " " + var21.size());
      var1.rutAreaPoints = var23;
      return true;
   }

   private final void checkNeighbors(World var1, ArrayList var2, int var3, int var4, int var5) {
      for(int var6 = -1; var6 <= 1; ++var6) {
         for(int var7 = -1; var7 <= 1; ++var7) {
            for(int var8 = -1; var8 <= 1; ++var8) {
               if((var6 != 0 && var6 != 2 || var6 != var7 || var8 != 0 && var8 != 2 || var7 == 0) && (var6 != 0 && var6 != 2 || var7 != 0 && var7 != 2 || var6 == var7 || var8 != 0 && var8 != 2 || var7 == 0)) {
                  int var9 = var1.getTypeId(var3 + var6, var4 + var7, var5 + var8);
                  if(var9 == mod_DustMod.rutBlock.id) {
                     TileEntityRut var10 = (TileEntityRut)var1.getTileEntity(var3 + var6, var4 + var7, var5 + var8);
                     if(!var10.isBeingUsed) {
                        var10.isBeingUsed = true;
                        var2.add(new Integer[]{Integer.valueOf(var3 + var6), Integer.valueOf(var4 + var7), Integer.valueOf(var5 + var8)});
                        this.checkNeighbors(var1, var2, var3 + var6, var4 + var7, var5 + var8);
                     }
                  }
               }
            }
         }
      }

   }

   private final void checkNeighborsWithDistance(World var1, ArrayList var2, int var3, int var4, int var5, int var6) {
      for(int var7 = -1; var7 <= 1; ++var7) {
         for(int var8 = -1; var8 <= 1; ++var8) {
            for(int var9 = -1; var9 <= 1; ++var9) {
               if((var7 != 0 && var7 != 2 || var7 != var8 || var9 != 0 && var9 != 2 || var8 == 0) && (var7 != 0 && var7 != 2 || var8 != 0 && var8 != 2 || var7 == var8 || var9 != 0 && var9 != 2 || var8 == 0)) {
                  int var10 = var1.getTypeId(var3 + var7, var4 + var8, var5 + var9);
                  if(var10 == mod_DustMod.rutBlock.id) {
                     TileEntityRut var11 = (TileEntityRut)var1.getTileEntity(var3 + var7, var4 + var8, var5 + var9);
                     if(!var11.isBeingUsed && var6 > 0) {
                        var11.isBeingUsed = true;
                        var2.add(new Integer[]{Integer.valueOf(var3 + var7), Integer.valueOf(var4 + var8), Integer.valueOf(var5 + var9)});
                        this.checkNeighborsWithDistance(var1, var2, var3 + var7, var4 + var8, var5 + var9, var6 - 1);
                     }
                  }
               }
            }
         }
      }

   }

   private final void checkNeighbors(World var1, ArrayList var2, int var3, int var4, int var5, int var6) {
      for(int var7 = -1; var7 <= 1; ++var7) {
         for(int var8 = -1; var8 <= 1; ++var8) {
            for(int var9 = -1; var9 <= 1; ++var9) {
               if((var7 != 0 && var7 != 2 || var7 != var8 || var9 != 0 && var9 != 2) && (var7 != 0 && var7 != 2 || var8 != 0 && var8 != 2 || var7 == var8 || var9 != 0 && var9 != 2)) {
                  int var10 = var1.getTypeId(var3 + var7, var4 + var8, var5 + var9);
                  if(var10 == mod_DustMod.rutBlock.id) {
                     TileEntityRut var11 = (TileEntityRut)var1.getTileEntity(var3 + var7, var4 + var8, var5 + var9);
                     if(!var11.isBeingUsed && var11.fluid == var6) {
                        var11.isBeingUsed = true;
                        var2.add(new Integer[]{Integer.valueOf(var3 + var7), Integer.valueOf(var4 + var8), Integer.valueOf(var5 + var9)});
                        this.checkNeighbors(var1, var2, var3 + var7, var4 + var8, var5 + var9);
                     }
                  }
               }
            }
         }
      }

   }

   public DustEvent setSecret(boolean var1) {
      this.secret = var1;
      return this;
   }

   public DustEvent setAllowed(boolean var1) {
      this.allowed = var1;
      return this;
   }

   public DustEvent setPermaAllowed(boolean var1) {
      this.permaAllowed = var1;
      return this;
   }

   public DustEvent setOverrideRemote(boolean var1) {
      this.overrideRemote = var1;
      return this;
   }

   public String toString() {
      return "DustEvent:" + this.name;
   }
}
