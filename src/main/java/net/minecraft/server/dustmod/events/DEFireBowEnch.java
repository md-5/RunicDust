package net.minecraft.server.dustmod.events;

import net.minecraft.server.Block;
import net.minecraft.server.Enchantment;
import net.minecraft.server.EntityItem;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DEFireBowEnch extends DustEvent {

   public void onInit(EntityDust var1) {
      ItemStack[] var2 = this.sacrifice(var1, new ItemStack[]{new ItemStack(Item.BOW, 1, 0), new ItemStack(Block.GOLD_BLOCK.id, 1, 0), new ItemStack(Item.FIREBALL, 9)});
      if(this.checkSacrifice(var2) && this.takeXP(var1, 30)) {
         var1.renderBeam = true;
         var1.renderStar = true;
         var1.bo = 255;
         var1.bb = 255;
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      var1.starScale = (float)((double)var1.starScale + 0.001D);
      if(var1.ticksExisted > 20) {
         EntityItem var2 = null;
         ItemStack var3 = new ItemStack(Item.BOW.id, 1, 0);
         var3.addEnchantment(Enchantment.ARROW_FIRE, 1);
         var2 = new EntityItem(var1.worldObj, var1.posX, var1.posY - 0.0D, var1.posZ, var3);
         if(var2 != null) {
            var2.setPosition(var1.posX, var1.posY, var1.posZ);
            var1.worldObj.addEntity(var2);
         }

         var1.fade();
      }

   }
}
