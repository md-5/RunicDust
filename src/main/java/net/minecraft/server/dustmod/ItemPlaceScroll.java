package net.minecraft.server.dustmod;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustManager;
import net.minecraft.server.dustmod.DustShape;

public class ItemPlaceScroll extends Item {

   private int id;


   public ItemPlaceScroll(int var1) {
      super(var1);
      this.id = mod_DustMod.dust.id;
      this.setMaxDurability(0);
      this.a(true);
      this.e(4);
      this.d(ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/scroll.png"));
   }

   public boolean a(ItemStack var1, EntityHuman var2, World var3, int var4, int var5, int var6, int var7) {
      DustShape var8 = DustManager.getShapeFromID(var1.getData());
      int var9 = MathHelper.floor((double)(var2.yaw * 4.0F / 360.0F) + 0.5D) & 3;
      if(mod_DustMod.isDust(var3.getTypeId(var4, var5, var6))) {
         --var5;
      }

      if(var8.drawOnWorld(var3, var4, var5, var6, var2, var9)) {
         --var1.count;
      }

      return true;
   }

   public String a(ItemStack var1) {
      return "tile.scroll" + DustManager.getShapeFromID(var1.getData()).name;
   }

   public String getLocalItemName(ItemStack var1) {
      return this.a(var1);
   }
}
