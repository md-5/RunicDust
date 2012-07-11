package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DEBounce extends DustEvent {

   public void onInit(EntityDust var1) {
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.SLIME_BALL, 4, -1)};
      this.sacrifice(var1, var2);
      if(var2[0].count > 0) {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      List var2 = this.getEntities(var1, 0.35D);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Entity var4 = (Entity)var3.next();
         if(var4 instanceof EntityLiving) {
            EntityLiving var5 = (EntityLiving)var4;
            double var6 = 0.0D;
            double var8 = var4.motY + var6;
            double var10000 = var1.locY - var5.lastY;
            if(!var5.onGround && !mod_DustMod.isJumping(var5) && var8 < 0.7D) {
               var5.getControllerJump().a();
               var5.getControllerJump().b();
               var4.b_(0.0D, 1.27D, 0.0D);
               var4.velocityChanged = true;
            }

            if(!var5.onGround) {
               var4.fallDistance = 0.0F;
            } else {
               var5.f(false);
            }
         }
      }

   }
}
