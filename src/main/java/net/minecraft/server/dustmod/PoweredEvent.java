package net.minecraft.server.dustmod;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntityFurnace;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.TileEntityDust;
import net.minecraft.server.dustmod.events.DEPowerRelay;

public abstract class PoweredEvent extends DustEvent {

   public static final int dayLength = 24000;
   public boolean consumeItems = true;


   public void onInit(EntityDust var1) {
      super.onInit(var1);
      if(this.getClass() != DEPowerRelay.class) {
         List var2 = DEPowerRelay.findDustEntities(var1);
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            EntityDust var4 = (EntityDust)var3.next();
            if(var4.event.getClass() == DEPowerRelay.class) {
               ((DEPowerRelay)var4.event).registerSelfTo(var4, var1);
            }
         }
      }

      var1.setFuel(this.getStartFuel());
      var1.requiresFuel = true;
      var1.renderStar = true;
      var1.setColorStar(255, 255, 255);
      var1.starScale = 1.0F;
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      if(var1.getFuel() <= 0 && !this.isPaused(var1)) {
         var1.fade();
      } else {
         if(!this.isPaused(var1)) {
            this.subtractFuel(var1);
         }

         if(this.consumeItems) {
            List var2 = this.getEntities(var1, 1.0D);
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               Entity var4 = (Entity)var3.next();
               if(!var4.dead && var4 instanceof EntityItem) {
                  EntityItem var5 = (EntityItem)var4;
                  var5.damageEntity((DamageSource)null, -20);
                  ItemStack var6 = var5.itemStack;
                  if(TileEntityFurnace.fuelTime(var6) != 0) {
                     this.addFuel(var1, TileEntityFurnace.fuelTime(var6) * var6.count);
                     var5.setDead();
                  }
               }
            }
         }

         if(var1.isFueledExternally()) {
            var1.starScale = 1.04F;
         } else {
            var1.starScale = 1.0F;
         }

         if(!this.isPaused(var1)) {
            double var7 = (double)var1.getFuel() / (double)this.getStableFuelAmount(var1);
            int var9 = (int)(255.0D * var7);
            if(var9 > 255) {
               var9 = 255;
            }

            var1.setColorStar(255, var9, var9);
         } else {
            var1.setColorStar(255, 255, 0);
         }

      }
   }

   public void onRightClick(EntityDust var1, TileEntityDust var2, EntityHuman var3) {
      super.onRightClick(var1, var2, var3);
   }

   public void onUnload(EntityDust var1) {
      super.onUnload(var1);
      if(this.getClass() != DEPowerRelay.class) {
         List var2 = DEPowerRelay.findDustEntities(var1);
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            EntityDust var4 = (EntityDust)var3.next();
            if(var4.event.getClass() == DEPowerRelay.class) {
               ((DEPowerRelay)var4.event).removeSelfFrom(var4, var1);
            }
         }
      }

   }

   public void subtractFuel(EntityDust var1) {
      var1.setFuel(var1.getFuel() - 1);
   }

   public void addFuel(EntityDust var1, int var2) {
      if(var1.getFuel() + var2 <= this.getMaxFuel()) {
         var1.setFuel(var1.getFuel() + var2);
      }
   }

   public abstract int getStartFuel();

   public abstract int getMaxFuel();

   public abstract int getStableFuelAmount(EntityDust var1);

   public abstract boolean isPaused(EntityDust var1);

   public int powerWanted(EntityDust var1) {
      int var2 = var1.getFuel();
      int var3 = 0;
      int var4 = this.getStableFuelAmount(var1);
      if(var4 > var2) {
         var3 = var4 - var2;
      }

      return var3;
   }
}
