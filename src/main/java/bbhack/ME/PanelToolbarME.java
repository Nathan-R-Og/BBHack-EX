 package bbhack.ME;
 
 import java.awt.FlowLayout;
 import java.awt.event.ItemEvent;
 import java.awt.event.ItemListener;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import javax.swing.JComboBox;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import bbhack.Info;
 import bbhack.MainMenu;
 
 
 
 
 public class PanelToolbarME
   extends JPanel
 {
   private static final long serialVersionUID = 1L;
   private MainMenu main;
   private MapEditor ME;
   FlowLayout layout;
   JLabel paletteLabel;
   JLabel tileset1Label;
   JLabel tileset2Label;
   JComboBox<String> paletteDropdown;
   JComboBox<String> tileset1Dropdown;
   JComboBox<String> tileset2Dropdown;
   
   public PanelToolbarME(MainMenu instance, MapEditor MEInstance) {
     this.main = instance;
     this.ME = MEInstance;
     
     this.layout = new FlowLayout();
     setLayout(this.layout);
     
     this.paletteLabel = new JLabel(" Palette:");
     this.tileset1Label = new JLabel(" Tileset 1:");
     this.tileset2Label = new JLabel(" Tileset 2:");
     
     this.paletteDropdown = new JComboBox<String>();
     this.tileset1Dropdown = new JComboBox<String>();
     this.tileset2Dropdown = new JComboBox<String>();
     
     add(this.paletteLabel);
     add(this.paletteDropdown);
     add(this.tileset1Label);
     add(this.tileset1Dropdown);
     add(this.tileset2Label);
     add(this.tileset2Dropdown);
     
     for (int i = 0; i < 32; i++) {
       this.paletteDropdown.addItem(Integer.toString(i));
       this.tileset1Dropdown.addItem(Info.tilesetNames[i]);
       this.tileset2Dropdown.addItem(Info.tilesetNames[i]);
     } 
     
     update();
     
     this.paletteDropdown.addItemListener(
         new ItemListener() {
           public void itemStateChanged(ItemEvent event) {
             int sectorX = PanelToolbarME.this.ME.panelChunkSelect.selectX / 4;
             int sectorY = PanelToolbarME.this.ME.panelChunkSelect.selectY / 4;
             PanelToolbarME.this.main.map.sectorPalette[sectorY * 64 + sectorX] = PanelToolbarME.this.paletteDropdown.getSelectedIndex();
             PanelToolbarME.this.repaintAll();
           }
         });
     this.tileset1Dropdown.addItemListener(
         new ItemListener() {
           public void itemStateChanged(ItemEvent event) {
             int sectorX = PanelToolbarME.this.ME.panelChunkSelect.selectX / 4;
             int sectorY = PanelToolbarME.this.ME.panelChunkSelect.selectY / 4;
             PanelToolbarME.this.main.map.sectorTileset1[sectorY * 64 + sectorX] = PanelToolbarME.this.tileset1Dropdown.getSelectedIndex();
             PanelToolbarME.this.repaintAll();
           }
         });
     this.tileset2Dropdown.addItemListener(
         new ItemListener() {
           public void itemStateChanged(ItemEvent event) {
             int sectorX = PanelToolbarME.this.ME.panelChunkSelect.selectX / 4;
             int sectorY = PanelToolbarME.this.ME.panelChunkSelect.selectY / 4;
             PanelToolbarME.this.main.map.sectorTileset2[sectorY * 64 + sectorX] = PanelToolbarME.this.tileset2Dropdown.getSelectedIndex();
             PanelToolbarME.this.repaintAll();
           }
         });
 
     
     this.ME.panelMap.addMouseListener(
         new MouseListener() {
           public void mousePressed(MouseEvent event) {
             PanelToolbarME.this.update();
           }
 
           
           public void mouseClicked(MouseEvent event) {}
 
           
           public void mouseEntered(MouseEvent event) {}
           
           public void mouseExited(MouseEvent event) {}
           
           public void mouseReleased(MouseEvent event) {}
         });
   }
   
   private void update() {
     int sectorX = this.ME.panelChunkSelect.selectX / 4;
     int sectorY = this.ME.panelChunkSelect.selectY / 4;
     
     this.paletteDropdown.setSelectedIndex(this.main.map.sectorPaletteGet(sectorX, sectorY));
     this.tileset1Dropdown.setSelectedIndex(this.main.map.sectorTileset1Get(sectorX, sectorY));
     this.tileset2Dropdown.setSelectedIndex(this.main.map.sectorTileset2Get(sectorX, sectorY));
   }
   
   private void repaintAll() {
     this.ME.panelMap.clearGraphicsCache();
     this.ME.panelMap.repaint();
     this.ME.panelChunkSelect.repaint();
   }
 }


