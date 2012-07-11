package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.Entity;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.Sacrifice;
import net.minecraft.server.dustmod.TileEntityRut;

public class DELiftTerrain extends DustEvent {

   public static final int ticksperblock = 32;


   public void onInit(EntityDust var1) {
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Block.RED_ROSE, 1)};
      var2 = this.sacrifice(var1, var2);
      if(this.checkSacrifice(var2) && this.takeXP(var1, 60)) {
         var1.renderStar = true;
         var1.setColorStar(255, 255, 0);
         int var3;
         int var4;
         int var5;
         int var6;
         if(var1.dusts.length > var1.dusts[0].length) {
            var3 = var1.dusts[7][3];
            var4 = var1.dusts[8][3];
            var5 = var1.dusts[8][4];
            var6 = var1.dusts[7][4];
         } else {
            var3 = var1.dusts[3][7];
            var4 = var1.dusts[3][8];
            var5 = var1.dusts[4][8];
            var6 = var1.dusts[4][7];
         }

         if(var3 == var4 && var4 == var5 && var5 == var6) {
            switch(var3) {
            case 1:
               var1.bb = 12;
               break;
            case 2:
               var1.bb = 16;
               break;
            case 3:
               var1.bb = 22;
               break;
            case 4:
               var1.bb = 32;
            }

            var1.renderFlamesRut = true;
            var1.rf = 0;
            var1.gf = 0;
            var1.bf = 255;
            var1.sacrificeWaiting = 600;
            this.addSacrificeList(new Sacrifice[]{new Sacrifice(99)});
            this.loadArea(var1);
         } else {
            var1.fizzle();
         }
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      var1.setColorStar(255, 255, 255);
      if(var1.ticksLived >= 64) {
         if(var1.rutAreaPoints == null) {
            var1.fade();
         }

         World var2 = var1.world;
         int var3 = var1.getX();
         int var4 = var1.getY();
         int var5 = var1.getZ();
         int var6 = var1.bb;
         if(var1.ticksLived % 32 == 0 && var1.gb <= var6) {
            Iterator var7 = var1.rutAreaPoints.iterator();

            while(var7.hasNext()) {
               Integer[] var8 = (Integer[])var7.next();
               var3 = var8[0].intValue();
               var4 = var8[1].intValue();
               var5 = var8[2].intValue();

               for(int var9 = -var6; var9 <= var6; ++var9) {
                  int var10 = -var9 + var1.gb - 1;
                  if(var4 + var10 <= 0) {
                     var1.fade();
                     return;
                  }

                  if(var9 != var6) {
                     int var11 = var2.getTypeId(var3, var4 + var10, var5);
                     int var12 = var2.getData(var3, var4 + var10, var5);
                     int var13 = var2.getTypeId(var3, var4 + var10 + 1, var5);
                     if(var2.getTypeId(var3, var4 + var10 + 2, var5) == 0 && var11 != 0) {
                        List var14 = this.getEntities(var1.world, (double)var3 + 0.5D, (double)var4 + (double)var10 + 1.0D, (double)var5 + 0.5D, 1.0D);
                        Iterator var15 = var14.iterator();

                        while(var15.hasNext()) {
                           Entity var16 = (Entity)var15.next();
                           if(var16 != var1) {
                              var16.setPosition(Math.floor(var16.locX) + 0.5D, (double)var4 + (double)var10 + 2.0D + (double)var16.height, Math.floor(var16.locZ) + 0.5D);
                           }
                        }
                     }

                     Block var20 = Block.byId[var11];
                     Block var10000 = Block.byId[var13];
                     boolean var21 = false;
                     TileEntity var17 = null;
                     NBTTagCompound var18 = null;
                     if(var20 != null && var20 instanceof BlockContainer) {
                        var21 = true;
                        var17 = var2.getTileEntity(var3, var4 + var10, var5);
                        var18 = new NBTTagCompound();
                        var17.b(var18);
                        var17.j();
                     }

                     var2.setTypeIdAndData(var3, var4 + var10 + 1, var5, 0, 0);
                     var2.setTypeIdAndData(var3, var4 + var10 + 1, var5, var11, var12);
                     var2.setTypeId(var3, var4 + var10, var5, Block.STONE.id);
                     if(var21) {
                        TileEntity var19 = var2.getTileEntity(var3, var4 + var10 + 1, var5);
                        if(var19 != null) {
                           var19.a(var18);
                           var19.x = var3;
                           var19.y = var4 + var10 + 1;
                           var19.z = var5;
                           var19.p = var12;
                        }
                     }

                     var2.setData(var3, var4 + var10 + 1, var5, var12);
                  }
               }
            }

            ++var1.gb;
            if(var1.gb >= var6) {
               var1.fade();
            }
         }

      }
   }

   private void loadArea(EntityDust var1) {
      this.findRutAreaFlat(var1, Block.CLAY.id);
      int var2 = var1.getY();
      int var3 = 0;

      for(int var4 = 0; var4 < var1.rutAreaPoints.size(); ++var4) {
         Integer[] var5 = (Integer[])var1.rutAreaPoints.get(var4);
         int var6 = var5[0].intValue();
         int var7 = var5[1].intValue();
         int var8 = var1.world.getHighestBlockYAt(var6, var7);
         if(var8 < var2) {
            var2 = var8;
         }

         if(var8 > var3) {
            var3 = var8;
         }

         var1.rutAreaPoints.set(var4, new Integer[]{Integer.valueOf(var6), Integer.valueOf(var8), Integer.valueOf(var7)});
      }

      var1.gb = var3 - var2;
   }

   public void onUnload(EntityDust var1) {
      if(var1.rutPoints == null) {
         this.findRuts(var1, Block.CLAY.id);
      }

      super.onUnload(var1);
      if(var1.rutPoints != null) {
         Iterator var2 = var1.rutPoints.iterator();

         while(var2.hasNext()) {
            Integer[] var3 = (Integer[])var2.next();
            int var4 = var1.world.random.nextInt(100);
            if(var4 > 15) {
               TileEntityRut var5 = (TileEntityRut)var1.world.getTileEntity(var3[0].intValue(), var3[1].intValue(), var3[2].intValue());
               if(var5 != null) {
                  ;
               }
            }
         }
      }

   }
}
