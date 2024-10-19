 package bbhack;
 
 import java.io.File;
 import java.io.FileNotFoundException;
 import java.io.IOException;
 import java.io.RandomAccessFile;
 
 public class RomFileIO
 {
   static final int ROM_SIZE = 524304;
   short[] data;
   File rompath;
   
   public RomFileIO(File rompathGiven) {
     this.rompath = rompathGiven;
     this.data = new short[524304];
     
     try {
       RandomAccessFile rom = loadRead(this.rompath);
       
       for (int i = 0; i < 524304; i++) {
         this.data[i] = rom.readByte();
       }
       
       rom.close();
     } catch (IOException e) {
       e.printStackTrace();
     } 
   }
 
   
   public RomFileIO() {}
 
   
   public void load(File rompathGiven) {
     this.rompath = rompathGiven;
     this.data = new short[524304];
     
     try {
       RandomAccessFile rom = loadRead(this.rompath);
       
       for (int i = 0; i < 524304; i++) {
         this.data[i] = rom.readByte();
       }
       
       rom.close();
     } catch (IOException e) {
       e.printStackTrace();
     } 
   }
   
   public void save() {
     try {
       RandomAccessFile rom = loadWrite(this.rompath);
       
       for (int i = 0; i < 524304; i++) {
         rom.writeByte(this.data[i]);
       }
       
       rom.close();
     } catch (IOException e) {
       e.printStackTrace();
     } 
   }
   
   public short get(int offset) {
     if (this.data.length >= offset)
       return (short)(this.data[offset] & 0xFF); 
     return 0;
   }
   
   public short[] get(int offset, int length) {
     if (this.data.length >= offset) {
       short[] bytes = new short[length];
       for (int i = 0; i < length; i++) {
         bytes[i] = this.data[offset + i];
       }
       return bytes;
     } 
     return new short[length];
   }
   
   public void write(int offset, short b) {
     this.data[offset] = b;
   }
 
   
   private RandomAccessFile loadRead(File rompath) throws FileNotFoundException, IOException {
     RandomAccessFile rom = new RandomAccessFile(rompath, "r");
     return rom;
   }
 
   
   private RandomAccessFile loadWrite(File rompath) throws FileNotFoundException, IOException {
     RandomAccessFile rom = new RandomAccessFile(rompath, "rw");
     return rom;
   }
   
   public void saveMap() {
     try {
       RandomAccessFile rom = loadWrite(this.rompath);
       
       int START = 8208;
       int END = 131088;
       
       rom.seek(8208L);
       for (int i = 8208; i < 131088; i++) {
         rom.writeByte(this.data[i]);
       }
       
       rom.close();
     } catch (IOException e) {
       e.printStackTrace();
     } 
   }
 }


