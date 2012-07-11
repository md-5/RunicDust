package net.minecraft.server.dustmod;

import net.minecraft.server.Item;
import net.minecraft.server.mod_DustMod;

public class ItemRunicTome extends Item {

   private int id;
   private int tex;


   public ItemRunicTome(int var1) {
      super(var1);
      this.id = mod_DustMod.dust.id;
   }
}
