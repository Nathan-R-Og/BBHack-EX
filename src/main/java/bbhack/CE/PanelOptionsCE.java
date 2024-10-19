 package bbhack.CE;
 
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Insets;
 import java.awt.image.BufferedImage;
 import java.awt.image.WritableRaster;
 import javax.swing.ImageIcon;
 import javax.swing.JComboBox;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import bbhack.Info;
 
 public class PanelOptionsCE extends JPanel {
   private static final long serialVersionUID = 1L;
   private GridBagLayout layout;
   private GridBagConstraints c;
   JComboBox<String> tileset;
   JComboBox<String> palette64;
   
   public PanelOptionsCE() {
     this.layout = new GridBagLayout();
     this.c = new GridBagConstraints();
     setLayout(this.layout);
     
     this.tileset = new JComboBox<String>();
     this.palette64 = new JComboBox<String>();
     JLabel tilesetLabel = new JLabel("Tileset: ");
     JLabel palette64Label = new JLabel("64x64 Palette: ");
     JLabel altLabel = new JLabel(" = tile uses alternate tileset (tile graphics change if loaded tilesets change)");
     altLabel.setIcon(new ImageIcon(getRedImage()));
     int i;
     for (i = 0; i < 32; i++) {
       this.tileset.addItem(Info.tilesetNames[i]);
     }
     for (i = 0; i < 32; i++) {
       this.palette64.addItem(Integer.toString(i));
     }
     
     this.c.gridx = 0; this.c.gridy = 0;
     this.c.gridwidth = 3;
     add(altLabel, this.c);
     this.c.gridwidth = 1;
     
     this.c.anchor = 13;
     this.c.insets = new Insets(24, 4, 6, 4);
     this.c.gridx = 0; this.c.gridy = 1;
     add(tilesetLabel, this.c);
     this.c.anchor = 17;
     this.c.insets = new Insets(24, 4, 6, 4);
     this.c.gridx = 1; this.c.gridy = 1;
     add(this.tileset, this.c);
     this.c.anchor = 13;
     this.c.insets = new Insets(6, 4, 6, 4);
     this.c.gridx = 0; this.c.gridy = 2;
     add(palette64Label, this.c);
     this.c.anchor = 17;
     this.c.insets = new Insets(6, 4, 6, 4);
     this.c.gridx = 1; this.c.gridy = 2;
     add(this.palette64, this.c);
   }
   
   private BufferedImage getRedImage() {
     int[] pixels = new int[3072];
     
     for (int j = 0; j < pixels.length; j += 3) {
       pixels[j] = 255;
       pixels[j + 1] = 0;
       pixels[j + 2] = 0;
     } 
     BufferedImage image = new BufferedImage(32, 32, 1);
     WritableRaster raster = image.getRaster();
     raster.setPixels(0, 0, 32, 32, pixels);
     return image;
   }
 }


