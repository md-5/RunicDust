package net.minecraft.server.dustmod;

import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityDust;

public class Sacrifice {

   public ItemStack itemType;
   public int entityType = -1;
   public boolean isComplete = false;
   public Entity entity;


   public Sacrifice(ItemStack var1) {
      this.itemType = var1;
   }

   public Sacrifice(EntityLiving var1) {
      this.entityType = EntityTypes.a(var1);
   }

   public Sacrifice(int var1) {
      this.entityType = var1;
   }

   public Sacrifice(String var1) {
      Entity var2 = EntityTypes.createEntityByName(var1, (World)null);
      this.entityType = EntityTypes.a(var2);
   }

   public boolean handleObject(EntityDust var1, Entity var2) {
      if(var2 == null) {
         return this.isComplete;
      } else {
         if(var2 instanceof EntityItem) {
            ItemStack var3 = ((EntityItem)var2).itemStack;
            if(var3.id == this.itemType.id && this.itemType.count > 0 && (var3.getData() == this.itemType.getData() || this.itemType.getData() == -1)) {
               int var4 = var3.count;
               var3.count -= this.itemType.count;
               if(var3.count <= 0) {
                  mod_DustMod.killEntity(var2);
               }

               this.itemType.count -= var4;
               if(this.itemType.count < 0) {
                  this.itemType.count = 0;
               }

               var1.data = var3.id;
               return true;
            }
         } else if(var2 instanceof EntityLiving) {
            int var5 = EntityTypes.a(var2);
            if(var5 == this.entityType) {
               var1.data = var5;
               EntityLiving var6 = (EntityLiving)var2;
               var6.damageEntity(DamageSource.MAGIC, var6.getHealth() * 10);
               return true;
            }
         }

         return false;
      }
   }

   public boolean matchObject(Entity var1) {
      if(var1 == null) {
         return false;
      } else {
         if(var1 instanceof EntityItem && this.itemType != null) {
            ItemStack var3 = ((EntityItem)var1).itemStack;
            if(var3.id == this.itemType.id && (var3.getData() == this.itemType.getData() || this.itemType.getData() == -1)) {
               return true;
            }
         } else if(var1 instanceof EntityLiving && this.entityType != -1) {
            int var2 = EntityTypes.a(var1);
            if(var2 == this.entityType) {
               return true;
            }
         }

         return false;
      }
   }

   public Sacrifice clone() {
      Sacrifice var1 = null;
      if(this.itemType != null) {
         var1 = new Sacrifice(new ItemStack(this.itemType.id, this.itemType.count, this.itemType.getData()));
      } else {
         var1 = new Sacrifice(this.entityType);
      }

      return var1;
   }
}
