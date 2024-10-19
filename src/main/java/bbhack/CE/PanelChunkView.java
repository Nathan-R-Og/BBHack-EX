 package bbhack.CE;
 
 import java.awt.Color;
 import java.awt.Graphics;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import java.awt.image.BufferedImage;
 import java.awt.image.WritableRaster;
 import java.util.Iterator;
 import javax.swing.JPanel;
 import bbhack.MainMenu;
 import bbhack.rom.ROMPalettes;
 import bbhack.tiles.Tile64;
 
 
 public class PanelChunkView
   extends JPanel
 {
   private static final long serialVersionUID = 1L;
   private MainMenu main;
   private ChunkEditor CE;
   private boolean mouse1Down;
   
   public PanelChunkView(MainMenu instance, ChunkEditor CEInstance) {
     this.main = instance;
     this.CE = CEInstance;
     
     this.mouse1Down = false;
 
     
     addMouseListener(
         new MouseListener() {
           public void mousePressed(MouseEvent event) {
             if (event.getButton() == 3) {
               int index64 = PanelChunkView.this.CE.selectedTileset1 * 64 + PanelChunkView.this.CE.panelChunkSelect.chunkSelected;
               int i = event.getY() / 32 * 4 + event.getX() / 32;
               
               PanelChunkView.this.CE.panelTileSelect.tileSelected = (PanelChunkView.this.main.gfx.graphics64[index64]).tileNums[i];
               PanelChunkView.this.CE.panelTileSelect.palette = PanelChunkView.this.main.gfx.graphics64[index64].getPalette(i);
               PanelChunkView.this.CE.panelTileSelect.repaint();
             }  if (event.getButton() == 1) {
               PanelChunkView.this.mouse1Down = true;
               int index64 = PanelChunkView.this.CE.selectedTileset1 * 64 + PanelChunkView.this.CE.panelChunkSelect.chunkSelected;
               int i = event.getY() / 32 * 4 + event.getX() / 32;
 
               
               PanelChunkView.this.main.gfx.graphics64[index64].setTile(
                   i, PanelChunkView.this.main.gfx.graphics16[PanelChunkView.this.CE.selectedTileset1 * 128 + PanelChunkView.this.CE.panelTileSelect.tileSelected]);
 
               
               PanelChunkView.this.main.gfx.graphics64[index64].setPalette(i, PanelChunkView.this.CE.panelTileSelect.palette);
 
               
               if (PanelChunkView.this.CE.useAlternateTileset) {
                 if (!(PanelChunkView.this.main.gfx.graphics64[index64]).altTileset.contains(Integer.valueOf(i))) {
                   (PanelChunkView.this.main.gfx.graphics64[index64]).altTileset.add(Integer.valueOf(i));
                 }
                 PanelChunkView.this.CE.useAlternateTileset = false;
                 PanelChunkView.this.CE.panelTileSelect.repaint();
               }
               else if ((PanelChunkView.this.main.gfx.graphics64[index64]).altTileset.contains(Integer.valueOf(i))) {
                 (PanelChunkView.this.main.gfx.graphics64[index64]).altTileset.remove(new Integer(i));
               } 
 
 
               
               PanelChunkView.this.CE.panelChunkSelect.repaint();
               PanelChunkView.this.repaint();
             } 
           }
 
           
           public void mouseEntered(MouseEvent event) {}
           
           public void mouseExited(MouseEvent event) {
             PanelChunkView.this.mouse1Down = false;
           }
           
           public void mouseReleased(MouseEvent event) {
             if (event.getButton() == 1) PanelChunkView.this.mouse1Down = false; 
           }
           
           public void mouseClicked(MouseEvent event) {}
         });
     addMouseMotionListener(
         new MouseMotionListener() {
           public void mouseDragged(MouseEvent event) {
             if (PanelChunkView.this.mouse1Down) {
               int index64 = PanelChunkView.this.CE.selectedTileset1 * 64 + PanelChunkView.this.CE.panelChunkSelect.chunkSelected;
               int i = event.getY() / 32 * 4 + event.getX() / 32;
               if (i > 15) {
                 return;
               }
               PanelChunkView.this.main.gfx.graphics64[index64].setTile(
                   i, PanelChunkView.this.main.gfx.graphics16[PanelChunkView.this.CE.selectedTileset1 * 128 + PanelChunkView.this.CE.panelTileSelect.tileSelected]);
 
               
               PanelChunkView.this.main.gfx.graphics64[index64].setPalette(i, PanelChunkView.this.CE.panelTileSelect.palette);
 
               
               PanelChunkView.this.CE.panelChunkSelect.repaint();
               PanelChunkView.this.repaint();
             } 
           }
 
 
           
           public void mouseMoved(MouseEvent event) {}
         });
   }
 
 
   
   protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     
     int[] pixels = new int[12288];
     
     int tileNum = this.CE.panelChunkSelect.chunkSelected;
     
     Tile64 curTile = this.main.gfx.graphics64[this.CE.selectedTileset1 * 64 + tileNum].getCopy();
     
     for (Iterator<Integer> iterator = curTile.altTileset.iterator(); iterator.hasNext(); ) { int index = ((Integer)iterator.next()).intValue();
       if (this.CE.panelPaletteSelect.altCheckbox.isSelected()) {
         this.CE.selectedTileset2 = this.CE.panelPaletteSelect.altDropdown.getSelectedIndex();
         
         curTile.setTile(index, this.main.gfx.graphics16[this.CE.selectedTileset2 * 128 + curTile.tileNums[index]]);
       }  }
 
     
     for (int j = 0; j < pixels.length; j += 3) {
       if (tileNum < 64) {
         if (curTile.altTileset.contains(Integer.valueOf(j / 3 / 1024 * 4 + j / 3 % 64 / 16)) && !this.CE.panelPaletteSelect.altCheckbox.isSelected()) {
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
     g.drawImage(tile.getScaledInstance(128, 128, 8), 0, 0, null);
 
     
     g.setColor(Color.GRAY);
     g.drawLine(31, 0, 31, 127);
     g.drawLine(63, 0, 63, 127);
     g.drawLine(95, 0, 95, 127);
     g.drawLine(0, 31, 127, 31);
     g.drawLine(0, 63, 127, 63);
     g.drawLine(0, 95, 127, 95);
   }
 }


