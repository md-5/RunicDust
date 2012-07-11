package net.minecraft.server.dustmod;

import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemReed;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;

public class ItemDust extends ItemReed {

   private int id;
   private int plantTex;
   private int gunTex;
   private int lapisTex;
   private int blazeTex;


   public ItemDust(int var1, Block var2) {
      super(var1, var2);
      this.id = var2.id;
      this.setMaxDurability(0);
      this.a(true);
      this.plantTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/plantdust.png");
      this.gunTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/gundust.png");
      this.lapisTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/lapisdust.png");
      this.blazeTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/blazedust.png");
   }

   public boolean a(ItemStack var1, EntityHuman var2, World var3, int var4, int var5, int var6, int var7) {
      int var8 = var3.getTypeId(var4, var5, var6);
      if(var8 == Block.SNOW.id) {
         var7 = 0;
      } else if(var8 != Block.VINE.id) {
         if(var7 == 0) {
            --var5;
         }

         if(var7 == 1) {
            ++var5;
         }

         if(var7 == 2) {
            --var6;
         }

         if(var7 == 3) {
            ++var6;
         }

         if(var7 == 4) {
            --var4;
         }

         if(var7 == 5) {
            ++var4;
         }
      }

      if(var1.count == 0) {
         return false;
      } else if(!var2.d(var4, var5, var6)) {
         return false;
      } else if(var5 == var3.getHeight() - 1 && Block.byId[this.id].material.isBuildable()) {
         return false;
      } else if(var3.mayPlace(this.id, var4, var5, var6, true, var7)) {
         Block var10000 = Block.byId[this.id];
         if(var3.setTypeIdAndData(var4, var5, var6, this.id, this.filterData(var1.getData()))) {
            if(var3.getTypeId(var4, var5, var6) == this.id) {
               Block.byId[this.id].a(var3, var4, var5, var6, var7);
               Block.byId[this.id].a(var3, var4, var5, var6, var2);
            }

            if(!var2.abilities.canInstantlyBuild) {
               --var1.count;
            }
         }

         return true;
      } else {
         if(var3.getTypeId(var4, var5, var6) == this.id) {
            Block.byId[this.id].interact(var3, var4, var5, var6, var2);
         }

         return false;
      }
   }

   public String a(ItemStack var1) {
      switch(var1.getData()) {
      case 1:
         return "tile.plantdust";
      case 2:
         return "tile.gundust";
      case 3:
         return "tile.lapisdust";
      case 4:
         return "tile.blazedust";
      default:
         return "tile.dust";
      }
   }

   public String getLocalItemName(ItemStack var1) {
      switch(var1.getData()) {
      case 1:
         return "tile.plantdust";
      case 2:
         return "tile.gundust";
      case 3:
         return "tile.lapisdust";
      case 4:
         return "tile.blazedust";
      default:
         return "tile.dust";
      }
   }

   public int getIconFromDamage(int var1) {
      switch(var1) {
      case 1:
         return this.plantTex;
      case 2:
         return this.gunTex;
      case 3:
         return this.lapisTex;
      case 4:
         return this.blazeTex;
      default:
         return 0;
      }
   }
}
