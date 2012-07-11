package net.minecraft.server.dustmod.events;

import net.minecraft.server.Block;
import net.minecraft.server.EntityItem;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DECompression extends DustEvent {

   public void onInit(EntityDust var1) {
      if(!this.takeSacrifice(var1, new ItemStack(Block.IRON_BLOCK.id, 1, -1))) {
         var1.fizzle();
      } else {
         int var2 = 0;

         for(ItemStack[] var3 = new ItemStack[]{new ItemStack(Item.COAL, 0, 0)}; var3[0].count == 0; System.out.println("DERP : " + var2 + " " + var3[0].count)) {
            var3[0].count = 32;
            var3 = this.sacrifice(var1, var3);
            if(var3[0].count <= 0) {
               ++var2;
            }
         }

         System.out.println("Diamond amt " + var2);
         var1.data = var2;
         var1.renderBeam = true;
         var1.renderStar = true;
         var1.bo = 255;
         var1.bb = 255;
      }
   }

   public void onTick(EntityDust var1) {
      var1.starScale = (float)((double)var1.starScale + 0.001D);
      if(var1.ticksExisted > 20) {
         int var2 = var1.data;
         int var3 = var2 / 64;
         int var4 = var2 % 64;
         System.out.println("Dropping " + var2 + " diamonds in " + var3 + "." + var4 + " stacks");

         for(int var5 = 0; var5 < var3; ++var5) {
            EntityItem var6 = null;
            ItemStack var7 = new ItemStack(Item.DIAMOND.id, 64, 0);
            var6 = new EntityItem(var1.worldObj, var1.posX, var1.posY - 0.0D, var1.posZ, var7);
            if(var6 != null) {
               var6.setPosition(var1.posX, var1.posY - 0.0D, var1.posZ);
               var1.worldObj.addEntity(var6);
            }
         }

         if(var4 > 0) {
            EntityItem var8 = null;
            ItemStack var9 = new ItemStack(Item.DIAMOND.id, var4, 0);
            var8 = new EntityItem(var1.worldObj, var1.posX, var1.posY - 0.0D, var1.posZ, var9);
            if(var8 != null) {
               var8.setPosition(var1.posX, var1.posY - 0.0D, var1.posZ);
               var1.worldObj.addEntity(var8);
            }
         }

         var1.fade();
      }

   }
}
