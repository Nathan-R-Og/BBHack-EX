 package bbhack.ME;
 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Graphics;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.image.BufferedImage;
 import java.awt.image.WritableRaster;
 import java.util.Iterator;
 import javax.swing.JPanel;
 import javax.swing.Timer;
 import bbhack.MainMenu;
 import bbhack.rom.ROMPalettes;
 import bbhack.tiles.Tile64;
 
 public class PanelMap extends JPanel {
   private MainMenu main;
   private PanelChunkSelectME panelChunkSelect;
   private static final int[] sectorColors = new int[] { 
       227, 38, 54, 
       196, 98, 16, 
       255, 191, 
       255, 126, 
       153, 102, 204, 
       164, 198, 57, 
       102, 93, 30, 
       251, 206, 177, 
       255, 255, 
       253, 238, 
       127, 255, 
       255, 127, 
       150, 75, 
       102, 255, 
       255, 8, 
       184, 134, 11, 
       205, 91, 69, 
       23, 114, 69, 
       127, 
       199, 44, 72, 
       228, 155, 15, 
       168, 119, 
       173, 255, 47, 
       102, 56, 84, 
       185, 22, 
       244, 161, 
       252, 247, 94, 
       192, 54, 44, 
       113, 166, 210, 
       144, 238, 144, 
       166, 147, 
       105, 105, 105 };
   
   private static final long serialVersionUID = 1L;
   
   private BufferedImage[][] mapGraphics;
   
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
     this.main = instance;
     
     setLayout(new BorderLayout());
 
     
     this.mapGraphics = new BufferedImage[48][48];
     
     this.viewX = 0;
     this.viewY = 0;
     this.viewWidth = 32;
     this.viewHeight = 32;
     this.scrollHLast = 0;
     this.scrollVLast = 0;
     
     this.chunkPreviewAlpha = -0.1F;
     this.chunkPreviewTimer = new Timer(40, 
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             PanelMap.this.repaint();
             if (PanelMap.this.chunkPreviewAlpha < 0.0F) {
               PanelMap.this.chunkPreviewTimer.stop();
             }
           }
         });
     
     this.viewGridChunk = true;
     this.viewGridSector = false;
   }
   
   public void setPanelChunkSelect(PanelChunkSelectME panel) {
     this.panelChunkSelect = panel;
   }
   
   public void scroll(int scrollH, int scrollV) {
     if (scrollH != this.scrollHLast || scrollV != this.scrollVLast) {
       
       if (scrollH > this.scrollHLast) {
         int diff = scrollH - this.scrollHLast;
         for (int x = 0; x < 48; x++) {
           for (int y = 0; y < 48; y++) {
             int newIndex = x - diff;
             if (newIndex >= 0)
             { if (x >= this.viewWidth || y >= this.viewHeight) this.mapGraphics[x][y] = null;
               
               this.mapGraphics[newIndex][y] = this.mapGraphics[x][y]; } 
           } 
         } 
       }  if (scrollH < this.scrollHLast) {
         int diff = this.scrollHLast - scrollH; int x;
         for (x = diff; x >= 0; x--) {
           for (int y = this.viewHeight; y >= 0; y--) {
             if (x < 48 && y < 48)
               this.mapGraphics[x][y] = null; 
           } 
         } 
         for (x = 47; x >= 0; x--) {
           for (int y = 47; y >= 0; y--) {
             int newIndex = x + diff;
             if (newIndex < 48)
             { if (x >= this.viewWidth || y >= this.viewHeight) this.mapGraphics[x][y] = null;
               
               this.mapGraphics[newIndex][y] = this.mapGraphics[x][y]; } 
           } 
         } 
       }  if (scrollV > this.scrollVLast) {
         int diff = scrollV - this.scrollVLast;
         for (int x = 0; x < 48; x++) {
           for (int y = 0; y < 48; y++) {
             int newIndex = y - diff;
             if (newIndex >= 0)
             { if (x >= this.viewWidth || y >= this.viewHeight) this.mapGraphics[x][y] = null;
               
               this.mapGraphics[x][newIndex] = this.mapGraphics[x][y]; } 
           } 
         } 
       }  if (scrollV < this.scrollVLast) {
         int diff = this.scrollVLast - scrollV; int x;
         for (x = this.viewWidth; x >= 0; x--) {
           for (int y = diff; y >= 0; y--) {
             if (x < 48 && y < 48)
               this.mapGraphics[x][y] = null; 
           } 
         } 
         for (x = 47; x >= 0; x--) {
           for (int y = 47; y >= 0; y--) {
             int newIndex = y + diff;
             if (newIndex < 48) {
               if (x >= this.viewWidth || y >= this.viewHeight) this.mapGraphics[x][y] = null;
               
               this.mapGraphics[x][newIndex] = this.mapGraphics[x][y];
             } 
           } 
         } 
       } 
       
       if (scrollH > 256 - this.viewWidth) scrollH = 256 - this.viewWidth; 
       if (scrollV > 224 - this.viewHeight) scrollV = 224 - this.viewHeight;
       
       this.scrollHLast = scrollH;
       this.scrollVLast = scrollV;
 
       
       this.viewX = scrollH;
       this.viewY = scrollV;
       
       repaint();
     } 
   }
   
   public void clearGraphicsCache() {
     for (int i = 0; i < 48; i++) {
       for (int j = 0; j < 48; j++) {
         this.mapGraphics[i][j] = null;
       }
     } 
   }
   
   public void refreshChunk(int x, int y) {
     this.mapGraphics[x][y] = null;
   }
 
   
   protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     
     BufferedImage[][] tempGraphics = new BufferedImage[48][48];
     
     this.viewWidth = getWidth() / 64 + 1;
     this.viewHeight = getHeight() / 64 + 1;
     
     int curX = 0;
     int curY = 0;
 
     
     for (int i = 0; i < this.viewWidth * this.viewHeight; i++) {
       BufferedImage tile;
       
       int mapX = this.viewX + curX;
       int mapY = this.viewY + curY;
       int sectorX = (this.viewX + curX) / 4;
       int sectorY = (this.viewY + curY) / 4;
       
       if (this.mapGraphics[curX][curY] == null) {
         Tile64 curTile; int[] pixels = new int[12288];
         
         boolean secondTileset = this.main.map.mapTilesetGet(mapX, mapY);
 
         
         if (!secondTileset) {
           curTile = this.main.gfx.graphics64[this.main.map.sectorTileset1Get(sectorX, sectorY) * 64 + this.main.map.mapTilesGet(mapX, mapY)].getCopy();
           for (Iterator<Integer> iterator = curTile.altTileset.iterator(); iterator.hasNext(); ) { int index = ((Integer)iterator.next()).intValue();
             curTile.setTile(index, this.main.gfx.graphics16[this.main.map.sectorTileset2Get(sectorX, sectorY) * 128 + curTile.tileNums[index]]); }
         
         } else {
           curTile = this.main.gfx.graphics64[this.main.map.sectorTileset2Get(sectorX, sectorY) * 64 + this.main.map.mapTilesGet(mapX, mapY)].getCopy();
           for (Iterator<Integer> iterator = curTile.altTileset.iterator(); iterator.hasNext(); ) { int index = ((Integer)iterator.next()).intValue();
             curTile.setTile(index, this.main.gfx.graphics16[this.main.map.sectorTileset1Get(sectorX, sectorY) * 128 + curTile.tileNums[index]]); }
         
         } 
         
         for (int j = 0; j < pixels.length; j += 3) {
           int paletteNum = this.main.map.sectorPaletteGet(sectorX, sectorY) * 4 + curTile.getPalette(j / 3 % 64 / 16, j / 3 / 1024);
           int colorNum = curTile.getValue(j / 3);
           
           pixels[j] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3];
           pixels[j + 1] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3 + 1];
           pixels[j + 2] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3 + 2];
         } 
 
         
         tile = new BufferedImage(64, 64, 1);
         WritableRaster raster = tile.getRaster();
         raster.setPixels(0, 0, 64, 64, pixels);
       } else {
         
         tile = this.mapGraphics[curX][curY];
       } 
       
       g.drawImage(tile, curX * 64, curY * 64, null);
       tempGraphics[curX][curY] = tile;
 
       
       g.setColor(Color.DARK_GRAY);
       if (this.viewGridChunk) {
         if ((mapX + 1) % 4 != 0 || !this.viewGridSector)
           g.drawLine(curX * 64 + 63, curY * 64, curX * 64 + 63, curY * 64 + 63); 
         if ((mapY + 1) % 4 != 0 || !this.viewGridSector)
           g.drawLine(curX * 64, curY * 64 + 63, curX * 64 + 63, curY * 64 + 63); 
       } 
       if (this.viewGridSector) {
         g.setColor(Color.RED);
         if ((mapX + 1) % 4 == 0)
           g.drawLine(curX * 64 + 63, curY * 64, curX * 64 + 63, curY * 64 + 63); 
         if ((mapY + 1) % 4 == 0) {
           g.drawLine(curX * 64, curY * 64 + 63, curX * 64 + 63, curY * 64 + 63);
         }
       } 
 
       
       if (this.chunkPreviewAlpha >= 0.0F) {
         int selected = this.panelChunkSelect.chunkSelected;
         if (selected != -1) {
           if (selected < 64) {
             if (!this.main.map.mapTilesetGet(mapX, mapY) && 
               selected == this.main.map.mapTilesGet(mapX, mapY)) {
               g.setColor(new Color(1.0F, 0.0F, 0.0F, this.chunkPreviewAlpha));
               g.fillRect(curX * 64, curY * 64, 64, 64);
             }
           
           }
           else if (this.main.map.mapTilesetGet(mapX, mapY) && 
             selected - 64 == this.main.map.mapTilesGet(mapX, mapY)) {
             g.setColor(new Color(1.0F, 0.0F, 0.0F, this.chunkPreviewAlpha));
             g.fillRect(curX * 64, curY * 64, 64, 64);
           } 
         }
       } 
 
 
 
 
 
 
 
 
       
       curX++;
       if (curX + 1 > this.viewWidth) {
         curX = 0;
         curY++;
       } 
     } 
     
     if (this.chunkPreviewAlpha >= 0.0F) {
       this.chunkPreviewAlpha = (float)(this.chunkPreviewAlpha - 0.1D);
     }
     this.mapGraphics = tempGraphics;
   }
 }


