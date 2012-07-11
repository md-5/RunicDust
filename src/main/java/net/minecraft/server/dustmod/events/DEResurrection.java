package net.minecraft.server.dustmod.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DEResurrection extends DustEvent {

   public void onInit(EntityDust var1) {
      var1.renderBeam = true;
      var1.renderStar = true;
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.GHAST_TEAR, 1), new ItemStack(Block.SOUL_SAND, 4)};
      var2 = this.sacrifice(var1, var2);
      if(!this.checkSacrifice(var2)) {
         System.out.println("k1");
         var1.fizzle();
      } else {
         ArrayList var3 = new ArrayList();
         List var4 = this.getEntities(var1);
         Iterator var5 = var4.iterator();

         EntityItem var7;
         while(var5.hasNext()) {
            Object var6 = var5.next();
            if(var6 instanceof EntityItem) {
               var7 = (EntityItem)var6;
               var3.add(var7);
            }
         }

         if(var3.size() == 0) {
            System.out.println("k2");
            var1.aI();
         } else {
            int var14 = -1;
            Iterator var15 = var3.iterator();

            while(var15.hasNext()) {
               var7 = (EntityItem)var15.next();
               if(var14 != -1) {
                  break;
               }

               int var8 = var7.itemStack.id;
               int var9 = var7.itemStack.getData();
               int var10 = 2;
               int var11 = 2;
               Iterator var12 = var3.iterator();

               EntityItem var13;
               while(var12.hasNext()) {
                  var13 = (EntityItem)var12.next();
                  if(var13.itemStack.id == var8 && var13.itemStack.getData() == var9) {
                     var10 -= var13.itemStack.count;
                  }
               }

               if(var10 <= 0 && mod_DustMod.getEntityIDFromDrop(new ItemStack(var8, 0, var9), 0) != -1) {
                  var12 = var3.iterator();

                  while(var12.hasNext()) {
                     var13 = (EntityItem)var12.next();
                     if(var13.itemStack.id == var8 && var13.itemStack.getData() == var9) {
                        while(var11 > 0 && var13.itemStack.count > 0) {
                           --var11;
                           --var13.itemStack.count;
                           if(var13.itemStack.count <= 0) {
                              var13.die();
                           }
                        }
                     }
                  }

                  var14 = mod_DustMod.getEntityIDFromDrop(new ItemStack(var8, 0, var9), 0);
                  if(var14 != -1) {
                     break;
                  }
               }
            }

            if(var14 == -1) {
               System.out.println("k4");
               var1.fizzle();
            } else {
               var1.data = (byte)var14;
            }
         }
      }
   }

   public void onTick(EntityDust var1) {
      var1.starScale = (float)((double)var1.starScale + 0.001D);
      if(var1.ticksLived > 120) {
         Entity var2 = null;
         var2 = EntityTypes.a(var1.data, var1.world);
         if(var2 != null) {
            var2.setPosition(var1.locX, var1.locY - 0.0D, var1.locZ);
            var1.world.addEntity(var2);
         }

         var1.fade();
      }

   }
}
