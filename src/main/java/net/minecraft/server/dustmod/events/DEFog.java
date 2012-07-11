package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PathEntity;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;
import net.minecraft.server.dustmod.TileEntityDust;

public class DEFog extends PoweredEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.WATER_BUCKET, 1), new ItemStack(Block.RED_MUSHROOM, 1)};
      var2 = this.sacrifice(var1, var2);
      if(!this.checkSacrifice(var2) || !this.takeXP(var1, 6)) {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      if(var1.ticksLived % 5 == 0) {
         boolean var2 = true;
         byte var3 = 10;
         byte var4 = 60;
         float var5 = 1.5F;
         float var6 = 0.4F;
         float var7 = 0.0F;
         float var8 = var5 - var6;
         int var9 = var1.ticksLived % (var4 * 2) > var4?1:-1;
         int var10 = var1.ticksLived % var4 - var4 / 2;
         var10 *= var9;
         float var11 = (float)var10 / (float)var4;
         var7 = var11 * var8 + var6 + var8 / 2.0F;
         if(var1.ticksLived % var4 == 0 && !var1.world.isStatic) {
            List var12 = this.getEntities(var1, (double)var3);
            List var13 = var12.subList(0, var12.size());
            EntityHuman var14 = var1.world.findNearbyPlayer(var1, (double)var3);
            Iterator var15 = var12.iterator();

            while(var15.hasNext()) {
               Object var16 = var15.next();
               Entity var17 = (Entity)var16;
               int var18 = MathHelper.floor(var17.locX);
               int var19 = MathHelper.floor(var17.locY);
               int var20 = MathHelper.floor(var17.locZ);
               int var21 = var1.world.a(EnumSkyBlock.BLOCK, var18, var19, var20);
               if(var21 >= 7) {
                  System.out.println("Err light");
               } else {
                  if(var17 instanceof EntityCreature) {
                     EntityCreature var22 = (EntityCreature)var17;
                     var22.a(var22);
                     var22.b(var22);
                     mod_DustMod.setEntityToAttack(var22, var22);
                     var22.setPathEntity((PathEntity)null);
                     mod_DustMod.setHasAttacked(var22, true);
                     var22.attackTicks = 30;
                     if(var14 != null) {
                        mod_DustMod.setCantSee(var22, var14);
                     }
                  }

                  if(var17 instanceof EntityLiving && Math.random() < 0.8D) {
                     EntityLiving var23 = (EntityLiving)var16;
                     var23.setPositionRotation(var23.locX, var23.locY, var23.locZ, (float)Math.random() * 360.0F, var23.pitch);
                  }
               }
            }
         }
      }

   }

   public void onRightClick(EntityDust var1, TileEntityDust var2, EntityHuman var3) {
      super.onRightClick(var1, var2, var3);
   }

   public void onUnload(EntityDust var1) {
      super.onUnload(var1);
   }

   public int getStartFuel() {
      return 24000;
   }

   public int getMaxFuel() {
      return 168000;
   }

   public int getStableFuelAmount(EntityDust var1) {
      return 24000;
   }

   public boolean isPaused(EntityDust var1) {
      return false;
   }
}
