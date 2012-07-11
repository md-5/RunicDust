package net.minecraft.server.dustmod;

import net.minecraft.server.Block;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumToolMaterial;
import net.minecraft.server.ItemPickaxe;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class ItemSpiritPickaxe extends ItemPickaxe {

   private static Block[] blocksEffectiveAgainst;


   public ItemSpiritPickaxe(int var1, EnumToolMaterial var2) {
      super(var1, var2);
      this.setMaxDurability(24);
      this.a = 16.0F;
   }

   public boolean a(ItemStack var1, int var2, int var3, int var4, int var5, EntityLiving var6) {
      byte var7 = 1;
      World var8 = var6.world;

      for(int var9 = -var7; var9 <= var7; ++var9) {
         for(int var10 = -var7; var10 <= var7; ++var10) {
            for(int var11 = -var7; var11 <= var7; ++var11) {
               int var12 = var8.getTypeId(var9 + var3, var10 + var4, var11 + var5);
               Block var13 = Block.byId[var12];
               if(var13 != null && var13.material == Material.STONE && var13 != Block.BEDROCK) {
                  var13.a(var8, var3 + var9, var4 + var10, var5 + var11, var8.getData(var3 + var9, var4 + var10, var5 + var11));
                  var13.b(var8, var3 + var9, var4 + var10, var5 + var11, var2, 0);
                  var8.setTypeId(var3 + var9, var4 + var10, var5 + var11, 0);
               }
            }
         }
      }

      return super.a(var1, var3, var4, var5, var2, var6);
   }
}
