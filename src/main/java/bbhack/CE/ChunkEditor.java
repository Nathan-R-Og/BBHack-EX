 package bbhack.CE;
 
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Dimension;
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
 import javax.swing.JDesktopPane;
 import javax.swing.JDialog;
 import javax.swing.JFrame;
 import javax.swing.JInternalFrame;
 import javax.swing.JMenu;
 import javax.swing.JMenuBar;
 import javax.swing.JMenuItem;
 import javax.swing.JOptionPane;
 import javax.swing.border.Border;
 import javax.swing.plaf.basic.BasicInternalFrameUI;
 import bbhack.Info;
 import bbhack.MainMenu;
 
 public class ChunkEditor
   extends JFrame
 {
   public MainMenu main;
   private ChunkEditor thisRef;
   private static final long serialVersionUID = 1L;
   private JDesktopPane desktop;
   InternalChunkSelectCE internalChunkSelect;
   JInternalFrame internalChunkView;
   JInternalFrame internalTileSelect;
   JInternalFrame internalOptions;
   
   public ChunkEditor(MainMenu instance) {
     super("Chunk Editor");
     this.main = instance;
     this.thisRef = this;
     
     setSize(832, 512);
     setVisible(true);
     setLocationRelativeTo((Component)null);
     
     this.selectedPalette = 4;
     this.selectedTileset1 = 10;
     this.selectedTileset2 = 3;
     
     this.useAlternateTileset = false;
     
     this.desktop = new JDesktopPane();
     this.desktop.setBackground(new Color(0.9F, 0.9F, 0.9F));
     add(this.desktop, "Center");
 
     
     this.panelChunkSelect = new PanelChunkSelectCE(this.main, this);
     this.internalChunkSelect = new InternalChunkSelectCE(this.panelChunkSelect);
     this.internalChunkSelect.show();
     this.internalChunkSelect.setFrameIcon((Icon)null);
     this.internalChunkSelect.setTitle("Chunk Selector");
     this.internalChunkSelect.add(this.panelChunkSelect);
     ((BasicInternalFrameUI)this.internalChunkSelect.getUI()).setNorthPane(null);
     this.internalChunkSelect.setBorder((Border)null);
     this.desktop.add(this.internalChunkSelect);
 
     
     this.panelChunkView = new PanelChunkView(this.main, this);
     this.internalChunkView = new JInternalFrame();
     this.internalChunkView.show();
     this.internalChunkView.setFrameIcon((Icon)null);
     this.internalChunkView.setTitle("Chunk Editor");
     this.internalChunkView.add(this.panelChunkView);
     ((BasicInternalFrameUI)this.internalChunkView.getUI()).setNorthPane(null);
     this.internalChunkView.setBorder((Border)null);
     this.desktop.add(this.internalChunkView);
 
     
     this.internalTileSelect = new JInternalFrame();
     this.panelPaletteSelect = new PanelPaletteSelectCE();
     this.internalTileSelect.add(this.panelPaletteSelect, "North");
     this.panelTileSelect = new PanelTileSelectCE(this.main, this);
     this.internalTileSelect.show();
     this.internalTileSelect.setFrameIcon((Icon)null);
     this.internalTileSelect.setTitle("16x16 Tile Selector");
     this.internalTileSelect.add(this.panelTileSelect);
     ((BasicInternalFrameUI)this.internalTileSelect.getUI()).setNorthPane(null);
     this.internalTileSelect.setBorder((Border)null);
     this.desktop.add(this.internalTileSelect);
 
     
     this.panelOptions = new PanelOptionsCE();
     this.internalOptions = new JInternalFrame();
     this.internalOptions.show();
     this.internalOptions.setFrameIcon((Icon)null);
     this.internalOptions.setTitle("Options");
     this.internalOptions.add(this.panelOptions);
     ((BasicInternalFrameUI)this.internalOptions.getUI()).setNorthPane(null);
     this.internalOptions.setBorder((Border)null);
     this.desktop.add(this.internalOptions);
 
     
     this.internalChunkSelect.setLocation(0, 0);
     this.panelChunkSelect.setPreferredSize(new Dimension(128, 384));
     this.internalChunkSelect.pack();
     
     this.internalChunkView.setLocation(this.internalChunkSelect.getWidth(), 0);
     this.panelChunkView.setPreferredSize(new Dimension(128, 128));
     this.internalChunkView.pack();
     
     this.internalTileSelect.setLocation(this.internalChunkSelect.getWidth(), this.internalChunkView.getHeight());
     this.internalTileSelect.setSize(getContentPane().getWidth() - this.internalChunkSelect.getWidth(), getContentPane().getHeight() - this.internalChunkView.getHeight());
     
     this.internalOptions.setLocation(this.internalChunkSelect.getWidth() + this.internalChunkView.getWidth(), 0);
     this.internalOptions.setSize(getContentPane().getWidth() - this.internalChunkSelect.getWidth() - this.internalChunkView.getWidth(), getContentPane().getHeight() - this.internalTileSelect.getHeight());
     
     this.internalChunkSelect.addHierarchyBoundsListener(
         new HierarchyBoundsListener()
         {
           public void ancestorMoved(HierarchyEvent event) {}
 
           
           public void ancestorResized(HierarchyEvent event) {
             ChunkEditor.this.internalChunkSelect.setLocation(0, 0);
             ChunkEditor.this.panelChunkSelect.setPreferredSize(new Dimension(128, 384));
             ChunkEditor.this.internalChunkSelect.pack();
             ChunkEditor.this.internalChunkSelect.setSize(ChunkEditor.this.internalChunkSelect.getWidth(), ChunkEditor.this.getContentPane().getHeight());
             
             ChunkEditor.this.internalChunkView.setLocation(ChunkEditor.this.internalChunkSelect.getWidth(), 0);
             ChunkEditor.this.panelChunkView.setPreferredSize(new Dimension(128, 128));
             ChunkEditor.this.internalChunkView.pack();
             
             ChunkEditor.this.internalTileSelect.setLocation(ChunkEditor.this.internalChunkSelect.getWidth(), ChunkEditor.this.internalChunkView.getHeight());
             ChunkEditor.this.internalTileSelect.setSize(ChunkEditor.this.getContentPane().getWidth() - ChunkEditor.this.internalChunkSelect.getWidth(), ChunkEditor.this.getContentPane().getHeight() - ChunkEditor.this.internalChunkView.getHeight());
             
             ChunkEditor.this.internalOptions.setLocation(ChunkEditor.this.internalChunkSelect.getWidth() + ChunkEditor.this.internalChunkView.getWidth(), 0);
             ChunkEditor.this.internalOptions.setSize(ChunkEditor.this.getContentPane().getWidth() - ChunkEditor.this.internalChunkSelect.getWidth() - ChunkEditor.this.internalChunkView.getWidth(), ChunkEditor.this.getContentPane().getHeight() - ChunkEditor.this.internalTileSelect.getHeight());
             
             ChunkEditor.this.internalChunkSelect.scroll.setMaximum(32 - ChunkEditor.this.panelChunkSelect.getHeight() / 64 + 15);
           }
         });
 
 
     
     this.panelPaletteSelect.altCheckbox.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             ChunkEditor.this.panelChunkView.repaint();
           }
         });
     this.panelPaletteSelect.altDropdown.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             ChunkEditor.this.panelChunkView.repaint();
           }
         });
 
 
     
     this.panelOptions.tileset.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             ChunkEditor.this.panelPaletteSelect.altCheckbox.setSelected(false);
             ChunkEditor.this.selectedTileset1 = ChunkEditor.this.panelOptions.tileset.getSelectedIndex();
             ChunkEditor.this.panelChunkSelect.repaint();
             ChunkEditor.this.panelTileSelect.repaint();
             ChunkEditor.this.panelChunkView.repaint();
           }
         });
     this.panelOptions.palette64.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             ChunkEditor.this.selectedPalette = ChunkEditor.this.panelOptions.palette64.getSelectedIndex();
             ChunkEditor.this.panelChunkSelect.repaint();
             ChunkEditor.this.panelTileSelect.repaint();
             ChunkEditor.this.panelChunkView.repaint();
           }
         });
 
     
     this.selectedTileset1 = this.panelOptions.tileset.getSelectedIndex();
     this.selectedPalette = this.panelOptions.palette64.getSelectedIndex();
     this.panelChunkSelect.repaint();
     this.panelTileSelect.repaint();
     this.panelChunkView.repaint();
 
 
 
     
     JMenuBar menu = new JMenuBar();
 
 
     
     JMenu menuFile = new JMenu("File");
     menu.add(menuFile);
     
     JMenuItem itemExit = new JMenuItem("Exit");
     menuFile.add(itemExit);
     JMenuItem itemSave = new JMenuItem("Save");
     menuFile.add(itemSave);
     itemSave.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             ChunkEditor.this.main.gfx.save64();
             JOptionPane.showMessageDialog(ChunkEditor.this.desktop, "Successfully saved!");
           }
         });
 
 
 
     
     JMenu menuHelp = new JMenu("Help");
     menu.add(menuHelp);
     
     JMenuItem itemShortcuts = new JMenuItem("Keyboard Shortcuts");
     menuHelp.add(itemShortcuts);
     itemShortcuts.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             String text = "<html><b>RIGHT CLICK:</b> Select (copy) a tile from the chunk editor</html>\n<html><b>1-4:</b> Select sub-palette 1 through 4</html>\n<html><b>INSERT:</b> Place a tile from the alternate tileset (shouldn't need to be used very often)</html>";
 
 
             
             JOptionPane.showMessageDialog(ChunkEditor.this.thisRef, ChunkEditor.this.main.createScrollingLabel(text, true), "Keyboard Shortcuts", 1);
           }
         });
     
     JMenuItem itemAbout = new JMenuItem("About");
     menuHelp.add(itemAbout);
     itemAbout.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             String text = "<html><center><b>BB Hack v1.0<br/>All-in-one Earthbound Zero hacking tool<br/>Written by uyuyuy99</b><br/><br/><br/>If you find any bugs, want to request features, or want to help me uncover some data from the EB0 ROM, just contact me:<br/>PM on starmen.net (uyuyuy99)<br/>PM on smwcentral.net (uyuyuy99)<br/>Making a post on the forum thread<br/>Email (uyuyuy99@gmail.com)</center><br/><br/><h2>Changelog</h2><u>v1.0</u> (September 6, 2012)<ul><li>Initial release! Includes map editor and chunk editor.</li></u></u></html>";
             JOptionPane.showMessageDialog(ChunkEditor.this.thisRef, ChunkEditor.this.main.createScrollingLabel(text, false), "About", 1);
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
             ChunkEditor.this.exitSave();
           }
         });
   }
   PanelChunkSelectCE panelChunkSelect; PanelChunkView panelChunkView; PanelTileSelectCE panelTileSelect; PanelOptionsCE panelOptions; PanelPaletteSelectCE panelPaletteSelect; public int selectedPalette; public int selectedTileset1; public int selectedTileset2; public boolean useAlternateTileset;
   private void exitSave() {
     final JOptionPane optionPane = new JOptionPane("<html>Save chunk data to ROM?<br/><br/><i>(Note: choosing 'no' will NOT<br/>discard your changes)</i></html>", 
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
                 ChunkEditor.this.main.gfx.save64();
               }
               ChunkEditor.this.main.repaintAll();
               dialog.dispose();
               ChunkEditor.this.dispose();
             } 
           }
         });
     dialog.pack();
     dialog.setLocationRelativeTo(this.thisRef);
     dialog.setVisible(true);
   }
   
   public void openFromMap(int tileNum, boolean altTile, int palette, int tileset1, int tileset2) {
     if (altTile) {
       int temp = tileset1;
       tileset1 = tileset2;
       tileset2 = temp;
     } 
     this.panelChunkSelect.chunkSelected = tileNum;
     this.selectedPalette = palette;
     this.panelOptions.palette64.setSelectedIndex(palette);
     this.selectedTileset1 = tileset1;
     this.panelOptions.tileset.setSelectedIndex(tileset1);
     this.selectedTileset2 = tileset2;
     this.panelPaletteSelect.altDropdown.setSelectedIndex(tileset2);
     
     this.panelChunkView.repaint();
     this.panelTileSelect.repaint();
     this.panelChunkSelect.repaint();
   }
   
   public void keyInsert() {
     this.useAlternateTileset = true;
     this.panelPaletteSelect.altCheckbox.setEnabled(true);
     this.panelPaletteSelect.altDropdown.setEnabled(true);
     this.panelPaletteSelect.altCheckbox.setSelected(true);
     this.panelTileSelect.repaint();
   }
   
   public void keyNumber(int number) {
     this.panelPaletteSelect.palette.setSelectedIndex(number);
     this.panelTileSelect.repaint();
   }
   
   public void repaintAll() {
     this.panelChunkSelect.repaint();
     this.panelChunkView.repaint();
     this.panelTileSelect.repaint();
   }
 }


