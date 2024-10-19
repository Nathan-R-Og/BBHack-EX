 package bbhack.CE;
 
 import java.awt.Color;
 import java.awt.Graphics;
 import java.awt.event.ItemEvent;
 import java.awt.event.ItemListener;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.image.BufferedImage;
 import java.awt.image.WritableRaster;
 import javax.swing.JPanel;
 import bbhack.MainMenu;
 import bbhack.rom.ROMPalettes;
 import bbhack.tiles.Tile16;
 
 
 
 
 
 public class PanelTileSelectCE
   extends JPanel
 {
   private static final long serialVersionUID = 1L;
   private MainMenu main;
   private ChunkEditor CE;
   int viewX;
   int viewWidth;
   int viewHeight;
   int tileSelected;
   int palette;
   
   public PanelTileSelectCE(MainMenu instance, ChunkEditor CEInstance) {
     this.main = instance;
     this.CE = CEInstance;
     
     setBackground(Color.BLACK);
     
     this.viewX = 0;
     this.viewWidth = 16;
     this.viewHeight = 16;
     
     this.tileSelected = 0;
     this.palette = 0;
 
     
     addMouseListener(
         new MouseListener() {
           public void mousePressed(MouseEvent event) {
             if (event.getButton() == 1) {
               int newValue = (event.getX() / 32 + PanelTileSelectCE.this.viewX) * PanelTileSelectCE.this.viewHeight + event.getY() / 32;
               if (newValue >= 128)
                 return;  PanelTileSelectCE.this.tileSelected = newValue;
               
               PanelTileSelectCE.this.repaint();
             } 
           }
 
 
           
           public void mouseEntered(MouseEvent event) {}
 
 
           
           public void mouseExited(MouseEvent event) {}
 
           
           public void mouseClicked(MouseEvent event) {}
 
           
           public void mouseReleased(MouseEvent event) {}
         });
     this.CE.panelPaletteSelect.palette.addItemListener(
         new ItemListener() {
           public void itemStateChanged(ItemEvent event) {
             PanelTileSelectCE.this.palette = PanelTileSelectCE.this.CE.panelPaletteSelect.palette.getSelectedIndex();
             PanelTileSelectCE.this.repaint();
           }
         });
 
 
     
     this.CE.panelPaletteSelect.altCheckbox.addItemListener(
         new ItemListener() {
           public void itemStateChanged(ItemEvent event) {
             PanelTileSelectCE.this.repaint();
           }
         });
     this.CE.panelPaletteSelect.altDropdown.addItemListener(
         new ItemListener() {
           public void itemStateChanged(ItemEvent event) {
             PanelTileSelectCE.this.repaint();
           }
         });
   }
 
 
   
   protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     
     int curX = 0;
     int curY = 0;
     
     this.viewWidth = getWidth() / 32;
     this.viewHeight = getHeight() / 32;
     
     for (int i = 0; i < this.viewWidth * this.viewHeight; i++) {
       int[] pixels = new int[768];
       
       int tileNum = this.viewX * this.viewHeight + i;
       if (tileNum < 128) {
         Tile16 curTile;
         if (this.CE.panelPaletteSelect.altCheckbox.isSelected() && this.CE.useAlternateTileset) {
           curTile = this.main.gfx.graphics16[this.CE.panelPaletteSelect.altDropdown.getSelectedIndex() * 128 + tileNum];
         } else {
           curTile = this.main.gfx.graphics16[this.CE.selectedTileset1 * 128 + tileNum];
         } 
         
         for (int j = 0; j < pixels.length; j += 3) {
           int paletteNum = this.CE.selectedPalette * 4 + this.palette;
           int colorNum = curTile.getValue(j / 3);
           
           pixels[j] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3];
           pixels[j + 1] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3 + 1];
           pixels[j + 2] = ROMPalettes.colors[this.main.palettes.palettes[paletteNum][colorNum] * 3 + 2];
         } 
         BufferedImage tile = new BufferedImage(16, 16, 1);
         WritableRaster raster = tile.getRaster();
         raster.setPixels(0, 0, 16, 16, pixels);
         g.drawImage(tile.getScaledInstance(32, 32, 8), curX * 32, curY * 32, null);
 
         
         if (tileNum < 128) {
           g.setColor(Color.GRAY);
           g.drawLine(curX * 32 + 31, curY * 32, curX * 32 + 31, curY * 32 + 31);
           g.drawLine(curX * 32, curY * 32 + 31, curX * 32 + 31, curY * 32 + 31);
         } 
 
         
         if (tileNum == this.tileSelected) {
           float alpha = 0.5F;
           g.setColor(new Color(1.0F, 1.0F, 0.0F, alpha));
           g.fillRect(curX * 32, curY * 32, 32, 32);
         } 
         
         curY++;
         if (curY >= this.viewHeight) {
           curY = 0;
           curX++;
         } 
       } 
     } 
   }
 }


