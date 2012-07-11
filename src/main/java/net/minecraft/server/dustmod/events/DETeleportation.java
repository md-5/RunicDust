package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityBlock;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;
import net.minecraft.server.dustmod.TileEntityDust;

public class DETeleportation extends PoweredEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      World var2 = var1.worldObj;
      ItemStack[] var3 = this.sacrifice(var1, new ItemStack[]{new ItemStack(Item.ENDER_PEARL, 1)});
      if(var3[0].count == 0 && this.takeXP(var1, 20)) {
         Integer[] var4 = null;
         Iterator var5 = var1.dustPoints.iterator();

         int var9;
         int var10;
         int var11;
         while(var5.hasNext()) {
            Integer[] var6 = (Integer[])var5.next();
            TileEntity var7 = var2.getTileEntity(var6[0].intValue(), var6[1].intValue(), var6[2].intValue());
            if(var7 != null && var7 instanceof TileEntityDust) {
               TileEntityDust var8 = (TileEntityDust)var7;
               var9 = 10;
               var10 = 4;

               for(var11 = 0; var11 < 4; ++var11) {
                  for(int var12 = 0; var12 < 4; ++var12) {
                     if(var8.getDust(var11, var12) == 2) {
                        --var9;
                     }

                     if(var8.getDust(var11, var12) == 4) {
                        --var10;
                     }
                  }
               }

               if(var9 == 0 && var10 == 0) {
                  var4 = var6;
                  var1.data = var2.getTypeId(var6[0].intValue(), var6[1].intValue() - 1, var6[2].intValue());
               }
            }
         }

         for(int var13 = -1; var13 <= 1 && var4 != null; ++var13) {
            for(int var14 = -1; var14 <= 1; ++var14) {
               if((var13 == 0 || var14 == 0) && mod_DustMod.isDust(var2.getTypeId(var4[0].intValue() + var13, var4[1].intValue(), var4[2].intValue() + var14))) {
                  TileEntityDust var15 = (TileEntityDust)var2.getTileEntity(var13 + var4[0].intValue(), var4[1].intValue(), var4[2].intValue() + var14);
                  int var16 = 4;
                  var9 = 4;

                  for(var10 = 0; var10 < 4; ++var10) {
                     for(var11 = 0; var11 < 4; ++var11) {
                        if(var15.getDust(var10, var11) == 2) {
                           --var16;
                        }

                        if(var15.getDust(var10, var11) == 4) {
                           --var9;
                        }
                     }
                  }

                  if(var16 == 0 && var9 == 0) {
                     var1.posX = (double)(var4[0].intValue() + var13) + 0.5D;
                     var1.posY = (double)var4[1].intValue() + 1.5D + 0.0D;
                     var1.posZ = (double)(var4[2].intValue() + var14) + 0.5D;
                     if(var13 == -1) {
                        var1.rotationYaw = 270.0F;
                     } else if(var13 == 1) {
                        var1.rotationYaw = 90.0F;
                     } else if(var14 == -1) {
                        var1.rotationYaw = 0.0F;
                     } else if(var14 == 1) {
                        var1.rotationYaw = 180.0F;
                     }
                  }
               }
            }
         }

         var1.renderStar = true;
         var1.starScaleY = 2.0F;
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      int[] var2 = mod_DustMod.toWarp(var1);
      mod_DustMod.addWarp(var2);
      if(var1.ram == 0) {
         mod_DustMod.addWarp(var2);
         var1.ram = 1;
      } else if(var1.ram > 1) {
         --var1.ram;
         var1.setColorOuter(255, 0, 0);
         var1.setColorInner(255, 0, 0);
      } else {
         var1.setColorInner(255, 255, 255);
         var1.setColorOuter(255, 255, 255);
      }

      List var3 = this.getEntities(var1, 10.0D);
      if(var1.ram > 1 && mod_DustMod.skipWarpTick > 0) {
         --mod_DustMod.skipWarpTick;
      }

      if(var1.ram == 1) {
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
                  int var17 = mod_DustMod.getVoidNetworkIndex(var2);
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
                        if(mod_DustMod.skipWarpTick > 0) {
                           var1.ram = 100;
                        } else {
                           if(var8 instanceof EntityLiving) {
                              ((EntityLiving)var8).enderTeleportTo((double)var19[0] + 0.5D, (double)var19[1] + 0.6D, (double)var19[2] + 0.5D);
                              ((EntityLiving)var8).damageEntity(DamageSource.MAGIC, 6);
                           } else {
                              var8.setPosition((double)var19[0] + 0.5D, (double)var19[1] + 0.6D, (double)var19[2] + 0.5D);
                           }

                           var8.locX = (double)var19[0] + 0.5D;
                           var8.locY = (double)var19[1] + 0.6D;
                           var8.locZ = (double)var19[2] + 0.5D;
                           var8.yaw = (float)var19[5];
                           var1.ram = 100;
                           EntityDust var20 = mod_DustMod.getWarpEntity(var19, var1.worldObj);
                           if(var20 != null) {
                              var20.ram = 100;
                           }

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
      }

   }

   public void onUnload(EntityDust var1) {
      mod_DustMod.removeWarp(mod_DustMod.toWarp(var1));
   }

   public int getStartFuel() {
      return 96000;
   }

   public int getMaxFuel() {
      return 288000;
   }

   public int getStableFuelAmount(EntityDust var1) {
      return '\ubb80';
   }

   public boolean isPaused(EntityDust var1) {
      return false;
   }
}
