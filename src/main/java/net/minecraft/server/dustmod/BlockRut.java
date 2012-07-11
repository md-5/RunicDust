package net.minecraft.server.dustmod;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemSpade;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.TileEntityRut;

public class BlockRut extends BlockContainer {

   public BlockRut(int var1) {
      super(var1, 7, Material.WOOD);
   }

   public int c() {
      return mod_DustMod.rutModelID;
   }

   public boolean b() {
      return false;
   }

   public boolean a() {
      return false;
   }

   public void a(World var1, int var2, int var3, int var4, int var5) {
      for(int var6 = -1; var6 <= 1; ++var6) {
         for(int var7 = -1; var7 <= 1; ++var7) {
            for(int var8 = -1; var8 <= 1; ++var8) {
               if(var6 == var7 || var6 == var8 || var7 == var8) {
                  TileEntityRut var9 = (TileEntityRut)var1.getTileEntity(var2, var3, var4);
                  int var10 = var1.getTypeId(var2 + var6, var3 + var7, var4 + var8);
                  if(var9.fluid == 0) {
                     if(var10 != Block.STATIONARY_LAVA.id && var10 != Block.LAVA.id) {
                        if(var10 == Block.STATIONARY_WATER.id || var10 == Block.WATER.id) {
                           var9.setFluid(Block.STATIONARY_WATER.id);
                        }
                     } else {
                        var9.setFluid(Block.STATIONARY_LAVA.id);
                     }
                  }

                  if(var9.fluid == Block.STATIONARY_WATER.id && (var10 == Block.STATIONARY_LAVA.id || var10 == Block.LAVA.id)) {
                     var9.setFluid(Block.COBBLESTONE.id);
                  }

                  if(var9.fluid == Block.STATIONARY_LAVA.id && (var10 == Block.STATIONARY_WATER.id || var10 == Block.WATER.id)) {
                     var9.setFluid(Block.OBSIDIAN.id);
                  }
               }
            }
         }
      }

      ((TileEntityRut)var1.getTileEntity(var2, var3, var4)).updateNeighbors();
   }

   public boolean blockActivated(World var1, int var2, int var3, int var4, EntityHuman var5) {
      ItemStack var6 = var5.U();
      boolean var7 = var6 == null;
      TileEntityRut var8 = (TileEntityRut)var1.getTileEntity(var2, var3, var4);
      if(var8.isBeingUsed) {
         return false;
      } else if(var8.fluid == 0 && !var7 && var6.id == Item.WATER_BUCKET.id) {
         if(!var5.abilities.canInstantlyBuild) {
            var6.id = Item.BUCKET.id;
         }

         var8.setFluid(Block.STATIONARY_WATER.id);
         return true;
      } else if(var8.fluid == 0 && !var7 && var6.id == Item.LAVA_BUCKET.id) {
         if(!var5.abilities.canInstantlyBuild) {
            var6.id = Item.BUCKET.id;
         }

         var8.setFluid(Block.STATIONARY_LAVA.id);
         return true;
      } else {
         if(!var7 && (var8.fluid == 0 || var8.fluidIsFluid())) {
            int var9 = var6.id;
            if(var9 < Block.byId.length && Block.byId[var9] != null) {
               Block var10 = Block.byId[var9];
               if(var10.b() && var10.a() && var10.m() <= TileEntityRut.hardnessStandard) {
                  if(!var5.abilities.canInstantlyBuild) {
                     --var6.count;
                  }

                  var8.setFluid(var9);
                  return true;
               }
            }
         }

         if(!var7 && var8.fluid != 0 && !var8.fluidIsFluid() && Block.byId[var8.fluid].m() <= TileEntityRut.hardnessStandard && Item.byId[var6.id] instanceof ItemSpade) {
            this.a(var1, var2, var3 + 1, var4, new ItemStack(var8.fluid, 1, 0));
            var8.setFluid(0);
            return true;
         } else if(!var7 && var6.id == mod_DustMod.chisel.id) {
            Vec3D var41 = var5.aJ();
            double var11 = var41.a;
            double var13 = var41.b;
            double var15 = var41.c;
            double var10000 = var5.locX - (double)var2;
            double var19 = var5.locY + (double)var5.getHeadHeight() - (double)var3;
            var10000 = var5.locZ - (double)var4;
            if(var11 == 0.0D) {
               var11 = 0.001D;
            }

            if(var13 == 0.0D) {
               var13 = 0.001D;
            }

            if(var15 == 0.0D) {
               var15 = 0.001D;
            }

            int var23 = determineOrientation(var1, var2, var3, var4, var5);
            Block var24 = Block.byId[var8.maskBlock];
            var1.makeSound((double)((float)var2 + 0.5F), (double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), var24.stepSound.getName(), (var24.stepSound.getVolume1() + 1.0F) / 6.0F, var24.stepSound.getVolume2() * 0.99F);

            double var25;
            double var27;
            double var29;
            double var31;
            double var35;
            double var33;
            int var39;
            double var37;
            int var40;
            for(var25 = 0.0D; var25 < 4.0D && var23 == 0; var25 += 0.01D) {
               var27 = var5.locX + var11 * var25;
               var29 = var5.locY + (double)var5.getHeadHeight() + var13 * var25;
               var31 = var5.locZ + var15 * var25;
               var33 = var29 - (double)var3;
               if(var33 >= 0.0D && var33 <= 0.02D) {
                  var35 = Math.abs(var27 - (double)var2) - 0.02D;
                  var37 = Math.abs(var31 - (double)var4) - 0.02D;
                  var39 = (int)Math.floor(var35 * 3.0D);
                  var40 = (int)Math.floor(var37 * 3.0D);
                  if(var39 >= 3) {
                     var39 = 2;
                  }

                  if(var40 >= 3) {
                     var40 = 2;
                  }

                  if(var39 < 0) {
                     var39 = 0;
                  }

                  if(var40 < 0) {
                     var40 = 0;
                  }

                  this.toggleRut(var8, var39, 0, var40);
                  break;
               }
            }

            for(var25 = 0.0D; var25 < 4.0D && var23 == 1; var25 += 0.01D) {
               var27 = var5.locX + var11 * var25;
               var29 = var5.locY + (double)var5.getHeadHeight() + var13 * var25;
               var31 = var5.locZ + var15 * var25;
               var33 = var29 - (double)var3;
               if(var33 >= 0.98D && var33 <= 1.0D) {
                  var35 = Math.abs(var27 - (double)var2) - 0.02D;
                  var37 = Math.abs(var31 - (double)var4) - 0.02D;
                  var39 = (int)Math.floor(var35 * 3.0D);
                  var40 = (int)Math.floor(var37 * 3.0D);
                  if(var39 >= 3) {
                     var39 = 2;
                  }

                  if(var40 >= 3) {
                     var40 = 2;
                  }

                  if(var39 < 0) {
                     var39 = 0;
                  }

                  if(var40 < 0) {
                     var40 = 0;
                  }

                  this.toggleRut(var8, var39, 2, var40);
                  break;
               }
            }

            for(var25 = 0.0D; var25 < 4.0D && var23 == 2; var25 += 0.01D) {
               var27 = var5.locX + var11 * var25;
               var29 = var5.locY + (double)var5.getHeadHeight() + var13 * var25;
               var31 = var5.locZ + var15 * var25;
               var33 = var31 - (double)var4;
               if(var33 >= 0.0D && var33 <= 0.02D) {
                  var35 = Math.abs(var29 - (double)var3) - 0.02D;
                  var37 = Math.abs(var27 - (double)var2) - 0.02D;
                  var39 = (int)Math.floor(var35 * 3.0D);
                  var40 = (int)Math.floor(var37 * 3.0D);
                  if(var39 >= 3) {
                     var39 = 2;
                  }

                  if(var40 >= 3) {
                     var40 = 2;
                  }

                  if(var39 < 0) {
                     var39 = 0;
                  }

                  if(var40 < 0) {
                     var40 = 0;
                  }

                  this.toggleRut(var8, var40, var39, 0);
                  break;
               }
            }

            for(var25 = 0.0D; var25 < 4.0D && var23 == 3; var25 += 0.01D) {
               var27 = var5.locX + var11 * var25;
               var29 = var5.locY + (double)var5.getHeadHeight() + var13 * var25;
               var31 = var5.locZ + var15 * var25;
               var33 = var31 - (double)var4;
               if(var33 >= 0.98D && var33 <= 1.0D) {
                  var35 = Math.abs(var29 - (double)var3) - 0.02D;
                  var37 = Math.abs(var27 - (double)var2) - 0.02D;
                  var39 = (int)Math.floor(var35 * 3.0D);
                  var40 = (int)Math.floor(var37 * 3.0D);
                  if(var39 >= 3) {
                     var39 = 2;
                  }

                  if(var40 >= 3) {
                     var40 = 2;
                  }

                  if(var39 < 0) {
                     var39 = 0;
                  }

                  if(var40 < 0) {
                     var40 = 0;
                  }

                  this.toggleRut(var8, var40, var39, 2);
                  break;
               }
            }

            for(var25 = 0.0D; var25 < 4.0D && var23 == 4; var25 += 0.01D) {
               var27 = var5.locX + var11 * var25;
               var29 = var5.locY + (double)var5.getHeadHeight() + var13 * var25;
               var31 = var5.locZ + var15 * var25;
               var33 = var27 - (double)var2;
               if(var33 >= 0.0D && var33 <= 0.02D) {
                  var35 = Math.abs(var29 - (double)var3) - 0.02D;
                  var37 = Math.abs(var31 - (double)var4) - 0.02D;
                  var39 = (int)Math.floor(var35 * 3.0D);
                  var40 = (int)Math.floor(var37 * 3.0D);
                  if(var39 >= 3) {
                     var39 = 2;
                  }

                  if(var40 >= 3) {
                     var40 = 2;
                  }

                  if(var39 < 0) {
                     var39 = 0;
                  }

                  if(var40 < 0) {
                     var40 = 0;
                  }

                  this.toggleRut(var8, 0, var39, var40);
                  break;
               }
            }

            for(var25 = 0.0D; var25 < 4.0D && var23 == 5; var25 += 0.01D) {
               var27 = var5.locX + var11 * var25;
               var29 = var5.locY + (double)var5.getHeadHeight() + var13 * var25;
               var31 = var5.locZ + var15 * var25;
               var33 = var27 - (double)var2;
               if(var33 >= 0.98D && var33 <= 1.0D) {
                  var35 = Math.abs(var29 - (double)var3) - 0.02D;
                  var37 = Math.abs(var31 - (double)var4) - 0.02D;
                  var39 = (int)Math.floor(var35 * 3.0D);
                  var40 = (int)Math.floor(var37 * 3.0D);
                  if(var39 >= 3) {
                     var39 = 2;
                  }

                  if(var40 >= 3) {
                     var40 = 2;
                  }

                  if(var39 < 0) {
                     var39 = 0;
                  }

                  if(var40 < 0) {
                     var40 = 0;
                  }

                  this.toggleRut(var8, 2, var39, var40);
                  break;
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public void toggleRut(TileEntityRut var1, int var2, int var3, int var4) {
      var1.setRut(var2, var3, var4, var1.getRut(var2, var3, var4) == 0?1:0);
      if(var1.isEmpty()) {
         var1.resetBlock();
      }

   }

   private static int determineOrientation(World var0, int var1, int var2, int var3, EntityHuman var4) {
      if(MathHelper.abs((float)var4.locX - (float)var1) < 2.0F && MathHelper.abs((float)var4.locZ - (float)var3) < 2.0F) {
         double var5 = var4.locY + 1.82D - (double)var4.height;
         if(var5 - (double)var2 > 2.0D) {
            return 1;
         }

         if((double)var2 - var5 > 0.0D) {
            return 0;
         }
      }

      int var7 = MathHelper.floor((double)(var4.yaw * 4.0F / 360.0F) + 0.5D) & 3;
      return var7 == 0?2:(var7 == 1?5:(var7 == 2?3:(var7 != 3?0:4)));
   }

   public void d(World var1, int var2, int var3, int var4) {
      TileEntityRut var5 = (TileEntityRut)var1.getTileEntity(var2, var3, var4);
      if(!var5.dead) {
         super.remove(var1, var2, var3, var4);
         int var6 = var5.maskBlock;
         int var7 = var5.maskMeta;
         int var8 = Block.byId[var6].getDropType(var7, new Random(), 0);
         int var9 = mod_DustMod.damageDropped(Block.byId[var6], var7);
         int var10 = Block.byId[var6].a(new Random());
         this.a(var1, var2, var3, var4, new ItemStack(var8, var10, var9));
         if(var5.fluid != 0 && !var5.fluidIsFluid() && var5.canEdit()) {
            this.a(var1, var2, var3, var4, new ItemStack(var5.fluid, 1, 0));
         }

      }
   }

   public int idDropped(int var1, Random var2, int var3) {
      return 0;
   }

   public TileEntity a_() {
      return new TileEntityRut();
   }
}
