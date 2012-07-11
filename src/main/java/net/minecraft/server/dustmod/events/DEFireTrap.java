package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.Entity;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.events.DETrap;

public class DEFireTrap extends DETrap {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.FLINT, 3)};
      var2 = this.sacrifice(var1, var2);
      if(var2[0].count > 0) {
         var1.fizzle();
      }
   }

   public void trigger(EntityDust var1, int var2) {
      double var3 = 0.0D;
      byte var5 = 0;
      byte var6 = 0;
      byte var7 = 0;
      switch(var2) {
      case 2:
         var3 = 0.05D;
         var5 = 3;
         var6 = 5;
         var7 = 2;
         break;
      case 3:
         var3 = 0.12D;
         var5 = 4;
         var6 = 7;
         var7 = 4;
         break;
      case 4:
         var3 = 0.4D;
         var5 = 6;
         var6 = 10;
         var7 = 8;
      }

      List var8 = this.getEntities(var1, (double)var5);
      Iterator var9 = var8.iterator();

      while(var9.hasNext()) {
         Entity var10 = (Entity)var9.next();
         var10.setOnFire(var6 + (int)(Math.random() * (double)var7));
      }

      int var15 = var1.getX();
      int var16 = var1.getY();
      int var11 = var1.getZ();

      for(int var12 = -var5; var12 <= var5; ++var12) {
         for(int var13 = -var5; var13 <= var5; ++var13) {
            for(int var14 = -var5; var14 <= var5; ++var14) {
               if(var1.world.getTypeId(var15 + var12, var16 + var13 - 1, var11 + var14) != 0 && var1.world.getTypeId(var15 + var12, var16 + var13, var11 + var14) == 0 && Math.random() < var3) {
                  var1.world.setTypeId(var15 + var12, var16 + var13, var11 + var14, Block.FIRE.id);
               }
            }
         }
      }

      var1.fade();
   }
}
