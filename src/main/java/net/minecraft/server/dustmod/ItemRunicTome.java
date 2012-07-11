package net.minecraft.server.dustmod;

import net.minecraft.server.Item;
import net.minecraft.server.mod_DustMod;

public class ItemRunicTome extends Item {

   private int blockID;
   private int tex;


   public ItemRunicTome(int var1) {
      super(var1);
      this.blockID = mod_DustMod.dust.id;
   }
}
