 package bbhack.ME;
import bbhack.Info;
 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.image.BufferedImage;
 import java.awt.image.WritableRaster;
 import java.util.Iterator;
 import javax.swing.JPanel;
 import javax.swing.Timer;
 import bbhack.MainMenu;
 import bbhack.rom.ROMPalettes;
 import bbhack.rom.ROMSpriteDefs;
import bbhack.types.SpriteDef;
import bbhack.types.Sprite;
 import bbhack.types.Tile64;
 import bbhack.types.EBObjects.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
 
 import java.awt.AlphaComposite;
 
 public class PanelMap extends JPanel {
   private MainMenu main;
   private PanelChunkSelectME panelChunkSelect;
   private static final long serialVersionUID = 1L;
   private BufferedImage[][] mapGraphics;
   private List<BufferedImage> objectGraphics;
   int viewX;
   int viewY;
   int viewWidth;
   int viewHeight;
   public int scrollHLast;
   public int scrollVLast;
   public float chunkPreviewAlpha;
   public Timer chunkPreviewTimer;
   boolean viewGridChunk;
   boolean viewGridSector;
   
   public PanelMap(MainMenu instance) {
     main = instance;
     
     setLayout(new BorderLayout());
 
     
     mapGraphics = new BufferedImage[48][48];
     
     viewX = 0;
     viewY = 0;
     viewWidth = 32;
     viewHeight = 32;
     scrollHLast = 0;
     scrollVLast = 0;
     
     chunkPreviewAlpha = -0.1F;
     chunkPreviewTimer = new Timer(40, (ActionEvent event) -> {
         repaint();
         if (chunkPreviewAlpha < 0.0F) {
             chunkPreviewTimer.stop();
         }
     });
     
     viewGridChunk = true;
     viewGridSector = false;
   }
   
   public void setPanelChunkSelect(PanelChunkSelectME panel) {
     panelChunkSelect = panel;
   }
   
   public void scroll(int scrollH, int scrollV) {
     if (scrollH != scrollHLast || scrollV != scrollVLast) {
       
       if (scrollH > scrollHLast) {
         int diff = scrollH - scrollHLast;
         for (int x = 0; x < 48; x++) {
           for (int y = 0; y < 48; y++) {
             int newIndex = x - diff;
             if (newIndex >= 0)
             { if (x >= viewWidth || y >= viewHeight) mapGraphics[x][y] = null;
               
               mapGraphics[newIndex][y] = mapGraphics[x][y]; } 
           } 
         } 
       }  if (scrollH < scrollHLast) {
         int diff = scrollHLast - scrollH; int x;
         for (x = diff; x >= 0; x--) {
           for (int y = viewHeight; y >= 0; y--) {
             if (x < 48 && y < 48)
               mapGraphics[x][y] = null; 
           } 
         } 
         for (x = 47; x >= 0; x--) {
           for (int y = 47; y >= 0; y--) {
             int newIndex = x + diff;
             if (newIndex < 48)
             { if (x >= viewWidth || y >= viewHeight) mapGraphics[x][y] = null;
               
               mapGraphics[newIndex][y] = mapGraphics[x][y]; } 
           } 
         } 
       }  if (scrollV > scrollVLast) {
         int diff = scrollV - scrollVLast;
         for (int x = 0; x < 48; x++) {
           for (int y = 0; y < 48; y++) {
             int newIndex = y - diff;
             if (newIndex >= 0)
             { if (x >= viewWidth || y >= viewHeight) mapGraphics[x][y] = null;
               
               mapGraphics[x][newIndex] = mapGraphics[x][y]; } 
           } 
         } 
       }  if (scrollV < scrollVLast) {
         int diff = scrollVLast - scrollV; int x;
         for (x = viewWidth; x >= 0; x--) {
           for (int y = diff; y >= 0; y--) {
             if (x < 48 && y < 48)
               mapGraphics[x][y] = null; 
           } 
         } 
         for (x = 47; x >= 0; x--) {
           for (int y = 47; y >= 0; y--) {
             int newIndex = y + diff;
             if (newIndex < 48) {
               if (x >= viewWidth || y >= viewHeight) mapGraphics[x][y] = null;
               
               mapGraphics[x][newIndex] = mapGraphics[x][y];
             } 
           } 
         } 
       } 
       
       if (scrollH > 256 - viewWidth) scrollH = 256 - viewWidth; 
       if (scrollV > 224 - viewHeight) scrollV = 224 - viewHeight;
       
       scrollHLast = scrollH;
       scrollVLast = scrollV;
 
       
       viewX = scrollH;
       viewY = scrollV;
       
       repaint();
     } 
   }
   
   public void clearGraphicsCache() {
     for (int i = 0; i < 48; i++) {
       for (int j = 0; j < 48; j++) {
         mapGraphics[i][j] = null;
       }
     } 
   }
   
   public void refreshChunk(int x, int y) {
     mapGraphics[x][y] = null;
   }
 
   
   protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     
     BufferedImage[][] tempGraphics = new BufferedImage[48][48];
     
     
     viewWidth = getWidth() / 64 + 1;
     viewHeight = getHeight() / 64 + 1;
     int curX = 0;
     int curY = 0;
     for (int i = 0; i < viewWidth * viewHeight; i++) {
       BufferedImage tile;
       
       int mapX = viewX + curX;
       int mapY = viewY + curY;
       int sectorX = (viewX + curX) / 4;
       int sectorY = (viewY + curY) / 4;
       
       if (mapGraphics[curX][curY] == null) {
         Tile64 curTile;
         int[] pixels = new int[0x3000];
         boolean secondTileset = main.map.mapTilesetGet(mapX, mapY);
         if (!secondTileset) {
           curTile = main.gfx.graphics64[main.map.sectorTileset1Get(sectorX, sectorY) * 0x40 + main.map.mapTilesGet(mapX, mapY)].getCopy();
           for (Iterator<Integer> iterator = curTile.altTileset.iterator(); iterator.hasNext(); ) {
             int index = ((Integer)iterator.next()).intValue();
             curTile.setTile(index, main.gfx.graphics16[main.map.sectorTileset2Get(sectorX, sectorY) * 0x80 + curTile.tileNums[index]]);
           }
         } else {
           curTile = main.gfx.graphics64[main.map.sectorTileset2Get(sectorX, sectorY) * 0x40 + main.map.mapTilesGet(mapX, mapY)].getCopy();
           for (Iterator<Integer> iterator = curTile.altTileset.iterator(); iterator.hasNext(); ) {
             int index = ((Integer)iterator.next()).intValue();
             curTile.setTile(index, main.gfx.graphics16[main.map.sectorTileset1Get(sectorX, sectorY) * 0x80 + curTile.tileNums[index]]);
           }
         } 
         
         for (int j = 0; j < pixels.length; j += 3) {
           int paletteNum = main.map.sectorPaletteGet(sectorX, sectorY) * 4 + curTile.getPalette(j / 3 % 0x40 / 0x10, j / 3 / 0x400);
           int colorNum = curTile.getValue(j / 3);
           
           pixels[j] = ROMPalettes.colors[main.palettes.palettes[paletteNum][colorNum] * 3];
           pixels[j + 1] = ROMPalettes.colors[main.palettes.palettes[paletteNum][colorNum] * 3 + 1];
           pixels[j + 2] = ROMPalettes.colors[main.palettes.palettes[paletteNum][colorNum] * 3 + 2];
         } 
 
         
         tile = new BufferedImage(64, 64, 1);
         WritableRaster raster = tile.getRaster();
         raster.setPixels(0, 0, 64, 64, pixels);
       } else {
         
         tile = mapGraphics[curX][curY];
       } 
       
       g.drawImage(tile, curX * 64, curY * 64, null);
       tempGraphics[curX][curY] = tile;
 
       
       //ui stuff
       g.setColor(Color.DARK_GRAY);
       if (viewGridChunk) {
         if ((mapX + 1) % 4 != 0 || !viewGridSector)
           g.drawLine(curX * 64 + 63, curY * 64, curX * 64 + 63, curY * 64 + 63); 
         if ((mapY + 1) % 4 != 0 || !viewGridSector)
           g.drawLine(curX * 64, curY * 64 + 63, curX * 64 + 63, curY * 64 + 63); 
       } 
       if (viewGridSector) {
         g.setColor(Color.RED);
         if ((mapX + 1) % 4 == 0)
           g.drawLine(curX * 64 + 63, curY * 64, curX * 64 + 63, curY * 64 + 63); 
         if ((mapY + 1) % 4 == 0) {
           g.drawLine(curX * 64, curY * 64 + 63, curX * 64 + 63, curY * 64 + 63);
         }
       } 
 
       if (chunkPreviewAlpha >= 0.0F) {
         int selected = panelChunkSelect.chunkSelected;
         if (selected != -1) {
           if (selected < 64) {
             if (!main.map.mapTilesetGet(mapX, mapY) && 
               selected == main.map.mapTilesGet(mapX, mapY)) {
               g.setColor(new Color(1.0F, 0.0F, 0.0F, chunkPreviewAlpha));
               g.fillRect(curX * 64, curY * 64, 64, 64);
             }
           
           }
           else if (main.map.mapTilesetGet(mapX, mapY) && 
             selected - 64 == main.map.mapTilesGet(mapX, mapY)) {
             g.setColor(new Color(1.0F, 0.0F, 0.0F, chunkPreviewAlpha));
             g.fillRect(curX * 64, curY * 64, 64, 64);
           } 
         }
       } 
 
       
       curX++;
       if (curX + 1 > viewWidth) {
         curX = 0;
         curY++;
       } 
     } 
     
     if (chunkPreviewAlpha >= 0.0F) {
       chunkPreviewAlpha = (float)(chunkPreviewAlpha - 0.1D);
     }
     mapGraphics = tempGraphics;
     
     //test
     if(false){
        for(int i = 0; i < main.sprites.Definitions.length; i++){
            int calcId = main.sprites.Definitions[i].offset;
            if(calcId >= 0x80) calcId -= 0x80;
            drawSpriteDef(g, i, calcId, 0, 0, i*2, 2, 2);
        }
     }
     objectGraphics = new ArrayList<>();
     if(true){
        for (List<List<EBObject>> bank : main.objects.Banks) {
            for (List<EBObject> area_bank : bank) {
                for (EBObject object : area_bank){
                    int fixObjX = object.x*2;
                    int fixObjY = (object.y-0x81)*2;
                    int offsetX = (fixObjX * 8);
                    int offsetY = (fixObjY * 8);
                    int viewXFix = viewX * 64;
                    int viewYFix = viewY * 64;
                    int viewWidthFix = viewWidth * 64;
                    int viewHeightFix = viewHeight * 64;
                    if(offsetX < viewXFix || offsetX > viewXFix+viewWidthFix){continue;}
                    if(offsetY < viewYFix || offsetY > viewYFix+viewHeightFix){continue;}
                    offsetX -= viewXFix;
                    offsetY -= viewYFix-8;
                    
                    if(object instanceof EBNPC){
                        SpriteDef def = ((EBNPC) object).mysprite;
                        if(def != null){
                            int calcId = def.offset;
                            int sectorX = object.x / 16;
                            int sectorY = (object.y - 0x80) / 16;
                            int myarea = main.map.sectorAreaGet(sectorX, sectorY);
                            if(calcId >= 0x80){
                                calcId -= 0x80;
                            }
                            else {
                                myarea=0;
                            }
                            drawSpriteDef(g, def, calcId, myarea, fixObjX, fixObjY, 2, 2);
                        }
                    }else if(object instanceof EBDoor){
                        try {
                            Image image = ImageIO.read(Info.class.getResource("/tiles/door.png"));
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.75f));
                            g.drawImage(image, offsetX, offsetY, 16, 16, null);
                            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                        } catch (IOException ex) {
                            Logger.getLogger(PanelMap.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else if(object instanceof EBFlagSet){
                        try {
                            Image image = ImageIO.read(Info.class.getResource("/tiles/setflag.png"));
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.75f));
                            g.drawImage(image, offsetX, offsetY, 16, 16, null);
                            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                        } catch (IOException ex) {
                            Logger.getLogger(PanelMap.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else{
                        try {
                            Image image = ImageIO.read(Info.class.getResource("/tiles/unk.png"));
                            g.drawImage(image, offsetX, offsetY, 16, 16, null);
                        } catch (IOException ex) {
                            Logger.getLogger(PanelMap.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        
     }
     //int fucker = 0;
     //drawSpriteDef(g, fucker, main.sprites.Definitions[fucker].offset, 0, 0, 0, 2, 2);
   }
   
   //width + height is kinda tacky. pls find some other way to calc sprites
   public void drawSpriteDef(Graphics g, int i, int offset, int area, int x, int y, int width, int height){
       if(main.sprites.Definitions[i].spriteStart == -1) return;
       for (int z = 0; z < width * height; z++){
           Sprite hi = main.sprites.Sprites[main.sprites.Definitions[i].spriteStart+z];
           drawCharTile(g, hi.index+offset, area, x-width, (y-height)-height/2, hi.x, hi.y, hi.flipX == 1, hi.flipY == 1);
       }
   }
   public void drawSpriteDef(Graphics g, SpriteDef Definition, int offset, int area, int x, int y, int width, int height){
       if(Definition.spriteStart == -1) return;
       for (int z = 0; z < width * height; z++){
           Sprite hi = main.sprites.Sprites[Definition.spriteStart+z];
           drawCharTile(g, hi.index+offset, area, x-width, (y-height)-height/2, hi.x, hi.y, hi.flipX == 1, hi.flipY == 1);
       }
   }
   
   //dummy palette
   int[] fakeColors = {
     0,0,0,0,
     0,0,0,255,
     181,50,32,255,
     247,217,166,255
    };
   //function to draw  a character tile from the spritedefs
   public void drawCharTile(Graphics g, int id, int area, int x, int y, int subX, int subY, boolean flipX, boolean flipY){

     //draw char stuff
     
     //use the area lookup table to get the general chr id
    //if (area == 0){area = 1;}
    byte bank = main.gfx.areaTable[area];
    if(area == 0 || bank == 0){
        bank = 0x60;
    }
    //offset from start of chr
    int addr = (Byte.toUnsignedInt(bank) * 0x400);
    //ofset from start of the character chr
    int chroff = (addr - 0x18000)/0x10;
    int calcId = id + chroff;
    if (calcId < 0){
        calcId = calcId;
    }
     
     //x*y*rgb
    int[] pixels = new int[8*8*4];
     for (int j = 0; j < pixels.length; j+=4) {
      //int paletteNum = 7;
      int colorNum = main.gfx.characters[calcId].getValue(j/4);


      //pixels[j] = ROMPalettes.colors[main.palettes.palettes[paletteNum][colorNum] * 3];
      //pixels[j + 1] = ROMPalettes.colors[main.palettes.palettes[paletteNum][colorNum] * 3 + 1];
      //pixels[j + 2] = ROMPalettes.colors[main.palettes.palettes[paletteNum][colorNum] * 3 + 2];
      pixels[j] = fakeColors[colorNum * 4]; //r
      pixels[j + 1] = fakeColors[colorNum * 4 + 1]; //g
      pixels[j + 2] = fakeColors[colorNum * 4 + 2]; //b
      pixels[j + 3] = fakeColors[colorNum * 4 + 3]; //a
    } 
     
    BufferedImage charTile = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
    WritableRaster raster = charTile.getRaster();
    raster.setPixels(0, 0, 8, 8, pixels);

   int offsetX = (x * 8) - (viewX*64);
   int offsetY = (y * 8) - (viewY*64);
   int useWidth = 8;
   int useHeight = 8;
   if(flipX) { 
       offsetX += useWidth;
       useWidth *= -1;
   }
   if(flipY) { 
       offsetY += useHeight;
       useHeight *= -1;
   }
       
   g.drawImage(charTile, offsetX+subX, offsetY+subY, useWidth, useHeight, null);
   objectGraphics.add(charTile);
   }
 }


