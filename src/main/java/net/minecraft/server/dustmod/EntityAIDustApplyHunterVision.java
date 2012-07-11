package net.minecraft.server.dustmod;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.PathfinderGoal;

public class EntityAIDustApplyHunterVision extends PathfinderGoal {

   private EntityCreature theEntity;
   private EntityHuman player;


   public EntityAIDustApplyHunterVision(EntityCreature var1) {
      this.theEntity = var1;
      this.setMutexBits(0);
   }

   public boolean shouldExecute() {
      return false;
   }

   public void updateTask() {
      super.e();
   }

   public boolean continueExecuting() {
      return false;
   }

   public void resetTask() {}

   public void startExecuting() {}
}
