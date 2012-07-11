package net.minecraft.server.dustmod.events;

import java.util.List;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DEBomb extends DustEvent {

   public void onInit(EntityDust var1) {
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.SULPHUR, 5)};
      var2 = this.sacrifice(var1, var2);
      if(var2[0].count > 0) {
         var2 = new ItemStack[]{new ItemStack(mod_DustMod.idust, 15, 2)};
         var2 = this.sacrifice(var1, var2);
         if(var2[0].count > 0) {
            var1.fizzle();
            return;
         }
      }

      int[] var3 = new int[4];
      int[] var4 = new int[4];
      int[][] var5 = var1.dusts;
      var3[0] = var5[3][1];
      var3[1] = var5[4][1];
      var3[2] = var5[3][2];
      var3[3] = var5[4][2];
      var4[0] = var5[0][4];
      var4[1] = var5[1][4];
      var4[2] = var5[1][3];
      var4[3] = var5[2][3];

      int var6;
      for(var6 = 0; var6 < 4; ++var6) {
         if(var3[0] != var3[var6]) {
            var1.fizzle();
            return;
         }

         if(var4[0] != var4[var6]) {
            var1.fizzle();
            return;
         }
      }

      var6 = var3[0];
      int var7 = var4[0];
      var6 <<= 3;
      var1.data = var6 | var7;
      var1.renderStar = true;
   }

   public void onTick(EntityDust var1) {
      int var2 = var1.data & 7;
      int var3 = var1.data >> 3 & 7;
      var1.renderStar = true;
      if(var2 != 1 && var1.ticksLived < var2 * 30) {
         var1.setColorInner(140, 140, 140);
         var1.setColorOuter(140, 140, 140);
      } else {
         var1.setColorInner(0, 0, 255);
         var1.setColorOuter(0, 0, 255);
         List var4 = this.getEntities(var1);
         if(var4.size() > 0 || var2 > 1) {
            this.trigger(var1, var3);
            var1.fade();
         }

      }
   }

   public void trigger(EntityDust var1, int var2) {
      var1.world.explode(var1, var1.locX, var1.locY - 0.0D, var1.locZ, (float)(var2 * var2) + 2.0F);
   }
}
