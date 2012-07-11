package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.TileEntityDust;
import net.minecraft.server.dustmod.events.DETrap;

public class DECage extends DETrap {

   public void onInit(EntityDust var1) {
      var1.renderStar = true;
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.IRON_INGOT, 6), new ItemStack(Item.INK_SACK, 8, 4)};
      var2 = this.sacrifice(var1, var2);
      if(!this.checkSacrifice(var2)) {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      var1.renderStar = true;
      if(var1.ticksLived < 80) {
         var1.setColorInner(140, 140, 140);
         var1.setColorOuter(140, 140, 140);
      } else {
         var1.setColorInner(0, 0, 255);
         var1.setColorOuter(0, 0, 255);
         List var2 = this.getEntities(var1, 2.0D);
         if(var2.size() > 0) {
            this.trigger(var1, var1.dustID);
         }

      }
   }

   public void trigger(EntityDust var1, int var2) {
      boolean var3 = false;
      List var4 = this.getEntities(var1, 2.0D);
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Entity var6 = (Entity)var5.next();
         if(var6 instanceof EntityLiving) {
            if(var6 instanceof EntityHuman) {
               EntityHuman var7 = (EntityHuman)var6;
               if(var7.name.equals(var1.summonerUN)) {
                  continue;
               }
            }

            var3 = true;
            EntityLiving var16 = (EntityLiving)var6;
            int var8 = (int)Math.floor(var16.locX);
            int var9 = (int)Math.floor(var16.locY - (double)var16.height);
            int var10 = (int)Math.floor(var16.locZ);
            var16.setPosition((double)var8 + 0.5D, (double)var9 + (double)var16.height, (double)var10 + 0.5D);
            World var11 = var1.world;

            for(int var12 = -1; var12 <= 1; ++var12) {
               for(int var13 = 0; var13 <= 1; ++var13) {
                  for(int var14 = -1; var14 <= 1; ++var14) {
                     if(var12 != 0 || var14 != 0) {
                        var11.setTypeId(var8 + var12, var9 + var13, var10 + var14, 0);
                        var11.setTypeId(var8 + var12, var9 + var13, var10 + var14, Block.IRON_FENCE.id);
                     }
                  }
               }
            }

            if(var11.getTypeId(var8, var9 - 1, var10) == 0) {
               Iterator var17 = var1.dustPoints.iterator();

               while(var17.hasNext()) {
                  Integer[] var18 = (Integer[])var17.next();
                  TileEntityDust var19 = (TileEntityDust)var11.getTileEntity(var18[0].intValue(), var18[1].intValue(), var18[2].intValue());
                  if(var19.getDusts()[3]) {
                     int var15 = var11.getTypeId(var18[0].intValue(), var18[1].intValue() - 1, var18[2].intValue());
                     var11.setTypeId(var18[0].intValue(), var18[1].intValue() - 1, var18[2].intValue(), 0);
                     var11.setTypeId(var8, var9 - 1, var10, var15);
                  }
               }
            }
            break;
         }
      }

      if(var3) {
         System.out.println("Found");
         var1.fade();
      }

   }
}
