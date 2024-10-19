 package bbhack.ME;
 
 import java.awt.Color;
 import java.awt.Graphics;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import java.awt.image.BufferedImage;
 import java.awt.image.WritableRaster;
 import javax.swing.JPanel;
 import bbhack.MainMenu;
 import bbhack.rom.ROMPalettes;
 import bbhack.tiles.Tile64;
 
 
 
 
 
 
 
 public class PanelChunkSelectME
   extends JPanel
 {
   private static final long serialVersionUID = 1L;
   private MainMenu main;
   private MapEditor ME;
   private PanelMap panelMap;
   int viewY;
   int viewWidth;
   int viewHeight;
   int selectX;
   int selectY;
   int chunkSelected;
   private boolean mouse1Down;
   
   public PanelChunkSelectME(MainMenu instance, MapEditor MEInstance, PanelMap panelMapInstance) {
     this.main = instance;
     this.ME = MEInstance;
     this.panelMap = panelMapInstance;
     
     this.selectX = 0;
     this.selectY = 0;
     
     this.viewY = 0;
     this.viewWidth = 4;
     this.viewHeight = 16;
     
     this.chunkSelected = -1;
     
     this.mouse1Down = false;
     
     this.panelMap.addMouseListener(
         new MouseListener() {
           public void mousePressed(MouseEvent event) {
             if (event.getButton() == 3) {
               PanelChunkSelectME.this.selectX = event.getX() / 64 + PanelChunkSelectME.this.panelMap.viewX;
               PanelChunkSelectME.this.selectY = event.getY() / 64 + PanelChunkSelectME.this.panelMap.viewY;
               
               int selected = PanelChunkSelectME.this.main.map.mapTilesGet(PanelChunkSelectME.this.selectX, PanelChunkSelectME.this.selectY);
               if (PanelChunkSelectME.this.main.map.mapTilesetGet(PanelChunkSelectME.this.selectX, PanelChunkSelectME.this.selectY)) {
                 selected += 64;
               }
               PanelChunkSelectME.this.chunkSelected = selected;
               if (selected / PanelChunkSelectME.this.viewWidth <= PanelChunkSelectME.this.viewY || selected / PanelChunkSelectME.this.viewWidth >= PanelChunkSelectME.this.viewY + PanelChunkSelectME.this.viewHeight) {
                 PanelChunkSelectME.this.viewY = selected / PanelChunkSelectME.this.viewWidth;
                 while (PanelChunkSelectME.this.viewY > 128 / PanelChunkSelectME.this.viewWidth - PanelChunkSelectME.this.viewHeight)
                   PanelChunkSelectME.this.viewY--; 
                 while (PanelChunkSelectME.this.viewY < 0) {
                   PanelChunkSelectME.this.viewY++;
                 }
                 PanelChunkSelectME.this.ME.internalChunkSelect.scroll.setValue(PanelChunkSelectME.this.viewY);
               } 
               
               PanelChunkSelectME.this.repaint();
             }  if (event.getButton() == 1) {
               if (!event.isShiftDown()) {
                 PanelChunkSelectME.this.mouse1Down = true;
                 
                 PanelChunkSelectME.this.selectX = event.getX() / 64 + PanelChunkSelectME.this.panelMap.viewX;
                 PanelChunkSelectME.this.selectY = event.getY() / 64 + PanelChunkSelectME.this.panelMap.viewY;
                 PanelChunkSelectME.this.repaint();
                 System.out.println("x: " + (PanelChunkSelectME.this.selectX * 4));
                 System.out.println("y: " + (PanelChunkSelectME.this.selectY * 4));
                 System.out.println();
                 
                 if (!event.isControlDown() && 
                   PanelChunkSelectME.this.chunkSelected != -1) {
                   int mapX = event.getX() / 64 + PanelChunkSelectME.this.panelMap.viewX;
                   int mapY = event.getY() / 64 + PanelChunkSelectME.this.panelMap.viewY;
                   PanelChunkSelectME.this.main.map.mapTiles[mapY * 256 + mapX] = PanelChunkSelectME.this.chunkSelected % 64;
                   
                   if (PanelChunkSelectME.this.chunkSelected < 64) { PanelChunkSelectME.this.main.map.mapTileset[mapY * 256 + mapX] = false; }
                   else { PanelChunkSelectME.this.main.map.mapTileset[mapY * 256 + mapX] = true; }
                   
                   PanelChunkSelectME.this.panelMap.refreshChunk(event.getX() / 64, event.getY() / 64);
                   PanelChunkSelectME.this.panelMap.repaint();
                 } 
               } else {
                 
                 int mapX = event.getX() / 64 + PanelChunkSelectME.this.panelMap.viewX;
                 int mapY = event.getY() / 64 + PanelChunkSelectME.this.panelMap.viewY;
                 int sectorX = mapX / 4;
                 int sectorY = mapY / 4;
                 PanelChunkSelectME.this.main.openCEFromMap(PanelChunkSelectME.this.main.map.mapTilesGet(mapX, mapY), PanelChunkSelectME.this.main.map.mapTilesetGet(mapX, mapY), 
                     PanelChunkSelectME.this.main.map.sectorPaletteGet(sectorX, sectorY), PanelChunkSelectME.this.main.map.sectorTileset1Get(sectorX, sectorY), PanelChunkSelectME.this.main.map.sectorTileset2Get(sectorX, sectorY));
               } 
             }
           }
 
           
           public void mouseEntered(MouseEvent event) {}
           
           public void mouseExited(MouseEvent event) {
             PanelChunkSelectME.this.mouse1Down = false;
           }
           
           public void mouseReleased(MouseEvent event) {
             if (event.getButton() == 1) PanelChunkSelectME.this.mouse1Down = false; 
           }
           
           public void mouseClicked(MouseEvent event) {}
         });
     this.panelMap.addMouseMotionListener(
         new MouseMotionListener() {
           public void mouseDragged(MouseEvent event) {
             if (PanelChunkSelectME.this.mouse1Down) {
               PanelChunkSelectME.this.selectX = event.getX() / 64 + PanelChunkSelectME.this.panelMap.viewX;
               PanelChunkSelectME.this.selectY = event.getY() / 64 + PanelChunkSelectME.this.panelMap.viewY;
               PanelChunkSelectME.this.repaint();
               
               if (!event.isControlDown() && 
                 PanelChunkSelectME.this.chunkSelected != -1) {
                 int mapX = event.getX() / 64 + PanelChunkSelectME.this.panelMap.viewX;
                 int mapY = event.getY() / 64 + PanelChunkSelectME.this.panelMap.viewY;
                 PanelChunkSelectME.this.main.map.mapTiles[mapY * 256 + mapX] = PanelChunkSelectME.this.chunkSelected % 64;
                 
                 if (PanelChunkSelectME.this.chunkSelected < 64) { PanelChunkSelectME.this.main.map.mapTileset[mapY * 256 + mapX] = false; }
                 else { PanelChunkSelectME.this.main.map.mapTileset[mapY * 256 + mapX] = true; }
                 
                 PanelChunkSelectME.this.panelMap.refreshChunk(event.getX() / 64, event.getY() / 64);
                 PanelChunkSelectME.this.panelMap.repaint();
               } 
             } 
           }
 
 
 
 
 
 
           
           public void mouseMoved(MouseEvent event) {}
         });
     addMouseListener(
         new MouseListener() {
           public void mousePressed(MouseEvent event) {
             if (event.getButton() == 1) {
               int newValue = (event.getY() / 64 + PanelChunkSelectME.this.viewY) * PanelChunkSelectME.this.viewWidth + event.getX() / 64;
               if (newValue >= 128)
                 return;  if (PanelChunkSelectME.this.chunkSelected != newValue) {
                 PanelChunkSelectME.this.chunkSelected = newValue;
                 PanelChunkSelectME.this.panelMap.chunkPreviewAlpha = 1.0F;
                 PanelChunkSelectME.this.panelMap.chunkPreviewTimer.start();
               } else {
                 PanelChunkSelectME.this.chunkSelected = -1;
                 PanelChunkSelectME.this.panelMap.chunkPreviewTimer.stop();
                 PanelChunkSelectME.this.panelMap.repaint();
               } 
               PanelChunkSelectME.this.repaint();
             } 
           }
 
           
           public void mouseEntered(MouseEvent event) {}
 
           
           public void mouseExited(MouseEvent event) {}
 
           
           public void mouseClicked(MouseEvent event) {}
 
           
           public void mouseReleased(MouseEvent event) {}
         });
   }
   
   private int getSelectedTileset1() {
     return this.main.map.sectorTileset1Get(this.selectX / 4, this.selectY / 4);
   }
   
   private int getSelectedTileset2() {
     return this.main.map.sectorTileset2Get(this.selectX / 4, this.selectY / 4);
   }
   
   private int getSelectedPalette() {
     return this.main.map.sectorPaletteGet(this.selectX / 4, this.selectY / 4);
   }
 
   
   protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     
     int curX = 0;
     int curY = 0;
     
     for (int i = 0; i < this.viewWidth * this.viewHeight; i++) {
       Tile64 curTile; int[] pixels = new int[12288];
       
       int tileNum = this.viewY * this.viewWidth + i;
       
       int selectedTileset1 = getSelectedTileset1();
       int selectedTileset2 = getSelectedTileset2();
       int selectedPalette = getSelectedPalette();
 
       
       if (tileNum < 64) {
         curTile = this.main.gfx.graphics64[selectedTileset1 * 64 + tileNum];
       } else {
         curTile = this.main.gfx.graphics64[selectedTileset2 * 64 + tileNum % 64];
       } 
       
       for (int j = 0; j < pixels.length; j += 3) {
         if (tileNum < 128) {
           int paletteNum = selectedPalette * 4 + curTile.getPalette(j / 3 % 64 / 16, j / 3 / 1024);
           int colorNum = curTile.getValue(j / 3);
           
           pixels[j] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3];
           pixels[j + 1] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3 + 1];
           pixels[j + 2] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3 + 2];
         } else {
           pixels[j] = 0;
           pixels[j + 1] = 0;
           pixels[j + 2] = 0;
         } 
       } 
       BufferedImage tile = new BufferedImage(64, 64, 1);
       WritableRaster raster = tile.getRaster();
       raster.setPixels(0, 0, 64, 64, pixels);
       g.drawImage(tile, curX * 64, curY * 64, null);
 
       
       g.setColor(Color.DARK_GRAY);
       g.drawLine(curX * 64 + 63, curY * 64, curX * 64 + 63, curY * 64 + 63);
       g.drawLine(curX * 64, curY * 64 + 63, curX * 64 + 63, curY * 64 + 63);
 
       
       if (tileNum == this.chunkSelected) {
         float alpha = 0.5F;
         g.setColor(new Color(1.0F, 1.0F, 0.0F, alpha));
         g.fillRect(curX * 64, curY * 64, 64, 64);
       } 
       
       curX++;
       if (curX >= this.viewWidth) {
         curX = 0;
         curY++;
       } 
     } 
   }
 }


