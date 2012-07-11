package net.minecraft.server.dustmod;

import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.TileEntityRut;

public class ItemChisel extends Item {

   private int tex;


   public ItemChisel(int var1) {
      super(var1);
      this.tex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/hammerandchisel.png");
      this.setMaxStackSize(1);
      this.setMaxDamage(238);
      this.setIconIndex(this.tex);
   }

   public boolean onItemUse(ItemStack var1, EntityHuman var2, World var3, int var4, int var5, int var6, int var7) {
      int var8 = var3.getTypeId(var4, var5, var6);
      int var9 = var3.getData(var4, var5, var6);
      Block var10 = Block.byId[var8];
      if(var10 == mod_DustMod.rutBlock) {
         var1.damage(1, var2);
      }

      if(var10 == null) {
         return false;
      } else if(var10.m() > Block.LOG.m()) {
         return false;
      } else if(var10.a() && var10.c() == 0 && var10.b()) {
         var1.damage(1, var2);
         var3.setTypeIdAndData(var4, var5, var6, mod_DustMod.rutBlock.id, var9);
         TileEntityRut var11 = (TileEntityRut)var3.getTileEntity(var4, var5, var6);
         var11.maskBlock = var8;
         var11.maskMeta = var9;
         mod_DustMod.rutBlock.interact(var3, var4, var5, var6, var2);
         return true;
      } else {
         return false;
      }
   }
}
