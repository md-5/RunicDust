package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DEHeal extends DustEvent {

   public void onInit(EntityDust var1) {
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.COAL.id, 2, -1)};
      var2 = this.sacrifice(var1, var2);
      if(this.checkSacrifice(var2) && this.takeXP(var1, 2)) {
         int var3 = var1.dustID;
         byte var4 = 1;
         byte var5 = 0;
         switch(var3) {
         case 1:
            var4 = 1;
            var5 = 4;
            break;
         case 2:
            var4 = 2;
            var5 = 5;
            break;
         case 3:
            var4 = 2;
            var5 = 10;
            break;
         case 4:
            var4 = 5;
            var5 = 32;
         }

         List var6 = this.getEntities(var1, 5.0D);
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            Entity var8 = (Entity)var7.next();
            if(var8 instanceof EntityLiving) {
               EntityLiving var9 = (EntityLiving)var8;
               var9.addEffect(new MobEffect(MobEffectList.REGENERATION.id, var5 * 20, var4));
            }

            if(var8 instanceof EntityHuman) {
               EntityHuman var10 = (EntityHuman)var8;
               if(var3 == 3) {
                  var10.getFoodData().eat(5, 0.6F);
               } else if(var3 == 4) {
                  var10.getFoodData().eat(8, 0.8F);
               }
            }
         }

         var1.starScale = 1.12F;
         var1.setColorInner(255, 255, 255);
         var1.setColorOuter(255, -255, -255);
         var1.renderStar = true;
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      if(var1.ticksExisted > 100) {
         var1.fade();
      }

   }
}
