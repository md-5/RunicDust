package net.minecraft.server.dustmod.events;

import net.minecraft.server.Block;
import net.minecraft.server.Enchantment;
import net.minecraft.server.EntityItem;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DESpiritTool extends DustEvent {

   public void onInit(EntityDust var1) {
      var1.renderBeam = true;
      var1.renderStar = true;
      var1.bo = 255;
      var1.bb = 255;
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.GOLD_PICKAXE, 1)};
      var2 = this.sacrifice(var1, var2);
      if(!this.checkSacrifice(var2)) {
         System.out.println("check1");
         var2 = new ItemStack[]{new ItemStack(Item.GOLD_SWORD, 1), new ItemStack(Block.GLOWSTONE, 1)};
         var2 = this.sacrifice(var1, var2);
         if(!this.checkSacrifice(var2)) {
            System.out.println("check2");
            var1.fizzle();
            return;
         }

         System.out.println("check3");
         var1.data = 2;
      } else {
         var2 = new ItemStack[]{new ItemStack(Block.TNT, 4)};
         var2 = this.sacrifice(var1, var2);
         if(!this.checkSacrifice(var2)) {
            System.out.println("check4");
            var1.fizzle();
            return;
         }

         System.out.println("check5");
         var1.data = 1;
      }

      if(!this.takeXP(var1, 18)) {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      var1.starScale = (float)((double)var1.starScale + 0.001D);
      if(var1.ticksLived > 20) {
         EntityItem var2 = null;
         int var3 = 0;
         if(var1.data == 1) {
            var3 = mod_DustMod.spiritPickaxe.id;
         } else if(var1.data == 2) {
            var3 = mod_DustMod.spiritSword.id;
         }

         ItemStack var4 = new ItemStack(var3, 1, 0);
         if(var1.data == 1) {
            var4.addEnchantment(Enchantment.KNOCKBACK, 10);
            var4.addEnchantment(Enchantment.DAMAGE_UNDEAD, 5);
         }

         var2 = new EntityItem(var1.world, var1.locX, var1.locY - 0.0D, var1.locZ, var4);
         if(var2 != null) {
            var2.setPosition(var1.locX, var1.locY - 0.0D, var1.locZ);
            var1.world.addEntity(var2);
         }

         var1.fade();
      }

   }
}
