 package bbhack;
 
 import java.io.File;
 import java.io.IOException;
 import java.io.RandomAccessFile;
 import java.util.Arrays;
 
 public class RomFileIO
 {
   byte[] data;
   //TODO: make it to where you can load, save and save as.
   File rompath;
   
   public RomFileIO(File rompathGiven) {
     load(rompathGiven);
   }
   public RomFileIO() {}
 
   
   public void load(File rompathGiven) {
     rompath = rompathGiven;
     try {
       RandomAccessFile rom = new RandomAccessFile(rompath, "r");
       data = new byte[(int) rom.length()];
       rom.readFully(data);
     } catch (IOException e) {
       e.printStackTrace();
     } 
   }
   
   public byte get(int offset) {
     if (data.length >= offset)
       return data[offset];
     return 0;
   }
   
   public byte[] get(int offset, int length) {
     byte[] bytes = new byte[length];
     for (int i = 0; i < length; i++){
        bytes[i] = get(offset+i);
     } 
     return bytes;
   }
   
   //deprecate these when we know they arent needed
   //if you find an error related to the conversion of
   //the short rom to byte rom, come here
   public short getOld(int offset) {
     if (data.length >= offset)
       return (short)(data[offset] & 0xFF); 
     return 0;
   }
   
   public short[] getOld(int offset, int length) {
     short[] bytes = new short[length];
     for (int i = 0; i < length; i++) {
        bytes[i] = getOld(offset+i);
     }
     return bytes;
   }
   
   //shouldnt this be called set to match with get?? get isnt called read, so..
   public void write(int offset, byte b) {
     data[offset] = b;
   }
 
   public void saveMap() {
     try {
       RandomAccessFile rom = new RandomAccessFile(rompath, "rw");
       
       int START = 0x2010;
       int END = 0x20010;
       
       rom.seek(START);
       rom.write(Arrays.copyOfRange(data, START, END));
       rom.close();
       
     } catch (IOException e) {
       e.printStackTrace();
     } 
   }
 }


