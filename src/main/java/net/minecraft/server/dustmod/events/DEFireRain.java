package net.minecraft.server.dustmod.events;

import net.minecraft.server.EntityArrow;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;

public class DEFireRain extends PoweredEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      var1.renderStar = true;
      var1.setColorOuter(255, 0, 0);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.BLAZE_ROD, 4)};
      var2 = this.sacrifice(var1, var2);
      if(!this.checkSacrifice(var2)) {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      byte var2 = 100;
      byte var3 = 20;

      for(int var4 = 0; var4 < var3 && var1.ticksExisted % 5 == 0; ++var4) {
         EntityArrow var5 = new EntityArrow(var1.worldObj, var1.posX + Math.random() * (double)var2 * 2.0D - (double)var2, 158.0D, var1.posZ + Math.random() * (double)var2 * 2.0D - (double)var2);
         var5.motionX = 0.0D;
         var5.motionY = -2.0D;
         var5.motionZ = 0.0D;
         var5.setFire(100);
         var5.fromPlayer = false;
         var1.worldObj.addEntity(var5);
      }

   }

   public int getStartFuel() {
      return 12000;
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
