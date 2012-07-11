package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.FurnaceRecipes;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;
import net.minecraft.server.dustmod.TileEntityDust;

public class DECampFire extends PoweredEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.ROTTEN_FLESH, 1), new ItemStack(Block.LOG, 8, -1)};
      var2 = this.sacrifice(var1, var2);
      if(var2[0].count <= 0 && var2[1].count <= 0) {
         var1.data = 2400;
         int var3 = var1.worldObj.getTypeId(var1.getX(), var1.getY(), var1.getZ());
         if(var3 != Block.FIRE.blockID) {
            int var4 = var1.worldObj.getTypeId(var1.getX(), var1.getY() - 1, var1.getZ());
            if(var3 == 0 && var4 != 0 && Block.byId[var4].isBlockSolidOnSide(var1.worldObj, var1.getX(), var1.getY() - 1, var1.getZ(), 1)) {
               var1.worldObj.setTypeId(var1.getX(), var1.getY(), var1.getZ(), Block.FIRE.blockID);
            }
         }

      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      int var2 = var1.worldObj.getTypeId(var1.getX(), var1.getY(), var1.getZ());
      if(var2 != Block.FIRE.blockID) {
         if(var1.worldObj.x() && var1.worldObj.isChunkLoaded(var1.getX(), var1.getY(), var1.getZ())) {
            var1.kill();
            return;
         }

         int var3 = var1.worldObj.getTypeId(var1.getX(), var1.getY() - 1, var1.getZ());
         if(var2 != 0 || var3 == 0 || !Block.byId[var3].isBlockSolidOnSide(var1.worldObj, var1.getX(), var1.getY() - 1, var1.getZ(), 1)) {
            var1.kill();
            return;
         }

         var1.worldObj.setTypeId(var1.getX(), var1.getY(), var1.getZ(), Block.FIRE.blockID);
      }

      List var14 = this.getEntities(var1, 0.5D);
      Iterator var4 = var14.iterator();

      while(var4.hasNext()) {
         Entity var5 = (Entity)var4.next();
         if(!var5.dead && var5 instanceof EntityItem) {
            EntityItem var6 = (EntityItem)var5;
            var6.damageEntity((DamageSource)null, -20);
            ItemStack var7 = var6.itemStack;
            ItemStack var8 = FurnaceRecipes.getInstance().getSmeltingResult(var7);
            if(var1.ticksExisted % 20 == 0) {
               double var9 = Math.random();
               double var11 = 0.1D * (double)var6.itemStack.count;
               if(var9 < var11) {
                  var6.setDead();
               } else if(var9 >= 0.6D) {
                  if(var8 != null) {
                     var6.itemStack.id = var8.id;
                     var6.itemStack.count *= var8.count;

                     while(var6.itemStack.count > var6.itemStack.getMaxStackSize()) {
                        EntityItem var13 = new EntityItem(var6.worldObj, var6.posX, var6.posY, var6.posZ, new ItemStack(var6.itemStack.id, var6.itemStack.getMaxStackSize(), var6.itemStack.getData()));
                        var6.worldObj.addEntity(var13);
                        this.shoot(var13);
                     }
                  }

                  this.shoot(var6);
               }
            }
         }
      }

   }

   public void shoot(EntityItem var1) {
      float var2 = 0.12F;
      var1.motionX = (double)((float)var1.worldObj.random.nextGaussian() * var2);
      var1.motionY = (double)((float)var1.worldObj.random.nextGaussian() * var2 + 0.5F);
      var1.motionZ = (double)((float)var1.worldObj.random.nextGaussian() * var2);
   }

   public void onRightClick(EntityDust var1, TileEntityDust var2, EntityHuman var3) {
      super.onRightClick(var1, var2, var3);
   }

   public void onUnload(EntityDust var1) {}

   public int getStartFuel() {
      return 6000;
   }

   public int getMaxFuel() {
      return Integer.MAX_VALUE;
   }

   public int getStableFuelAmount(EntityDust var1) {
      return 24000;
   }

   public boolean isPaused(EntityDust var1) {
      return false;
   }
}
