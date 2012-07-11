package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.IMonster;
import net.minecraft.server.MathHelper;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;
import net.minecraft.server.dustmod.Sacrifice;

public class DEForcefield extends PoweredEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      int var2 = var1.dusts[5][5];
      int var3 = var1.dusts[5][6];
      int var4 = var1.dusts[6][5];
      int var5 = var1.dusts[6][6];
      if(var2 == var3 && var3 == var4 && var4 == var5 && this.takeXP(var1, 50)) {
         var1.setColorOuter(0, 128, 62);
         var1.renderStar = true;
         var1.bb = var2;
         var1.sacrificeWaiting = 600;
         this.addSacrificeList(new Sacrifice[]{new Sacrifice(99), new Sacrifice(99)});
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      float var2 = (float)var1.bb * 8.0F;
      List var3 = this.getEntities(var1, (double)var2);
      double var4 = 1.02D;
      double var6 = 48000.0D;
      Iterator var8 = var3.iterator();

      while(var8.hasNext()) {
         Entity var9 = (Entity)var8.next();
         float var10;
         if(var9 instanceof IMonster && (var10 = var9.i(var1)) < var2) {
            double var11 = var9.locX - var1.locX;
            double var13 = var9.locZ - var1.locZ;
            float var15 = (float)(Math.atan2(var13, var11) * 180.0D / 3.141592653589793D) - 90.0F;
            float var16 = var2 / var10 * 0.5F;
            var9.motX -= (double)(MathHelper.cos((var15 + 270.0F) * 0.01745329F) * var16);
            var9.motZ -= (double)(MathHelper.sin((var15 + 270.0F) * 0.01745329F) * var16);
         }
      }

      if(var1.ticksLived % 25 == 0) {
         var1.shineRadiusSphere(var2, 0.0D, 0.5D, 0.5D);
      }

   }

   public int getStartFuel() {
      return 24000;
   }

   public int getMaxFuel() {
      return '\ubb80';
   }

   public int getStableFuelAmount(EntityDust var1) {
      return 24000;
   }

   public boolean isPaused(EntityDust var1) {
      return false;
   }
}
