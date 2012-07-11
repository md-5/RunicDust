package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.Entity;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.dustmod.BlockDust;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.TileEntityDust;

public class DEWall extends DustEvent {

   public static final int ticksperblock = 7;


   public void onInit(EntityDust var1) {
      var1.ignoreRune = true;
      ItemStack[] var2 = this.sacrifice(var1, new ItemStack[]{new ItemStack(Block.IRON_ORE, 5)});
      if(var2[0].count == 0 && this.takeXP(var1, 3)) {
         World var3 = var1.world;
         Integer[][] var4 = new Integer[2][3];
         int var5 = 0;
         Iterator var6 = var1.dustPoints.iterator();

         while(var6.hasNext()) {
            Integer[] var7 = (Integer[])var6.next();
            TileEntity var8 = var3.getTileEntity(var7[0].intValue(), var7[1].intValue(), var7[2].intValue());
            if(var8 != null && var8 instanceof TileEntityDust) {
               TileEntityDust var9 = (TileEntityDust)var8;
               int var10 = 5;
               int var11 = 5;

               for(int var12 = 0; var12 < 4; ++var12) {
                  for(int var13 = 0; var13 < 4; ++var13) {
                     if(var9.getDust(var12, var13) == 1) {
                        --var10;
                     }

                     if(var9.getDust(var12, var13) == 2) {
                        --var11;
                     }
                  }
               }

               if(var10 == 0 && var11 == 0) {
                  var4[var5] = var7;
                  ++var5;
                  if(var5 >= 2) {
                     break;
                  }
               }
            }
         }

         int var14 = var4[0][0].intValue() - var4[1][0].intValue();
         int var15 = var4[0][2].intValue() - var4[1][2].intValue();
         if(var14 == 0 && var15 != 0) {
            var1.data = 1;
         } else {
            if(var15 != 0 || var14 == 0) {
               var1.fizzle();
               return;
            }

            var1.data = 0;
         }

      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      if(var1.ticksLived % 7 == 0) {
         World var2 = var1.world;
         int var3 = var1.ticksLived / 7;
         int var4 = var1.getX();
         int var5 = var1.getY();
         int var6 = var1.getZ();
         boolean var7 = var1.data == 0;
         byte var8 = 2;
         byte var9 = 8;

         int var10;
         for(var10 = -var8; var10 <= var8; ++var10) {
            List var11 = this.getEntities(var1.world, (double)(var4 + (var7?var10:0)), (double)(var5 + var3), (double)(var6 + (var7?0:var10)), 1.0D);
            Iterator var12 = var11.iterator();

            while(var12.hasNext()) {
               Entity var13 = (Entity)var12.next();
               var13.setPosition(var13.locX, (double)(var5 + var3) + 1.8D, var13.locZ);
            }
         }

         for(var10 = -var9; var10 <= var9 + 1; ++var10) {
            for(int var17 = -var8; var17 <= var8; ++var17) {
               if(var5 - var10 + var3 <= 0) {
                  var1.fade();
                  return;
               }

               int var18 = var2.getTypeId(var4 + (var7?var17:0), var5 - var10 + var3, var6 + (var7?0:var17));
               int var19 = var2.getTypeId(var4 + (var7?var17:0), var5 - var10 + var3, var6 + (var7?0:var17));
               int var14 = var2.getTypeId(var4 + (var7?var17:0), var5 - var10 + var3 + 1, var6 + (var7?0:var17));
               Block var15 = Block.byId[var18];
               Block var16 = Block.byId[var14];
               if(var15 instanceof BlockDust) {
                  var18 = 0;
                  var15 = null;
               } else if(var16 instanceof BlockDust) {
                  boolean var20 = false;
                  var16 = null;
               }

               if(var15 != null && var15 instanceof BlockContainer || var16 != null && var16 instanceof BlockContainer) {
                  var1.fade();
                  return;
               }

               var2.setTypeIdAndData(var4 + (var7?var17:0), var5 - var10 + var3 + 1, var6 + (var7?0:var17), var18, var19);
               var2.setTypeId(var4 + (var7?var17:0), var5 - var10 + var3, var6 + (var7?0:var17), 0);
            }
         }

         if(var3 > 4) {
            var1.fade();
         }
      }

   }
}
