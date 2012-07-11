package net.minecraft.server.dustmod.events;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;
import net.minecraft.server.dustmod.TileEntityDust;

public class DEHunterVision extends PoweredEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.BLAZE_POWDER, 3), new ItemStack(Item.EYE_OF_ENDER, 1)};
      var2 = this.sacrifice(var1, var2);
      if(!this.checkSacrifice(var2) || !this.takeXP(var1, 12)) {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      mod_DustMod.hunterVisionActive = var1.data % 2 == 0;
   }

   public void onRightClick(EntityDust var1, TileEntityDust var2, EntityHuman var3) {
      super.onRightClick(var1, var2, var3);
      ++var1.data;
   }

   public void onUnload(EntityDust var1) {
      super.onUnload(var1);
      mod_DustMod.hunterVisionActive = false;
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
      return var1.data % 2 == 1;
   }
}
