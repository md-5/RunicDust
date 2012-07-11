package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import net.minecraft.server.Block;
import net.minecraft.server.EntityItem;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntityMobSpawner;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DESpawnerCollector extends DustEvent {

   public void onInit(EntityDust var1) {
      var1.renderStar = true;
      var1.starScale = 1.05F;
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Block.GOLD_ORE, 6)};
      var2 = this.sacrifice(var1, var2);
      if(!this.checkSacrifice(var2) || !this.takeXP(var1, 13)) {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      int[] var2 = new int[3];

      Integer[] var4;
      for(Iterator var3 = var1.dustPoints.iterator(); var3.hasNext(); var2[2] += var4[2].intValue()) {
         var4 = (Integer[])var3.next();
         var2[0] += var4[0].intValue();
         var2[1] += var4[1].intValue();
      }

      var2[0] /= 8;
      var2[1] /= 8;
      var2[2] /= 8;
      if(var1.world.getTypeId(var2[0], var2[1], var2[2]) == Block.MOB_SPAWNER.id) {
         ((TileEntityMobSpawner)var1.world.getTileEntity(var2[0], var2[1], var2[2])).j();
         if(var1.ticksLived > 100) {
            var1.world.setTypeId(var2[0], var2[1], var2[2], 0);
            var1.world.notify(var2[0], var2[1], var2[2]);
            EntityItem var5 = new EntityItem(var1.world);
            var5.setPosition(var1.locX, var1.locY - 0.0D, var1.locZ);
            var5.itemStack = new ItemStack(Block.MOB_SPAWNER, 1);
            var1.world.addEntity(var5);
         }
      }

      if(var1.ticksLived > 100) {
         var1.fade();
      }

   }
}
