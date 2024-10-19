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
     this.main = instance;
 
     
     this.mapTiles = new int[0x10000];
     this.mapTileset = new boolean[0x10000];
     this.mapEvent = new boolean[0x10000];
 
     
     this.sectorPalette = new int[0x1000];
     this.sectorArea = new int[0x1000];
     this.sectorTileset1 = new int[0x1000];
     this.sectorTileset2 = new int[0x1000];
 
     
     int[] banksMap = { 0x4010, 0x8010, 0xC010, 0x10010, 0x14010, 0x18010, 0x1C010 };
     int curBank = 0; byte b; int i, arrayOfInt1[];
     for (i = (arrayOfInt1 = banksMap).length, b = 0; b < i; ) { int offset = arrayOfInt1[b];
       for (int k = 0; k < 0x2000; k++) {
         int tileOffs = offset + k;
         int arrayOffs = curBank * 0x2000 + k;
         int currentByte = this.main.rom.get(tileOffs);
         
         this.mapTiles[arrayOffs] = currentByte % 0x40;
         
         int upper2 = currentByte / 0x40;
         if (upper2 % 2 == 1) { this.mapTileset[arrayOffs] = true; }
         else { this.mapTileset[arrayOffs] = false; }
          if (upper2 > 1) { this.mapEvent[arrayOffs] = true; }
         else { this.mapEvent[arrayOffs] = false; }
       
       }  curBank++;
       
       b++; }
     
     int[] banksSector = { 0x7810, 0xB810, 0xF810, 0x13810, 0x17810, 0x1B810, 0x1F810 };
     curBank = 0; int[] arrayOfInt2; i = 0;
     for (int j = (arrayOfInt2 = banksSector).length; i < j; ) { int offset = arrayOfInt2[i];
       for (int k = 0; k < 512; k++) {
         int tileOffs = offset + k * 4;
         int arrayOffs = curBank * 512 + k;
 
         
         this.sectorPalette[arrayOffs] = this.main.rom.get(tileOffs) % 0x40;
         this.sectorArea[arrayOffs] = this.main.rom.get(tileOffs + 1) % 0x40;
         this.sectorTileset1[arrayOffs] = this.main.rom.get(tileOffs + 2) % 0x40;
         this.sectorTileset2[arrayOffs] = this.main.rom.get(tileOffs + 3) % 0x40;
       } 
       curBank++;
       i++; }
   
   }
   
   public void save() {
     int[] banksSector = { 0x7810, 0xB810, 0xF810, 0x13810, 0x17810, 0x1B810, 0x1F810 };
     int curBank = 0; byte b; int i, arrayOfInt1[];
     for (i = (arrayOfInt1 = banksSector).length, b = 0; b < i; ) { int offset = arrayOfInt1[b];
       for (int k = 0; k < 512; k++) {
         int tileOffs = offset + k * 4;
         int arrayOffs = curBank * 512 + k;
         
         this.main.rom.write(tileOffs, (short)(this.main.rom.get(tileOffs) / 64 * 64 + this.sectorPalette[arrayOffs]));
         this.main.rom.write(tileOffs + 1, (short)(this.main.rom.get(tileOffs + 1) / 64 * 64 + this.sectorArea[arrayOffs]));
         this.main.rom.write(tileOffs + 2, (short)(this.main.rom.get(tileOffs + 2) / 64 * 64 + this.sectorTileset1[arrayOffs]));
         this.main.rom.write(tileOffs + 3, (short)(this.main.rom.get(tileOffs + 3) / 64 * 64 + this.sectorTileset2[arrayOffs]));
       } 
       curBank++;
       
       b++; }
     
     int[] banksMap = { 0x4010, 0x8010, 0xC010, 0x10010, 0x14010, 0x18010, 0x1C010 };
     curBank = 0; int[] arrayOfInt2; i = 0;
     for (int j = (arrayOfInt2 = banksMap).length; i < j; ) { int offset = arrayOfInt2[i];
       for (int k = 0; k < 8192; k++) {
         int tileOffs = offset + k;
         int arrayOffs = curBank * 8192 + k;
         int curByte = this.mapTiles[arrayOffs];
         
         boolean curTileset = this.mapTileset[arrayOffs];
         boolean curEvent = this.mapEvent[arrayOffs];
 
         
         if (curTileset)
           curByte += 64; 
         if (curEvent) {
           curByte += 128;
         }
         
         this.main.rom.write(tileOffs, (short)curByte);
       } 
       curBank++;
       i++; }
     
     this.main.rom.saveMap();
   }
   
   public int mapTilesGet(int x, int y) {
     return this.mapTiles[y * 256 + x];
   }
   
   public boolean mapTilesetGet(int x, int y) {
     return this.mapTileset[y * 256 + x];
   }
   
   public boolean mapEventGet(int x, int y) {
     return this.mapEvent[y * 256 + x];
   }
   
   public int sectorPaletteGet(int x, int y) {
     return this.sectorPalette[y * 64 + x];
   }
   
   public int sectorAreaGet(int x, int y) {
     return this.sectorArea[y * 64 + x];
   }
   
   public int sectorTileset1Get(int x, int y) {
     return this.sectorTileset1[y * 64 + x];
   }
   
   public int sectorTileset2Get(int x, int y) {
     return this.sectorTileset2[y * 64 + x];
   }
 }


