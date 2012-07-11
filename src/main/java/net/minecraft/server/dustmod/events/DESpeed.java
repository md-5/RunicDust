package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DESpeed extends DustEvent {

   public void onInit(EntityDust var1) {
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.SUGAR, 4, -1), new ItemStack(Item.BLAZE_POWDER, 2, -1)};
      this.sacrifice(var1, var2);
      if(var2[0].count <= 0 && var2[1].count <= 0) {
         int var3 = var1.dusts[var1.dusts.length - 1][var1.dusts[0].length - 1];
         byte var4 = 0;
         short var5 = 0;
         switch(var3) {
         case 1:
            var4 = 1;
            var5 = 750;
            break;
         case 2:
            var4 = 1;
            var5 = 1500;
            break;
         case 3:
            var4 = 2;
            var5 = 3000;
            break;
         case 4:
            var4 = 4;
            var5 = 4500;
         }

         List var6 = this.getEntities(var1, 3.0D);
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            Entity var8 = (Entity)var7.next();
            if(var8 instanceof EntityLiving) {
               ((EntityLiving)var8).addEffect(new MobEffect(MobEffectList.FASTER_MOVEMENT.id, var5, var4));
            }
         }

         var1.starScale = 1.12F;
         var1.setColorOuter(0, 255, 0);
         var1.renderStar = true;
         var1.fade();
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {}
}
