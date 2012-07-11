package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;

public class DEXP extends PoweredEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      var1.renderBeam = true;
      var1.renderStar = true;
      var1.renderFlamesDust = true;
      var1.rf = 0;
      var1.gf = 255;
      var1.bf = 0;
      var1.data = 24000;
      var1.setColorBeam(255, 255, 255);
      ++var1.posY;
      int var2 = 1;
      int var3 = 1;
      List var4 = this.getEntities(var1);
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         if(var6 instanceof EntityItem) {
            EntityItem var7 = (EntityItem)var6;
            ItemStack var8 = var7.itemStack;
            if(var8.id == mod_DustMod.negateSacrifice.id) {
               var3 = 0;
               var2 = 0;
               break;
            }

            if(var8.id == Item.MONSTER_EGG.id) {
               int var9 = var8.getData();
               if(mod_DustMod.isMobIDHostile(var9)) {
                  for(; var3 > 0 && var8.count > 0; --var3) {
                     --var8.count;
                     if(var8.count <= 0) {
                        var7.setDead();
                     }
                  }
               } else {
                  for(; var2 > 0 && var8.count > 0; --var2) {
                     --var8.count;
                     if(var8.count <= 0) {
                        var7.setDead();
                     }
                  }
               }
            }
         }
      }

      if(var2 > 0 || var3 > 0 || !this.takeXP(var1, 80)) {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      if(var1.ticksExisted < 60) {
         var1.renderBeam = false;
      } else {
         var1.renderBeam = true;
         EntityHuman var2 = var1.worldObj.a(var1.summonerUN);
         if(var2 != null) {
            double var3 = (double)var1.getFuel() / 2400.0D;
            int var5 = (int)Math.floor(var3 * 255.0D);
            var1.setColorBeam(255, var5, var5);
            int var6 = var1.getX();
            int var7 = var1.getY();
            int var8 = var1.getZ();
            List var9 = this.getEntities(var1, 3.0D);
            Iterator var10 = var9.iterator();

            while(var10.hasNext()) {
               Entity var11 = (Entity)var10.next();
               if(var11.locY > (double)(var1.getY() - 2) && var11.locY < (double)var1.getY() + 1.5D) {
                  if(var11 instanceof EntityItem) {
                     var11.die();
                     this.addFuel(var1, ((EntityItem)var11).itemStack.count * 10);
                  }

                  if(var11 instanceof EntityLiving && var11 != var2) {
                     if(var11.motY < 0.0D) {
                        EntityLiving var12 = (EntityLiving)var11;
                        if(var12.getHealth() > 0) {
                           int var13 = mod_DustMod.getExperiencePoints(var12, (EntityHuman)null);
                           var12.damageEntity(DamageSource.MAGIC, 10000000);

                           for(int var14 = 0; var14 < 2; ++var14) {
                              int var15 = var13;

                              while(var15 > 0) {
                                 int var16 = EntityExperienceOrb.getOrbValue(var15);
                                 var15 -= var16;
                                 double var17 = (double)(var6 + (Math.random() > 0.5D?1:-1)) + Math.random() * 0.4D - 0.2D;
                                 double var19 = (double)(var8 + (Math.random() > 0.5D?1:-1)) + Math.random() * 0.4D - 0.2D;
                                 EntityExperienceOrb var21 = new EntityExperienceOrb(var1.worldObj, var17 + 0.5D, (double)var7, var19 + 0.5D, var16);
                                 var21.motionX = var21.motionY = var21.motionZ = 0.0D;
                                 var1.worldObj.addEntity(var21);
                              }
                           }

                           this.addFuel(var1, 1000);
                           mod_DustMod.killEntity(var11);
                           break;
                        }
                     }
                  } else if(var11 == var2 && var1.ticksExisted % 20 == 0 && var1.ticksExisted != 0) {
                     var2.damageEntity(DamageSource.MAGIC, 2);
                  }
               }
            }

         }
      }
   }

   public int getStartFuel() {
      return 24000;
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
