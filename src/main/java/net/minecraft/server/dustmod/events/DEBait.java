package net.minecraft.server.dustmod.events;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.ControllerLook;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PathEntity;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.EntityAIDustFollowBaitRune;
import net.minecraft.server.dustmod.EntityDust;
import net.minecraft.server.dustmod.PoweredEvent;

public class DEBait extends PoweredEvent {

   public static HashMap entdrops = new HashMap();


   public void onInit(EntityDust var1) {
      super.onInit(var1);
      var1.renderStar = true;
      int var2 = -1;
      List var3 = this.getEntities(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         if(var5 instanceof EntityItem) {
            EntityItem var6 = (EntityItem)var5;
            ItemStack var7 = var6.itemStack;
            if(var7.id == Item.MONSTER_EGG.id) {
               var2 = var7.getData();
               --var7.count;
               if(var7.count <= 0) {
                  var6.die();
               }
            }
         }
      }

      ItemStack[] var8 = new ItemStack[]{new ItemStack(Block.GOLD_BLOCK, 1)};
      var8 = this.sacrifice(var1, var8);
      if(this.checkSacrifice(var8) && var2 != -1 && this.takeXP(var1, 5)) {
         var1.data = var2;
         var1.starScale = 1.005F;
         var1.setColorOuter(255, 1, 1);
      } else {
         var1.fizzle();
      }
   }

   public void onTick(EntityDust var1) {
      super.onTick(var1);
      List var2 = this.getEntities(var1, 16.0D);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Entity var4 = (Entity)var3.next();
         if(var4 instanceof EntityCreature && EntityTypes.a(var4) == var1.data) {
            EntityCreature var5 = (EntityCreature)var4;
            if(!mod_DustMod.isAIEnabled(var5)) {
               ++var5.locY;
               var5.setPathEntity((PathEntity)null);
               var5.setTarget(var1);
               var5.setPathEntity(var1.world.a(var5, var1.getX(), var1.getY(), var1.getZ(), 10.0F, true, false, false, true));
               mod_DustMod.updateState(var5);
               var5.motY += 0.5D;
               ControllerLook var6 = var5.getControllerLook();
               var6.a(var1, 1.0F, 1.0F);
               var5.e(16.0F);
               var5.velocityChanged = true;
               mod_DustMod.setEntityToAttack(var5, var1);
               var5.b(var1.getX(), var1.getY(), var1.getZ(), 0);
               mod_DustMod.setEntityToAttack(var5, var1);
            } else {
               mod_DustMod.setEntityToAttack(var5, var1);
               if(!mod_DustMod.hasAITask(var5, new EntityAIDustFollowBaitRune((EntityCreature)null, 0.0F))) {
                  mod_DustMod.addAITask(var5, new EntityAIDustFollowBaitRune(var5, 0.22F), -1);
               }
            }
         }
      }

   }

   public static int getEntity(ItemStack var0) {
      Iterator var1 = entdrops.keySet().iterator();

      ItemStack var2;
      do {
         if(!var1.hasNext()) {
            return -1;
         }

         var2 = (ItemStack)var1.next();
      } while(var2.id != var0.id || var2.getData() != var0.getData());

      return ((Integer)entdrops.get(var2)).intValue();
   }

   public int getStartFuel() {
      return 168000;
   }

   public int getMaxFuel() {
      return 168000;
   }

   public int getStableFuelAmount(EntityDust var1) {
      return 120000;
   }

   public boolean isPaused(EntityDust var1) {
      return false;
   }

   static {
      entdrops.put(new ItemStack(Item.PORK.id, 0, 0), Integer.valueOf(90));
      entdrops.put(new ItemStack(Item.RAW_BEEF.id, 0, 0), Integer.valueOf(92));
      entdrops.put(new ItemStack(Item.RAW_CHICKEN.id, 0, 0), Integer.valueOf(93));
      entdrops.put(new ItemStack(Item.INK_SACK.id, 0, 0), Integer.valueOf(94));
      entdrops.put(new ItemStack(Item.LEATHER.id, 0, 0), Integer.valueOf(95));
      entdrops.put(new ItemStack(Block.RED_MUSHROOM.id, 0, 0), Integer.valueOf(96));
      entdrops.put(new ItemStack(Block.PUMPKIN.id, 0, 0), Integer.valueOf(97));
      entdrops.put(new ItemStack(Item.GRILLED_PORK.id, 0, 0), Integer.valueOf(57));
      entdrops.put(new ItemStack(Item.SULPHUR.id, 0, 0), Integer.valueOf(50));
      entdrops.put(new ItemStack(Item.BONE.id, 0, 0), Integer.valueOf(51));
      entdrops.put(new ItemStack(Item.STRING.id, 0, 0), Integer.valueOf(52));
      entdrops.put(new ItemStack(Item.ROTTEN_FLESH.id, 0, 0), Integer.valueOf(54));
      entdrops.put(new ItemStack(Item.SLIME_BALL.id, 0, 0), Integer.valueOf(55));
      entdrops.put(new ItemStack(Item.GHAST_TEAR.id, 0, 0), Integer.valueOf(56));
      entdrops.put(new ItemStack(Item.ENDER_PEARL.id, 0, 0), Integer.valueOf(58));
      entdrops.put(new ItemStack(Item.SPIDER_EYE.id, 0, 0), Integer.valueOf(59));
      entdrops.put(new ItemStack(Block.SMOOTH_BRICK.id, 0, 0), Integer.valueOf(60));
      entdrops.put(new ItemStack(Item.BLAZE_ROD.id, 0, 0), Integer.valueOf(61));
      entdrops.put(new ItemStack(Item.MAGMA_CREAM.id, 0, 0), Integer.valueOf(62));
   }
}
