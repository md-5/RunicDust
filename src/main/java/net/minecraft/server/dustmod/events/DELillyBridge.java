package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import net.minecraft.server.Block;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.TileEntityDust;

public class DELillyBridge extends DustEvent {

   public void onInit(EntityDust var1) {
      World var2 = var1.world;
      ItemStack[] var3 = this.sacrifice(var1, new ItemStack[]{new ItemStack(Block.LEAVES, 4, -1)});
      if(var3[0].count != 0) {
         var1.fizzle();
      } else {
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
               var9 = 6;
               var10 = 6;

               for(var11 = 0; var11 < 4; ++var11) {
                  for(int var12 = 0; var12 < 4; ++var12) {
                     if(var8.getDust(var11, var12) == 1) {
                        --var9;
                     }

                     if(var8.getDust(var11, var12) == 2) {
                        --var10;
                     }
                  }
               }

               if(var9 == 0 && var10 == 0) {
                  var4 = var6;
                  break;
               }
            }
         }

         for(int var13 = -1; var13 <= 1 && var4 != null; ++var13) {
            for(int var14 = -1; var14 <= 1; ++var14) {
               if((var13 == 0 || var14 == 0) && mod_DustMod.isDust(var2.getTypeId(var4[0].intValue() + var13, var4[1].intValue(), var4[2].intValue() + var14))) {
                  TileEntityDust var15 = (TileEntityDust)var2.getTileEntity(var13 + var4[0].intValue(), var4[1].intValue(), var4[2].intValue() + var14);
                  int var16 = 7;
                  var9 = 7;

                  for(var10 = 0; var10 < 4; ++var10) {
                     for(var11 = 0; var11 < 4; ++var11) {
                        if(var15.getDust(var10, var11) == 1) {
                           --var16;
                        }

                        if(var15.getDust(var10, var11) == 2) {
                           --var9;
                        }
                     }
                  }

                  if(var16 == 0 && var9 == 0) {
                     var1.locX = (double)(var4[0].intValue() + var13) + 0.5D;
                     var1.locY = (double)var4[1].intValue() + 1.5D + 0.0D;
                     var1.locZ = (double)(var4[2].intValue() + var14) + 0.5D;
                     if(var13 == -1) {
                        var1.yaw = 270.0F;
                     } else if(var13 == 1) {
                        var1.yaw = 90.0F;
                     } else if(var14 == -1) {
                        var1.yaw = 0.0F;
                     } else if(var14 == 1) {
                        var1.yaw = 180.0F;
                     }
                  }
               }
            }
         }

         var1.renderStar = true;
         var1.setColorOuter(0, 255, 0);
      }
   }

   public void onTick(EntityDust var1) {
      byte var2 = 20;
      if(var1.ticksLived % var2 == 0) {
         World var3 = var1.world;
         int var4 = (var1.ticksLived / var2 + 1) * 2;
         int var5 = var1.getY() - 1;
         int var6 = var1.getX();
         int var7 = var1.getZ();
         if(var1.yaw == 90.0F) {
            var6 -= var4;
         } else if(var1.yaw == 270.0F) {
            var6 += var4;
         } else if(var1.yaw == 180.0F) {
            var7 -= var4;
         } else if(var1.yaw == 0.0F) {
            var7 += var4;
         }

         for(int var8 = -1; var8 <= 1; ++var8) {
            if(var3.getMaterial(var6, var5 + var8 - 1, var7) == Material.WATER && var3.getTypeId(var6, var5 + var8, var7) == 0) {
               var3.setTypeId(var6, var5 + var8, var7, Block.WATER_LILY.id);
            }
         }
      }

      if(var1.ticksLived > 16 * var2) {
         var1.fade();
      }

   }
}
