package net.minecraft.server.dustmod;

import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.ModLoader;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustManager;
import net.minecraft.server.dustmod.TileEntityDustTable;

public class BlockDustTable extends BlockContainer {

   public static int standTex;
   public static int standTexSide;


   public BlockDustTable(int var1) {
      super(var1, 166, Material.WOOD);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
      this.f(0);
      standTex = ModLoader.addOverride("/terrain.png", mod_DustMod.path + "/standTop.png");
      standTexSide = ModLoader.addOverride("/terrain.png", mod_DustMod.path + "/standSide.png");
   }

   public boolean b() {
      return false;
   }

   public void a(World var1, int var2, int var3, int var4, EntityLiving var5) {
      int var6 = MathHelper.floor((double)(var5.yaw * 4.0F / 360.0F) + 0.5D) & 3;
      var1.setData(var2, var3, var4, var6 - 1);
   }

   public boolean a() {
      return false;
   }

   public int a(int var1, int var2) {
      return this.getBlockTextureFromSide(var1);
   }

   public int getBlockTextureFromSide(int var1) {
      return var1 == 1?standTex:(var1 == 0?Block.WOOD.textureId:standTexSide);
   }

   public TileEntity a_() {
      return new TileEntityDustTable();
   }

   public boolean blockActivated(World var1, int var2, int var3, int var4, EntityHuman var5) {
      if(var5.U() != null && var5.U().id == mod_DustMod.runicPaper.id) {
         int var9 = ((TileEntityDustTable)var1.getTileEntity(var2, var3, var4)).page - 1;
         if(var9 == -1) {
            return true;
         } else {
            var9 = DustManager.getShape(var9).id;
            ItemStack var7 = new ItemStack(mod_DustMod.dustScroll, 1, var9);
            ItemStack var8 = var5.U();
            if(var8.count == 1) {
               var8.id = mod_DustMod.dustScroll.id;
               var8.setData(var7.getData());
            } else {
               var5.inventory.pickup(var7);
               --var8.count;
            }

            return true;
         }
      } else if(var5.isSneaking()) {
         this.b(var1, var2, var3, var4, var5);
         return true;
      } else {
         TileEntityDustTable var6 = (TileEntityDustTable)var1.getTileEntity(var2, var3, var4);
         --var6.page;
         if(var6.page < 0) {
            var6.page = DustManager.names.size() - 0;
         }

         return true;
      }
   }

   public void b(World var1, int var2, int var3, int var4, EntityHuman var5) {
      TileEntityDustTable var6 = (TileEntityDustTable)var1.getTileEntity(var2, var3, var4);
      ++var6.page;
      if(var6.page >= DustManager.names.size() - 0 + 1) {
         var6.page = 0;
      }

   }
}
