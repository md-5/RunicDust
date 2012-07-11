package net.minecraft.server.dustmod.events;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DEFarm extends DustEvent {

   public void onInit(EntityDust var1) {
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.IRON_INGOT, 8, -1)};
      this.sacrifice(var1, var2);
      if(var2[0].count <= 0 && this.takeXP(var1, 4)) {
         int var3 = var1.dusts[var1.dusts.length / 2][var1.dusts[0].length / 2];
         byte var4 = 1;
         byte var5 = 0;
         byte var6 = 1;
         switch(var3) {
         case 1:
            var4 = 1;
            var5 = 0;
            var6 = 2;
            break;
         case 2:
            var4 = 2;
            var5 = 1;
            var6 = 3;
            break;
         case 3:
            var4 = 3;
            var5 = 3;
            var6 = 3;
            break;
         case 4:
            var4 = 4;
            var5 = 4;
            var6 = 5;
         }

         int var7 = var1.getX();
         int var8 = var1.getY();
         int var9 = var1.getZ();
         World var10 = var1.world;
         var10.setTypeId(var7, var8 - 1, var9, Block.STATIONARY_WATER.id);
         Random var11 = new Random();

         for(int var12 = -var4; var12 <= var4; ++var12) {
            int var13 = -var4;

            while(var13 <= var4) {
               int var14 = var4;

               while(true) {
                  if(var14 >= -var4) {
                     int var15 = var10.getTypeId(var12 + var7, var14 + var8, var13 + var9);
                     int var16 = var10.getTypeId(var12 + var7, var14 + var8 - 1, var13 + var9);
                     if(var16 != Block.DIRT.id && var16 != Block.GRASS.id && var16 != Block.SOIL.id && var16 != Block.SAND.id || var15 != 0 && !mod_DustMod.isDust(var15) && var15 != Block.LONG_GRASS.id) {
                        --var14;
                        continue;
                     }

                     var10.setTypeId(var7 + var12, var8 + var14 - 1, var9 + var13, Block.SOIL.id);
                     int var17 = var5 + var11.nextInt(var6);
                     if(var17 > 7) {
                        var17 = 7;
                     }

                     var10.setTypeIdAndData(var7 + var12, var8 + var14, var9 + var13, 0, 0);
                     var10.setTypeIdAndData(var7 + var12, var8 + var14, var9 + var13, Block.CROPS.id, var17);
                  }

                  ++var13;
                  break;
               }
            }
         }

         var10.setTypeId(var7, var8 - 1, var9, Block.STATIONARY_WATER.id);
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      var1.fade();
   }
}
