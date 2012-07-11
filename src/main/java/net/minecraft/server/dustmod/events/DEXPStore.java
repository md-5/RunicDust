package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.TileEntityDust;

public class DEXPStore extends DustEvent {

   public void onInit(EntityDust var1) {
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.IRON_INGOT, 16, -1)};
      this.sacrifice(var1, var2);
      if(var2[0].count <= 0 && (this.takeXP(var1, 6) || mod_DustMod.debug)) {
         var1.renderStar = true;
         var1.setColorInner(0, 255, 0);
         var1.setColorOuter(0, 255, 0);
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      List var2 = this.getEntities(var1);
      if(var1.ram > 0) {
         var1.setColorInner(255, 255, 0);
         var1.setColorOuter(255, 255, 0);
      } else {
         var1.setColorInner(0, 255, 0);
         var1.setColorOuter(0, 255, 0);
      }

      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Entity var4 = (Entity)var3.next();
         if(var4 instanceof EntityHuman && var1.ram <= 0) {
            EntityHuman var5 = (EntityHuman)var4;
            if(var5.name.equals(var1.summonerUN) && var5.expLevel > 0) {
               var5.levelDown(1);
               ++var1.data;
            }
         } else if(var4 instanceof EntityExperienceOrb) {
            var1.bb += ((EntityExperienceOrb)var4).y_();
            mod_DustMod.killEntity(var4);
         }
      }

      double var6 = 8.0D;
      var2 = this.getEntities(var1, 4.0D);
      Iterator var10 = var2.iterator();

      while(var10.hasNext()) {
         Entity var8 = (Entity)var10.next();
         if(var8 instanceof EntityExperienceOrb) {
            EntityExperienceOrb var9 = (EntityExperienceOrb)var8;
            var1.bb += ((EntityExperienceOrb)var8).y_();
            mod_DustMod.killEntity(var8);
         }
      }

      if(var1.ram > 0) {
         --var1.ram;
      }

   }

   public void onRightClick(EntityDust var1, TileEntityDust var2, EntityHuman var3) {
      super.onRightClick(var1, var2, var3);
      if(var3.name.equals(var1.summonerUN)) {
         var1.ram = 100;
         this.drop(var1);
      }

   }

   public void onUnload(EntityDust var1) {
      this.drop(var1);
      super.onUnload(var1);
   }

   public void drop(EntityDust var1) {
      EntityHuman var2 = var1.world.findNearbyPlayer(var1, 12.0D);
      if(var2 instanceof EntityHuman) {
         EntityHuman var3 = (EntityHuman)var2;
         var3.levelDown(-var1.data);
         var1.data = 0;
         var3.giveExp(var1.bb);
         var1.bb = 0;
      }

   }
}
