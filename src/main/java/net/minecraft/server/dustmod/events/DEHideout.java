package net.minecraft.server.dustmod.events;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.BlockFluids;
import net.minecraft.server.BlockSand;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.World;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.TileEntityDust;

public class DEHideout extends DustEvent {

   public static final int thick = 2;


   public void onInit(EntityDust var1) {
      super.onInit(var1);
      if(!this.takeHunger(var1, 4)) {
         var1.fizzle();
      } else {
         var1.renderStar = true;
         var1.setColorStar(255, 255, 255);
         int var2 = var1.getX();
         int var3 = var1.getY();
         int var4 = var1.getZ();
         World var5 = var1.world;
         if(var5.getTypeId(var2, var3 - 2 - 1, var4) == 0) {
            this.doCheck(var1);
         } else {
            byte var6 = 1;
            byte var7 = 3;
            switch(var1.dustID) {
            case 1:
               var6 = 1;
               var7 = 3;
               break;
            case 2:
               var6 = 2;
               var7 = 3;
               break;
            case 3:
               var6 = 2;
               var7 = 5;
               break;
            case 4:
               var6 = 4;
               var7 = 6;
            }

            for(int var8 = -var6; var8 <= var6; ++var8) {
               for(int var9 = -var6; var9 <= var6; ++var9) {
                  for(int var10 = -2; var10 >= -var7 - 2; --var10) {
                     if(var10 == -2) {
                        Block var11 = Block.byId[var5.getTypeId(var2 + var8, var3 + var10 + 1, var4 + var9)];
                        if(var11 != null && var11 instanceof BlockSand) {
                           var5.setTypeId(var2 + var8, var3 + var10, var4 + var9, Block.SANDSTONE.id);
                        }
                     } else {
                        var5.setTypeId(var2 + var8, var3 + var10, var4 + var9, 0);
                     }
                  }
               }
            }

            Block var12 = Block.byId[var5.getTypeId(var2, var3 - var7 - 2 - 1, var4)];
            if(var12 != null && !(var12 instanceof BlockFluids)) {
               var5.setRawTypeId(var2, var3 - var7 - 2 - 1, var4, Block.COBBLESTONE.id);
               var5.setRawTypeId(var2, var3 - var7 - 2, var4, Block.TORCH.id);
            }

            this.doCheck(var1);
         }
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      if(var1.ticksLived % 10 == 0) {
         this.yCheck(var1);
      }

      if(var1.ram <= 0) {
         var1.setColorStar(255, 255, 255);
         List var2 = this.getEntities(var1, 0.2D);
         Iterator var3 = var2.iterator();

         Entity var4;
         EntityHuman var5;
         while(var3.hasNext()) {
            var4 = (Entity)var3.next();
            if(var4 instanceof EntityHuman) {
               var5 = (EntityHuman)var4;
               var1.ram = 45;
               var5.enderTeleportTo((double)var1.getX() + 0.5D, (double)(var1.data + 1) + 0.5D, (double)var1.getZ() + 0.5D);
               var5.fallDistance = 0.0F;
            }
         }

         var2 = this.getEntities(var1.world, (double)var1.getX(), (double)(var1.data + 2), (double)var1.getZ(), 0.5D);
         var3 = var2.iterator();

         while(var3.hasNext()) {
            var4 = (Entity)var3.next();
            if(var4 instanceof EntityHuman && var4.isSneaking()) {
               var5 = (EntityHuman)var4;
               var5.enderTeleportTo((double)var1.getX() + 0.5D, (double)var1.getY() + 0.5D, (double)var1.getZ() + 0.5D);
               var5.fallDistance = 0.0F;
               var1.ram = 45;
            }
         }
      } else {
         var1.setColorStar(255, 255, 0);
         --var1.ram;
      }

   }

   private void yCheck(EntityDust var1) {
      int var2 = var1.getX();
      int var3 = var1.data;
      int var4 = var1.getZ();
      World var5 = var1.world;
      int var6 = var5.getTypeId(var2, var3, var4);
      int var7 = var5.getTypeId(var2, var3 + 1, var4);
      Block var8 = Block.byId[var6];
      Block var9 = Block.byId[var7];
      if((var8 == null || var8.a()) && var8 != null) {
         if(var9 != null && var9.a()) {
            this.doCheck(var1);
         }
      } else {
         this.doCheck(var1);
      }

   }

   private void doCheck(EntityDust var1) {
      int var2;
      for(var2 = var1.getY() - 1 - 2; var2 > 3 && var2 > var1.getY() - 1 - 2 - 64; --var2) {
         int var3 = var1.world.getTypeId(var1.getX(), var2, var1.getZ());
         Block var4 = Block.byId[var3];
         if(var4 != null && var4.a()) {
            break;
         }
      }

      var1.data = var2;
   }

   public void onRightClick(EntityDust var1, TileEntityDust var2, EntityHuman var3) {
      super.onRightClick(var1, var2, var3);
   }

   public void onUnload(EntityDust var1) {
      super.onUnload(var1);
   }
}
