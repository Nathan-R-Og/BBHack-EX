 package bbhack.CE;
 
 import java.awt.Color;
 import java.awt.Graphics;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.image.BufferedImage;
 import java.awt.image.WritableRaster;
 import javax.swing.JPanel;
 import bbhack.MainMenu;
 import bbhack.rom.ROMPalettes;
 import bbhack.tiles.Tile64;
 
 
 
 
 
 public class PanelChunkSelectCE
   extends JPanel
 {
   private static final long serialVersionUID = 1L;
   private MainMenu main;
   private ChunkEditor CE;
   int viewY;
   int viewWidth;
   int viewHeight;
   int chunkSelected;
   
   public PanelChunkSelectCE(MainMenu instance, ChunkEditor CEInstance) {
     this.main = instance;
     this.CE = CEInstance;
     
     this.viewY = 0;
     this.viewWidth = 2;
     this.viewHeight = 8;
     
     this.chunkSelected = 0;
 
     
     addMouseListener(
         new MouseListener() {
           public void mousePressed(MouseEvent event) {
             if (event.getButton() == 1) {
               if (PanelChunkSelectCE.this.CE.useAlternateTileset) {
                 PanelChunkSelectCE.this.CE.useAlternateTileset = false;
                 PanelChunkSelectCE.this.CE.panelChunkView.repaint();
                 PanelChunkSelectCE.this.CE.panelTileSelect.repaint();
               } 
               
               int newValue = (event.getY() / 64 + PanelChunkSelectCE.this.viewY) * PanelChunkSelectCE.this.viewWidth + event.getX() / 64;
               if (newValue >= 64)
                 return;  PanelChunkSelectCE.this.chunkSelected = newValue;
               
               PanelChunkSelectCE.this.CE.panelChunkView.repaint();
               PanelChunkSelectCE.this.repaint();
             } 
           }
 
           
           public void mouseEntered(MouseEvent event) {}
 
           
           public void mouseExited(MouseEvent event) {}
 
           
           public void mouseClicked(MouseEvent event) {}
 
           
           public void mouseReleased(MouseEvent event) {}
         });
   }
 
   
   protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     
     int curX = 0;
     int curY = 0;
     
     if ((this.main.gfx.graphics64[this.CE.selectedTileset1 * 64 + this.chunkSelected]).altTileset.size() > 0 || this.CE.useAlternateTileset) {
       this.CE.panelPaletteSelect.altCheckbox.setEnabled(true);
       this.CE.panelPaletteSelect.altDropdown.setEnabled(true);
     } else {
       this.CE.panelPaletteSelect.altCheckbox.setEnabled(false);
       this.CE.panelPaletteSelect.altDropdown.setEnabled(false);
     } 
     
     for (int i = 0; i < this.viewWidth * this.viewHeight; i++) {
       int[] pixels = new int[12288];
       
       int tileNum = this.viewY * this.viewWidth + i;
       
       Tile64 curTile = this.main.gfx.graphics64[this.CE.selectedTileset1 * 64 + tileNum].getCopy();
       
       for (int j = 0; j < pixels.length; j += 3) {
         if (tileNum < 64) {
           if (curTile.altTileset.contains(Integer.valueOf(j / 3 / 1024 * 4 + j / 3 % 64 / 16))) {
             pixels[j] = 255;
             pixels[j + 1] = 0;
             pixels[j + 2] = 0;
           } else {
             int paletteNum = this.CE.selectedPalette * 4 + curTile.getPalette(j / 3 % 64 / 16, j / 3 / 1024);
             int colorNum = curTile.getValue(j / 3);
             
             pixels[j] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3];
             pixels[j + 1] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3 + 1];
             pixels[j + 2] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3 + 2];
           } 
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
 
       
       g.setColor(Color.GRAY);
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


