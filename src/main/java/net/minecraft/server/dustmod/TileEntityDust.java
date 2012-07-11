package net.minecraft.server.dustmod;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet132TileEntityData;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.network.PacketHandler;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;

public class TileEntityDust extends TileEntity implements IInventory {

   public static final int size = 4;
   public boolean active = false;
   private int[][] pattern = new int[4][4];
   private boolean[] dusts;
   private int toDestroy = -1;
   private int ticksLived = 0;
   private EntityDust entityDust = null;
   public List transaction = new ArrayList();


   public void onOpen(CraftHumanEntity var1) {
      this.transaction.add(var1);
   }

   public void onClose(CraftHumanEntity var1) {
      this.transaction.remove(var1);
   }

   public List getViewers() {
      return this.transaction;
   }

   public void setMaxStackSize(int var1) {}

   public ItemStack[] getContents() {
      ItemStack[] var1 = new ItemStack[this.getSizeInventory()];

      for(int var2 = 0; var2 < this.getSizeInventory(); ++var2) {
         var1[var2] = this.getStackInSlot(var2);
      }

      return var1;
   }

   public void setEntityDust(EntityDust var1) {
      this.entityDust = var1;
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);

      for(int var2 = 0; var2 < 4; ++var2) {
         for(int var3 = 0; var3 < 4; ++var3) {
            var1.setInt(var2 + "dust" + var3, this.pattern[var2][var3]);
         }
      }

      var1.setInt("toDestroy", this.toDestroy);
      var1.setInt("ticks", this.ticksLived);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);

      for(int var2 = 0; var2 < 4; ++var2) {
         for(int var3 = 0; var3 < 4; ++var3) {
            this.pattern[var2][var3] = var1.getInt(var2 + "dust" + var3);
         }
      }

      if(var1.hasKey("toDestroy")) {
         this.toDestroy = var1.getInt("toDestroy");
      }

      if(var1.hasKey("ticks")) {
         this.ticksLived = var1.getInt("ticks");
      }

   }

   public void setDust(int var1, int var2, int var3) {
      this.pattern[var1][var2] = var3;
      this.dusts = null;
      // this.onInventoryChanged(); TODO WTF
   }

   public int[][] getPattern() {
      return this.pattern;
   }

   public int getDust(int var1, int var2) {
      return this.pattern[var1][var2];
   }

   public void q_() {
      super.q_();
      if(this.ticksLived > 0 && this.isEmpty() && this.world.getData(this.x, this.y, this.z) != 10) {
         this.world.setTypeId(this.x, this.y, this.z, 0);
         System.out.println("Killing, empty");
         this.j();
      } else {
         ++this.ticksLived;
         if(this.toDestroy == 0) {
            if(this.world.getData(this.x, this.y, this.z) != 2) {
               this.toDestroy = -1;
               return;
            }

            for(int var1 = 0; (double)var1 < Math.random() * 2.0D + 2.0D; ++var1) {
               this.world.a("smoke", (double)this.x + Math.random(), (double)this.y + Math.random() / 2.0D, (double)this.z + Math.random(), 0.07D, 0.01D, 0.07D);
            }

            ArrayList var4 = new ArrayList();

            int var3;
            for(int var2 = 0; var2 < 4; ++var2) {
               for(var3 = 0; var3 < 4; ++var3) {
                  if(this.getDust(var2, var3) != 0) {
                     var4.add(Integer.valueOf(var2 * 4 + var3));
                  }
               }
            }

            Random var5 = new Random();
            if(var4.size() == 0) {
               ;
            }

            var3 = ((Integer)var4.get(var5.nextInt(var4.size()))).intValue();
            this.setDust((int)Math.floor((double)(var3 / 4)), var3 % 4, 0);
            this.toDestroy = (int)Math.round(Math.random() * 200.0D + 100.0D);
            this.world.notify(this.x, this.y, this.z);
            if(this.isEmpty() && this.world.getData(this.x, this.y, this.z) != 10) {
               this.world.setTypeId(this.x, this.y, this.z, 0);
               this.world.setTileEntity(this.x, this.y, this.z, (TileEntity)null);
               this.j();
            }
         } else if(this.toDestroy > 0) {
            --this.toDestroy;
         }

         if(this.ticksLived % 100 == 0 && this.toDestroy <= -1 && this.k() == 2) {
            this.toDestroy = (int)Math.round(Math.random() * 200.0D + 100.0D);
         }

      }
   }

   public void onRightClick(EntityHuman var1) {
      if(this.entityDust != null) {
         this.entityDust.onRightClick(this, var1);
      }

   }

   public int[][][] getRendArrays() {
      int[][][] var1 = new int[3][5][5];
      int[][] var2 = new int[6][6];

      int var3;
      int var4;
      for(var3 = 0; var3 < 4; ++var3) {
         for(var4 = 0; var4 < 4; ++var4) {
            var2[var3 + 1][var4 + 1] = this.pattern[var3][var4];
            var1[0][var3][var4] = this.pattern[var3][var4];
         }
      }

      TileEntityDust var5;
      if(mod_DustMod.isDust(this.world.getTypeId(this.x - 1, this.y, this.z))) {
         var5 = (TileEntityDust)this.world.getTileEntity(this.x - 1, this.y, this.z);

         for(var4 = 0; var4 < 4; ++var4) {
            var2[0][var4 + 1] = var5.pattern[3][var4];
         }
      }

      if(mod_DustMod.isDust(this.world.getTypeId(this.x + 1, this.y, this.z))) {
         var5 = (TileEntityDust)this.world.getTileEntity(this.x + 1, this.y, this.z);

         for(var4 = 0; var4 < 4; ++var4) {
            var2[5][var4 + 1] = var5.pattern[0][var4];
         }
      }

      if(mod_DustMod.isDust(this.world.getTypeId(this.x, this.y, this.z - 1))) {
         var5 = (TileEntityDust)this.world.getTileEntity(this.x, this.y, this.z - 1);

         for(var4 = 0; var4 < 4; ++var4) {
            var2[var4 + 1][0] = var5.pattern[var4][3];
         }
      }

      if(mod_DustMod.isDust(this.world.getTypeId(this.x, this.y, this.z + 1))) {
         var5 = (TileEntityDust)this.world.getTileEntity(this.x, this.y, this.z + 1);

         for(var4 = 0; var4 < 4; ++var4) {
            var2[var4 + 1][5] = var5.pattern[var4][0];
         }
      }

      for(var3 = 0; var3 < 4; ++var3) {
         for(var4 = 0; var4 < 5; ++var4) {
            if(var2[var3 + 1][var4] == var2[var3 + 1][var4 + 1]) {
               var1[1][var3][var4] = var2[var3 + 1][var4];
            }
         }
      }

      for(var3 = 0; var3 < 5; ++var3) {
         for(var4 = 0; var4 < 4; ++var4) {
            if(var2[var3][var4 + 1] == var2[var3 + 1][var4 + 1]) {
               var1[2][var3][var4] = var2[var3][var4 + 1];
            }
         }
      }

      return var1;
   }

   public boolean isEmpty() {
      for(int var1 = 0; var1 < 4; ++var1) {
         for(int var2 = 0; var2 < 4; ++var2) {
            if(this.pattern[var1][var2] != 0) {
               return false;
            }
         }
      }

      return true;
   }

   public int getAmount() {
      int var1 = 0;

      for(int var2 = 0; var2 < 4; ++var2) {
         for(int var3 = 0; var3 < 4; ++var3) {
            if(this.pattern[var2][var3] != 0) {
               ++var1;
            }
         }
      }

      return var1;
   }

   public boolean[] getDusts() {
      if(this.dusts == null) {
         this.dusts = new boolean[5];

         for(int var1 = 0; var1 < 4; ++var1) {
            for(int var2 = 0; var2 < 4; ++var2) {
               if(this.pattern[var1][var2] >= 0) {
                  this.dusts[this.pattern[var1][var2]] = true;
               }
            }
         }
      }

      return this.dusts;
   }

   public int getRandomDustColor() {
      int var1 = 0;
      int[] var2 = new int[4];
      boolean[] var3 = this.getDusts();

      for(int var4 = 1; var4 < 5; ++var4) {
         if(var3[var4]) {
            var2[var1] = var4;
            ++var1;
         }
      }

      if(var1 <= 0) {
         return 0;
      } else {
         Random var7 = new Random();
         int[] var5 = mod_DustMod.getColor(var2[var7.nextInt(var1)]);
         boolean var6 = false;
         return (new Color(var5[0], var5[1], var5[2])).getRGB();
      }
   }

   public void empty() {
      for(int var1 = 0; var1 < 4; ++var1) {
         for(int var2 = 0; var2 < 4; ++var2) {
            this.setDust(var1, var2, 0);
         }
      }

   }

   public void copyTo(TileEntityDust var1) {
      var1.dusts = Arrays.copyOf(this.dusts, this.dusts.length);

      int var2;
      for(var2 = 0; var2 < this.pattern.length; ++var2) {
         var1.pattern[var2] = Arrays.copyOf(this.pattern[var2], this.pattern[var2].length);
      }

      var1.toDestroy = this.toDestroy;
      var1.ticksLived = this.ticksLived;
      var2 = var1.x;
      int var3 = var1.y;
      int var4 = var1.z;
      var1.world.setRawData(var2, var3, var4, this.world.getData(this.x, this.y, this.z));
   }

   public void onDataPacket(NetworkManager var1, Packet132TileEntityData var2) {
      super.onDataPacket(var1, var2);
   }

   public int getSizeInventory() {
      return 16;
   }

   public ItemStack getStackInSlot(int var1) {
      int var2 = var1 % 4;
      int var3 = (var1 - 4) / 4;
      return this.pattern[var3][var2] == 0?null:new ItemStack(mod_DustMod.idust.id, 1, this.pattern[var3][var2]);
   }

   public ItemStack decrStackSize(int var1, int var2) {
      int var3 = var1 % 4;
      int var4 = (var1 - 4) / 4;
      this.pattern[var4][var3] = 0;
      return null;
   }

   public ItemStack getStackInSlotOnClosing(int var1) {
      return null;
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {
      int var3 = var1 % 4;
      int var4 = (var1 - 4) / 4;
      int var5 = var2.count;
      int var6 = var2.getData();
      int var7 = var2.id;
      if(var7 == mod_DustMod.idust.id && var5 > 0) {
         this.pattern[var4][var3] = var6;
      }

   }

   public String getName() {
      return "dusttileentity";
   }

   public int getInventoryStackLimit() {
      return 1;
   }

   public boolean isUseableByPlayer(EntityHuman var1) {
      return false;
   }

   public void openChest() {}

   public void closeChest() {}

   public Packet getDescriptionPacket() {
      return PacketHandler.getTEDPacket(this);
   }

    @Override
    public int getSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ItemStack getItem(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public int getMaxStackSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void f() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void g() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
