 package bbhack.rom;
 
 import bbhack.MainMenu;
 
 
 
 public class ROMMapSectors
 {
   private MainMenu main;
   public int[] mapTiles;
   public boolean[] mapTileset;
   public boolean[] mapEvent;
   public int[] sectorPalette;
   public int[] sectorArea;
   public int[] sectorTileset1;
   public int[] sectorTileset2;
   
   public ROMMapSectors(MainMenu instance) {
     main = instance;
 
     mapTiles = new int[0x10000];
     mapTileset = new boolean[0x10000];
     mapEvent = new boolean[0x10000];
 
     sectorPalette = new int[0x1000];
     sectorArea = new int[0x1000];
     sectorTileset1 = new int[0x1000];
     sectorTileset2 = new int[0x1000];
 
     int[] banksMap = { 0x4010, 0x8010, 0xC010, 0x10010, 0x14010, 0x18010, 0x1C010 };
     int curBank = 0; byte b; int i, arrayOfInt1[];
     for (i = (arrayOfInt1 = banksMap).length, b = 0; b < i; ) { int offset = arrayOfInt1[b];
       for (int k = 0; k < 0x2000; k++) {
         int tileOffs = offset + k;
         int arrayOffs = curBank * 0x2000 + k;
         int currentByte = main.rom.get(tileOffs);
         
         mapTiles[arrayOffs] = currentByte % 0x40;
         
         int upper2 = currentByte / 0x40;
         if (upper2 % 2 == 1) { mapTileset[arrayOffs] = true; }
         else { mapTileset[arrayOffs] = false; }
          if (upper2 > 1) { mapEvent[arrayOffs] = true; }
         else { mapEvent[arrayOffs] = false; }
       
       }  curBank++;
       
       b++; }
     
     int[] banksSector = { 0x7810, 0xB810, 0xF810, 0x13810, 0x17810, 0x1B810, 0x1F810 };
     curBank = 0; int[] arrayOfInt2; i = 0;
     for (int j = (arrayOfInt2 = banksSector).length; i < j; ) { int offset = arrayOfInt2[i];
       for (int k = 0; k < 0x200; k++) {
         int tileOffs = offset + k * 4;
         int arrayOffs = curBank * 0x200 + k;
         
         sectorPalette[arrayOffs] = Byte.toUnsignedInt(main.rom.get(tileOffs)) % 0x40;
         sectorArea[arrayOffs] = Byte.toUnsignedInt(main.rom.get(tileOffs+1)) % 0x40;
         sectorTileset1[arrayOffs] = Byte.toUnsignedInt(main.rom.get(tileOffs+2)) % 0x40;
         sectorTileset2[arrayOffs] = Byte.toUnsignedInt(main.rom.get(tileOffs+3)) % 0x40;
       } 
       curBank++;
       i++; }
   
   }
   
   public void save() {
     int[] banksSector = { 0x7810, 0xB810, 0xF810, 0x13810, 0x17810, 0x1B810, 0x1F810 };
     int curBank = 0; byte b; int i, arrayOfInt1[];
     for (i = (arrayOfInt1 = banksSector).length, b = 0; b < i; ) { int offset = arrayOfInt1[b];
       for (int k = 0; k < 0x200; k++) {
         int tileOffs = offset + k * 4;
         int arrayOffs = curBank * 0x200 + k;
         
         main.rom.write(tileOffs, (byte)(Byte.toUnsignedInt(main.rom.get(tileOffs)) / 0x40 * 0x40 + sectorPalette[arrayOffs]));
         main.rom.write(tileOffs + 1, (byte)(Byte.toUnsignedInt(main.rom.get(tileOffs + 1)) / 0x40 * 0x40 + sectorArea[arrayOffs]));
         main.rom.write(tileOffs + 2, (byte)(Byte.toUnsignedInt(main.rom.get(tileOffs + 2)) / 0x40 * 0x40 + sectorTileset1[arrayOffs]));
         main.rom.write(tileOffs + 3, (byte)(Byte.toUnsignedInt(main.rom.get(tileOffs + 3)) / 0x40 * 0x40 + sectorTileset2[arrayOffs]));
       } 
       curBank++;
       
       b++; }
     
     int[] banksMap = { 0x4010, 0x8010, 0xC010, 0x10010, 0x14010, 0x18010, 0x1C010 };
     curBank = 0; int[] arrayOfInt2; i = 0;
     for (int j = (arrayOfInt2 = banksMap).length; i < j; ) { int offset = arrayOfInt2[i];
       for (int k = 0; k < 0x2000; k++) {
         int tileOffs = offset + k;
         int arrayOffs = curBank * 0x2000 + k;
         int curByte = mapTiles[arrayOffs];
         
         boolean curTileset = mapTileset[arrayOffs];
         boolean curEvent = mapEvent[arrayOffs];
 
         
         if (curTileset)
           curByte += 0x40; 
         if (curEvent) {
           curByte += 0x80;
         }
         
         main.rom.write(tileOffs, (byte) curByte);
       } 
       curBank++;
       i++; }
     
     main.rom.saveMap();
   }
   
   public int mapTilesGet(int x, int y) {
     return mapTiles[y * 0x100 + x];
   }
   
   public boolean mapTilesetGet(int x, int y) {
     return mapTileset[y * 0x100 + x];
   }
   
   public boolean mapEventGet(int x, int y) {
     return mapEvent[y * 0x100 + x];
   }
   
   public int sectorPaletteGet(int x, int y) {
     return sectorPalette[y * 0x40 + x];
   }
   
   public int sectorAreaGet(int x, int y) {
     return sectorArea[y * 0x40 + x];
   }
   
   public int sectorTileset1Get(int x, int y) {
     return sectorTileset1[y * 0x40 + x];
   }
   
   public int sectorTileset2Get(int x, int y) {
     return sectorTileset2[y * 0x40 + x];
   }
 }


