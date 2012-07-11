package net.minecraft.server.dustmod.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.EntityItem;
import net.minecraft.server.ItemStack;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DEVoid extends DustEvent {

   public void onInit(EntityDust var1) {
      if(!this.takeXP(var1, 3)) {
         var1.fizzle();
      } else {
         var1.renderStar = true;
         var1.setColorInner(255, 0, 255);
         var1.setColorOuter(255, 0, 255);
         List var2 = this.getSacrifice(var1);
         if(var2 != null && !var2.isEmpty()) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               EntityItem var4 = (EntityItem)var3.next();
               mod_DustMod.addItemToVoidInventory(var1, var4.itemStack);
               mod_DustMod.killEntity(var4);
            }

            mod_DustMod.updateVoidInventory();
            var1.data = 0;
         } else {
            var1.starScale = 1.02F;
            var1.data = 1;
         }

      }
   }

   public void onTick(EntityDust var1) {
      if(var1.data == 1) {
         if(var1.ticksLived > 100) {
            var1.fade();
            ArrayList var2 = mod_DustMod.getVoidInventory(var1);
            if(var2 == null) {
               return;
            }

            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               ItemStack var4 = (ItemStack)var3.next();
               EntityItem var5 = null;
               var5 = new EntityItem(var1.world, var1.locX, var1.locY - 0.0D, var1.locZ, var4);
               if(var5 != null) {
                  var5.setPosition(var1.locX, var1.locY, var1.locZ);
                  var1.world.addEntity(var5);
               }
            }

            mod_DustMod.clearVoidInventory(var1);
            mod_DustMod.updateVoidInventory();
         }
      } else {
         if(var1.ticksLived > 35) {
            var1.ticksLived += 3;
            var1.starScale = (float)((double)var1.starScale - 0.001D);
         }

         if(var1.ticksLived > 100) {
            var1.aI();
         }
      }

   }
}
