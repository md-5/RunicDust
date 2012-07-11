package net.minecraft.server.dustmod.network;

import forge.IConnectionHandler;
import forge.IPacketHandler;
import forge.MessageManager;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet1Login;
import net.minecraft.server.Packet250CustomPayload;
import net.minecraft.server.dustmod.TileEntityDust;
import net.minecraft.server.dustmod.TileEntityDustTable;
import net.minecraft.server.dustmod.TileEntityRut;

public class PacketHandler implements IPacketHandler, IConnectionHandler {

   public static final String CHANNEL_TEDust = "TEdust";
   public static final String CHANNEL_TELexicon = "TElexicon";
   public static final String CHANNEL_TERut = "TERut";


   public static Packet getTEDPacket(TileEntityDust var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream(140);
      DataOutputStream var2 = new DataOutputStream(var1);
      int var3 = var0.x;
      int var4 = var0.y;
      int var5 = var0.z;

      try {
         var2.writeInt(var3);
         var2.writeInt(var4);
         var2.writeInt(var5);

         for(int var6 = 0; var6 < 4; ++var6) {
            for(int var7 = 0; var7 < 4; ++var7) {
               var2.writeInt(var0.getDust(var6, var7));
            }
         }
      } catch (IOException var8) {
         ;
      }

      Packet250CustomPayload var9 = new Packet250CustomPayload();
      var9.tag = "TEdust";
      var9.data = var1.toByteArray();
      var9.length = var1.size();
      var9.lowPriority = true;
      return var9;
   }

   public static Packet getTELPacket(TileEntityDustTable var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream(140);
      DataOutputStream var2 = new DataOutputStream(var1);
      int var3 = var0.x;
      int var4 = var0.y;
      int var5 = var0.z;

      try {
         var2.writeInt(var3);
         var2.writeInt(var4);
         var2.writeInt(var5);
         var2.writeInt(var0.page);
      } catch (IOException var7) {
         ;
      }

      Packet250CustomPayload var6 = new Packet250CustomPayload();
      var6.tag = "TElexicon";
      var6.data = var1.toByteArray();
      var6.length = var1.size();
      var6.lowPriority = true;
      return var6;
   }

   public static Packet getTERPacket(TileEntityRut var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream(140);
      DataOutputStream var2 = new DataOutputStream(var1);
      int var3 = var0.x;
      int var4 = var0.y;
      int var5 = var0.z;

      try {
         var2.writeInt(var3);
         var2.writeInt(var4);
         var2.writeInt(var5);
         var2.writeInt(var0.maskBlock);
         var2.writeInt(var0.maskMeta);
         var2.writeInt(var0.fluid);

         for(int var6 = 0; var6 < 3; ++var6) {
            for(int var7 = 0; var7 < 3; ++var7) {
               for(int var8 = 0; var8 < 3; ++var8) {
                  var2.writeInt(var0.ruts[var6][var7][var8]);
               }
            }
         }
      } catch (IOException var9) {
         ;
      }

      Packet250CustomPayload var10 = new Packet250CustomPayload();
      var10.tag = "TERut";
      var10.data = var1.toByteArray();
      var10.length = var1.size();
      var10.lowPriority = true;
      return var10;
   }

   public void onPacketData(NetworkManager var1, String var2, byte[] var3) {}

   public void onConnect(NetworkManager var1) {
      MessageManager.getInstance().registerChannel(var1, this, "TEdust");
      MessageManager.getInstance().registerChannel(var1, this, "TElexicon");
      MessageManager.getInstance().registerChannel(var1, this, "TERut");
   }

   public void onLogin(NetworkManager var1, Packet1Login var2) {}

   public void onDisconnect(NetworkManager var1, String var2, Object[] var3) {
      MessageManager.getInstance().removeConnection(var1);
   }
}
