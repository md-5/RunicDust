package net.minecraft.server.dustmod.events;

import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DEFlatten extends DustEvent {

   public int tickRate = 20;


   public void onInit(EntityDust var1) {
      int var2 = var1.dusts[9][9];
      int var3 = var1.dusts[9][10];
      int var4 = var1.dusts[10][9];
      int var5 = var1.dusts[10][10];
      if(var2 == var3 && var3 == var4 && var4 == var5) {
         ItemStack[] var7 = new ItemStack[]{new ItemStack(Block.IRON_ORE, 20)};
         var7 = this.sacrifice(var1, var7);
         byte var8 = 0;
         switch(var2) {
         case 1:
            var8 = 10;
            break;
         case 2:
            var8 = 12;
            break;
         case 3:
            var8 = 15;
            break;
         case 4:
            var8 = 20;
         }

         if(var7[0].count <= 0 && this.takeXP(var1, var8)) {
            var1.setColorOuter(114, 53, 62);
            var1.renderStar = true;
            var1.data = var2;
         } else {
            var1.fizzle();
         }
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      if(var1.ticksExisted % this.tickRate == 0) {
         byte var2 = 0;
         byte var3 = 4;
         switch(var1.data) {
         case 1:
            var2 = 4;
            break;
         case 2:
            var2 = 6;
            var3 = 5;
            break;
         case 3:
            var2 = 8;
            var3 = 7;
            break;
         case 4:
            var2 = 10;
            var3 = 9;
         }

         int var4 = var1.ticksExisted / this.tickRate;
         if(var4 > var2) {
            var4 = var2;
         }

         int var5 = var1.getX();
         int var6 = var1.getY();
         int var7 = var1.getZ();
         World var8 = var1.worldObj;

         for(int var9 = -var4; var9 <= var4; ++var9) {
            for(int var10 = -var4; var10 <= var4; ++var10) {
               if(var9 != 0 || var10 != 0) {
                  for(int var11 = var3 - 1; var11 >= 0; --var11) {
                     int var12 = var8.getTypeId(var5 + var9, var6 + var11, var7 + var10);
                     int var13 = var8.getTypeId(var5 + var9, var6 + var11, var7 + var10);
                     if(var12 != 0 && !mod_DustMod.isDust(var12) && !(Block.byId[var12] instanceof BlockContainer)) {
                        int var14 = var8.getTypeId(var5 + var9, var6 + var11 + 1, var7 + var10);
                        int var15 = var8.getTypeId(var5 + var9, var6 + var11 - 1, var7 + var10);
                        if(var12 == Block.WATER.id || var12 == Block.STATIONARY_WATER.id) {
                           var8.setTypeId(var5 + var9, var6 + var11, var7 + var10, Block.COBBLESTONE.id);
                           var12 = 0;
                           var13 = 0;
                        }

                        if(var14 == Block.WATER.id || var14 == Block.STATIONARY_WATER.id) {
                           var8.setTypeId(var5 + var9, var6 + var11 + 1, var7 + var10, Block.COBBLESTONE.id);
                        }

                        if(var15 == 0) {
                           var8.setTypeIdAndData(var5 + var9, var6 + var11 - 1, var7 + var10, var12, var13);
                        }

                        var8.setTypeId(var5 + var9, var6 + var11, var7 + var10, 0);
                        break;
                     }
                  }
               }
            }
         }

         if(var1.ticksExisted / this.tickRate > var2 + var3) {
            var1.fade();
         }
      }

   }
}
