 package bbhack.CE;
 
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Insets;
 import javax.swing.JCheckBox;
 import javax.swing.JComboBox;
 import javax.swing.JPanel;
 import bbhack.Info;
 
 public class PanelPaletteSelectCE
   extends JPanel {
   private static final long serialVersionUID = 1L;
   private GridBagLayout layout;
   private GridBagConstraints c;
   JComboBox<String> palette;
   JCheckBox altCheckbox;
   JComboBox<String> altDropdown;
   
   public PanelPaletteSelectCE() {
     this.layout = new GridBagLayout();
     this.c = new GridBagConstraints();
     setLayout(this.layout);
     
     this.palette = new JComboBox<String>();
     this.altCheckbox = new JCheckBox("Preview alternate tileset: ");
     this.altDropdown = new JComboBox<String>();
     
     this.c.insets = new Insets(0, 0, 0, 48);
     this.c.anchor = 17;
     add(this.palette, this.c);
     this.c.insets = new Insets(4, 0, 4, 0);
     this.c.weightx = 0.0D;
     this.c.anchor = 10;
     add(this.altCheckbox, this.c);
     add(this.altDropdown, this.c);
     
     this.altCheckbox.setEnabled(false);
     this.altDropdown.setEnabled(false);
     
     int i;
     for (i = 0; i < 4; i++) {
       this.palette.addItem("Palette " + i);
     }
 
     
     for (i = 0; i < 32; i++) {
       this.altDropdown.addItem(Info.tilesetNames[i]);
     }
     
     setVisible(true);
   }
 }


