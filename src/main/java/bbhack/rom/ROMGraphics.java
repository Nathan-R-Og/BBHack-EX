 package bbhack.rom;
 
 import java.util.ArrayList;
 import bbhack.MainMenu;
 import bbhack.tiles.Tile16;
 import bbhack.tiles.Tile64;
 import bbhack.tiles.Tile8;
 
 
 
 public class ROMGraphics
 {
   private MainMenu main;
   public Tile8[] graphics8;
   public Tile16[] graphics16;
   public Tile64[] graphics64;
   
   public ROMGraphics(MainMenu instance) {
     this.main = instance;
     this.graphics8 = new Tile8[0x800];
     this.graphics16 = new Tile16[0x1000];
     this.graphics64 = new Tile64[0x800];
 
 
 
 
 
 
     
     for (int i = 0; i < this.graphics8.length; i++) {
       this.graphics8[i] = new Tile8();
     }
     
     for (int tileset = 0; tileset < 0x20; tileset++) {
       for (int tile = 0; tile < 0x40; tile++) {
         short[] bytes1short = this.main.rom.get(0x40010 + tileset * 0x400 + tile * 0x10, 8);
         short[] bytes2short = this.main.rom.get(0x40018 + tileset * 0x400 + tile * 0x10, 8);
         byte[] bytes1 = new byte[bytes1short.length];
         byte[] bytes2 = new byte[bytes2short.length];
         int[] bits1 = new int[0x40];
         int[] bits2 = new int[0x40];
         
         int n;
         for (n = 0; n < bytes1short.length; n++) {
           bytes1[n] = (byte)bytes1short[n];
           bytes2[n] = (byte)bytes2short[n];
         } 
         
         for (n = 0; n < bytes1.length; n++) {
           int i1 = 7;
           for (byte b = 1; b != 0; b = (byte)(b << 1)) {
             bits1[n * 8 + i1] = ((bytes1[n] & b) != 0) ? 1 : 0;
             i1--;
           } 
         } 
         for (n = 0; n < bytes2.length; n++) {
           int i1 = 7;
           for (byte b = 1; b != 0; b = (byte)(b << 1)) {
             bits2[n * 8 + i1] = ((bytes2[n] & b) != 0) ? 1 : 0;
             i1--;
           } 
         } 
         
         for (n = 0; n < 0x40; n++) {
           this.graphics8[tileset * 0x40 + tile].setValue(n, bits1[n] + bits2[n] * 2);
         }
       } 
     } 
 
 
 
 
 
     
     int[] banks16 = { 0x3010, 0x7010, 0xB010, 0xF010, 0x13010, 0x17010, 0x1B010, 0x1F010 };
     int curBank = 0; byte b1; int j, arrayOfInt1[];
     for (j = (arrayOfInt1 = banks16).length, b1 = 0; b1 < j; ) { int offset = arrayOfInt1[b1];
       for (int n = 0; n < 0x200; n++) {
         int i1 = curBank * 4 + n / 0x80;
         int tileOffs = offset + n * 4;
         this.graphics16[curBank * 0x200 + n] = new Tile16(this.graphics8[i1 * 0x40 + this.main.rom.get(tileOffs) % 0x40], this.graphics8[i1 * 0x40 + this.main.rom.get(tileOffs + 1) % 0x40], 
             this.graphics8[i1 * 0x40 + this.main.rom.get(tileOffs + 2) % 0x40], this.graphics8[i1 * 0x40 + this.main.rom.get(tileOffs + 3) % 0x40], n % 0x80);
       } 
       curBank++;
       
       b1++; }
     
     int[][] palettes64 = new int[0x800][0x10];
     int[] banks64Palette = { 0x3010, 0x7010, 0xB010, 0xF010, 0x13010, 0x17010, 0x1B010, 0x1F010 };
     curBank = 0; byte b2; int k, arrayOfInt2[];
     for (k = (arrayOfInt2 = banks64Palette).length, b2 = 0; b2 < k; ) { int offset = arrayOfInt2[b2];
       for (int n = 0; n < 0x100; n++) {
         int paletteOffs = offset + n * 16;
         for (int i1 = 0; i1 < 0x10; i1++) {
           palettes64[curBank * 0x100 + n][i1] = this.main.rom.get(paletteOffs + i1) / 0x40;
         }
       } 
       curBank++;
       
       b2++; }
     
     int[] banks64 = { 0x2010, 0x6010, 0xA010, 0xE010, 0x12010, 0x16010, 0x1A010, 0x1E010 };
     curBank = 0; int[] arrayOfInt3; k = 0;
     for (int m = (arrayOfInt3 = banks64).length; k < m; ) { int offset = arrayOfInt3[k];
       for (int n = 0; n < 0x100; n++) {
         int i1 = curBank * 4 + n / 0x40;
         int tileOffs = offset + n * 0x10;
         Tile16[] curTiles = new Tile16[0x10];
         ArrayList<Integer> altTileset = new ArrayList<Integer>();
         int[] tileNums = new int[0x10];
 
         
         for (int i2 = 0; i2 < 0x10; i2++) {
           int subOffs = tileOffs + i2;
           int tileNum = this.main.rom.get(subOffs) % 0x80;
           curTiles[i2] = this.graphics16[i1 * 0x80 + tileNum];
           
           tileNums[i2] = tileNum;
           
           if (this.main.rom.get(subOffs) > 0x7F) {
             altTileset.add(i2);
           }
         } 
         
         this.graphics64[curBank * 0x100 + n] = new Tile64(curTiles, palettes64[curBank * 0x100 + n], altTileset, tileNums);
       } 
       curBank++;
       k++; }
   
   }
   
   public void save64() {
     int[] banks64 = { 8208, 24592, 40976, 57360, 73744, 90128, 106512, 122896 };
     int curBank = 0; byte b1; int i, arrayOfInt1[];
     for (i = (arrayOfInt1 = banks64).length, b1 = 0; b1 < i; ) { int offset = arrayOfInt1[b1];
       for (int k = 0; k < 256; k++) {
         int tileOffs = offset + k * 16;
         Tile64 tile = this.graphics64[curBank * 256 + k];
 
         
         for (int m = 0; m < 16; m++) {
           int subOffs = tileOffs + m;
           int curByte = tile.tileNums[m];
           
           if (tile.altTileset.contains(Integer.valueOf(m))) {
             curByte += 128;
           }
           
           this.main.rom.write(subOffs, (short)curByte);
         } 
       } 
       curBank++;
       
       b1++; }
     
     int[][] palettes64 = new int[2048][16];
     int[] banks64Palette = { 12304, 28688, 45072, 61456, 77840, 94224, 110608, 126992 };
     curBank = 0; byte b2; int j, arrayOfInt2[];
     for (j = (arrayOfInt2 = banks64Palette).length, b2 = 0; b2 < j; ) { int offset = arrayOfInt2[b2];
       for (int k = 0; k < 256; k++) {
         int paletteOffs = offset + k * 16;
         int[] paletteList = this.graphics64[curBank * 256 + k].getPalettes();
         for (int m = 0; m < 16; m++) {
           this.main.rom.write(paletteOffs + m, (short)(this.main.rom.get(paletteOffs + m) % 64 + paletteList[m] * 64));
         }
       } 
       curBank++;
       b2++; }
     
     this.main.rom.saveMap();
   }
 }


