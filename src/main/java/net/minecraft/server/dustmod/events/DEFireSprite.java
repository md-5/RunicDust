package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;

public class DEFireSprite extends PoweredEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      var1.renderStar = true;
      var1.follow = true;
      var1.setColorInner(255, 0, 0);
      var1.setColorOuter(255, 0, 0);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.GHAST_TEAR, 1), new ItemStack(Item.FIREBALL, 2)};
      this.sacrifice(var1, var2);
      if(!this.checkSacrifice(var2) || !this.takeXP(var1, 22)) {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      var1.renderStar = true;
      var1.follow = true;
      EntityHuman var2 = var1.world.a(var1.summonerUN);
      if(var2 != null) {
         var1.setOnFire(0);
         byte var3 = 3;
         List var4 = this.getEntities(var1, (double)var3);
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            Entity var6 = (Entity)var5.next();
            if(var6 != var2 && var6 != var1 && var6 instanceof EntityLiving) {
               var6.setOnFire(2 + (int)(Math.random() * 5.0D));
            }
         }

         if(var1.ticksLived % 100 == 0 && Math.random() < 0.5D) {
            int var12 = var1.getX();
            int var13 = var1.getY();
            int var7 = var1.getZ();
            boolean var8 = false;

            for(int var9 = -var3; var9 <= var3 && !var8; ++var9) {
               for(int var10 = -var3; var10 <= var3 && !var8; ++var10) {
                  for(int var11 = -var3; var11 <= var3 && !var8; ++var11) {
                     if(var1.world.getTypeId(var12 + var9, var13 + var10 - 1, var7 + var11) != 0 && var1.world.getTypeId(var12 + var9, var13 + var10, var7 + var11) == 0 && Math.random() < 0.05D) {
                        var1.world.setTypeId(var12 + var9, var13 + var10, var7 + var11, Block.FIRE.id);
                        var8 = true;
                     }
                  }
               }
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
