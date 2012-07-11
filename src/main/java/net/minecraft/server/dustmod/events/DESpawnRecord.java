package net.minecraft.server.dustmod.events;

import java.util.Random;
import net.minecraft.server.EntityItem;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;

public class DESpawnRecord extends DustEvent {

   public void onInit(EntityDust var1) {
      var1.renderBeam = var1.renderStar = true;
      var1.setColorOuter(0, 255, 0);
      var1.setColorBeam(0, 255, 0);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.DIAMOND, 1)};
      this.sacrifice(var1, var2);
      if(var2[0].count > 0) {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      var1.starScale = (float)((double)var1.starScale + 1.0E-4D);
      if(var1.ticksExisted > 120) {
         Random var2 = new Random();
         EntityItem var3 = new EntityItem(var1.worldObj, var1.posX, var1.posY - 0.0D - 1.0D, var1.posZ, new ItemStack(2000 + var2.nextInt(11) + 256, 1, 0));
         var1.worldObj.addEntity(var3);
         var1.fade();
      }

   }
}
