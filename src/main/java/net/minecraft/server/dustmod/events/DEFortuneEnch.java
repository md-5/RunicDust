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

public class DEFortuneEnch extends DustEvent {

   public void onInit(EntityDust var1) {
      List var2 = this.getSacrifice(var1);
      int var3 = Item.DIAMOND_PICKAXE.id;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         EntityItem var5 = (EntityItem)var4.next();
         ItemStack var6 = var5.itemStack;
         if(var6.id == Item.DIAMOND_PICKAXE.id || var6.id == Item.DIAMOND_SWORD.id) {
            var3 = var6.id;
            break;
         }
      }

      ItemStack[] var7 = this.sacrifice(var1, new ItemStack[]{new ItemStack(var3, 1, 0), new ItemStack(Block.GOLD_BLOCK.id, 1, 0), new ItemStack(Block.DIAMOND_ORE.id, 1, 0), new ItemStack(Block.REDSTONE_ORE.id, 1, 0), new ItemStack(Block.LAPIS_ORE.id, 1, 0)});
      if(this.checkSacrifice(var7) && this.takeXP(var1, 40)) {
         var1.renderBeam = true;
         var1.renderStar = true;
         var1.bo = 255;
         var1.bb = 255;
         var1.data = var3;
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      var1.starScale = (float)((double)var1.starScale + 0.001D);
      if(var1.ticksLived > 20) {
         EntityItem var2 = null;
         ItemStack var3 = new ItemStack(var1.data, 1, 0);
         if(var1.data == Item.DIAMOND_SWORD.id) {
            var3.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 4);
         }

         if(var1.data == Item.DIAMOND_PICKAXE.id) {
            var3.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 4);
         }

         var2 = new EntityItem(var1.world, var1.locX, var1.locY - 0.0D, var1.locZ, var3);
         if(var2 != null) {
            var2.setPosition(var1.locX, var1.locY, var1.locZ);
            var1.world.addEntity(var2);
         }

         var1.fade();
      }

   }
}
