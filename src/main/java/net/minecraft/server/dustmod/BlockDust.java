package net.minecraft.server.dustmod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.TileEntityDust;

public class BlockDust extends BlockContainer {

   public BlockDust(int var1, int var2) {
      super(var1, var2, Material.ORIENTABLE);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public int a(int var1, int var2) {
      return this.textureId;
   }

   public AxisAlignedBB e(World var1, int var2, int var3, int var4) {
      return null;
   }

   public boolean a() {
      return false;
   }

   public boolean b() {
      return false;
   }

   public void a(World var1, int var2, int var3, int var4, Entity var5) {
      EntityHuman var7;
      if(var5 instanceof EntityItem) {
         EntityItem var6 = (EntityItem)var5;
         var6.age = 0;
         var7 = var1.findNearbyPlayer(var6, 3.0D);
         if(var7 == null) {
            var6.pickupDelay = 20;
            return;
         }

         double var8 = (double)var7.i(var6);
         if(var8 > 1.6D) {
            var6.pickupDelay = 5;
         }
      }

      if(var5 instanceof EntityExperienceOrb) {
         EntityExperienceOrb var10 = (EntityExperienceOrb)var5;
         var10.b = 0;
         var7 = var1.findNearbyPlayer(var10, 3.0D);
         if(var7 == null) {
            var10.setPosition(var10.lastX, var10.lastY, var10.lastZ);
            return;
         }
      }

   }

   public void a(World var1, int var2, int var3, int var4, EntityLiving var5) {
      super.a(var1, var2, var3, var4, var5);
      this.blockActivated(var1, var2, var3, var4, (EntityHuman)var5);
      if(((EntityHuman)var5).U() != null) {
         ++((EntityHuman)var5).U().count;
      }

   }

   public boolean a(World var1, int var2, int var3, int var4) {
      Block var5 = Block.byId[var1.getTypeId(var2, var3 - 1, var4)];
      return var5 == null?false:var5.b() || var5 == Block.GLASS;
   }

   public int c() {
      return mod_DustMod.dustModelID;
   }

   public int colorMultiplier(IBlockAccess var1, int var2, int var3, int var4) {
      switch(var1.getData(var2, var3, var4)) {
      case 0:
         TileEntityDust var5 = (TileEntityDust)var1.getTileEntity(var2, var3, var4);
         return var5.getRandomDustColor();
      case 1:
         return 14483456;
      case 2:
         return 15724527;
      default:
         System.out.println("derp? " + var1.getData(var2, var3, var4));
         return 0;
      }
   }

   public void a(World var1, int var2, int var3, int var4, int var5) {
      int var6 = var1.getData(var2, var3, var4);
      if(var1.getTypeId(var2, var3 - 1, var4) != 0 && Block.byId[var1.getTypeId(var2, var3 - 1, var4)].material.isBuildable()) {
         if(var1.isBlockIndirectlyPowered(var2, var3, var4) && var6 == 0) {
            this.updatePattern(var1, var2, var3, var4, (EntityHuman)null);
            mod_DustMod.notifyBlockChange(var1, var2, var3, var4, 0);
         }
      } else {
         var1.setTypeId(var2, var3, var4, 0);
      }

      super.a(var1, var2, var3, var4, var5);
   }

   public void d(World var1, int var2, int var3, int var4) {
      if(var1.getData(var2, var3, var4) > 0) {
         var1.setTypeId(var2, var3, var4, 0);
      } else {
         TileEntityDust var5 = (TileEntityDust)var1.getTileEntity(var2, var3, var4);
         if(var5 == null) {
            return;
         }

         for(int var6 = 0; var6 < 4; ++var6) {
            for(int var7 = 0; var7 < 4; ++var7) {
               int var8 = var5.getDust(var6, var7);
               if(var8 > 0) {
                  this.a(var1, var2, var3, var4, new ItemStack(mod_DustMod.idust.id, 1, var8));
               }
            }
         }
      }

   }

   protected int damageDropped(int var1) {
      return var1;
   }

   public boolean blockActivated(World var1, int var2, int var3, int var4, EntityHuman var5) {
      if(var1.getData(var2, var3, var4) == 1) {
         TileEntityDust var29 = (TileEntityDust)var1.getTileEntity(var2, var3, var4);
         var29.onRightClick(var5);
         return true;
      } else if(var1.getData(var2, var3, var4) > 1) {
         return false;
      } else if(var5.isSneaking()) {
         if(var5.U() == null || var5.U().id != mod_DustMod.tome.id) {
            this.b(var1, var2, var3, var4, var5);
         }

         return false;
      } else if(var5.U() != null && var5.U().id == mod_DustMod.tome.id) {
         this.updatePattern(var1, var2, var3, var4, var5);
         mod_DustMod.notifyBlockChange(var1, var2, var3, var4, 0);
         return true;
      } else {
         if(var5.U() != null && var5.U().id == mod_DustMod.spiritSword.id) {
            this.b(var1, var2, var3, var4, var5);
         }

         if(var5.U() != null && var5.U().id == mod_DustMod.idust.id) {
            int var6 = var5.U().getData();
            Vec3D var7 = var5.aJ();
            double var8 = var7.a;
            double var10 = var7.b;
            double var12 = var7.c;

            for(double var14 = 0.0D; var14 < 4.0D; var14 += 0.01D) {
               double var16 = var5.locX + var8 * var14;
               double var18 = var5.locY + (double)var5.getHeadHeight() + var10 * var14;
               double var20 = var5.locZ + var12 * var14;
               if(var18 - (double)var3 <= 0.02D) {
                  double var22 = Math.abs(var16 - (double)var2) - 0.02D;
                  double var24 = Math.abs(var20 - (double)var4) - 0.02D;
                  int var26 = (int)Math.floor(var22 * 4.0D);
                  int var27 = (int)Math.floor(var24 * 4.0D);
                  if(var26 >= 4) {
                     var26 = 3;
                  }

                  if(var27 >= 4) {
                     var27 = 3;
                  }

                  if(var26 < 0) {
                     var26 = 0;
                  }

                  if(var27 < 0) {
                     var27 = 0;
                  }

                  TileEntityDust var28 = (TileEntityDust)var1.getTileEntity(var2, var3, var4);
                  if(var28.getDust(var26, var27) <= 0) {
                     if(var28.getDust(var26, var27) == -2) {
                        this.setVariableDust(var28, var26, var27, var5, var6);
                     } else {
                        var28.setDust(var26, var27, var6);
                        if(!var5.abilities.canInstantlyBuild) {
                           --var5.U().count;
                           if(var5.U().count == 0) {
                              var5.V();
                           }
                        }
                     }

                     mod_DustMod.notifyBlockChange(var1, var2, var3, var4, 0);
                     var1.makeSound((double)((float)var2 + 0.5F), (double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), this.stepSound.getName(), (this.stepSound.getVolume1() + 1.0F) / 6.0F, this.stepSound.getVolume2() * 0.99F);
                     return true;
                  }
                  break;
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }

   private void setVariableDust(TileEntityDust var1, int var2, int var3, EntityHuman var4, int var5) {
      if(var1.getDust(var2, var3) == -2) {
         boolean var6 = false;
         int var7;
         if(!var4.abilities.canInstantlyBuild) {
            for(var7 = 0; var7 < var4.inventory.items.length; ++var7) {
               ItemStack var8 = var4.inventory.items[var7];
               if(var8 != null && var8.id == mod_DustMod.idust.id && var8.getData() == var5) {
                  --var8.count;
                  if(var8.count == 0) {
                     var4.inventory.items[var7] = null;
                  }

                  var6 = true;
                  break;
               }
            }
         } else {
            var6 = true;
         }

         if(var6) {
            var1.setDust(var2, var3, var5);

            for(var7 = -1; var7 <= 1; ++var7) {
               for(int var15 = -1; var15 <= 1; ++var15) {
                  if(var7 == 0 || var15 == 0) {
                     int var9 = var1.x;
                     int var10 = var1.z;
                     int var11 = var2 + var7;
                     int var12 = var3 + var15;
                     if(var11 < 0) {
                        var11 = 4 - 1;
                        --var9;
                     } else if(var11 >= 4) {
                        var11 = 0;
                        ++var9;
                     }

                     if(var12 < 0) {
                        var12 = 4 - 1;
                        --var10;
                     } else if(var12 >= 4) {
                        var12 = 0;
                        ++var10;
                     }

                     TileEntity var13 = var1.world.getTileEntity(var9, var1.y, var10);
                     if(var13 instanceof TileEntityDust) {
                        mod_DustMod.notifyBlockChange(var1.world, var9, var1.y, var10, 0);
                        TileEntityDust var14 = (TileEntityDust)var13;
                        this.setVariableDust(var14, var11, var12, var4, var5);
                     }
                  }
               }
            }

         }
      }
   }

   public TileEntity a_() {
      return new TileEntityDust();
   }

   public void b(World var1, int var2, int var3, int var4, EntityHuman var5) {
      Vec3D var6 = var5.aJ();
      double var7 = var6.a;
      double var9 = var6.b;
      double var11 = var6.c;

      for(double var13 = 0.0D; var13 < 4.0D; var13 += 0.01D) {
         double var15 = var5.locX + var7 * var13;
         double var17 = var5.locY + (double)var5.getHeadHeight() + var9 * var13;
         double var19 = var5.locZ + var11 * var13;
         if(var17 - (double)var3 <= 0.02D) {
            double var21 = Math.abs(var15 - (double)var2) - 0.02D;
            double var23 = Math.abs(var19 - (double)var4) - 0.02D;
            int var25 = (int)Math.floor(var21 * 4.0D);
            int var26 = (int)Math.floor(var23 * 4.0D);
            if(var25 >= 4) {
               var25 = 3;
            }

            if(var26 >= 4) {
               var26 = 3;
            }

            if(var25 < 0) {
               var25 = 0;
            }

            if(var26 < 0) {
               var26 = 0;
            }

            TileEntityDust var27 = (TileEntityDust)var1.getTileEntity(var2, var3, var4);
            if(var27.getDust(var25, var26) != 0 && var1.getData(var2, var3, var4) == 0) {
               if(var27.getDust(var25, var26) > 0 && !var5.abilities.canInstantlyBuild) {
                  this.a(var1, var2, var3, var4, new ItemStack(mod_DustMod.idust.id, 1, var27.getDust(var25, var26)));
               }

               var1.makeSound((double)((float)var2 + 0.5F), (double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), this.stepSound.getName(), (this.stepSound.getVolume1() + 1.0F) / 6.0F, this.stepSound.getVolume2() * 0.99F);
               mod_DustMod.notifyBlockChange(var1, var2, var3, var4, 0);
               var27.setDust(var25, var26, 0);
               if(var27.isEmpty() && var1.getData(var2, var3, var4) != 10) {
                  var1.setTypeId(var2, var3, var4, 0);
                  this.a(var1, var2, var3, var4, 0);
               }
            }
            break;
         }
      }

   }

   public int a(int var1, Random var2, int var3) {
      return 0;
   }

   public void updatePattern(World var1, int var2, int var3, int var4, EntityHuman var5) {
      ArrayList var6 = new ArrayList();
      this.addNeighbors(var1, var2, var3, var4, var6);
      if(var6.size() != 0) {
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            Integer[] var8 = (Integer[])var7.next();
            if(var1.getTypeId(var8[0].intValue(), var3, var8[2].intValue()) == this.id) {
               var1.setData(var8[0].intValue(), var3, var8[2].intValue(), 1);
            }
         }

         int var20 = ((Integer[])var6.get(0))[0].intValue();
         int var21 = ((Integer[])var6.get(0))[2].intValue();
         int var9 = ((Integer[])var6.get(0))[0].intValue();
         int var10 = ((Integer[])var6.get(0))[2].intValue();
         Iterator var11 = var6.iterator();

         while(var11.hasNext()) {
            Integer[] var12 = (Integer[])var11.next();
            if(var12[0].intValue() < var20) {
               var20 = var12[0].intValue();
            }

            if(var12[2].intValue() < var21) {
               var21 = var12[2].intValue();
            }

            if(var12[0].intValue() > var9) {
               var9 = var12[0].intValue();
            }

            if(var12[2].intValue() > var10) {
               var10 = var12[2].intValue();
            }
         }

         byte var22 = 4;
         int var23 = var9 - var20;
         int var13 = var10 - var21;
         int[][] var14 = new int[(var9 - var20 + 1) * var22][(var10 - var21 + 1) * var22];

         for(int var15 = 0; var15 <= var23; ++var15) {
            for(int var16 = 0; var16 <= var13; ++var16) {
               if(var1.getTypeId(var15 + var20, var3, var16 + var21) == this.id) {
                  TileEntityDust var17 = (TileEntityDust)var1.getTileEntity(var15 + var20, var3, var16 + var21);

                  for(int var18 = 0; var18 < var22; ++var18) {
                     for(int var19 = 0; var19 < var22; ++var19) {
                        var14[var18 + var15 * var22][var19 + var16 * var22] = var17.getDust(var18, var19);
                     }
                  }
               }
            }
         }

         mod_DustMod.callShape(var1, (double)var20 + (double)var23 / 2.0D + 0.5D, (double)var3 + 1.0D, (double)var21 + (double)var13 / 2.0D + 0.5D, var14, var6, var5 == null?null:var5.name);
      }
   }

   public void addNeighbors(World var1, int var2, int var3, int var4, List var5) {
      for(int var6 = -1; var6 <= 1; ++var6) {
         for(int var7 = -1; var7 <= 1; ++var7) {
            if(var1.getTypeId(var2 + var6, var3, var4 + var7) == this.id && var1.getData(var2 + var6, var3, var4 + var7) == 0) {
               boolean var8 = true;
               Iterator var9 = var5.iterator();

               while(var9.hasNext()) {
                  Integer[] var10 = (Integer[])var9.next();
                  if(var10[0].intValue() == var2 + var6 && var10[2].intValue() == var4 + var7) {
                     var8 = false;
                     break;
                  }
               }

               if(var8) {
                  var5.add(new Integer[]{Integer.valueOf(var2 + var6), Integer.valueOf(var3), Integer.valueOf(var4 + var7)});
                  this.addNeighbors(var1, var2 + var6, var3, var4 + var7, var5);
               }
            }
         }
      }

   }
}
