package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityWeatherLighting;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.events.DETrap;

public class DELightning extends DETrap {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.IRON_INGOT, 3)};
      var2 = this.sacrifice(var1, var2);
      if(var2[0].count > 0) {
         var1.fizzle();
      }
   }

   public void trigger(EntityDust var1, int var2) {
      List var3 = this.getEntities(var1, 2.0D * (double)var2);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Entity var5 = (Entity)var4.next();
         if(var5 instanceof EntityLiving && (double)var1.i(var5) < 2.0D * (double)var2) {
            var1.world.strikeLightning(new EntityWeatherLighting(var1.world, var1.locX, var1.locY - 0.0D, var1.locZ));
            var1.fade();
         }
      }

   }
}
