package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityBlock;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.TileEntityDust;

public class DEMiniTele extends DustEvent {

   public void onInit(EntityDust var1) {
      World var2 = var1.world;
      if(!this.takeXP(var1, 5)) {
         var1.fizzle();
      } else {
         Object var3 = null;
         Iterator var4 = var1.dustPoints.iterator();

         while(var4.hasNext()) {
            Integer[] var5 = (Integer[])var4.next();
            TileEntity var6 = var2.getTileEntity(var5[0].intValue(), var5[1].intValue(), var5[2].intValue());
            if(var6 != null && var6 instanceof TileEntityDust) {
               TileEntityDust var7 = (TileEntityDust)var6;
               int var8 = 8;
               int var9 = 6;

               for(int var10 = 0; var10 < 4; ++var10) {
                  for(int var11 = 0; var11 < 4; ++var11) {
                     if(var7.getDust(var10, var11) == 2) {
                        --var8;
                     }

                     if(var7.getDust(var10, var11) == 4) {
                        --var9;
                     }
                  }
               }

               if(var8 == 0 && var9 == 0) {
                  var1.data = var2.getTypeId(var5[0].intValue(), var5[1].intValue() - 1, var5[2].intValue());
               }
            }
         }

         var1.renderStar = true;
      }
   }

   public void onTick(EntityDust var1) {
      int[] var2 = mod_DustMod.toWarp(var1);
      List var3 = this.getEntities(var1, 10.0D);
      Object[] var4 = var3.toArray();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Object var7 = var4[var6];
         Entity var8 = (Entity)var7;
         if(!(var8 instanceof EntityBlock) || !((EntityBlock)var8).hasParentDust) {
            double var9 = var8.locX - (double)var1.getX();
            double var11 = var8.locY - (double)var1.getY();
            double var13 = var8.locZ - (double)var1.getZ();
            double var15 = 1.0D;
            if(!(var8 instanceof EntityDust) && Math.abs(var9) < var15 && Math.abs(var13) < var15 && Math.abs(var11) < 3.0D) {
               byte var17 = 0;
               if(var8 instanceof EntityHuman && ((EntityHuman)var8).K > 0.8F) {
                  var1.ram = 100;
               }

               for(int var18 = var17 + 1; var18 != var17 && var17 != -1; ++var18) {
                  if(var18 >= mod_DustMod.voidNetwork.size()) {
                     var18 = 0;
                  }

                  if(var18 == var17) {
                     break;
                  }

                  int[] var19 = (int[])mod_DustMod.voidNetwork.get(var18);
                  if(((double)Math.abs(var2[0] - var19[0]) >= 0.5D || (double)Math.abs(var2[1] - var19[1]) >= 0.5D || (double)Math.abs(var2[2] - var19[2]) >= 0.5D) && var19[6] == var8.world.worldProvider.dimension && var19[7] == var2[7] && var2[3] == var19[3] && var2[4] == var19[4]) {
                     if(mod_DustMod.skipWarpTick <= 0) {
                        if(var8 instanceof EntityLiving) {
                           ((EntityLiving)var8).enderTeleportTo((double)var19[0] + 0.5D, (double)var19[1] + 0.6D, (double)var19[2] + 0.5D);
                        } else {
                           var8.setPosition((double)var19[0] + 0.5D, (double)var19[1] + 0.6D, (double)var19[2] + 0.5D);
                        }

                        var8.locX = (double)var19[0] + 0.5D;
                        var8.locY = (double)var19[1] + 0.6D;
                        var8.locZ = (double)var19[2] + 0.5D;
                        var8.yaw = (float)var19[5];
                        mod_DustMod.getWarpEntity(var19, var1.world);
                        if(var8 instanceof EntityHuman) {
                           ((EntityHuman)var8).K = 0.9F;
                        }

                        mod_DustMod.skipWarpTick = 10;
                     }
                     break;
                  }
               }
            }
         }
      }

      if(var1.ticksLived > 100) {
         var1.setColorOuter(255, 0, 0);
         var1.setColorInner(255, 0, 0);
         var1.fade();
      }

   }

   public void onUnload(EntityDust var1) {}
}
