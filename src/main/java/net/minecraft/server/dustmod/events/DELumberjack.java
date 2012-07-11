package net.minecraft.server.dustmod.events;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.EntityItem;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DELumberjack extends DustEvent {

   public void onInit(EntityDust var1) {
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.STICK, 2), new ItemStack(Block.LOG, 3, -1)};
      var2 = this.sacrifice(var1, var2);
      if(var2[0].count <= 0 && var2[1].count <= 0) {
         var1.data = var1.dustID;
         var1.renderStar = true;
         var1.setColorOuter(77, 65, 47);
         var1.setColorInner(77, 65, 47);
         var1.starScale = 1.08F;
         int var3 = var1.getX();
         int var4 = var1.getY();
         int var5 = var1.getZ();
         if(var1.world.getTypeId(var3, var4, var5) != Block.LOG.id) {
            var1.fizzle();
         }

      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      if(var1.ticksLived > 100) {
         int var2 = var1.getX();
         int var3 = var1.getY();
         int var4 = var1.getZ();
         World var5 = var1.world;
         if(var5.getTypeId(var2, var3, var4) == Block.LOG.id) {
            this.checkWood(var5, var2, var3, var4, var1.data, var2, var3, var4);
         }

         var1.fade();
      }

   }

   public void checkWood(World var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      double var9 = (double)(var2 - var6);
      double var11 = (double)(var3 - var7);
      double var13 = (double)(var4 - var8);
      double var15 = 8.0D;
      var15 *= var15;
      if(var9 * var9 + var11 * var11 + var13 * var13 <= var15) {
         double var17 = 0.0D;
         double var19 = 0.0D;
         byte var21 = 1;
         byte var22 = 1;
         boolean var27;
         boolean var28;
         switch(var5) {
         case 1:
            var17 = 0.05D;
            var19 = 0.07D;
            var28 = true;
            var27 = true;
         case 2:
            var17 = 0.12D;
            var19 = 0.2D;
            var28 = true;
            var27 = true;
         case 3:
            var17 = 0.2D;
            var19 = 0.35D;
            var28 = true;
            var27 = true;
         case 4:
            var17 = 0.35D;
            var19 = 0.5D;
            var21 = 6;
            var22 = 12;
         }

         Random var23 = new Random();
         int var24;
         if(Math.random() < var17) {
            for(var24 = var23.nextInt(var21); var24 > 0; --var24) {
               Block.LOG.b(var1, var2, var3, var4, var1.getData(var2, var3, var4), 1);
            }
         }

         if(Math.random() < var19) {
            for(var24 = var23.nextInt(var22); var24 > 0; --var24) {
               EntityItem var25 = new EntityItem(var1, (double)var2, (double)var3, (double)var4, new ItemStack(Item.STICK.id, 1, 0));
               var1.addEntity(var25);
            }
         }

         Block.LOG.b(var1, var2, var3, var4, var1.getData(var2, var3, var4), 1);
         var1.setTypeId(var2, var3, var4, 0);

         for(var24 = -2; var24 <= 2; ++var24) {
            for(int var29 = 1; var29 <= 1; ++var29) {
               for(int var26 = -2; var26 <= 2; ++var26) {
                  if((var24 == 0 || var26 == 0) && var1.getTypeId(var2 + var24, var3 + var29, var4 + var26) == Block.LOG.id) {
                     this.checkWood(var1, var2 + var24, var3 + var29, var4 + var26, var5, var6, var7, var8);
                  }
               }
            }
         }

      }
   }
}
