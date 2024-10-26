 package bbhack.rom;
 
 import java.util.ArrayList;
 import bbhack.MainMenu;
 import bbhack.types.Tile16;
 import bbhack.types.Tile64;
 import bbhack.types.Tile8;
 
 
 
 public class ROMGraphics
 {
   private MainMenu main;
   public Tile8[] graphics8;
   public Tile16[] graphics16;
   public Tile64[] graphics64;
   //the tiles needed to reconstruct character/object sprites
   public Tile8[] characters;
   public byte[] areaTable;
   int chr_start = 0x40010; //start of chr rom
   int[] banks1664P = { 0x3010, 0x7010, 0xB010, 0xF010, 0x13010, 0x17010, 0x1B010, 0x1F010 };
   int[] banks64 = { 0x2010, 0x6010, 0xA010, 0xE010, 0x12010, 0x16010, 0x1A010, 0x1E010 };
   
   public ROMGraphics(MainMenu instance) {
     main = instance;
     graphics8 = new Tile8[0x800];
     graphics16 = new Tile16[0x1000];
     graphics64 = new Tile64[0x800];
     characters = new Tile8[0x80*12];
 
     
     for (int i = 0; i < graphics8.length; i++) {
       graphics8[i] = new Tile8();
     }
     
     for (int i = 0; i < characters.length; i++) {
       characters[i] = new Tile8();
     }
     
     //start of character/object chr banks
     int characters_start = chr_start+0x18000; 
     for(int tile = 0; tile < characters.length; tile++){
         int newAddr = (tile * 0x10) + characters_start;
         byte[] bytes = main.rom.get(newAddr, 0x10);
         int[] palette = new int[8*8];
         for (int n = 0; n < bytes.length / 2; n++) {
           byte bpl1 = bytes[n];
           byte bpl2 = bytes[n+8];
           int i = 7;
           for (byte bit = 1; bit != 0; bit = (byte) (bit << 1)){
               //get each pixel
               int bpl1Result = ((bpl1 & bit) != 0) ? 1 : 0;
               int bpl2Result = ((bpl2 & bit) != 0) ? 2 : 0; //lsh 1
               palette[(n*8)+i] = bpl1Result | bpl2Result;
               i--;
           }
         }
         for (int i = 0; i < palette.length; i++){
            characters[tile].setValue(i, palette[i]);
         }
     }
     //area bank lookup table
     areaTable = main.rom.get(0x3D644, 0x3D644+0x40);
     
     
     //get the actual tileset tile graphics
     //chr format, obviously
     for (int tileset = 0; tileset < 0x20; tileset++) {
       for (int tile = 0; tile < 0x40; tile++) {
         byte[] bytes1 = main.rom.get(chr_start + tileset * 0x400 + tile * 0x10, 8);
         byte[] bytes2 = main.rom.get((chr_start + 8) + tileset * 0x400 + tile * 0x10, 8);
         int[] bits1 = new int[0x40];
         int[] bits2 = new int[0x40];
         
         int n;
         for (n = 0; n < bytes1.length; n++) {
           int i1 = 7;
           for (byte b = 1; b != 0; b = (byte)(b << 1)) {
             bits1[n * 8 + i1] = ((Byte.toUnsignedInt(bytes1[n]) & b) != 0) ? 1 : 0;
             i1--;
           } 
         } 
         for (n = 0; n < bytes2.length; n++) {
           int i1 = 7;
           for (byte b = 1; b != 0; b = (byte)(b << 1)) {
             bits2[n * 8 + i1] = ((Byte.toUnsignedInt(bytes2[n]) & b) != 0) ? 1 : 0;
             i1--;
           } 
         } 
         
         for (n = 0; n < 0x40; n++) {
           graphics8[tileset * 0x40 + tile].setValue(n, bits1[n] + bits2[n] * 2);
         }
       } 
     } 
    
     
     
     int curBank = 0;
     for (int offset : banks1664P) {
        for (int n = 0; n < 0x200; n++) {
          int i1 = curBank * 4 + n / 0x80;
          int tileOffs = offset + n * 4;
          graphics16[curBank * 0x200 + n] = 
          new Tile16(graphics8[i1 * 0x40 + Byte.toUnsignedInt(main.rom.get(tileOffs)) % 0x40],
                    graphics8[i1 * 0x40 + Byte.toUnsignedInt(main.rom.get(tileOffs + 1)) % 0x40], 
                    graphics8[i1 * 0x40 + Byte.toUnsignedInt(main.rom.get(tileOffs + 2)) % 0x40],
                    graphics8[i1 * 0x40 + Byte.toUnsignedInt(main.rom.get(tileOffs + 3)) % 0x40],
                    n % 0x80);
        } 
       curBank++;
     }
     
     int[][] palettes64 = new int[0x800][0x10];
     curBank = 0;
     for (int offset : banks1664P) {
        for (int n = 0; n < 0x100; n++) {
          int paletteOffs = offset + n * 16;
          for (int i1 = 0; i1 < 0x10; i1++) {
            palettes64[curBank * 0x100 + n][i1] = Byte.toUnsignedInt(main.rom.get(paletteOffs + i1)) / 0x40;
          }
        } 
        curBank++;
     }
     
     curBank = 0;
     for (int offset : banks64) {
        for (int n = 0; n < 0x100; n++) {
          int i1 = curBank * 4 + n / 0x40;
          int tileOffs = offset + n * 0x10;
          Tile16[] curTiles = new Tile16[0x10];
          ArrayList<Integer> altTileset = new ArrayList<>();
          int[] tileNums = new int[0x10];

          for (int i2 = 0; i2 < 0x10; i2++) {
            int subOffs = tileOffs + i2;
            int tileNum = Byte.toUnsignedInt(main.rom.get(subOffs)) % 0x80;
            curTiles[i2] = graphics16[i1 * 0x80 + tileNum];

            tileNums[i2] = tileNum;

            if (Byte.toUnsignedInt(main.rom.get(subOffs)) > 0x7F) {
              altTileset.add(i2);
            }
          } 
          graphics64[curBank * 0x100 + n] = new Tile64(curTiles, palettes64[curBank * 0x100 + n], altTileset, tileNums);
        } 
        curBank++;
     }
   
   }
   
   public void save64() {
     int curBank = 0;
     for (int offset : banks64) {
        for (int k = 0; k < 0x100; k++) {
          int tileOffs = offset + k * 0x10;
          Tile64 tile = graphics64[curBank * 0x100 + k];

          for (int m = 0; m < 0x10; m++) {
            int subOffs = tileOffs + m;
            int curByte = tile.tileNums[m];

            if (tile.altTileset.contains(m)) {
              curByte += 0x80;
            }

            main.rom.write(subOffs, (byte) curByte);
          } 
        } 
        curBank++;
     }
     
     curBank = 0;
     for (int offset : banks1664P) {
        for (int k = 0; k < 0x100; k++) {
          int paletteOffs = offset + k * 0x10;
          int[] paletteList = graphics64[curBank * 0x100 + k].getPalettes();
          for (int m = 0; m < 0x10; m++) {
            main.rom.write(paletteOffs + m, (byte) (main.rom.get(paletteOffs + m) % 0x40 + paletteList[m] * 0x40));
          }
        } 
        curBank++;
     }
     
     main.rom.saveMap();
   }
 }


