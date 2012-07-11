package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.Sacrifice;
import net.minecraft.server.dustmod.TileEntityDust;

public class DEEggifier extends DustEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.EGG, 1), new ItemStack(Item.DIAMOND, 1)};
      var2 = this.sacrifice(var1, var2);
      if(this.checkSacrifice(var2) && this.takeXP(var1, 10)) {
         var1.renderStar = true;
         var1.setColorStar(255, 2555, 255);
         var1.sacrificeWaiting = 600;
         Iterator var3 = mod_DustMod.entdrops.values().iterator();

         while(var3.hasNext()) {
            Integer var4 = (Integer)var3.next();
            this.addSacrificeList(new Sacrifice[]{new Sacrifice(var4.intValue())});
            System.out.println("Adding sacrifice " + var4);
         }

      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      var1.starScale = (float)((double)var1.starScale + 0.001D);
      if(var1.ticksLived > 120) {
         EntityItem var2 = null;
         var2 = new EntityItem(var1.world, var1.locX, var1.locY, var1.locZ, new ItemStack(Item.MONSTER_EGG, 1, var1.data));
         if(var2 != null) {
            var2.setPosition(var1.locX, var1.locY - 0.0D, var1.locZ);
            var1.world.addEntity(var2);
         }

         var1.fade();
      }

   }

   public void onRightClick(EntityDust var1, TileEntityDust var2, EntityHuman var3) {
      super.onRightClick(var1, var2, var3);
   }

   public void onUnload(EntityDust var1) {
      super.onUnload(var1);
   }
}
