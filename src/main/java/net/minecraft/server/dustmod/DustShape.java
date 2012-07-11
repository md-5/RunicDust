package net.minecraft.server.dustmod;

import java.util.ArrayList;
import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.TileEntityDust;

public class DustShape {

   public static final int n = -1;
   public int width;
   public int length;
   public int setOnFire;
   public boolean solid = true;
   public String name;
   public String pName = "";
   public String notes = "";
   public String desc = "";
   protected int[][][] data;
   public int[] manRot = new int[8];
   private int[] setloc = new int[]{0, 0, 0};
   public int cx;
   public int cy;
   public int ox;
   public int oy;
   public ArrayList blocks;
   public int[] dustAmt;
   public final int id;


   public DustShape(int var1, int var2, int var3, String var4, boolean var5, int var6, int var7, int var8, int var9, int var10) {
      this.id = var10;
      this.name = var4;
      this.width = var1;
      this.length = var2;
      this.setOnFire = var3;
      this.data = new int[this.setOnFire][this.width][this.length];
      this.dustAmt = new int[5];
      this.solid = var5;
      this.cx = var9;
      this.cy = var8;
      this.ox = var7;
      this.oy = var6;

      int var12;
      int var13;
      for(int var11 = 0; var11 < this.setOnFire; ++var11) {
         for(var12 = 0; var12 < this.width; ++var12) {
            for(var13 = 0; var13 < this.length; ++var13) {
               this.data[var11][var12][var13] = 0;
            }
         }
      }

      if(var7 >= 0 && var6 >= 0) {
         this.blocks = new ArrayList();
         int[] var16 = this.getBlock(var7 + var1, var6 + var2);
         var12 = var16[0] + 2;
         var13 = var16[1] + 2;

         for(int var14 = 0; var14 < var12; ++var14) {
            this.blocks.add(new ArrayList());

            for(int var15 = 0; var15 < var13; ++var15) {
               ((ArrayList)this.blocks.get(var14)).add(new int[4][4]);
            }
         }

         this.updateData();
      } else {
         throw new IllegalArgumentException("Dust offset cannot be negative.");
      }
   }

   public DustShape setNotes(String var1) {
      this.notes = var1;
      return this;
   }

   public DustShape setManualRotationDerp(int[] var1) {
      this.manRot = var1;
      return this;
   }

   private void updateData() {
      this.blocks = this.updateData(this.data, this.ox, this.oy);
   }

   private ArrayList updateData(int[][][] var1, int var2, int var3) {
      int var4 = var1[0].length;
      int var5 = var1[0][0].length;
      if(var1 == this.data) {
         this.width = var4;
         this.length = var5;
      }

      ArrayList var6 = new ArrayList();
      int[] var7 = this.getBlock(var2 + var4, var3 + var5, var2, var3);
      int var8 = var7[0] + 2;
      int var9 = var7[1] + 2;

      int var10;
      int var11;
      for(var10 = 0; var10 < var8; ++var10) {
         var6.add(new ArrayList());

         for(var11 = 0; var11 < var9; ++var11) {
            ((ArrayList)var6.get(var10)).add(new int[4][4]);
         }
      }

      this.dustAmt = new int[5];

      for(var10 = 0; var10 < var1.length; ++var10) {
         for(var11 = 0; var11 < var1[0].length; ++var11) {
            for(int var12 = 0; var12 < var1[0][0].length; ++var12) {
               int[] var13 = this.getBlock(var11, var12, var2, var3);
               int var14 = var1[var10][var11][var12];
               if(var14 == -1) {
                  var14 = -2;
               }

               ((int[][])((ArrayList)var6.get(var13[0])).get(var13[1]))[var13[2]][var13[3]] = var14;
               if(var14 >= 0) {
                  ++this.dustAmt[var14];
               }
            }
         }
      }

      return var6;
   }

   public int[] getBlock(int var1, int var2) {
      return this.getBlock(var1, var2, this.ox, this.oy);
   }

   public int[] getBlock(int var1, int var2, int var3, int var4) {
      int var5 = (int)Math.floor((double)((var1 + var3) / 4));
      int var6 = (int)Math.floor((double)((var2 + var4) / 4));
      int var7 = var1 + var3 - var5 * 4;
      int var8 = var2 + var4 - var6 * 4;
      return new int[]{var5, var6, var7, var8};
   }

   public DustShape setDataAt(int var1, int var2, int var3, int var4) {
      this.data[var2][var1][var3] = var4;
      this.setloc[0] = var1;
      this.setloc[1] = var2;
      this.setloc[2] = var3;
      this.updateData();
      return this;
   }

   public int getDataAt(int var1, int var2, int var3) {
      return this.data[var2][var1][var3];
   }

   public void setData(int[][][] var1) {
      this.data = var1;
      this.updateData();
   }

   public int[][][] getData() {
      return this.data;
   }

   public void translate(int var1, int var2, int var3, int var4) {
      this.data[this.setloc[1]][this.setloc[0]][this.setloc[2]] = var4;
      this.setloc[0] += var1;
      this.setloc[var2] += var2;
      this.setloc[var3] += var3;
   }

   public int[][] compareData(int[][] var1) {
      int var2 = var1.length;
      int var3 = var1[0].length;
      int var4 = this.width - var2;
      int var5 = this.length - var3;
      if(var4 < 0 || var5 < 0) {
         var4 = this.width - var3;
         var5 = this.length - var2;
      }

      int var6;
      int var7;
      for(var6 = 0; var6 <= var4; ++var6) {
         for(var7 = 0; var7 <= var5; ++var7) {
            if((var1 = this.compareChunk(var1, var6, 0, var7)) == null) {
               return (int[][])null;
            }
         }
      }

      var2 = var1.length;
      var3 = var1[0].length;
      if(this.solid) {
         var6 = 0;

         for(var7 = 0; var7 < var2; ++var7) {
            for(int var8 = 0; var8 < var3; ++var8) {
               int var9 = var1[var7][var8];
               if(var6 == 0 && var9 != 0) {
                  var6 = var9;
               } else if(var6 != 0 && var6 != var9 && var9 != 0) {
                  return (int[][])null;
               }
            }
         }
      }

      if((var2 != this.width || var3 != this.length) && (var2 != this.length || var3 != this.width)) {
         return (int[][])null;
      } else {
         return var1;
      }
   }

   protected int[][] compareChunk(int[][] var1, int var2, int var3, int var4) {
      this.width = this.data[0].length;
      this.length = this.data[0][0].length;
      boolean var5 = true;

      int var6;
      int var7;
      int var8;
      int var9;
      int var10;
      for(var6 = 0; var6 < 4; ++var6) {
         var7 = var1.length;
         var8 = var1[0].length;
         if(var7 != this.width || var8 != this.length) {
            var5 = false;
         }

         label121:
         for(var9 = 0; var9 < var7 && var5; ++var9) {
            for(var10 = 0; var10 < var8 && var5; ++var10) {
               try {
                  if(var9 >= this.width || var10 >= this.length || var1[var9][var10] != this.data[var3][var9 + var2][var10 + var4] && (var1[var9][var10] == 0 || this.data[var3][var9 + var2][var10 + var4] != -1)) {
                     var5 = false;
                     break label121;
                  }
               } catch (Exception var13) {
                  var5 = false;
                  break label121;
               }
            }
         }

         if(var5) {
            return var1;
         }

         var1 = rotateMatrix(var1);
         var5 = true;
      }

      var1 = flipMatrix(var1);

      for(var6 = 0; var6 < 4; ++var6) {
         var7 = var1.length;
         var8 = var1[0].length;
         if(var7 != this.width || var8 != this.length) {
            var5 = false;
         }

         label86:
         for(var9 = 0; var9 < var7 && var5; ++var9) {
            for(var10 = 0; var10 < var8 && var5; ++var10) {
               try {
                  if(var9 >= this.width || var10 >= this.length || var1[var9][var10] != this.data[var3][var9 + var2][var10 + var4] && (var1[var9][var10] == 0 || this.data[var3][var9 + var2][var10 + var4] != -1)) {
                     var5 = false;
                     break label86;
                  }
               } catch (Exception var12) {
                  var5 = false;
                  break label86;
               }
            }
         }

         if(var5) {
            return var1;
         }

         var1 = rotateMatrix(var1);
         var5 = true;
      }

      return (int[][])null;
   }

   public static int[][] rotateMatrix(int[][] var0) {
      int[][] var1 = new int[var0[0].length][var0.length];
      int var2 = var0.length;
      int var3 = var0[0].length;

      for(int var4 = 0; var4 < var2; ++var4) {
         for(int var5 = 0; var5 < var3; ++var5) {
            var1[var5][var2 - 1 - var4] = var0[var4][var5];
         }
      }

      return var1;
   }

   public static int[][] flipMatrix(int[][] var0) {
      int[][] var1 = new int[var0.length][var0[0].length];
      int var2 = var0.length;
      int var3 = var0[0].length;

      for(int var4 = 0; var4 < var2; ++var4) {
         for(int var5 = 0; var5 < var3; ++var5) {
            var1[var2 - var4 - 1][var5] = var0[var4][var5];
         }
      }

      return var1;
   }

   public boolean drawOnWorld(World var1, int var2, int var3, int var4, EntityHuman var5, int var6) {
      ++var3;
      var6 = (var6 + 3) % 4;
      if(var6 == 3) {
         var6 = 1;
      } else if(var6 == 1) {
         var6 = 3;
      }

      int var7 = var2;
      int var8 = var4;
      int var9 = this.cx;
      int var10 = this.cy;
      int var11 = this.ox;
      int var12 = this.oy;
      int[][][] var13 = new int[this.setOnFire][this.width][this.length];

      int var14;
      int var15;
      for(var14 = 0; var14 < this.setOnFire; ++var14) {
         for(var15 = 0; var15 < this.width; ++var15) {
            for(int var16 = 0; var16 < this.length; ++var16) {
               var13[var14][var15][var16] = this.data[var14][var15][var16];
            }
         }
      }

      for(var14 = 0; var14 < var6; ++var14) {
         var13[0] = rotateMatrix(var13[0]);
      }

      var14 = (int)Math.floor((double)(this.data[0].length / 4));
      var14 *= 4;
      var15 = (int)Math.floor((double)(this.data[0][0].length / 4));
      var15 *= 4;
      switch(var6) {
      case 0:
         var7 = var2 + this.manRot[0];
         var8 = var4 + this.manRot[1];
         break;
      case 1:
         var11 = this.oy;
         var12 = var14 - (this.data[0].length + this.ox) % 4;
         var7 = var2 + this.manRot[2];
         var8 = var4 + this.manRot[3];
         break;
      case 2:
         var11 = var14 - (this.data[0].length + this.ox) % 4;
         var12 = var15 - (this.data[0][0].length + this.oy) % 4;
         var7 = var2 + this.manRot[4];
         var8 = var4 + this.manRot[5];
         break;
      case 3:
         var11 = var15 - (this.data[0][0].length + this.oy) % 4;
         var12 = this.ox;
         var7 = var2 + this.manRot[6];
         var8 = var4 + this.manRot[7];
      }

      ArrayList var17 = this.updateData(var13, var11, var12);
      int[] var26 = this.getBlock(var9, var10, var11, var12);
      var7 -= var26[0];
      var8 -= var26[1];
      int[] var18 = new int[5];
      ItemStack[] var19 = var5.inventory.items;
      int var20 = var19.length;

      for(int var21 = 0; var21 < var20; ++var21) {
         ItemStack var22 = var19[var21];
         if(var22 != null && var22.id == mod_DustMod.idust.id) {
            int var10001 = var22.getData();
            var18[var10001] += var22.count;
         }
      }

      if(!this.hasEnough(var18) && !var5.abilities.canInstantlyBuild) {
         var5.a("Not enough dust!");
         return false;
      } else {
         var18 = new int[5];

         int var27;
         TileEntityDust var30;
         for(var27 = 0; var27 < var17.size(); ++var27) {
            for(var20 = 0; var20 < ((ArrayList)var17.get(0)).size(); ++var20) {
               if((var1.getTypeId(var7 + var27, var3, var8 + var20) == 0 || mod_DustMod.isDust(var1.getTypeId(var7 + var27, var3, var8 + var20)) && var1.getData(var7 + var27, var3, var8 + var20) == 2 || var1.getTypeId(var7 + var27, var3, var8 + var20) == Block.LONG_GRASS.id) && var1.getTypeId(var7 + var27, var3 - 1, var8 + var20) != 0 && (Block.byId[var1.getTypeId(var7 + var27, var3 - 1, var8 + var20)].b() || var1.getTypeId(var7 + var27, var3 - 1, var8 + var20) == Block.GLASS.id)) {
                  var1.setTypeId(var7 + var27, var3, var8 + var20, 0);
                  var1.setTypeId(var7 + var27, var3, var8 + var20, mod_DustMod.dust.id);
                  TileEntity var28 = var1.getTileEntity(var7 + var27, var3, var8 + var20);
                  if(var28 != null && var28 instanceof TileEntityDust) {
                     var30 = (TileEntityDust)var28;
                     var1.setRawData(var7 + var27, var3, var8 + var20, 0);
                  } else {
                     var30 = new TileEntityDust();
                     var1.setTileEntity(var7 + var2, var3, var8 + var4, var30);
                  }

                  var30.empty();
                  int[][] var23 = (int[][])((ArrayList)var17.get(var27)).get(var20);

                  for(int var24 = 0; var24 < 4; ++var24) {
                     for(int var25 = 0; var25 < 4; ++var25) {
                        var30.setDust(var24, var25, var23[var24][var25]);
                        if(var23[var24][var25] > 0) {
                           ++var18[var23[var24][var25]];
                        }
                     }
                  }
               }
            }
         }

         for(var27 = 0; var27 < var17.size(); ++var27) {
            for(var20 = 0; var20 < ((ArrayList)var17.get(0)).size(); ++var20) {
               if(mod_DustMod.isDust(var1.getTypeId(var7 + var27, var3, var8 + var20))) {
                  var30 = (TileEntityDust)var1.getTileEntity(var7 + var27, var3, var8 + var20);
                  if(var30.isEmpty()) {
                     var1.setTypeId(var7 + var27, var3, var8 + var20, 0);
                  } else {
                     var1.notify(var7 + var27, var3, var8 + var20);
                  }
               }
            }
         }

         if(!var5.abilities.canInstantlyBuild) {
            for(var27 = 1; var27 < 5; ++var27) {
               for(var20 = 0; var20 < var5.inventory.items.length; ++var20) {
                  ItemStack var29 = var5.inventory.items[var20];
                  if(var29 != null && var29.id == mod_DustMod.idust.id && var29.getData() == var27 && var18[var27] > 0) {
                     for(; var18[var27] > 0 && var29.count > 0; --var18[var27]) {
                        --var29.count;
                        if(var29.count == 0) {
                           var5.inventory.items[var20] = null;
                        }
                     }
                  }
               }
            }

            var5.inventory.update();
         }

         this.updateData();
         return true;
      }
   }

   public boolean isEmpty(int[][] var1) {
      int[][] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int[] var5 = var2[var4];
         int[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            int var9 = var6[var8];
            if(var9 != 0) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean hasEnough(int[] var1) {
      for(int var2 = 1; var2 < 5; ++var2) {
         if(var1[var2] < this.dustAmt[var2]) {
            return false;
         }
      }

      return true;
   }

   public DustShape setName(String var1) {
      this.pName = var1;
      return this;
   }

   public DustShape setDesc(String var1) {
      this.desc = var1;
      return this;
   }
}
