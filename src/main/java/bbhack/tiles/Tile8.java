 package bbhack.tiles;
 
 public class Tile8
 {
   private int[] values;
   
   public Tile8(int[] valuesGiven) {
     this.values = valuesGiven;
   }
   
   public Tile8() {
     this.values = new int[64];
   }
   
   public int[] getValues() {
     return this.values;
   }
   
   public int getValue(int i) {
     return this.values[i];
   }
   
   public int getValue(int x, int y) {
     int i = y * 8 + x;
     return this.values[i];
   }
   
   public void setValue(int i, int value) {
     this.values[i] = value;
   }
 }


