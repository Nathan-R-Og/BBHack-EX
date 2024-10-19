 package bbhack.ME;
 
 import java.awt.Component;
 import java.awt.Image;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.HierarchyBoundsListener;
 import java.awt.event.HierarchyEvent;
 import java.awt.event.WindowAdapter;
 import java.awt.event.WindowEvent;
 import java.beans.PropertyChangeEvent;
 import java.beans.PropertyChangeListener;
 import java.util.ArrayList;
 import javax.swing.Icon;
 import javax.swing.ImageIcon;
 import javax.swing.JCheckBoxMenuItem;
 import javax.swing.JDesktopPane;
 import javax.swing.JDialog;
 import javax.swing.JFrame;
 import javax.swing.JMenu;
 import javax.swing.JMenuBar;
 import javax.swing.JMenuItem;
 import javax.swing.JOptionPane;
 import javax.swing.border.Border;
 import javax.swing.plaf.basic.BasicInternalFrameUI;
 import bbhack.Info;
 import bbhack.MainMenu;
 
 public class MapEditor
   extends JFrame
 {
   public MainMenu main;
   private MapEditor thisRef;
   private static final long serialVersionUID = 1L;
   private JDesktopPane desktop;
   
   public MapEditor(MainMenu instance) {
     super("Map Editor");
     this.main = instance;
     this.thisRef = this;
     
     setSize(1024, 576);
     setVisible(true);
     setLocationRelativeTo((Component)null);
     
     this.desktop = new JDesktopPane();
     add(this.desktop, "Center");
     
     this.panelMap = new PanelMap(this.main);
     this.internalMap = new InternalMap(this.panelMap);
     this.internalMap.show();
     this.internalMap.setSize(getContentPane().getWidth(), getContentPane().getHeight());
     this.internalMap.setLocation(0, 0);
     this.internalMap.setResizable(true);
     this.internalMap.setFrameIcon((Icon)null);
     this.internalMap.setTitle("Map Editor");
     this.internalMap.add(this.panelMap);
     ((BasicInternalFrameUI)this.internalMap.getUI()).setNorthPane(null);
     this.internalMap.setBorder((Border)null);
     this.desktop.add(this.internalMap);
     
     this.panelChunkSelect = new PanelChunkSelectME(this.main, this, this.panelMap);
     this.internalChunkSelect = new InternalChunkSelectME(this.panelChunkSelect);
     this.internalChunkSelect.show();
     this.internalChunkSelect.setSize(this.panelChunkSelect.viewWidth * 64 + (this.internalChunkSelect.getInsets()).left + (this.internalChunkSelect.getInsets()).right, getContentPane().getHeight());
     this.internalChunkSelect.setLocation(getContentPane().getWidth() - this.internalChunkSelect.getWidth(), 0);
     this.internalChunkSelect.setFrameIcon((Icon)null);
     this.internalChunkSelect.setTitle("Chunk Selector");
     this.internalChunkSelect.add(this.panelChunkSelect);
     ((BasicInternalFrameUI)this.internalChunkSelect.getUI()).setNorthPane(null);
     this.internalChunkSelect.setBorder((Border)null);
     this.panelChunkSelect.viewHeight = this.panelChunkSelect.getHeight() / 64 + 1;
     this.desktop.add(this.internalChunkSelect);
     
     this.internalMap.setSize(getContentPane().getWidth() - this.internalChunkSelect.getWidth(), getContentPane().getHeight());
     
     this.panelMap.setPanelChunkSelect(this.panelChunkSelect);
     
     this.panelToolbar = new PanelToolbarME(this.main, this);
     this.internalMap.add(this.panelToolbar, "North");
     
     this.internalMap.addHierarchyBoundsListener(
         new HierarchyBoundsListener()
         {
           public void ancestorMoved(HierarchyEvent event) {}
 
           
           public void ancestorResized(HierarchyEvent event) {
             if (MapEditor.this.panelMap.viewX > 256 - MapEditor.this.panelMap.viewWidth)
               MapEditor.this.panelMap.viewX = 256 - MapEditor.this.panelMap.viewWidth; 
             if (MapEditor.this.panelMap.viewY > 224 - MapEditor.this.panelMap.viewHeight) {
               MapEditor.this.panelMap.viewY = 224 - MapEditor.this.panelMap.viewHeight;
             }
             MapEditor.this.internalMap.setLocation(0, 0);
             MapEditor.this.internalMap.setSize(MapEditor.this.getContentPane().getWidth() - MapEditor.this.internalChunkSelect.getWidth(), MapEditor.this.getContentPane().getHeight());
             MapEditor.this.internalMap.scrollH.setMaximum(255 - MapEditor.this.internalMap.getWidth() / 64 + 15);
             MapEditor.this.internalMap.scrollV.setMaximum(223 - MapEditor.this.internalMap.getHeight() / 64 + 15);
             
             MapEditor.this.internalChunkSelect.setLocation(MapEditor.this.getContentPane().getWidth() - MapEditor.this.internalChunkSelect.getWidth(), 0);
             MapEditor.this.internalChunkSelect.setSize(MapEditor.this.panelChunkSelect.viewWidth * 64 + (MapEditor.this.internalChunkSelect.getInsets()).left + (MapEditor.this.internalChunkSelect.getInsets()).right + 
                 MapEditor.this.internalChunkSelect.scroll.getWidth(), MapEditor.this.getContentPane().getHeight());
             MapEditor.this.internalChunkSelect.scroll.setMaximum(32 - MapEditor.this.panelChunkSelect.getHeight() / 64 + 15);
             MapEditor.this.panelChunkSelect.viewHeight = MapEditor.this.panelChunkSelect.getHeight() / 64 + 1;
           }
         });
 
 
 
 
     
     JMenuBar menu = new JMenuBar();
 
 
     
     JMenu menuFile = new JMenu("File");
     menu.add(menuFile);
     
     JMenuItem itemSave = new JMenuItem("Save");
     menuFile.add(itemSave);
     itemSave.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             MapEditor.this.main.map.save();
             JOptionPane.showMessageDialog(MapEditor.this.desktop, "Successfully saved!");
           }
         });
     
     JMenuItem itemExit = new JMenuItem("Exit");
     menuFile.add(itemExit);
     itemExit.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             MapEditor.this.exitSave();
           }
         });
 
 
 
     
     JMenu menuView = new JMenu("View");
     menu.add(menuView);
     
     final JCheckBoxMenuItem itemViewGridChunk = new JCheckBoxMenuItem("Chunk Grid");
     menuView.add(itemViewGridChunk);
     itemViewGridChunk.setSelected(true);
     itemViewGridChunk.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             MapEditor.this.panelMap.viewGridChunk = itemViewGridChunk.isSelected();
             MapEditor.this.panelMap.repaint();
           }
         });
     
     final JCheckBoxMenuItem itemViewGridSector = new JCheckBoxMenuItem("Sector Grid");
     menuView.add(itemViewGridSector);
     itemViewGridSector.setSelected(false);
     itemViewGridSector.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             MapEditor.this.panelMap.viewGridSector = itemViewGridSector.isSelected();
             MapEditor.this.panelMap.repaint();
           }
         });
 
 
 
     
     JMenu menuHelp = new JMenu("Help");
     menu.add(menuHelp);
     
     JMenuItem itemShortcuts = new JMenuItem("Keyboard Shortcuts");
     menuHelp.add(itemShortcuts);
     itemShortcuts.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             String text = "<html><b>RIGHT CLICK:</b> Select (copy) a chunk from the map</html>\n<html><b>CTRL+LEFT CLICK:</b> Select tileset/palette from map without placing tile</html>\n<html><b>SHIFT+LEFT CLICK:</b> Open chunk in chunk editor from the map editor</html>";
 
 
             
             JOptionPane.showMessageDialog(MapEditor.this.thisRef, MapEditor.this.main.createScrollingLabel(text, true), "Keyboard Shortcuts", 1);
           }
         });
     
     JMenuItem itemAbout = new JMenuItem("About");
     menuHelp.add(itemAbout);
     itemAbout.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             String text = "<html><center><b>BB Hack v1.0<br/>All-in-one Earthbound Zero hacking tool<br/>Written by uyuyuy99</b><br/><br/><br/>If you find any bugs, want to request features, or want to help me uncover some data from the EB0 ROM, just contact me:<br/>PM on starmen.net (uyuyuy99)<br/>PM on smwcentral.net (uyuyuy99)<br/>Making a post on the forum thread<br/>Email (uyuyuy99@gmail.com)</center><br/><br/><h2>Changelog</h2><u>v1.0</u> (September 6, 2012)<ul><li>Initial release! Includes map editor and chunk editor.</li></u></u></html>";
             JOptionPane.showMessageDialog(MapEditor.this.thisRef, MapEditor.this.main.createScrollingLabel(text, false), "About", 1);
           }
         });
 
 
     
     setJMenuBar(menu);
 
 
 
 
     
     Image windowIcon1 = (new ImageIcon(Info.class.getResource("/icons/main1.png"))).getImage();
     Image windowIcon2 = (new ImageIcon(Info.class.getResource("/icons/main2.png"))).getImage();
     ArrayList<Image> windowIcons = new ArrayList<Image>();
     windowIcons.add(windowIcon1);
     windowIcons.add(windowIcon2);
     setIconImages(windowIcons);
     
     addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent event) {
             MapEditor.this.exitSave();
           }
         });
   }
   InternalMap internalMap; InternalChunkSelectME internalChunkSelect; PanelMap panelMap; PanelChunkSelectME panelChunkSelect; PanelToolbarME panelToolbar;
   private void exitSave() {
     final JOptionPane optionPane = new JOptionPane("<html>Save map data to ROM?<br/><br/><i>(Note: choosing 'no' will NOT<br/>discard your changes)</i></html>", 
         2, 0);
     final JDialog dialog = new JDialog(this.thisRef, "Save", true);
     dialog.setContentPane(optionPane);
     dialog.setDefaultCloseOperation(0);
     dialog.setResizable(false);
     
     optionPane.addPropertyChangeListener(
         new PropertyChangeListener() {
           public void propertyChange(PropertyChangeEvent event) {
             String prop = event.getPropertyName();
             if (dialog.isVisible() && event.getSource() == optionPane && prop.equals("value")) {
               if (((Integer)optionPane.getValue()).intValue() == 0) {
                 MapEditor.this.main.map.save();
               }
               dialog.dispose();
               MapEditor.this.dispose();
             } 
           }
         });
     dialog.pack();
     dialog.setLocationRelativeTo(this.thisRef);
     dialog.setVisible(true);
   }
   
   public void clearGraphicsCache() {
     this.panelMap.clearGraphicsCache();
   }
   
   public void repaintAll() {
     this.panelMap.repaint();
     this.panelChunkSelect.repaint();
   }
 }


