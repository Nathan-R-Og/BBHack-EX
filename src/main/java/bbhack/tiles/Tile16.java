 package bbhack.tiles;
 
 
 public class Tile16
 {
   private Tile8[] tiles;
   private int[] values;
   public int tileNum;
   
   public Tile16(Tile8[] tilesGiven, int tileNumGiven) {
     this.tiles = tilesGiven;
     this.values = new int[256];
     this.tileNum = tileNumGiven;
     
     for (int i = 0; i < 256; i++) {
       this.values[i] = this.tiles[i / 128 * 2 + i % 16 / 8].getValue(i % 8, i / 16 % 8);
     }
   }
   
   public Tile16(Tile8 t1, Tile8 t2, Tile8 t3, Tile8 t4, int tileNumGiven) {
     this.tiles = new Tile8[] { t1, t2, t3, t4 };
     this.values = new int[256];
     this.tileNum = tileNumGiven;
     
     for (int i = 0; i < 256; i++) {
       this.values[i] = this.tiles[i / 128 * 2 + i % 16 / 8].getValue(i % 8, i / 16 % 8);
     }
   }
   
   public int[] getValues() {
     return this.values;
   }
   
   public int getValue(int i) {
     return this.values[i];
   }
   
   public int getValue(int x, int y) {
     int i = y * 16 + x;
     return this.values[i];
   }
   
   public Tile8[] getTiles() {
     return this.tiles;
   }
   
   public Tile8 getTile(int i) {
     return this.tiles[i];
   }
   
   public Tile8 getTile(int x, int y) {
     int i = y * 2 + x;
     return this.tiles[i];
   }
 }


