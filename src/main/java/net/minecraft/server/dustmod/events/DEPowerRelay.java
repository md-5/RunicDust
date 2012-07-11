package net.minecraft.server.dustmod.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;
import net.minecraft.server.dustmod.TileEntityDust;

public class DEPowerRelay extends PoweredEvent {

   public static int distance = 32;
   public HashMap networks = new HashMap();


   public void onInit(EntityDust var1) {
      super.onInit(var1);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Item.MONSTER_EGG, 1, 120)};
      var2 = this.sacrifice(var1, var2);
      if(this.checkSacrifice(var2) && this.takeXP(var1, 15)) {
         this.networks.put(var1, new ArrayList());
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      double var2 = (double)var1.getFuel() / (double)this.getStableFuelAmount(var1);
      int var4 = (int)(255.0D * var2);
      if(var4 > 255) {
         var4 = 255;
      }

      var1.setColorStar(255, var4, var4);
      if(var1.ticksLived % 10 == 0) {
         this.disperseFuel(var1);
      }

   }

   public void onRightClick(EntityDust var1, TileEntityDust var2, EntityHuman var3) {
      super.onRightClick(var1, var2, var3);
   }

   public void onUnload(EntityDust var1) {
      super.onUnload(var1);
      this.networks.remove(var1);
      List var2 = findDustEntities(var1);
      if(var2 != null) {
         new ArrayList();
         boolean var4 = false;
         boolean var5 = false;

         EntityDust var7;
         for(Iterator var6 = var2.iterator(); var6.hasNext(); var7.fueledExternally = false) {
            var7 = (EntityDust)var6.next();
         }

      }
   }

   public void subtractFuel(EntityDust var1) {}

   public void addFuel(EntityDust var1, int var2) {
      super.addFuel(var1, var2);
      this.disperseFuel(var1);
   }

   public void disperseFuel(EntityDust var1) {
      List var2 = findDustEntities(var1);
      int var3 = 0;
      int var4 = 0;
      var1.data = 0;
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         EntityDust var6 = (EntityDust)var5.next();
         int var7;
         if(var6.requiresFuel && var6.event != this) {
            ++var3;
            var7 = ((PoweredEvent)var6.event).powerWanted(var6);
            var4 += var7;
            this.registerSelfTo(var1, var6);
         } else if(var6.event == this && var6 != var1) {
            var7 = (var6.getFuel() + var1.getFuel()) / 2;
            var6.setFuel(var7);
            var1.setFuel(var7);
         }
      }

      this.checkNetwork(var1);
      Object var19 = (List)this.networks.get(var1);
      if(var19 == null) {
         var19 = new ArrayList();
         this.networks.put(var1, var19);
      }

      EntityDust[] var20 = new EntityDust[((List)var19).size()];
      ((List)var19).toArray(var20);
      boolean var21 = var1.getFuel() >= var4;
      int var8 = var21?var4:var1.getFuel();
      EntityDust[] var9 = var20;
      int var10 = var20.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         EntityDust var12 = var9[var11];
         if(!var12.dead && var1.j(var12) <= (double)(distance * distance)) {
            var12.fueledExternally = true;
            PoweredEvent var13 = (PoweredEvent)var12.event;
            var1.data += var13.getStableFuelAmount(var12);
            int var14 = var13.powerWanted(var12);
            if(var14 > 50) {
               var14 = 50;
            }

            if(var21) {
               int var15 = var12.getFuel();
               var12.setFuel(var12.getFuel() + var14);
               var1.setFuel(var1.getFuel() - var14);
            } else {
               double var16 = (double)var14 / (double)var4;
               int var18 = (int)(var16 * (double)var8);
               var12.setFuel(var12.getFuel() + var18);
               var1.setFuel(var1.getFuel() - var18);
            }
         } else {
            this.removeSelfFrom(var1, var12);
            var12.fueledExternally = false;
         }
      }

   }

   public static List findDustEntities(EntityDust var0) {
      int var1 = var0.getX();
      int var2 = var0.getY();
      int var3 = var0.getZ();
      byte var4 = 32;
      List var5 = var0.world.a(var0.getClass(), AxisAlignedBB.b((double)var1, (double)var2, (double)var3, (double)var1 + 1.0D, (double)var2 + 1.0D, (double)var3 + 1.0D).grow((double)var4, (double)var4, (double)var4));
      return var5;
   }

   public int getStartFuel() {
      return 0;
   }

   public int getMaxFuel() {
      return Integer.MAX_VALUE;
   }

   public int getStableFuelAmount(EntityDust var1) {
      return var1.data;
   }

   public boolean isPaused(EntityDust var1) {
      return true;
   }

   public void registerSelfTo(EntityDust var1, EntityDust var2) {
      this.checkNetwork(var1);
      List var3 = (List)this.networks.get(var1);
      if(!var3.contains(var2)) {
         var3.add(var2);
      }

   }

   public void removeSelfFrom(EntityDust var1, EntityDust var2) {
      this.checkNetwork(var1);
      List var3 = (List)this.networks.get(var1);
      if(var3.contains(var2)) {
         var3.remove(var2);
      }

   }

   private void checkNetwork(EntityDust var1) {
      if(!this.networks.containsKey(var1)) {
         this.networks.put(var1, new ArrayList());
      }

   }

}
