package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.IMonster;
import net.minecraft.server.MathHelper;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;

public class DELoyaltySprite extends PoweredEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      var1.reanimate = true;
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      if(var1.data == 0) {
         var1.locY = (double)((float)(var1.getY() - 1) + MathHelper.sin((float)var1.ticksLived / 16.0F) * 0.5F);
         List var2 = this.getEntities(var1, 3.5D);
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Entity var4 = (Entity)var3.next();
            if(var4 instanceof EntityLiving && !(var4 instanceof EntityHuman)) {
               var1.data = EntityTypes.a(var4);
               var1.motX = var1.motX;
               var1.motY = var1.motY;
               var1.motZ = var1.motZ;
            }
         }
      } else {
         EntityHuman var8 = var1.world.a(var1.summonerUN);
         if(var8 == null) {
            return;
         }

         EntityLiving var9 = null;
         if(var1.toFollow == null) {
            --var1.locY;
            List var10 = this.getEntities(var1, 3.0D);
            Iterator var5 = var10.iterator();

            while(var5.hasNext()) {
               Entity var6 = (Entity)var5.next();
               if(var6 instanceof EntityLiving && !(var6 instanceof EntityHuman) && EntityTypes.a(var6) == var1.data) {
                  var9 = (EntityLiving)var6;
               }
            }
         } else {
            var9 = (EntityLiving)var1.toFollow;
         }

         if(var9 == null || var9.dead) {
            if(var1.ram == 1) {
               var1.fade();
            } else if(var1.ram == 0) {
               var1.ram = 10;
            }

            --var1.ram;
            return;
         }

         var9.extinguish();
         var1.toFollow = var9;
         var1.follow = true;
         boolean var11 = false;
         if(var9 instanceof EntityCreature) {
            var11 = ((EntityCreature)var9).I() != null && ((EntityCreature)var9).I() != var8;
         }

         List var12 = this.getEntities(var1, 5.0D);
         if(!var11) {
            Iterator var14 = var12.iterator();

            while(var14.hasNext()) {
               Entity var7 = (Entity)var14.next();
               if(var7 != var9 && var7 != var8) {
                  if(var7 instanceof IMonster && !(var7 instanceof EntityCreeper)) {
                     mod_DustMod.setEntityToAttack((EntityCreature)var9, var7);
                     var11 = true;
                     break;
                  }

                  if(var7 instanceof EntityLiving && var9 instanceof IMonster && var9 instanceof EntityCreature && !var11 && !(var7 instanceof EntityCreeper)) {
                     mod_DustMod.setEntityToAttack((EntityCreature)var9, var7);
                     var11 = true;
                     break;
                  }
               }
            }
         }

         if(var9 instanceof EntityCreature) {
            EntityCreature var13 = (EntityCreature)var9;
            if(var13.I() == var8 && !(var9 instanceof EntityAnimal) && !(var9 instanceof EntityWolf)) {
               var13.attackTicks = 20;
            }

            if(!var11) {
               if(var8.onGround && var13.j(var8) > 256.0D) {
                  var13.setPositionRotation(var8.locX, var8.locY + (double)var8.height, var8.locZ, 0.0F, 0.0F);
               }

               mod_DustMod.setEntityToAttack(var13, var8);
            }
         }
      }

   }

   public int getStartFuel() {
      return 72000;
   }

   public int getMaxFuel() {
      return 168000;
   }

   public int getStableFuelAmount(EntityDust var1) {
      return '\u8ca0';
   }

   public boolean isPaused(EntityDust var1) {
      return false;
   }
}
