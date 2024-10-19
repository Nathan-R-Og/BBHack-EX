 package bbhack.tiles;
 
 import java.util.ArrayList;
 
 public class Tile64
   implements Cloneable
 {
   private Tile16[] tiles;
   private int[] values;
   private int[] palettes;
   public ArrayList<Integer> altTileset = new ArrayList<Integer>();
   public int[] tileNums;
   
   public Tile64(Tile16[] tilesGiven, int[] palettesGiven, ArrayList<Integer> altTilesetGiven, int[] tileNumsGiven) {
     this.tiles = tilesGiven;
     this.values = new int[0x1000];
     this.palettes = palettesGiven;
     this.altTileset = altTilesetGiven;
     this.tileNums = tileNumsGiven;
     
     for (int i = 0; i < 0x1000; i++) {
       this.values[i] = this.tiles[i / 0x400 * 4 + i % 0x40 / 0x10].getValue(i % 0x10, i / 0x40 % 0x10);
     }
   }
   
   public int[] getValues() {
     return this.values;
   }
   
   public int getValue(int i) {
     return this.values[i];
   }
   
   public int getValue(int x, int y) {
     int i = y * 64 + x;
     return this.values[i];
   }
   
   public Tile16[] getTiles() {
     return this.tiles;
   }
   
   public Tile16 getTile(int i) {
     return this.tiles[i];
   }
   
   public Tile16 getTile(int x, int y) {
     int i = y * 4 + x;
     return this.tiles[i];
   }
   
   public int[] getPalettes() {
     return this.palettes;
   }
   
   public int getPalette(int i) {
     return this.palettes[i];
   }
   
   public int getPalette(int x, int y) {
     int i = y * 4 + x;
     return this.palettes[i];
   }
   
   public void setTile(int index, Tile16 tile) {
     this.tiles[index] = tile;
     this.tileNums[index] = tile.tileNum;
     
     for (int i = 0; i < 4096; i++) {
       this.values[i] = this.tiles[i / 1024 * 4 + i % 64 / 16].getValue(i % 16, i / 64 % 16);
     }
   }
   
   public void setPalette(int index, int value) {
     this.palettes[index] = value;
   }
 
   
   public Tile64 getCopy() {
     return new Tile64((Tile16[])this.tiles.clone(), (int[])this.palettes.clone(), (ArrayList<Integer>)this.altTileset.clone(), (int[])this.tileNums.clone());
   }
 }


