 package bbhack;
 import java.awt.Image;
 import java.awt.KeyEventPostProcessor;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.GridBagLayout;
 import java.awt.GridBagConstraints;
 import java.awt.Insets;
 import java.awt.KeyboardFocusManager;
 import java.awt.event.KeyEvent;
 import java.awt.Dimension;
 import java.io.File;
 import java.util.ArrayList;
 import java.util.StringTokenizer;
 import javax.swing.Icon;
 import javax.swing.ImageIcon;
 import javax.swing.JButton;
 import javax.swing.JFileChooser;
 import javax.swing.JLabel;
 import javax.swing.JMenu;
 import javax.swing.JFrame;
 import javax.swing.JMenuBar;
 import javax.swing.JMenuItem;
 import javax.swing.JOptionPane;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.BoxLayout;
 import javax.swing.Box;
 import bbhack.CE.ChunkEditor;
 import bbhack.rom.ROMPalettes;
 import bbhack.rom.ROMMapSectors;
 import bbhack.rom.ROMGraphics;
 import bbhack.ME.MapEditor;
 
 public class MainMenu extends JFrame {
   public MainMenu thisRef = this;
   
   public RomFileIO rom;
   
   public ROMPalettes palettes;
   
   public ROMGraphics gfx;
   public ROMMapSectors map;
   private static final long serialVersionUID = 1L;
   private JPanel panel;
   private GridBagLayout layout;
   private GridBagConstraints c;
   private EnemyGroupEditor EGE;
   private MapEditor ME;
   private ChunkEditor CE;
   private JButton buttonEnemyGroups;
   private JButton buttonMap;
   private JButton buttonChunks;
   private JButton buttonItems;
   
   public MainMenu() {
     super("BB Hack v1.0");
     
     this.rom = new RomFileIO();
     
     this.layout = new GridBagLayout();
     this.panel = new JPanel(this.layout);
     getContentPane().add(this.panel, "Center");
     this.panel.setLayout(this.layout);
 
     
     this.c = new GridBagConstraints();
     this.c.anchor = 10;
     this.c.insets = new Insets(4, 4, 4, 4);
 
 
 
     
     JMenuBar menu = new JMenuBar();
 
 
     
     JMenu menuFile = new JMenu("File");
     menu.add(menuFile);
     
     JMenuItem itemLoad = new JMenuItem("Load");
     menuFile.add(itemLoad);
     itemLoad.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             JFileChooser fileChooser = new JFileChooser();
             int returnVal = fileChooser.showOpenDialog(MainMenu.this.thisRef);
             
             if (returnVal == 0) {
               MainMenu.this.loadROM(fileChooser.getSelectedFile());
             }
           }
         });
     
     JMenuItem itemExit = new JMenuItem("Exit");
     menuFile.add(itemExit);
     itemExit.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             System.exit(0);
           }
         });
 
 
 
     
     JMenu menuHelp = new JMenu("Help");
     menu.add(menuHelp);
     JMenuItem itemAbout = new JMenuItem("About");
     menuHelp.add(itemAbout);
     itemAbout.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             String text = "<html><center><b>BB Hack v1.0<br/>All-in-one Earthbound Zero hacking tool<br/>Written by uyuyuy99</b><br/><br/><br/>If you find any bugs, want to request features, or want to help me uncover some data from the EB0 ROM, just contact me:<br/>PM on starmen.net (uyuyuy99)<br/>PM on smwcentral.net (uyuyuy99)<br/>Making a post on the forum thread<br/>Email (uyuyuy99@gmail.com)</center><br/><br/><h2>Changelog</h2><u>v1.0</u> (September 6, 2012)<ul><li>Initial release! Includes map editor and chunk editor.</li></u></u></html>";
             JOptionPane.showMessageDialog(MainMenu.this.thisRef, MainMenu.this.createScrollingLabel(text, false), "About", 1);
           }
         });
 
 
     
     setJMenuBar(menu);
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
     
     this.c.gridx = 0; this.c.gridy = 2;
     this.buttonMap = new JButton("Map Editor");
     this.buttonMap.setToolTipText("Edit the EB0 world map.");
     this.panel.add(this.buttonMap, this.c);
     this.buttonMap.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             if (MainMenu.this.rom.rompath == null) {
               JOptionPane.showMessageDialog(MainMenu.this.panel, "You need to load a ROM first, silly.", "Error", 0);
               return;
             } 
             if (MainMenu.this.ME == null) {
               MainMenu.this.ME = new MapEditor(MainMenu.this.thisRef);
               MainMenu.this.ME.setDefaultCloseOperation(2);
             }
             else if (!MainMenu.this.ME.isVisible()) {
               MainMenu.this.ME = new MapEditor(MainMenu.this.thisRef);
               MainMenu.this.ME.setDefaultCloseOperation(2);
             } 
           }
         });
 
 
     
     this.c.gridx = 0; this.c.gridy = 3;
     this.buttonChunks = new JButton("Chunk Editor");
     this.buttonChunks.setToolTipText("Edit the composition of the 64x64 tiles used in map editing.");
     this.panel.add(this.buttonChunks, this.c);
     this.buttonChunks.addActionListener(
         new ActionListener() {
           public void actionPerformed(ActionEvent event) {
             if (MainMenu.this.rom.rompath == null) {
               JOptionPane.showMessageDialog(MainMenu.this.panel, "You need to load a ROM first, silly.", "Error", 0);
               return;
             } 
             if (MainMenu.this.CE == null) {
               MainMenu.this.CE = new ChunkEditor(MainMenu.this.thisRef);
               MainMenu.this.CE.setDefaultCloseOperation(2);
             }
             else if (!MainMenu.this.CE.isVisible()) {
               MainMenu.this.CE = new ChunkEditor(MainMenu.this.thisRef);
               MainMenu.this.CE.useAlternateTileset = false;
               MainMenu.this.CE.setDefaultCloseOperation(2);
             } 
           }
         });
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
     Icon iconMap = new ImageIcon(Info.class.getResource("/icons/map_editor.png"));
     this.buttonMap.setIcon(iconMap);
     
     Icon iconChunks = new ImageIcon(Info.class.getResource("/icons/chunk_editor.png"));
     this.buttonChunks.setIcon(iconChunks);
 
     
     Image windowIcon1 = (new ImageIcon(Info.class.getResource("/icons/main1.png"))).getImage();
     Image windowIcon2 = (new ImageIcon(Info.class.getResource("/icons/main2.png"))).getImage();
     ArrayList<Image> windowIcons = new ArrayList<Image>();
     windowIcons.add(windowIcon1);
     windowIcons.add(windowIcon2);
     setIconImages(windowIcons);
     
     KeyEventPostProcessor pp = new KeyEventPostProcessor() {
         public boolean postProcessKeyEvent(KeyEvent event) {
           int key = event.getKeyCode();
           if (key == 155 && 
             MainMenu.this.CE != null && 
             MainMenu.this.CE.isVisible() && MainMenu.this.CE.isFocused()) {
             MainMenu.this.CE.keyInsert();
           }
           
           if ((key == 49 || key == 50 || key == 51 || key == 52) && 
             MainMenu.this.CE != null && 
             MainMenu.this.CE.isVisible() && MainMenu.this.CE.isFocused()) {
             MainMenu.this.CE.keyNumber(key - 49);
           }
 
           
           return true;
         }
       };
     KeyboardFocusManager.getCurrentKeyboardFocusManager()
       .addKeyEventPostProcessor(pp);
   }
   
   private void loadROM(File path) {
     this.rom.load(path);
     
     this.palettes = new ROMPalettes(this);
     this.gfx = new ROMGraphics(this);
     this.map = new ROMMapSectors(this);
     
     repaintAll();
   }
   
   public void repaintAll() {
     if (this.ME != null) {
       this.ME.clearGraphicsCache();
       if (this.ME.isVisible()) this.ME.repaintAll(); 
     }  if (this.CE != null && 
       this.CE.isVisible()) this.CE.repaintAll();
   
   }
   
   public JScrollPane createScrollingLabel(String text, boolean shorter) {
     int emptyLine = ((new JLabel("newline")).getPreferredSize()).height;
     JPanel labels = new JPanel();
     labels.setLayout(new BoxLayout(labels, 1));
     text = text.replaceAll("\n\n", "\nnewline\n");
     StringTokenizer st = new StringTokenizer(text, "\n");
     
     while (st.hasMoreTokens()) {
       JLabel temp = new JLabel(st.nextToken());
       if (temp.getText().equals("newline")) {
         labels.add(Box.createVerticalStrut(emptyLine)); continue;
       } 
       labels.add(temp);
     } 
     
     JScrollPane out = new JScrollPane(labels, 22, 31);
     if (shorter) { out.setPreferredSize(new Dimension((out.getPreferredSize()).width + 20, 130)); }
     else { out.setPreferredSize(new Dimension((out.getPreferredSize()).width + 20, 220)); }
      return out;
   }
   
   public void openCEFromMap(int tileNum, boolean altTile, int palette, int tileset1, int tileset2) {
     boolean fullOpen = false;
     if (this.CE == null)
       fullOpen = true; 
     if (this.CE != null && 
       !this.CE.isVisible()) {
       fullOpen = true;
     }
 
     
     if (fullOpen) {
       this.CE = new ChunkEditor(this.thisRef);
       this.CE.setDefaultCloseOperation(2);
     } else {
       this.CE.requestFocus();
     } 
     
     this.CE.openFromMap(tileNum, altTile, palette, tileset1, tileset2);
   }
 }


