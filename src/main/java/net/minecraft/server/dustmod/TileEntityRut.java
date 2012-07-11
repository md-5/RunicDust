package net.minecraft.server.dustmod;

import net.minecraft.server.Block;
import net.minecraft.server.BlockSand;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.network.PacketHandler;

public class TileEntityRut extends TileEntity {

   public static final float hardnessStandard = Block.GRAVEL.m();
   public int maskBlock;
   public int maskMeta;
   public int prevFluid;
   public int fluid;
   public int[][][] ruts = new int[3][3][3];
   public boolean isBeingUsed = false;
   public boolean isDead = false;
   public int ticksExisted = 0;
   public boolean[][][] neighborSolid = (boolean[][][])null;
   public boolean changed = true;


   public TileEntityRut() {
      for(int var1 = 0; var1 < 3; ++var1) {
         for(int var2 = 0; var2 < 3; ++var2) {
            for(int var3 = 0; var3 < 3; ++var3) {
               this.ruts[var1][var2][var3] = 0;
            }
         }
      }

   }

   public boolean hasChanged() {
      boolean var1 = this.changed;
      this.changed = false;
      return var1;
   }

   public void updateEntity() {
      super.q_();
      if(this.neighborSolid == null) {
         this.neighborSolid = new boolean[3][3][3];
         this.updateNeighbors();
      }

      if(!this.isEmpty() && (!(Block.byId[this.maskBlock] instanceof BlockSand) || !BlockSand.canFall(this.worldObj, this.xCoord, this.yCoord - 1, this.zCoord))) {
         int var1;
         int var2;
         int var3;
         int var4;
         if(this.worldObj.getTime() % 14L == 0L && this.prevFluid == this.fluid && this.fluidIsFluid()) {
            this.worldObj.setRawData(this.xCoord, this.yCoord, this.zCoord, this.maskMeta);
            var1 = this.xCoord;
            var2 = this.yCoord;
            var3 = this.zCoord;
            super.q_();

            for(var4 = -1; var4 <= 1; ++var4) {
               for(int var5 = -1; var5 <= 0; ++var5) {
                  for(int var6 = -1; var6 <= 1; ++var6) {
                     if((var4 != -1 && var4 != 1 || var4 != var5 || var6 != -1 && var6 != 1) && (var4 != -1 && var4 != 1 || var5 != -1 && var5 != 1 || var4 == var5 || var6 != -1 && var6 != 1) && (var5 != 0 || var4 != -1 && var4 != 1 || var6 != -1 && var6 != 1) && this.worldObj.getTypeId(var1 + var4, var2 + var5, var3 + var6) == mod_DustMod.rutBlock.id) {
                        TileEntityRut var7 = (TileEntityRut)this.worldObj.getTileEntity(var1 + var4, var2 + var5, var3 + var6);
                        if(var7.fluid == 0) {
                           var7.setFluid(this.fluid);
                        } else if(var7.fluid == Block.STATIONARY_WATER.id && this.fluid == Block.STATIONARY_LAVA.id) {
                           var7.setFluid(Block.COBBLESTONE.id);
                           this.setFluid(Block.COBBLESTONE.id);
                        } else if(this.fluid == Block.STATIONARY_WATER.id && var7.fluid == Block.STATIONARY_LAVA.id) {
                           var7.setFluid(Block.COBBLESTONE.id);
                           this.setFluid(Block.COBBLESTONE.id);
                        }
                     }
                  }
               }
            }
         }

         if(this.worldObj.getTime() % 60L == 0L && this.fluid == 0) {
            for(var1 = -1; var1 <= 1; ++var1) {
               for(var2 = -1; var2 <= 1; ++var2) {
                  for(var3 = -1; var3 <= 1; ++var3) {
                     if(var1 == var2 || var1 == var3 || var2 == var3) {
                        var4 = this.worldObj.getTypeId(this.xCoord + var1, this.yCoord + var2, this.zCoord + var3);
                        if(this.fluid == 0) {
                           if(var4 != Block.STATIONARY_LAVA.id && var4 != Block.LAVA.id) {
                              if(var4 == Block.STATIONARY_WATER.id || var4 == Block.WATER.id) {
                                 this.setFluid(Block.STATIONARY_WATER.id);
                              }
                           } else {
                              this.setFluid(Block.STATIONARY_LAVA.id);
                           }
                        }
                     }
                  }
               }
            }
         }

         this.prevFluid = this.fluid;
      } else {
         this.isDead = true;
         this.worldObj.setTypeIdAndData(this.xCoord, this.yCoord, this.zCoord, this.maskBlock, this.maskMeta);
         this.invalidate();
      }
   }

   public void updateNeighbors() {
      if(this.neighborSolid == null) {
         this.neighborSolid = new boolean[3][3][3];
      }

      this.changed = true;

      for(int var1 = -1; var1 <= 1; ++var1) {
         for(int var2 = -1; var2 <= 1; ++var2) {
            for(int var3 = -1; var3 <= 1; ++var3) {
               int var4 = this.worldObj.getTypeId(this.xCoord + var1, this.yCoord + var2, this.zCoord + var3);
               this.neighborSolid[var1 + 1][var2 + 1][var3 + 1] = var4 != 0 && (Block.byId[var4].a() || Block.byId[var4] == mod_DustMod.rutBlock);
            }
         }
      }

   }

   public boolean isNeighborSolid(int var1, int var2, int var3) {
      if(this.neighborSolid == null) {
         this.updateNeighbors();
      }

      return this.neighborSolid[var1 + 1][var2 + 1][var3 + 1];
   }

   public void writeToNBT(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("maskBlock", this.maskBlock);
      var1.setInt("maskMeta", this.maskMeta);
      var1.setInt("fluid", this.fluid);
      var1.setBoolean("isBeingUsed", this.isBeingUsed);

      for(int var2 = 0; var2 < 3; ++var2) {
         for(int var3 = 0; var3 < 3; ++var3) {
            for(int var4 = 0; var4 < 3; ++var4) {
               var1.setInt("rut[" + var2 + "," + var3 + "," + var4 + "]", this.ruts[var2][var3][var4]);
            }
         }
      }

   }

   public void readFromNBT(NBTTagCompound var1) {
      super.a(var1);
      if(var1.hasKey("maskBlock")) {
         this.maskBlock = var1.getInt("maskBlock");
      } else {
         this.maskBlock = Block.WORKBENCH.id;
         System.out.println("damn block");
      }

      if(var1.hasKey("maskMeta")) {
         this.maskMeta = var1.getInt("maskMeta");
      } else {
         this.maskMeta = 2;
      }

      if(var1.hasKey("fluid")) {
         this.fluid = var1.getInt("fluid");
      } else {
         this.fluid = 2;
      }

      if(var1.hasKey("isBeingUsed")) {
         this.isBeingUsed = var1.getBoolean("isBeingUsed");
      }

      for(int var2 = 0; var2 < 3; ++var2) {
         for(int var3 = 0; var3 < 3; ++var3) {
            for(int var4 = 0; var4 < 3; ++var4) {
               String var5 = "rut[" + var2 + "," + var3 + "," + var4 + "]";
               if(var1.hasKey(var5)) {
                  this.ruts[var2][var3][var4] = var1.getInt(var5);
               }
            }
         }
      }

   }

   public void setRut(int var1, int var2, int var3, int var4) {
      if(!this.isBeingUsed) {
         this.changed = true;
         if(this.canEdit()) {
            if((var1 == 0 || var1 == 2) && var1 == var2 && (var3 == 0 || var3 == 2)) {
               return;
            }

            if((var1 == 0 || var1 == 2) && (var2 == 0 || var2 == 2) && var1 != var2 && (var3 == 0 || var3 == 2)) {
               return;
            }

            this.ruts[var1][var2][var3] = var4;
         }

         System.out.println("Setting [" + var1 + "," + var2 + "," + var3 + "]");
         mod_DustMod.notifyBlockChange(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 0);
      }
   }

   public int getRut(int var1, int var2, int var3) {
      return this.ruts[var1][var2][var3];
   }

   public void resetBlock() {
      this.isDead = true;
      this.worldObj.setTypeIdAndData(this.xCoord, this.yCoord, this.zCoord, this.maskBlock, this.maskMeta);
   }

   public boolean fluidIsFluid() {
      Block var1 = Block.byId[this.fluid];
      return var1 == null || var1 == Block.STATIONARY_WATER || var1 == Block.STATIONARY_LAVA;
   }

   public void setFluid(int var1) {
      if(this.fluid != var1) {
         this.fluid = var1;
         mod_DustMod.notifyBlockChange(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 0);
         this.changed = true;
      }

   }

   public boolean canEdit() {
      Block var1 = Block.byId[this.fluid];
      return (this.fluidIsFluid() || var1.m() <= hardnessStandard) && !this.isBeingUsed;
   }

   public boolean isEmpty() {
      for(int var1 = 0; var1 < 3; ++var1) {
         for(int var2 = 0; var2 < 3; ++var2) {
            for(int var3 = 0; var3 < 3; ++var3) {
               if(this.getRut(var1, var2, var3) != 0) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public Packet getDescriptionPacket() {
      return PacketHandler.getTERPacket(this);
   }

}
