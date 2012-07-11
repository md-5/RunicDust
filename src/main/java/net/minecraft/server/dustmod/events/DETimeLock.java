package net.minecraft.server.dustmod.events;

import net.minecraft.server.Block;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;

public class DETimeLock extends PoweredEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Block.OBSIDIAN, 8), new ItemStack(Item.SLIME_BALL, 4), new ItemStack(Item.INK_SACK, 1, 4)};
      var2 = this.sacrifice(var1, var2);
      if(!this.checkSacrifice(var2)) {
         var1.fizzle();
      } else {
         var1.renderStar = true;
         var1.setColorInner(0, 255, 255);
         var1.setColorOuter(0, 255, 255);
         int[] var3 = new int[4];
         int[][] var4 = var1.dusts;
         var3[0] = var4[3][2];
         var3[1] = var4[4][2];
         var3[2] = var4[3][3];
         var3[3] = var4[4][3];

         for(int var5 = 0; var5 < 4; ++var5) {
            if(var3[0] != var3[var5]) {
               var1.fizzle();
               return;
            }
         }

         switch(var3[0]) {
         case 1:
            var1.data = 9000;
            break;
         case 2:
            var1.data = 18000;
            break;
         case 3:
            var1.data = 24000;
            break;
         case 4:
            var1.data = '\ubb80';
         }

      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      var1.setColorOuter(0, 255, 255);
      if(var1.ram == 0) {
         if(var1.worldObj.getTime() > 2147483647L) {
            var1.ram = (int)var1.worldObj.getTime() % 24000;
         } else {
            var1.ram = (int)var1.worldObj.getTime();
         }
      }

      var1.worldObj.setTime((long)var1.ram);
      var1.ticksExisted = 100;
      --var1.data;
      if(var1.data <= 0) {
         var1.fade();
      }

   }

   public int getStartFuel() {
      return 24000;
   }

   public int getMaxFuel() {
      return 72000;
   }

   public int getStableFuelAmount(EntityDust var1) {
      return 12000;
   }

   public boolean isPaused(EntityDust var1) {
      return false;
   }
}
