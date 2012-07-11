package net.minecraft.server.dustmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;
import net.minecraft.server.mod_DustMod;
import net.minecraft.server.dustmod.DustEvent;
import net.minecraft.server.dustmod.DustManager;
import net.minecraft.server.dustmod.TileEntityDust;

public class EntityDust extends Entity {

   public static int DW_ri = 10;
   public static int DW_gi = 11;
   public static int DW_bi = 12;
   public static int DW_ro = 13;
   public static int DW_go = 14;
   public static int DW_bo = 15;
   public static int DW_rb = 16;
   public static int DW_gb = 17;
   public static int DW_bb = 18;
   public static int DW_rf = 19;
   public static int DW_gf = 20;
   public static int DW_bf = 21;
   public static int DW_starScale = 22;
   public static int DW_starScaleY = 23;
   public static int DW_renderBeam = 24;
   public static int DW_renderStar = 25;
   public static int DW_eventName = 26;
   public boolean reanimate = false;
   public boolean renderBeam = false;
   public boolean renderStar = false;
   public boolean ignoreRune = false;
   public boolean renderFlamesDust = false;
   public boolean renderFlamesRut = false;
   public boolean fade = false;
   public boolean follow = false;
   public Entity toFollow = null;
   public int ri;
   public int gi;
   public int bi;
   public int ro;
   public int go;
   public int bo;
   public int rb;
   public int gb;
   public int bb;
   public int rf;
   public int gf;
   public int bf;
   public float starScale = 1.0F;
   public float starScaleY = 1.0F;
   public int sacrificeWaiting = -1;
   public int fuelAmount = 0;
   public boolean requiresFuel = false;
   private boolean hasSetTileEntities = false;
   public String eventName = "";
   public DustEvent event;
   public HashMap flameRenderHelperDust;
   public HashMap flameRenderHelperRut;
   public List dustPoints;
   public List rutPoints;
   public List rutAreaPoints;
   public int[][] dusts;
   public int dustID;
   public int ram = 0;
   public boolean givenFuelThisTick = false;
   public List genericList;
   public long entityDustID;
   public int data;
   public String summonerUN;
   public int lifetime = -1;
   public boolean fueledExternally = false;


   public EntityDust(World var1) {
      super(var1);
      this.cd = true;
      this.ri = this.gi = this.bi = 255;
      this.ro = 255;
      this.go = 236;
      this.bo = 128;
      this.width = 0.2F;
      this.height = 0.01F;
      this.bQ = true;
      this.be = 50.0D;
   }

   protected void b() {
      this.datawatcher.a(DW_ri, new Integer(this.ri));
      this.datawatcher.a(DW_gi, new Integer(this.gi));
      this.datawatcher.a(DW_bi, new Integer(this.bi));
      this.datawatcher.a(DW_ro, new Integer(this.ro));
      this.datawatcher.a(DW_go, new Integer(this.go));
      this.datawatcher.a(DW_bo, new Integer(this.bo));
      this.datawatcher.a(DW_rb, new Integer(this.rb));
      this.datawatcher.a(DW_gb, new Integer(this.gb));
      this.datawatcher.a(DW_bb, new Integer(this.bb));
      this.datawatcher.a(DW_rf, new Integer(this.rf));
      this.datawatcher.a(DW_gf, new Integer(this.gf));
      this.datawatcher.a(DW_bf, new Integer(this.bf));
      this.datawatcher.a(DW_starScale, Byte.valueOf((new Float(this.starScale)).byteValue()));
      this.datawatcher.a(DW_starScaleY, Byte.valueOf((new Float(this.starScaleY)).byteValue()));
      this.datawatcher.a(DW_renderBeam, Byte.valueOf((byte)(this.renderBeam?1:0)));
      this.datawatcher.a(DW_renderStar, Byte.valueOf((byte)(this.renderStar?1:0)));
      if(this.eventName == null) {
         this.eventName = "";
      }

      this.datawatcher.a(DW_eventName, this.eventName);
   }

   public void updatedatawatcher() {
      this.datawatcher.watch(DW_ri, new Integer(this.ri));
      this.datawatcher.watch(DW_gi, new Integer(this.gi));
      this.datawatcher.watch(DW_bi, new Integer(this.bi));
      this.datawatcher.watch(DW_ro, new Integer(this.ro));
      this.datawatcher.watch(DW_go, new Integer(this.go));
      this.datawatcher.watch(DW_bo, new Integer(this.bo));
      this.datawatcher.watch(DW_rb, new Integer(this.rb));
      this.datawatcher.watch(DW_gb, new Integer(this.gb));
      this.datawatcher.watch(DW_bb, new Integer(this.bb));
      this.datawatcher.watch(DW_rf, new Integer(this.rf));
      this.datawatcher.watch(DW_gf, new Integer(this.gf));
      this.datawatcher.watch(DW_bf, new Integer(this.bf));
      this.datawatcher.watch(DW_starScale, Byte.valueOf((new Float(this.starScale)).byteValue()));
      this.datawatcher.watch(DW_starScaleY, Byte.valueOf((new Float(this.starScaleY)).byteValue()));
      this.datawatcher.watch(DW_renderBeam, Byte.valueOf((byte)(this.renderBeam?1:0)));
      this.datawatcher.watch(DW_renderStar, Byte.valueOf((byte)(this.renderStar?1:0)));
      if(this.eventName == null) {
         this.eventName = "";
      }

      this.datawatcher.watch(DW_eventName, this.eventName);
   }

   public void q_Fromdatawatcher() {
      this.ri = this.datawatcher.getInt(DW_ri);
      this.gi = this.datawatcher.getInt(DW_gi);
      this.bi = this.datawatcher.getInt(DW_bi);
      this.ro = this.datawatcher.getInt(DW_ro);
      this.go = this.datawatcher.getInt(DW_go);
      this.bo = this.datawatcher.getInt(DW_bo);
      this.rb = this.datawatcher.getInt(DW_rb);
      this.gb = this.datawatcher.getInt(DW_gb);
      this.bb = this.datawatcher.getInt(DW_bb);
      this.rf = this.datawatcher.getInt(DW_rf);
      this.gf = this.datawatcher.getInt(DW_gf);
      this.bf = this.datawatcher.getInt(DW_bf);
      this.starScale = (new Float((float)this.datawatcher.getByte(DW_starScale))).floatValue();
      this.starScaleY = (new Float((float)this.datawatcher.getByte(DW_starScaleY))).floatValue();
      this.renderBeam = this.datawatcher.getByte(DW_renderBeam) == 1;
      this.renderStar = this.datawatcher.getByte(DW_renderStar) == 1;
      this.eventName = this.datawatcher.getString(DW_eventName);
      if(this.eventName == null) {
         this.eventName = "";
      }

   }

   public void setColorStar(int var1, int var2, int var3) {
      this.setColorInner(var1, var2, var3);
      this.setColorOuter(var1, var2, var3);
   }

   public void setColorInner(int var1, int var2, int var3) {
      this.ri = var1;
      this.gi = var2;
      this.bi = var3;
      this.updatedatawatcher();
   }

   public void setColorOuter(int var1, int var2, int var3) {
      this.ro = var1;
      this.go = var2;
      this.bo = var3;
      this.updatedatawatcher();
   }

   public void setColorBeam(int var1, int var2, int var3) {
      this.rb = var1;
      this.gb = var2;
      this.bb = var3;
      this.updatedatawatcher();
   }

   public void setColorFire(int var1, int var2, int var3) {
      this.rf = var1;
      this.gf = var2;
      this.bf = var3;
      this.updatedatawatcher();
   }

   public void setEvent(DustEvent var1, String var2) {
      this.event = var1;
      this.eventName = var2;
   }

   protected void a(NBTTagCompound var1) {
      this.cd = true;
      this.ri = this.gi = this.bi = 255;
      this.ro = 255;
      this.go = 236;
      this.bo = 128;
      this.width = 0.2F;
      this.height = 0.01F;
      this.bQ = true;
      if(var1.hasKey("entityDustID")) {
         this.entityDustID = var1.getLong("entityDustID");
         mod_DustMod.registerEntityDust(this, this.entityDustID);
      } else {
         this.entityDustID = -1L;
      }

      this.eventName = var1.getString("eventname");
      this.renderBeam = var1.getBoolean("rendBeam");
      this.renderStar = var1.getBoolean("rendStar");
      if(var1.hasKey("renderFlamesDust")) {
         this.renderFlamesDust = var1.getBoolean("renderFlamesDust");
      }

      if(var1.hasKey("renderFlamesRut")) {
         this.renderFlamesRut = var1.getBoolean("renderFlamesRut");
      }

      if(var1.hasKey("sacrificeWaitingIsInvalid") && var1.getBoolean("sacrificeWaitingIsInvalid")) {
         this.sacrificeWaiting = -1;
      } else if(!var1.hasKey("sacrificeWaitingIsInvalid")) {
         this.sacrificeWaiting = -1;
      } else if(var1.hasKey("sacrificeWaiting")) {
         this.sacrificeWaiting = var1.getInt("sacrificeWatitng");
      }

      this.ignoreRune = var1.getBoolean("ignoreRune");
      this.ticksLived = var1.getInt("ticksexist");
      this.data = var1.getInt("data");
      if(var1.hasKey("requiresFuel")) {
         this.requiresFuel = var1.getBoolean("requiresFuel");
      }

      if(var1.hasKey("remainingStrength")) {
         this.fuelAmount = var1.getInt("remainingStrength");
      }

      this.event = DustManager.get(this.eventName);
      if(this.event == null) {
         this.reanimate = true;
         System.out.println("This rune has been updated!");
      }

      this.dustPoints = new ArrayList();
      int var2 = var1.getInt("pointsize");

      int var3;
      Integer[] var4;
      for(var3 = 0; var3 < var2; ++var3) {
         var4 = new Integer[]{Integer.valueOf(var1.getInt(var3 + "x")), Integer.valueOf(var1.getInt(var3 + "y")), Integer.valueOf(var1.getInt(var3 + "z"))};
         this.dustPoints.add(var4);
      }

      if(var1.hasKey("rutsize")) {
         this.rutPoints = new ArrayList();
         var2 = var1.getInt("rutsize");

         for(var3 = 0; var3 < var2; ++var3) {
            var4 = new Integer[]{Integer.valueOf(var1.getInt("r" + var3 + "x")), Integer.valueOf(var1.getInt("r" + var3 + "y")), Integer.valueOf(var1.getInt("r" + var3 + "z"))};
            this.rutPoints.add(var4);
         }
      }

      this.dustID = var1.getInt("dustid");
      this.ri = var1.getInt("ri");
      this.gi = var1.getInt("gi");
      this.bi = var1.getInt("bi");
      this.ro = var1.getInt("ro");
      this.go = var1.getInt("go");
      this.bo = var1.getInt("bo");
      this.rb = var1.getInt("rb");
      this.gb = var1.getInt("gb");
      this.bb = var1.getInt("bb");
      if(var1.hasKey("rf")) {
         this.rf = var1.getInt("rf");
      }

      if(var1.hasKey("gf")) {
         this.gf = var1.getInt("gf");
      }

      if(var1.hasKey("bf")) {
         this.bf = var1.getInt("bf");
      }

      this.fade = var1.getBoolean("fade");
      this.follow = var1.getBoolean("follow");
      this.summonerUN = var1.getString("summonerUN");
      this.starScale = var1.getFloat("starScale");
      this.starScaleY = var1.getFloat("starScaleY");
   }

   protected void b(NBTTagCompound var1) {
      if(this.lifetime == -1) {
         var1.setString("eventname", this.eventName);
         var1.setInt("ticksexist", this.ticksLived);
         var1.setBoolean("rendBeam", this.renderBeam);
         var1.setBoolean("rendStar", this.renderStar);
         var1.setBoolean("renderFlamesDust", this.renderFlamesDust);
         var1.setBoolean("renderFlamesRut", this.renderFlamesRut);
         var1.setBoolean("requiresFuel", this.requiresFuel);
         var1.setBoolean("ignoreRune", this.ignoreRune);
         var1.setBoolean("fade", this.fade);
         var1.setBoolean("follow", this.follow);
         var1.setInt("data", this.data);
         var1.setInt("remainingStrength", this.fuelAmount);
         var1.setInt("sacrificeWaiting", this.sacrificeWaiting);
         var1.setBoolean("sacrificeWaitingIsInvalid", this.sacrificeWaiting == -1);
         var1.setInt("dustid", this.dustID);
         var1.setFloat("starScale", this.starScale);
         var1.setFloat("starScaleY", this.starScaleY);
         var1.setString("summonerUN", this.summonerUN);
         var1.setInt("ri", this.ri);
         var1.setInt("gi", this.gi);
         var1.setInt("bi", this.bi);
         var1.setInt("ro", this.ro);
         var1.setInt("go", this.go);
         var1.setInt("bo", this.bo);
         var1.setInt("rb", this.rb);
         var1.setInt("gb", this.gb);
         var1.setInt("bb", this.bb);
         var1.setInt("rf", this.rf);
         var1.setInt("gf", this.gf);
         var1.setInt("bf", this.bf);
         var1.setLong("entityDustID", this.entityDustID);
         var1.setInt("pointsize", this.dustPoints.size());

         int var2;
         Integer[] var3;
         for(var2 = 0; var2 < this.dustPoints.size(); ++var2) {
            var3 = (Integer[])this.dustPoints.get(var2);
            var1.setInt(var2 + "x", var3[0].intValue());
            var1.setInt(var2 + "y", var3[1].intValue());
            var1.setInt(var2 + "z", var3[2].intValue());
         }

         if(this.rutPoints != null) {
            var1.setInt("rutsize", this.rutPoints.size());

            for(var2 = 0; var2 < this.rutPoints.size(); ++var2) {
               var3 = (Integer[])this.rutPoints.get(var2);
               var1.setInt("r" + var2 + "x", var3[0].intValue());
               var1.setInt("r" + var2 + "y", var3[1].intValue());
               var1.setInt("r" + var2 + "z", var3[2].intValue());
            }
         }

      }
   }

   public void aA() {
      boolean var1 = false;
      if(this.world.isStatic && this.event == null) {
         this.event = DustManager.get(this.eventName);
         if(this.event != null) {
            var1 = this.event.overrideRemote;
         }
      }

      if(this.world.isStatic && this.event == null && !var1) {
         super.aA();
         if(this.ticksLived % 10 == 0 && this.ticksLived < 100 || this.ticksLived % 60 == 0) {
            this.q_Fromdatawatcher();
         }

      } else {
         if(!this.world.isStatic && (this.ticksLived % 10 == 0 && this.ticksLived < 100 || this.ticksLived % 60 == 0)) {
            this.updatedatawatcher();
         }

         if(!this.world.isStatic) {
            this.givenFuelThisTick = false;
            Iterator var2;
            Integer[] var3;
            if(!this.hasSetTileEntities) {
               var2 = this.dustPoints.iterator();

               while(var2.hasNext()) {
                  var3 = (Integer[])var2.next();
                  TileEntityDust var4 = (TileEntityDust)this.world.getTileEntity(var3[0].intValue(), var3[1].intValue(), var3[2].intValue());
                  if(var4 != null) {
                     var4.setEntityDust(this);
                  }
               }

               this.hasSetTileEntities = true;
            }

            if(this.lifetime != -1) {
               super.aA();
               if(this.lifetime == 0) {
                  this.aI();
               }

               --this.lifetime;
               return;
            }

            this.cd = true;
            if(this.dustPoints == null) {
               this.aI();
               return;
            }

            int var21;
            if(this.reanimate) {
               var2 = this.dustPoints.iterator();

               while(var2.hasNext()) {
                  var3 = (Integer[])var2.next();
                  var21 = this.world.getTypeId(var3[0].intValue(), var3[1].intValue(), var3[2].intValue());
                  boolean var5 = false;
                  if(mod_DustMod.isDust(var21)) {
                     this.world.setData(var3[0].intValue(), var3[1].intValue(), var3[2].intValue(), 0);
                  }
               }

               super.aI();
               return;
            }

            if(!this.ignoreRune) {
               var2 = this.dustPoints.iterator();

               while(var2.hasNext()) {
                  var3 = (Integer[])var2.next();
                  var21 = this.world.getTypeId(var3[0].intValue(), var3[1].intValue(), var3[2].intValue());
                  if(!mod_DustMod.isDust(var21)) {
                     this.aI();
                     return;
                  }
               }
            }

            if(this.event == null || this.eventName == null || this.eventName.isEmpty()) {
               this.aI();
               return;
            }

            if(this.follow) {
               Object var18 = this.toFollow;
               if(var18 == null) {
                  EntityHuman var19 = this.world.a(this.summonerUN);
                  if(var19 == null) {
                     return;
                  }

                  var18 = var19;
               }

               byte var20 = 60;
               double var6 = 0.27D;
               double var8 = (double)(this.ticksLived % var20);
               double var10 = Math.sin(var8 / (double)var20 * 3.141592653589793D * 2.0D);
               double var12 = Math.cos(var8 / (double)var20 * 3.141592653589793D * 2.0D);
               double var14 = var12 * var6;
               double var16 = var10 * var6;
               this.setPosition(((Entity)var18).locX + var14, ((Entity)var18).locY + 0.8D + 0.0D + 1.2D, ((Entity)var18).locZ + var16);
            }

            if(this.fade) {
               this.ticksLived += 2;
               if(this.ticksLived % 200 > 190) {
                  this.aI();
               }

               super.aA();
               return;
            }

            this.event.tick(this);
         } else if(var1) {
            this.event.tick(this);
         }

         super.aA();
      }
   }

   public void shineRadius(float var1, double var2, double var4, double var6) {
      this.shineRadius(var1, var2, var4, var6, 2, "reddust");
   }

   public void shineRadius(float var1, double var2, double var4, double var6, int var8, String var9) {
      ++var2;
      Random var10 = new Random();
      float var11 = 0.7F;
      float var12 = 0.0F;

      for(int var13 = 1; var13 <= 2; ++var13) {
         float var14 = var1 / (float)var13;

         for(double var15 = 0.0D; var15 < 1.5707963267948966D; var15 += 0.1D) {
            float var17 = (float)((double)var14 * Math.cos(var15));
            float var18 = (float)((double)var14 * Math.sin(var15));

            int var19;
            for(var19 = 0; var19 < var8; ++var19) {
               this.world.a(var9, (double)((float)this.locX + var17 + var10.nextFloat() * var11 - 0.5F), (double)((float)this.locY + var12), (double)((float)this.locZ + var18 + var10.nextFloat() * var11 - 0.5F), var2, var4, var6);
            }

            for(var19 = 0; var19 < var8; ++var19) {
               this.world.a(var9, (double)((float)this.locX - var17 + var10.nextFloat() * var11 - 0.5F), (double)((float)this.locY + var12), (double)((float)this.locZ + var18 + var10.nextFloat() * var11 - 0.5F), var2, var4, var6);
            }

            for(var19 = 0; var19 < var8; ++var19) {
               this.world.a(var9, (double)((float)this.locX + var17 + var10.nextFloat() * var11 - 0.5F), (double)((float)this.locY + var12), (double)((float)this.locZ - var18 + var10.nextFloat() * var11 - 0.5F), var2, var4, var6);
            }

            for(var19 = 0; var19 < var8; ++var19) {
               this.world.a(var9, (double)((float)this.locX - var17 + var10.nextFloat() * var11 - 0.5F), (double)((float)this.locY + var12), (double)((float)this.locZ - var18 + var10.nextFloat() * var11 - 0.5F), var2, var4, var6);
            }
         }
      }

   }

   public void shineRadiusSphere(float var1, double var2, double var4, double var6) {
      --var2;
      Random var8 = new Random();
      float var9 = 0.7F;
      float var10 = var1;
      String var11 = "reddust";
      float var12 = 0.0F;

      for(double var13 = 0.0D; var13 < 1.5707963267948966D; var13 += 0.05D) {
         float var15 = (float)Math.sin(var13);
         float var16 = (float)Math.cos(var13);
         float var17 = var10 * var15;

         for(double var18 = 0.0D; var18 < 1.5707963267948966D; var18 += 0.05D) {
            float var20 = (float)Math.sin(var18);
            float var21 = (float)Math.cos(var18);
            float var22 = var10 * var21 * var16;
            float var23 = var10 * var20 * var16;
            this.world.a(var11, (double)((float)this.locX + var22 + var8.nextFloat() * var9 - 0.5F), (double)((float)this.locY + var12 + var17 + var8.nextFloat() * var9), (double)((float)this.locZ + var23 + var8.nextFloat() * var9 - 0.5F), var2, var4, var6);
            this.world.a(var11, (double)((float)this.locX - var22 + var8.nextFloat() * var9 - 0.5F), (double)((float)this.locY + var12 + var17 + var8.nextFloat() * var9), (double)((float)this.locZ + var23 + var8.nextFloat() * var9 - 0.5F), var2, var4, var6);
            this.world.a(var11, (double)((float)this.locX + var22 + var8.nextFloat() * var9 - 0.5F), (double)((float)this.locY + var12 + var17 + var8.nextFloat() * var9), (double)((float)this.locZ - var23 + var8.nextFloat() * var9 - 0.5F), var2, var4, var6);
            this.world.a(var11, (double)((float)this.locX - var22 + var8.nextFloat() * var9 - 0.5F), (double)((float)this.locY + var12 + var17 + var8.nextFloat() * var9), (double)((float)this.locZ - var23 + var8.nextFloat() * var9 - 0.5F), var2, var4, var6);
         }
      }

   }

   public void onRightClick(TileEntityDust var1, EntityHuman var2) {
      this.event.onRightClick(this, var1, var2);
   }

   public void aI() {
      if(this.event != null) {
         this.event.onUnload(this);
      }

      if(this.dustPoints == null) {
         super.aI();
      } else {
         Iterator var1 = this.dustPoints.iterator();

         while(var1.hasNext()) {
            Integer[] var2 = (Integer[])var1.next();
            int var3 = this.world.getTypeId(var2[0].intValue(), var2[1].intValue(), var2[2].intValue());
            boolean var4 = false;
            if(mod_DustMod.isDust(var3)) {
               this.world.setData(var2[0].intValue(), var2[1].intValue(), var2[2].intValue(), 2);
            }
         }

         super.aI();
      }
   }

   public void fade() {
      this.fade = true;
   }

   public void fizzle() {
      int var1 = (int)Math.floor(this.locX);
      int var2 = (int)Math.floor(this.locZ);

      for(int var3 = 0; (double)var3 < Math.random() * 5.0D + 3.0D; ++var3) {
         this.world.a("largesmoke", (double)var1 + Math.random() * 2.0D, this.locY - 1.0D - 0.0D + Math.random() / 2.0D, (double)var2 + Math.random() * 2.0D, 0.0D, 0.0D, 0.0D);
      }

      this.aI();
   }

   public int getX() {
      return (int)Math.floor(this.locX);
   }

   public int getY() {
      return (int)Math.floor(this.locY - 0.0D);
   }

   public int getZ() {
      return (int)Math.floor(this.locZ);
   }

   public void setFuel(int var1) {
      this.fuelAmount = var1;
   }

   public int getFuel() {
      return this.fuelAmount;
   }

   public boolean isFueledExternally() {
      return this.fueledExternally;
   }

}
