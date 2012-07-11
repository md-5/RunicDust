package net.minecraft.server.dustmod;

import java.util.Random;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.TileEntity;
import net.minecraft.server.dustmod.network.PacketHandler;

public class TileEntityDustTable extends TileEntity {

   public int ticks;
   public float pageFlipping;
   public float prevPageFlipping;
   public float floatd;
   public float floate;
   public float floating;
   public float prevFloating;
   public float rotation;
   public float prevRotation;
   public float rotAmt;
   private static Random rand = new Random();
   public int page = 0;
   public int dir = -1;


   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("page", this.page);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.page = var1.getInt("page");
      this.pageFlipping = this.prevPageFlipping = this.floatd = (float)this.page / 2.0F;
   }

   public void q_() {
      this.dir = this.world.getData(this.x, this.y, this.z);
      this.floatd = (float)this.page / 2.0F;
      super.q_();
      this.prevFloating = this.floating;
      this.prevRotation = this.rotation;
      EntityHuman var1 = this.world.findNearbyPlayer((double)((float)this.x + 0.5F), (double)((float)this.y + 0.5F), (double)((float)this.z + 0.5F), 3.0D);
      if(var1 != null) {
         this.floating += 0.1F;
      } else {
         this.floating -= 0.1F;
      }

      this.rotation = this.prevRotation = this.rotAmt = (float)this.dir * 1.5707964F;
      if(this.floating < 0.0F) {
         this.floating = 0.0F;
      }

      if(this.floating > 1.0F) {
         this.floating = 1.0F;
      }

      ++this.ticks;
      this.prevPageFlipping = this.pageFlipping;
      float var2 = (this.floatd - this.pageFlipping) * 0.4F;
      float var3 = 0.2F;
      if(var2 < -var3) {
         var2 = -var3;
      }

      if(var2 > var3) {
         var2 = var3;
      }

      this.floate += (var2 - this.floate) * 0.9F;
      this.pageFlipping += this.floate;
   }

   public Packet getDescriptionPacket() {
      return PacketHandler.getTELPacket(this);
   }

}
