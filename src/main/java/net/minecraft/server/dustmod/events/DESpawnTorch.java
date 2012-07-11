package net.minecraft.server.dustmod.events;

import net.minecraft.server.Block;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DESpawnTorch extends DustEvent {

   public void onInit(EntityDust var1) {
      var1.data = 0;
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.FLINT, 1)};
      var2 = this.sacrifice(var1, var2);
      if(var2[0].count <= 0) {
         var1.data = 1;
      }

      World var3 = var1.world;
      int var4 = var1.getX();
      int var5 = var1.getY();
      int var6 = var1.getZ();
      if(var1.data == 1) {
         var1.renderBeam = true;
         var1.rb = var1.gb = var1.bb = 255;
      } else {
         var1.locY += 0.35D;
         var1.renderStar = true;
         var1.ignoreRune = true;
         var3.setRawTypeId(var4, var5, var6, 0);
         var3.setTypeIdAndData(var4, var5, var6, Block.TORCH.id, 0);
      }

   }

   protected void onTick(EntityDust var1) {
      super.onTick(var1);
      if(var1.data == 0 && var1.world.getTypeId(var1.getX(), var1.getY(), var1.getZ()) != Block.TORCH.id) {
         var1.aI();
      }

   }
}
