package net.minecraft.server.dustmod;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.PathfinderGoal;

public class EntityAIDustApplyHunterVision extends PathfinderGoal {

   private EntityCreature theEntity;
   private EntityHuman player;


   public EntityAIDustApplyHunterVision(EntityCreature var1) {
      this.theEntity = var1;
      this.a(0);
   }

   public boolean a() {
      return false;
   }

   public void e() {
      super.e();
   }

   public boolean b() {
      return false;
   }

   public void d() {}

   public void c() {}
}
