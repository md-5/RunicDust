package net.minecraft.server.dustmod.events;

import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DELunar extends DustEvent {

   public static boolean oneActive = false;


   public void onInit(EntityDust var1) {
      var1.renderBeam = false;
      var1.renderStar = true;
      ItemStack[] var2 = this.sacrifice(var1, new ItemStack[]{new ItemStack(Item.NETHER_STALK, 4), new ItemStack(Item.INK_SACK, 1, 4)});
      if(var2[0].count == 0 && var2[1].count == 0) {
         var1.data = 0;
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      if(var1.data == 1 && !oneActive) {
         oneActive = false;
         var1.data = 0;
         var1.renderBeam = false;
         var1.renderStar = true;
      }

      long var2 = var1.world.getTime() + 1000L;
      if(var1.data == 1 && var1.world.e()) {
         var1.world.setTime(var1.world.getTime() + 25L);
      } else if(var1.data == 0 && var1.world.e() && !oneActive) {
         oneActive = true;
         var1.data = 1;
      }

      if(var1.data == 1) {
         var1.renderBeam = true;
         var1.renderStar = false;
      }

      if(var1.data == 1 && !var1.world.e()) {
         var1.aI();
      }

   }

   protected void onUnload(EntityDust var1) {
      super.onUnload(var1);
      if(var1.data == 1) {
         oneActive = false;
      }

   }

}
