package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.events.DETrap;

public class DEPoisonTrap extends DETrap {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.SPIDER_EYE, 3)};
      var2 = this.sacrifice(var1, var2);
      if(var2[0].count > 0) {
         var1.fizzle();
      }
   }

   public void trigger(EntityDust var1, int var2) {
      byte var3 = 0;
      byte var4 = 0;
      byte var5 = 0;
      switch(var2) {
      case 2:
         var3 = 3;
         var4 = 5;
         var5 = 2;
         break;
      case 3:
         var3 = 4;
         var4 = 7;
         var5 = 4;
         break;
      case 4:
         var3 = 6;
         var4 = 10;
         var5 = 8;
      }

      List var6 = this.getEntities(var1, (double)var3);
      Iterator var7 = var6.iterator();

      while(var7.hasNext()) {
         Entity var8 = (Entity)var7.next();
         if(var8 instanceof EntityLiving) {
            ((EntityLiving)var8).addEffect(new MobEffect(MobEffectList.POISON.id, (var4 + (int)Math.floor(Math.random() * (double)var5)) * 20, 2));
         }
      }

      var1.fade();
   }
}
