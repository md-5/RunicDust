package net.minecraft.server.dustmod;

import net.minecraft.server.Enchantment;
import net.minecraft.server.Entity;
import net.minecraft.server.EnumToolMaterial;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemSword;
import net.minecraft.server.World;

public class ItemSpiritSword extends ItemSword {

   public ItemSpiritSword(int var1) {
      super(var1, EnumToolMaterial.DIAMOND);
      this.setMaxDamage(24);
   }

   public void onUpdate(ItemStack var1, World var2, Entity var3, int var4, boolean var5) {
      if(!var1.hasEnchantments()) {
         var1.addEnchantment(Enchantment.KNOCKBACK, 10);
         var1.addEnchantment(Enchantment.DAMAGE_UNDEAD, 5);
      }

      super.onUpdate(var1, var2, var3, var4, var5);
   }

   public int getDamageVsEntity(Entity var1) {
      return 12;
   }
}
