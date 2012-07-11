package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockFluids;
import net.minecraft.server.Entity;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DEObelisk extends DustEvent {

   public static final int ticksperblock = 20;


   public void onInit(EntityDust var1) {
      var1.renderBeam = true;
      var1.setColorBeam(114, 53, 62);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Block.IRON_ORE, 2)};
      this.sacrifice(var1, var2);
      if(var2[0].count != 0) {
         var1.fizzle();
      } else {
         var1.ri = 1;
      }
   }

   public void onTick(EntityDust var1) {
      if(var1.ticksLived >= 40) {
         byte var2 = 16;
         World var3 = var1.world;
         int var4 = var1.getX();
         int var5 = var1.getY();
         int var6 = var1.getZ();
         if(var1.ticksLived % 20 == 0 && var1.data < var2) {
            if(var1.ri > 0) {
               List var7 = this.getEntities(var1.world, (double)var4 + 0.5D, (double)var5 + (double)var1.data + 2.0D, (double)var6 + 0.5D, 1.0D);
               Iterator var8 = var7.iterator();

               while(var8.hasNext()) {
                  Entity var9 = (Entity)var8.next();
                  var9.setPosition((double)var4 + 0.5D, (double)var5 + (double)var1.data + 3.0D, (double)var6 + 0.5D);
               }
            }

            int var10;
            Block var12;
            int var14;
            int var15;
            int var16;
            if(var1.ri == 1) {
               for(var14 = -8; var14 < var2; ++var14) {
                  var15 = -var14 + var1.data - 1;
                  if(var5 + var15 <= 0) {
                     var1.fade();
                     return;
                  }

                  var16 = var3.getTypeId(var4, var5 + var15, var6);
                  var10 = var3.getData(var4, var5 + var15, var6);
                  int var11 = var3.getTypeId(var4, var5 + var15 + 1, var6);
                  var12 = Block.byId[var16];
                  if((var16 == 0 || var12 != null && var12 instanceof BlockFluids) && var3.getTypeId(var4, var5 + var15 + 2, var6) != 0) {
                     var16 = Block.COBBLESTONE.id;
                     var12 = Block.COBBLESTONE;
                  }

                  Block var13 = Block.byId[var11];
                  if(var12 != null && var12 instanceof BlockContainer && var13 != null && !(var13 instanceof BlockContainer)) {
                     var1.fade();
                     return;
                  }

                  var3.setTypeIdAndData(var4, var5 + var15 + 1, var6, var16, var10);
                  var3.setTypeId(var4, var5 + var15, var6, 0);
               }
            } else {
               for(var14 = var2; var14 >= -9; --var14) {
                  if(var5 - var14 + var1.data <= 0) {
                     var1.fade();
                     return;
                  }

                  var15 = var3.getTypeId(var4, var5 - var14 + var1.data, var6);
                  var16 = var3.getData(var4, var5 - var14 + var1.data, var6);
                  var10 = var3.getTypeId(var4, var5 - var14 + var1.data + var1.ri, var6);
                  Block var17 = Block.byId[var15];
                  var12 = Block.byId[var10];
                  if(var17 != null && var17 instanceof BlockContainer || var12 != null && var12 instanceof BlockContainer) {
                     var1.fade();
                     return;
                  }

                  var3.setTypeIdAndData(var4, var5 - var14 + var1.data + var1.ri, var6, var15, var16);
                  var3.setTypeId(var4, var5 - var14 + var1.data, var6, 0);
               }
            }

            var1.data += var1.ri;
         }

         if(var1.data >= var2 && var3.getTypeId(var4, var5 + var2 - 1, var6) == 0) {
            var1.ri = -1;
            --var1.data;
         }

         if(var1.data < 0) {
            var1.fade();
         }

         if(var1.ticksLived - 20 * (var2 + 2) > '\u8ca0' && var1.ri > 0) {
            var1.fade();
         }

      }
   }
}
