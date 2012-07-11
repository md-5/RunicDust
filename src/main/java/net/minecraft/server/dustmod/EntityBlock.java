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

   protected void b() {
      super.b();
      this.datawatcher.a(10, new Integer(this.id));
      this.datawatcher.a(11, new Integer(this.meta));
   }

   public void updatedatawatcher() {
      this.datawatcher.watch(10, new Integer(this.id));
      this.datawatcher.watch(11, new Integer(this.meta));
   }

   public void q_Fromdatawatcher() {
      this.id = this.datawatcher.getInt(10);
      this.meta = this.datawatcher.getInt(11);
   }

   public boolean canBePushed() {
      return false;
   }

   public boolean canBeCollidedWith() {
      return !this.dead;
   }

   public void a() {
      if(this.isBurning()) {
         this.extinguish();
      }

      if(this.world.isStatic) {
         super.aA();
         if(this.ticksLived % 10 == 0 && this.ticksLived < 100 || this.ticksLived % 60 == 0) {
            this.q_Fromdatawatcher();
         }

      } else {
         if(this.ticksLived % 10 == 0 && this.ticksLived < 100 || this.ticksLived % 60 == 0) {
            this.updatedatawatcher();
         }

         this.motX = this.motY = this.motZ = 0.0D;
         DustEvent var1;
         if(this.justBorn && this.hasParentDust && this.parentDust == null) {
            this.parentDust = mod_DustMod.getDustAtID(this.parentDustID);
            if(this.parentDust == null) {
               this.die();
               return;
            }

            var1 = this.parentDust.event;
            if(var1 != null) {
               var1.registerFollower(this.parentDust, this);
            }

            this.justBorn = false;
         }

         if(this.lingering && this.world.getTypeId(this.lx, this.ly, this.lz) != this.id) {
            this.die();
         } else {
            if(this.going) {
               if(this.lingering && this.f((double)this.lx, (double)this.ly, (double)this.lz) > 0.5D) {
                  this.world.setTypeId(this.lx, this.ly, this.lz, 0);
                  this.lingering = false;
               }

               double var2 = this.goTo(this.gv, this.gx, this.gy, this.gz);
               if(this.lingerWhenArrived || this.lingering) {
                  this.bQ = true;
               }

               this.a(this.motX, this.motY, this.motZ);
               if(this.lingerWhenArrived || this.lingering) {
                  this.bQ = false;
               }

               double var4 = 0.02D;
               if(this.placeWhenArrived && (var2 < 0.4D || Math.abs(this.motX) < var4 && Math.abs(this.motY) < var4 && Math.abs(this.motY) < var4)) {
                  if(this.lingering) {
                     this.world.setTypeId(this.lx, this.ly, this.lz, 0);
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
                  this.die();
               }
            } else if(this.parentDust != null && this.parentDust.dead || this.id == 0) {
               this.die();
               return;
            }

         }
      }
   }

   protected void writeEntityToNBT(NBTTagCompound var1) {
      var1.setInt("tile", this.id);
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
         this.die();
      } else {
         this.id = var1.getInt("tile");
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
      return this.world;
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
      this.lx = MathHelper.floor(this.locX);
      this.ly = MathHelper.floor(this.locY - 0.5D);
      this.lz = MathHelper.floor(this.locZ);
      this.going = false;
      this.placeWhenArrived = false;
      this.gv = 0.0D;
      if(this.world.getTypeId(this.lx, this.ly, this.lz) == 0) {
         this.world.setTypeIdAndData(this.lx, this.ly, this.lz, this.id, this.meta);
         if(this.lingerWhenArrived) {
            this.lingering = true;
            this.lingerWhenArrived = false;
         } else {
            this.die();
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
      double var9 = var3 - this.locX;
      double var11 = var5 - this.locY;
      double var13 = var7 - this.locZ;
      this.going = true;
      this.gv = var1;
      Vec3D var15 = Vec3D.create(var9, var11, var13);
      double var16 = this.f(var3, var5, var7);
      if(var16 < 0.4D) {
         this.motX = this.motY = this.motZ = 0.0D;
         return var16;
      } else {
         if(var16 < this.gv) {
            this.gv = var16;
         }

         var15 = var15.b();
         double var18 = var15.a * var1;
         double var20 = var15.b * var1;
         double var22 = var15.c * var1;
         this.motX = var18;
         this.motY = var20;
         this.motZ = var22;
         return var16;
      }
   }
}
