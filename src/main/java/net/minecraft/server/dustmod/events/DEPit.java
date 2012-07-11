package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityWeatherLighting;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DEPit extends DustEvent {

   public void onInit(EntityDust var1) {
      int var2 = var1.dustID;
      byte var3 = 1;
      switch(var2) {
      case 1:
         var3 = 8;
         break;
      case 2:
         var3 = 16;
         break;
      case 3:
         var3 = 20;
         break;
      case 4:
         var3 = 48;
      }

      boolean var4 = var2 > 2;
      ItemStack[] var5 = new ItemStack[]{new ItemStack(!var4?Block.LOG.id:Item.IRON_INGOT.id, 2, -1)};
      var5 = this.sacrifice(var1, var5);
      if(!this.checkSacrifice(var5)) {
         var1.fizzle();
      } else {
         int var6 = var1.getX();
         int var7 = var1.getY() - 1;
         int var8 = var1.getZ();
         World var9 = var1.world;
         if(var9.getTypeId(var6, var7, var8) != 0) {
            var1.fizzle();
         } else {
            for(int var10 = 0; var10 <= var3; ++var10) {
               int var11 = var9.getTypeId(var6, var7 - var10, var8);
               Block var12 = Block.byId[var11];
               if(var12 != null && var11 != Block.BEDROCK.id) {
                  var12.a(var9, var6, var7 - var10, var8, var9.getData(var6, var7 - var10, var8));
                  var12.b(var9, var6, var7 - var10, var8, var9.getData(var6, var7 - var10, var8), 0);
                  var9.setTypeId(var6, var7 - var10, var8, 0);
               }
            }

            var9.strikeLightning(new EntityWeatherLighting(var9, (double)var6, (double)var7, (double)var8));
         }
      }
   }

   public void onTick(EntityDust var1) {
      List var2 = this.getEntities(var1, 5.0D);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Entity var4 = (Entity)var3.next();
         if(var4 instanceof EntityHuman) {
            ((EntityHuman)var4).extinguish();
         }
      }

      if(var1.ticksLived > 5) {
         var1.aI();
      }

   }
}
