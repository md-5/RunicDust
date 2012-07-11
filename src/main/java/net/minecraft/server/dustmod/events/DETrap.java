package net.minecraft.server.dustmod.events;

import java.util.List;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public abstract class DETrap extends DustEvent {

   protected double range = 1.0D;


   public void onInit(EntityDust var1) {
      int var2 = mod_DustMod.compareDust(mod_DustMod.gunDID, var1.dustID);
      if(var2 < 0) {
         var1.fizzle();
      } else {
         var1.renderStar = true;
      }
   }

   public void onTick(EntityDust var1) {
      var1.renderStar = true;
      if(var1.ticksExisted < 80) {
         var1.setColorInner(140, 140, 140);
         var1.setColorOuter(140, 140, 140);
      } else {
         var1.setColorInner(0, 0, 255);
         var1.setColorOuter(0, 0, 255);
         List var2 = this.getEntitiesExcluding(var1.worldObj, var1, var1.posX, var1.posY, var1.posZ, 2.0D);
         if(var2.size() > 0) {
            this.trigger(var1, var1.dustID);
            var1.fade();
         }

      }
   }

   public abstract void trigger(EntityDust var1, int var2);
}
