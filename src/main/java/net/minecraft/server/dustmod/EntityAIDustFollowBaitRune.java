package net.minecraft.server.dustmod;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityDust;

public class EntityAIDustFollowBaitRune extends PathfinderGoal {

   private EntityCreature theEntity;
   private EntityDust dust;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private float speed;


   public EntityAIDustFollowBaitRune(EntityCreature var1, float var2) {
      this.theEntity = var1;
      this.speed = var2;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      Entity var1 = mod_DustMod.getEntityToAttack(this.theEntity);
      if(var1 == null) {
         return false;
      } else if(var1 instanceof EntityDust) {
         this.dust = (EntityDust)var1;
         this.movePosX = (double)this.dust.getX();
         this.movePosY = (double)this.dust.getY();
         this.movePosZ = (double)this.dust.getZ();
         return true;
      } else {
         return false;
      }
   }

   public void updateTask() {
      super.e();
      this.theEntity.getNavigator().a(this.movePosX, this.movePosY, this.movePosZ, this.speed);
   }

   public boolean continueExecuting() {
      return this.dust == null || !this.dust.isDead;
   }

   public void resetTask() {
      this.dust = null;
   }

   public void startExecuting() {
      this.theEntity.getNavigator().a(this.movePosX, this.movePosY, this.movePosZ, this.speed);
   }
}
