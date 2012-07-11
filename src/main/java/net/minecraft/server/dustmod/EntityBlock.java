package net.minecraft.server.dustmod;

import net.minecraft.server.EntityFallingBlock;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class EntityBlock extends EntityFallingBlock {

   public int meta;
   public int fallTime;
   public boolean save = true;
   public boolean hasParentDust = false;
   public long parentDustID = -1L;
   public EntityDust parentDust = null;
   public boolean going = false;
   public boolean placeWhenArrived = false;
   public boolean lingerWhenArrived = false;
   public boolean lingering = false;
   public double gx;
   public double gy;
   public double gz;
   public int lx;
   public int ly;
   public int lz;
   public double gv;
   public int origX;
   public int origY;
   public int origZ;
   boolean justBorn = true;


   public EntityBlock(World var1) {
      super(var1);
   }

   public EntityBlock(World var1, double var2, double var4, double var6, int var8) {
      super(var1, var2, var4, var6, var8, 0);
   }

   protected void entityInit() {
      super.b();
      this.dataWatcher.a(10, new Integer(this.blockID));
      this.dataWatcher.a(11, new Integer(this.meta));
   }

   public void updateDataWatcher() {
      this.dataWatcher.watch(10, new Integer(this.blockID));
      this.dataWatcher.watch(11, new Integer(this.meta));
   }

   public void updateEntityFromDataWatcher() {
      this.blockID = this.dataWatcher.getInt(10);
      this.meta = this.dataWatcher.getInt(11);
   }

   public boolean canBePushed() {
      return false;
   }

   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   public void onUpdate() {
      if(this.isBurning()) {
         this.extinguish();
      }

      if(this.worldObj.isStatic) {
         super.onEntityUpdate();
         if(this.ticksExisted % 10 == 0 && this.ticksExisted < 100 || this.ticksExisted % 60 == 0) {
            this.updateEntityFromDataWatcher();
         }

      } else {
         if(this.ticksExisted % 10 == 0 && this.ticksExisted < 100 || this.ticksExisted % 60 == 0) {
            this.updateDataWatcher();
         }

         this.motionX = this.motionY = this.motionZ = 0.0D;
         DustEvent var1;
         if(this.justBorn && this.hasParentDust && this.parentDust == null) {
            this.parentDust = mod_DustMod.getDustAtID(this.parentDustID);
            if(this.parentDust == null) {
               this.setDead();
               return;
            }

            var1 = this.parentDust.event;
            if(var1 != null) {
               var1.registerFollower(this.parentDust, this);
            }

            this.justBorn = false;
         }

         if(this.lingering && this.worldObj.getTypeId(this.lx, this.ly, this.lz) != this.blockID) {
            this.setDead();
         } else {
            if(this.going) {
               if(this.lingering && this.getDistance((double)this.lx, (double)this.ly, (double)this.lz) > 0.5D) {
                  this.worldObj.setTypeId(this.lx, this.ly, this.lz, 0);
                  this.lingering = false;
               }

               double var2 = this.goTo(this.gv, this.gx, this.gy, this.gz);
               if(this.lingerWhenArrived || this.lingering) {
                  this.noClip = true;
               }

               this.moveEntity(this.motionX, this.motionY, this.motionZ);
               if(this.lingerWhenArrived || this.lingering) {
                  this.noClip = false;
               }

               double var4 = 0.02D;
               if(this.placeWhenArrived && (var2 < 0.4D || Math.abs(this.motionX) < var4 && Math.abs(this.motionY) < var4 && Math.abs(this.motionY) < var4)) {
                  if(this.lingering) {
                     this.worldObj.setTypeId(this.lx, this.ly, this.lz, 0);
                     this.lingering = false;
                  }

                  this.place();
               }
            } else if(this.hasParentDust && this.parentDust == null) {
               this.parentDust = mod_DustMod.getDustAtID(this.parentDustID);
               if(this.parentDust != null) {
                  var1 = this.parentDust.event;
                  if(var1 != null) {
                     var1.registerFollower(this.parentDust, this);
                  }
               } else {
                  this.setDead();
               }
            } else if(this.parentDust != null && this.parentDust.isDead || this.blockID == 0) {
               this.setDead();
               return;
            }

         }
      }
   }

   protected void writeEntityToNBT(NBTTagCompound var1) {
      var1.setInt("tile", this.blockID);
      var1.setInt("meta", this.meta);
      var1.setBoolean("save", this.save);
      var1.setBoolean("hasparentdust", this.hasParentDust);
      var1.setLong("parentDustID", this.parentDustID);
      var1.setBoolean("going", this.going);
      var1.setBoolean("pwa", this.placeWhenArrived);
      var1.setDouble("gx", this.gx);
      var1.setDouble("gy", this.gy);
      var1.setDouble("gz", this.gz);
      var1.setDouble("gv", this.gv);
      var1.setBoolean("lingering", this.lingering);
      var1.setBoolean("lingerWA", this.lingerWhenArrived);
      var1.setInt("lx", this.lx);
      var1.setInt("ly", this.ly);
      var1.setInt("lz", this.lz);
   }

   protected void readEntityFromNBT(NBTTagCompound var1) {
      if(!var1.getBoolean("save")) {
         this.setDead();
      } else {
         this.blockID = var1.getInt("tile");
         this.hasParentDust = var1.getBoolean("hasparentdust");
         this.parentDustID = var1.getLong("parentDustID");
         this.going = var1.getBoolean("going");
         this.placeWhenArrived = var1.getBoolean("pwa");
         this.gx = var1.getDouble("gx");
         this.gy = var1.getDouble("gy");
         this.gz = var1.getDouble("gz");
         this.gv = var1.getDouble("gv");
         this.lingering = var1.getBoolean("lingering");
         this.lingerWhenArrived = var1.getBoolean("lingerWA");
         this.lx = var1.getInt("lx");
         this.ly = var1.getInt("ly");
         this.lz = var1.getInt("lz");
      }
   }

   public float getShadowSize() {
      return 0.0F;
   }

   public World getWorld() {
      return this.worldObj;
   }

   public void setSave(boolean var1) {
      this.save = var1;
   }

   public void placeAndLinger(double var1, double var3, double var5, double var7) {
      this.lingerWhenArrived = this.placeWhenArrived = true;
      this.goToAndPlace(var1, var3, var5, var7);
   }

   public void setOriginal(int var1, int var2, int var3) {
      this.origX = var1;
      this.origY = var2;
      this.origZ = var3;
   }

   public void returnToOrigin(double var1) {
      this.goTo(var1, (double)this.origX, (double)this.origY, (double)this.origZ);
      this.placeWhenArrived = true;
   }

   public void place() {
      this.lx = MathHelper.floor(this.posX);
      this.ly = MathHelper.floor(this.posY - 0.5D);
      this.lz = MathHelper.floor(this.posZ);
      this.going = false;
      this.placeWhenArrived = false;
      this.gv = 0.0D;
      if(this.worldObj.getTypeId(this.lx, this.ly, this.lz) == 0) {
         this.worldObj.setTypeIdAndData(this.lx, this.ly, this.lz, this.blockID, this.meta);
         if(this.lingerWhenArrived) {
            this.lingering = true;
            this.lingerWhenArrived = false;
         } else {
            this.setDead();
         }
      }

   }

   public double goToAndPlace(double var1, double var3, double var5, double var7) {
      this.placeWhenArrived = true;
      return this.goTo(var1, var3, var5, var7);
   }

   public double goTo(double var1, double var3, double var5, double var7) {
      this.gx = var3;
      this.gy = var5;
      this.gz = var7;
      double var9 = var3 - this.posX;
      double var11 = var5 - this.posY;
      double var13 = var7 - this.posZ;
      this.going = true;
      this.gv = var1;
      Vec3D var15 = Vec3D.create(var9, var11, var13);
      double var16 = this.getDistance(var3, var5, var7);
      if(var16 < 0.4D) {
         this.motionX = this.motionY = this.motionZ = 0.0D;
         return var16;
      } else {
         if(var16 < this.gv) {
            this.gv = var16;
         }

         var15 = var15.b();
         double var18 = var15.a * var1;
         double var20 = var15.b * var1;
         double var22 = var15.c * var1;
         this.motionX = var18;
         this.motionY = var20;
         this.motionZ = var22;
         return var16;
      }
   }
}
