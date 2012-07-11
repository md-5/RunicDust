package net.minecraft.server.dustmod;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityDust;

public class EntityAIDustFollowBaitRune extends PathfinderGoal {

   private EntityCreature theEntity;
   private EntityDust dust;
   private double movelocX;
   private double movelocY;
   private double movelocZ;
   private float speed;


   public EntityAIDustFollowBaitRune(EntityCreature var1, float var2) {
      this.theEntity = var1;
      this.speed = var2;
      this.a(1);
   }

   public boolean a() {
      Entity var1 = mod_DustMod.getEntityToAttack(this.theEntity);
      if(var1 == null) {
         return false;
      } else if(var1 instanceof EntityDust) {
         this.dust = (EntityDust)var1;
         this.movelocX = (double)this.dust.getX();
         this.movelocY = (double)this.dust.getY();
         this.movelocZ = (double)this.dust.getZ();
         return true;
      } else {
         return false;
      }
   }

   public void e() {
      super.e();
      this.theEntity.al().a(this.movelocX, this.movelocY, this.movelocZ, this.speed);
   }

   public boolean b() {
      return this.dust == null || !this.dust.dead;
   }

   public void d() {
      this.dust = null;
   }

   public void c() {
      this.theEntity.al().a(this.movelocX, this.movelocY, this.movelocZ, this.speed);
   }
}
