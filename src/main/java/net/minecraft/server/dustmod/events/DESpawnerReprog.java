package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntityMobSpawner;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DESpawnerReprog extends DustEvent {

   public void onInit(EntityDust var1) {
      var1.renderBeam = true;
      var1.renderStar = true;
      var1.starScale = 1.05F;
      int var2 = -1;
      List var3 = this.getEntities(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         if(var5 instanceof EntityItem) {
            EntityItem var6 = (EntityItem)var5;
            ItemStack var7 = var6.itemStack;
            if(var7.id == Item.MONSTER_EGG.id) {
               var2 = var7.getData();
               --var7.count;
               if(var7.count <= 0) {
                  var6.setDead();
               }
            }
         }
      }

      ItemStack[] var8 = new ItemStack[]{new ItemStack(Item.ENDER_PEARL, 4)};
      var8 = this.sacrifice(var1, var8);
      if(this.checkSacrifice(var8) && var2 != -1 && this.takeXP(var1, 25)) {
         var1.data = var2;
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      if(var1.ticksExisted > 120) {
         String var2 = EntityTypes.b(EntityTypes.a(var1.data, var1.worldObj));
         int[] var3 = new int[3];

         Integer[] var5;
         for(Iterator var4 = var1.dustPoints.iterator(); var4.hasNext(); var3[2] += var5[2].intValue()) {
            var5 = (Integer[])var4.next();
            var3[0] += var5[0].intValue();
            var3[1] += var5[1].intValue();
         }

         var3[0] /= 8;
         var3[1] /= 8;
         var3[2] /= 8;
         if(var1.worldObj.getTypeId(var3[0], var3[1], var3[2]) == Block.MOB_SPAWNER.id) {
            TileEntityMobSpawner var9 = (TileEntityMobSpawner)var1.worldObj.getTileEntity(var3[0], var3[1], var3[2]);
            var9.a(var2);
            var9.validate();
         }

         var1.fade();
      } else {
         int[] var6 = new int[3];

         Integer[] var10;
         for(Iterator var7 = var1.dustPoints.iterator(); var7.hasNext(); var6[2] += var10[2].intValue()) {
            var10 = (Integer[])var7.next();
            var6[0] += var10[0].intValue();
            var6[1] += var10[1].intValue();
         }

         var6[0] /= 8;
         var6[1] /= 8;
         var6[2] /= 8;
         TileEntityMobSpawner var8 = (TileEntityMobSpawner)var1.worldObj.getTileEntity(var6[0], var6[1], var6[2]);
         var8.b = 0.0D;
      }

   }
}
