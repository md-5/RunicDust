package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.Enchantment;
import net.minecraft.server.EntityItem;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.Sacrifice;

public class DESilkTouchEnch extends DustEvent {

   public void onInit(EntityDust var1) {
      List var2 = this.getSacrifice(var1);
      int var3 = -1;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         EntityItem var5 = (EntityItem)var4.next();
         ItemStack var6 = var5.itemStack;
         if(var6.id == Item.DIAMOND_PICKAXE.id || var6.id == Item.DIAMOND_SPADE.id) {
            var3 = var6.id;
            var5.setDead();
            break;
         }
      }

      ItemStack[] var7 = this.sacrifice(var1, new ItemStack[]{new ItemStack(var3, 1, 0), new ItemStack(Block.GOLD_BLOCK.id, 1, 0)});
      if(this.checkSacrifice(var7) && this.takeXP(var1, 20) && var3 != -1) {
         var1.renderBeam = true;
         var1.renderStar = true;
         var1.bo = 255;
         var1.bb = 255;
         var1.rf = var3;
         var1.sacrificeWaiting = 600;
         this.addSacrificeList(new Sacrifice[]{new Sacrifice(120)});
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      var1.starScale = (float)((double)var1.starScale + 0.001D);
      if(var1.ticksExisted > 20) {
         EntityItem var2 = null;
         ItemStack var3 = new ItemStack(var1.rf, 1, 0);
         var3.addEnchantment(Enchantment.SILK_TOUCH, 1);
         var2 = new EntityItem(var1.worldObj, var1.posX, var1.posY - 0.0D, var1.posZ, var3);
         if(var2 != null) {
            var2.setPosition(var1.posX, var1.posY, var1.posZ);
            var1.worldObj.addEntity(var2);
         }

         var1.fade();
      }

   }
}
