package net.minecraft.server.dustmod.events;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityBlock;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;

public class DEEarthSprite extends PoweredEvent {

   public void onInit(EntityDust var1) {
      super.onInit(var1);
      var1.follow = true;
      var1.renderStar = true;
      var1.setColorInner(255, 0, 0);
      var1.setColorOuter(255, 0, 0);
      ItemStack[] var2 = new ItemStack[]{new ItemStack(Block.GLASS.id, 16, 0), new ItemStack(Item.GHAST_TEAR.id, 1, 0), new ItemStack(Block.IRON_ORE.id, 20, 0)};
      var2 = this.sacrifice(var1, var2);
      if(var2[0].count == 0 && var2[1].count == 0 && var2[2].count == 0 && this.takeXP(var1, 25)) {
         for(int var3 = 0; var3 < 8; ++var3) {
            EntityBlock var4 = new EntityBlock(var1.worldObj, (double)var1.getX(), (double)(var1.getY() + 2), (double)var1.getZ(), Block.GLASS.id);
            var4.hasParentDust = true;
            var4.parentDustID = var1.entityDustID;
            var4.parentDust = var1;
            this.registerFollower(var1, var4);
            var1.worldObj.addEntity(var4);
         }

      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      var1.renderStar = false;
      var1.renderBeam = false;
      var1.follow = true;
      World var2 = var1.worldObj;
      EntityHuman var3 = var1.worldObj.a(var1.summonerUN);
      if(var3 != null) {
         if(var1.genericList == null) {
            var1.genericList = new ArrayList();
         }

         if(var1.genericList.size() > 0) {
            int var4 = 0;
            float var5 = mod_DustMod.getMoveForward(var3);
            boolean var6 = var5 == 0.0F && var3.isSneaking() && Math.abs(var3.motionY) < 0.08D;
            int var7 = MathHelper.floor(var3.posX);
            int var8 = MathHelper.floor(var3.posY);
            int var9 = MathHelper.floor(var3.posZ);
            if(var6) {
               var3.setPosition((double)var7 + 0.5D, (double)var8 + (double)var3.yOffset - 1.06D, (double)var9 + 0.5D);
               var3.setMoveForward(0.0F);
            }

            for(Iterator var10 = var1.genericList.iterator(); var10.hasNext(); ++var4) {
               Object var11 = var10.next();
               EntityBlock var12 = (EntityBlock)var11;
               int var13 = 0;
               byte var14 = 0;
               int var15 = 0;
               if(var4 % 2 == 0) {
                  var14 = 1;
               }

               if(var4 < 4) {
                  var13 = var4 < 2?1:-1;
               } else {
                  var15 = var4 < 6?1:-1;
               }

               if(var6) {
                  var12.setPosition((double)(var7 + var13), (double)(var8 + var14), (double)(var9 + var15));
                  var12.placeAndLinger(0.6D, (double)(var7 + var13), (double)(var8 + var14), (double)(var9 + var15));
               } else {
                  byte var16 = 60;
                  double var17 = 3.0D;
                  double var19 = (double)((var1.ticksExisted + var4 * 8) % var16);
                  double var21 = (double)((var1.ticksExisted + var4 * 30) % var16);
                  double var23 = Math.sin(var19 / (double)var16 * 3.141592653589793D * 2.0D);
                  double var25 = Math.sin(var21 / (double)var16 * 3.141592653589793D * 2.0D);
                  double var27 = Math.cos(var19 / (double)var16 * 3.141592653589793D * 2.0D);
                  double var29 = var27 * var17;
                  double var31 = var23 * var17;
                  double var33 = var25 * 1.0D;
                  var12.goTo(2.8D, var3.posX + var29, var3.posY + var33, var3.posZ + var31);
               }
            }
         }

         if(var1.ticksExisted > 72000) {
            var1.fade();
         }

      }
   }

   public void onUnload(EntityDust var1) {
      super.onUnload(var1);
      if(var1.genericList != null) {
         Iterator var2 = var1.genericList.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            EntityBlock var4 = (EntityBlock)var3;
            int var5 = (int)var4.posY;
            int var6 = var5;

            while(true) {
               if(var6 >= 0) {
                  if(var1.worldObj.getTypeId((int)var4.posX, var6, (int)var4.posZ) == 0) {
                     --var6;
                     continue;
                  }

                  var5 = var6 + 1;
               }

               var4.setOriginal((int)var4.posX, var5 + 1, (int)var4.posZ);
               var4.returnToOrigin(0.2D);
               break;
            }
         }

      }
   }

   public void registerFollower(EntityDust var1, Object var2) {
      if(var2 instanceof EntityBlock) {
         if(var1.genericList == null) {
            var1.genericList = new ArrayList();
         }

         var1.genericList.add(var2);
      }

   }

   public int getStartFuel() {
      return 72000;
   }

   public int getMaxFuel() {
      return 168000;
   }

   public int getStableFuelAmount(EntityDust var1) {
      return '\u8ca0';
   }

   public boolean isPaused(EntityDust var1) {
      return false;
   }
}
