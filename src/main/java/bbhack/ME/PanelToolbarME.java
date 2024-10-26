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
   JLabel paletteLabel = new JLabel(" Palette:");
   JLabel areaLabel = new JLabel(" Area:");
   JLabel tileset1Label = new JLabel(" Tileset 1:");
   JLabel tileset2Label = new JLabel(" Tileset 2:");
   JComboBox<String> paletteDropdown = new JComboBox<String>();
   JComboBox<String> areaDropdown = new JComboBox<String>();
   JComboBox<String> tileset1Dropdown = new JComboBox<String>();
   JComboBox<String> tileset2Dropdown = new JComboBox<String>();
   
   public PanelToolbarME(MainMenu instance, MapEditor MEInstance) {
     this.main = instance;
     this.ME = MEInstance;
     
     this.layout = new FlowLayout();
     setLayout(this.layout);
     
     add(this.paletteLabel);
     add(this.paletteDropdown);
     add(this.areaLabel);
     add(this.areaDropdown);
     add(this.tileset1Label);
     add(this.tileset1Dropdown);
     add(this.tileset2Label);
     add(this.tileset2Dropdown);
     
     for (int i = 0; i < 0x20; i++) {
       this.paletteDropdown.addItem(Integer.toString(i));
       this.tileset1Dropdown.addItem(Info.tilesetNames[i]);
       this.tileset2Dropdown.addItem(Info.tilesetNames[i]);
     }
     for (int i = 0; i < 0x40; i++) {
       this.areaDropdown.addItem(Integer.toHexString(i).toUpperCase());
     };
     update();
     
     this.paletteDropdown.addItemListener((ItemEvent event) -> {
         int sectorX = PanelToolbarME.this.ME.panelChunkSelect.selectX / 4;
         int sectorY = PanelToolbarME.this.ME.panelChunkSelect.selectY / 4;
         PanelToolbarME.this.main.map.sectorPalette[sectorY * 64 + sectorX] = PanelToolbarME.this.paletteDropdown.getSelectedIndex();
         PanelToolbarME.this.repaintAll();
     });
     this.areaDropdown.addItemListener((ItemEvent event) -> {
         int sectorX = PanelToolbarME.this.ME.panelChunkSelect.selectX / 4;
         int sectorY = PanelToolbarME.this.ME.panelChunkSelect.selectY / 4;
         PanelToolbarME.this.main.map.sectorArea[sectorY * 64 + sectorX] = PanelToolbarME.this.areaDropdown.getSelectedIndex();
         PanelToolbarME.this.repaintAll();
     });
     this.tileset1Dropdown.addItemListener((ItemEvent event) -> {
         int sectorX = PanelToolbarME.this.ME.panelChunkSelect.selectX / 4;
         int sectorY = PanelToolbarME.this.ME.panelChunkSelect.selectY / 4;
         PanelToolbarME.this.main.map.sectorTileset1[sectorY * 64 + sectorX] = PanelToolbarME.this.tileset1Dropdown.getSelectedIndex();
         PanelToolbarME.this.repaintAll();
     });
     this.tileset2Dropdown.addItemListener((ItemEvent event) -> {
         int sectorX = PanelToolbarME.this.ME.panelChunkSelect.selectX / 4;
         int sectorY = PanelToolbarME.this.ME.panelChunkSelect.selectY / 4;
         PanelToolbarME.this.main.map.sectorTileset2[sectorY * 64 + sectorX] = PanelToolbarME.this.tileset2Dropdown.getSelectedIndex();
         PanelToolbarME.this.repaintAll();
     });
 
     
     this.ME.panelMap.addMouseListener(
         new MouseListener() {
           public void mousePressed(MouseEvent event) {
             update();
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
     this.areaDropdown.setSelectedIndex(this.main.map.sectorAreaGet(sectorX, sectorY));
     this.tileset1Dropdown.setSelectedIndex(this.main.map.sectorTileset1Get(sectorX, sectorY));
     this.tileset2Dropdown.setSelectedIndex(this.main.map.sectorTileset2Get(sectorX, sectorY));
   }
   
   private void repaintAll() {
     this.ME.panelMap.clearGraphicsCache();
     this.ME.panelMap.repaint();
     this.ME.panelChunkSelect.repaint();
   }
 }


