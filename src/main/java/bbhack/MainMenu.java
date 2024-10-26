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
import bbhack.rom.ROMPalettes;
import bbhack.rom.ROMMapSectors;
import bbhack.rom.ROMGraphics;
import bbhack.rom.ROMSpriteDefs;
import bbhack.rom.ROMObjects;
import bbhack.ME.MapEditor;
import bbhack.CE.ChunkEditor;
import bbhack.EGE.EnemyGroupEditor;




public class MainMenu extends JFrame {
  public RomFileIO rom = new RomFileIO();
  private GridBagLayout layout = new GridBagLayout();
  private JPanel panel = new JPanel(layout);
  private static final long serialVersionUID = 1L;
  private GridBagConstraints c = new GridBagConstraints();

  public ROMPalettes palettes;
  public ROMGraphics gfx;
  public ROMSpriteDefs sprites;
  public ROMObjects objects;
  public ROMMapSectors map;
  private MapEditor ME;
  private JButton buttonMap;
  private ChunkEditor CE;
  private JButton buttonChunks;
  private EnemyGroupEditor EGE;
  private JButton buttonBattles;
  private JButton buttonItems;
  
  public MainMenu() {
    //set the window title
    super("BB Hack v1.1");
    getContentPane().add(panel, "Center");
    panel.setLayout(layout);

    c.anchor = 10;
    c.insets = new Insets(4, 4, 4, 4);

    JMenuBar menu = new JMenuBar();
    JMenu menuFile = new JMenu("File");
    menu.add(menuFile);

    JMenuItem itemLoad = new JMenuItem("Load");
    menuFile.add(itemLoad);
    itemLoad.addActionListener((ActionEvent event) -> {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(this);
        
        if (returnVal == 0) {
            loadROM(fileChooser.getSelectedFile());
        }
    }
    );

    JMenuItem itemExit = new JMenuItem("Exit");
    menuFile.add(itemExit);
    itemExit.addActionListener((ActionEvent event) -> {
        System.exit(0);
    }
    );


    JMenu menuHelp = new JMenu("Help");
    menu.add(menuHelp);
    
    JMenuItem itemAbout = new JMenuItem("About");
    menuHelp.add(itemAbout);
    itemAbout.addActionListener((ActionEvent event) -> {
        JOptionPane.showMessageDialog(this, createScrollingLabel(Info.aboutText, false), "About", 1);
    }
    );

    setJMenuBar(menu);

    c.gridx = 0; c.gridy = 2;
    buttonMap = new JButton("Map Editor");
    buttonMap.setIcon(new ImageIcon(Info.class.getResource("/icons/map_editor.png")));
    buttonMap.setToolTipText("Edit the EB0 world map.");
    panel.add(buttonMap, c);
    buttonMap.addActionListener((ActionEvent event) -> {
        if (rom.rompath == null) {
            JOptionPane.showMessageDialog(panel, "You need to load a ROM first, silly.", "Error", 0);
            return;
        }
        if (ME == null) {
            ME = new MapEditor(this);
            ME.setDefaultCloseOperation(2);
        }
        else if (!ME.isVisible()) {
            ME = new MapEditor(this);
            ME.setDefaultCloseOperation(2);
        }
    });


    c.gridx = 0; c.gridy = 3;
    buttonChunks = new JButton("Chunk Editor");
    buttonChunks.setIcon(new ImageIcon(Info.class.getResource("/icons/chunk_editor.png")));
    buttonChunks.setToolTipText("Edit the composition of the 64x64 tiles used in map editing.");
    panel.add(buttonChunks, c);
    buttonChunks.addActionListener((ActionEvent event) -> {
        if (rom.rompath == null) {
            JOptionPane.showMessageDialog(panel, "You need to load a ROM first, silly.", "Error", 0);
            return;
        }
        if (CE == null) {
            CE = new ChunkEditor(this);
            CE.setDefaultCloseOperation(2);
        }
        else if (!CE.isVisible()) {
            CE = new ChunkEditor(this);
            CE.useAlternateTileset = false;
            CE.setDefaultCloseOperation(2);
        }
    }
    );

    /*c.gridx = 0; c.gridy = 4;
    buttonBattles = new JButton("Battle Editor");
    buttonBattles.setIcon(new ImageIcon(Info.class.getResource("/icons/enemygroup_editor.png")));
    buttonBattles.setToolTipText("Edit the sets of enemies faced during random encounters.");
    panel.add(buttonBattles, c);
    buttonBattles.addActionListener((ActionEvent event) -> {
        if (rom.rompath == null) {
            JOptionPane.showMessageDialog(panel, "You need to load a ROM first, silly.", "Error", 0);
            return;
        }
        if (EGE == null) {
            EGE = new EnemyGroupEditor(this);
            EGE.setDefaultCloseOperation(2);
        }
        else if (!EGE.isVisible()) {
            EGE = new EnemyGroupEditor(this);
        }
    }
    );*/


    Image windowIcon1 = (new ImageIcon(Info.class.getResource("/icons/main1.png"))).getImage();
    Image windowIcon2 = (new ImageIcon(Info.class.getResource("/icons/main2.png"))).getImage();
    ArrayList<Image> windowIcons = new ArrayList<Image>();
    windowIcons.add(windowIcon1);
    windowIcons.add(windowIcon2);
    setIconImages(windowIcons);

    KeyEventPostProcessor pp = (KeyEvent event) -> {
        int key = event.getKeyCode();
        if (key == KeyEvent.VK_INSERT &&
                CE != null &&
                CE.isVisible() && CE.isFocused()) {
            CE.keyInsert();
        }
        
        if ((key == KeyEvent.VK_1 || key == KeyEvent.VK_2 || key == KeyEvent.VK_3 || key == KeyEvent.VK_4) &&
                CE != null &&
                CE.isVisible() && CE.isFocused()) {
            CE.keyNumber(key - 49);
        }
        return true;
    };
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(pp);
  }

  private void loadROM(File path) {
    //load rom
    rom.load(path);

    //reload stuff
    System.out.println("init palettes...");
    palettes = new ROMPalettes(this);
    System.out.println("init gfx...");
    gfx = new ROMGraphics(this);
    System.out.println("init map...");
    map = new ROMMapSectors(this);
    System.out.println("init sprites...");
    sprites = new ROMSpriteDefs(this);
    System.out.println("init objects...");
    objects = new ROMObjects(this);

    //repaint
    repaintAll();
  }

  public void repaintAll() {
    //if me and using
    if (ME != null) {
      ME.clearGraphicsCache();
      if (ME.isVisible()) ME.repaintAll(); 
    }
    //if ce and using
    if (CE != null && CE.isVisible()) CE.repaintAll();
  }

  //creates the formatting for the About window.
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
    if (CE == null)
      fullOpen = true; 
    if (CE != null && 
      !CE.isVisible()) {
      fullOpen = true;
    }


    if (fullOpen) {
      CE = new ChunkEditor(this);
      CE.setDefaultCloseOperation(2);
    } else {
      CE.requestFocus();
    } 

    CE.openFromMap(tileNum, altTile, palette, tileset1, tileset2);
  }
}


