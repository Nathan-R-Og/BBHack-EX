 package bbhack;
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Image;
 import java.awt.Insets;
 import java.util.ArrayList;
 import javax.swing.ImageIcon;
 import javax.swing.JComboBox;
 import javax.swing.JFrame;
 import javax.swing.JMenu;
 import javax.swing.JMenuBar;
 import javax.swing.JMenuItem;
 import javax.swing.JPanel;
 
 public class EnemyGroupEditor extends JFrame {
   private static final long serialVersionUID = 1L;
   private JPanel panel;
   private GridBagLayout layout;
   
   public EnemyGroupEditor() {
     super("Enemy Groups Editor");
     
     this.layout = new GridBagLayout();
     this.panel = new JPanel(this.layout);
     getContentPane().add(this.panel, "Center");
     this.panel.setLayout(this.layout);
 
     
     this.c = new GridBagConstraints();
     this.c.anchor = 10;
     this.c.insets = new Insets(8, 8, 8, 8);
 
 
 
     
     JMenuBar menu = new JMenuBar();
 
 
     
     JMenu menuFile = new JMenu("File");
     menu.add(menuFile);
     JMenuItem itemExit = new JMenuItem("Exit");
     menuFile.add(itemExit);
 
 
     
     JMenu menuHelp = new JMenu("Help");
     menu.add(menuHelp);
     JMenuItem itemAbout = new JMenuItem("About");
     menuHelp.add(itemAbout);
 
     
     setJMenuBar(menu);
 
 
 
     
     this.c.gridx = 0; this.c.gridy = 0;
     initGroupList();
     this.dropdownGroupList = new JComboBox<String>(this.groupList);
     this.dropdownGroupList.setToolTipText("Edit the sets of enemies faced during random encounters.");
     this.panel.add(this.dropdownGroupList, this.c);
 
     
     Image windowIcon1 = (new ImageIcon(getClass().getResource("/icons/main1.png"))).getImage();
     Image windowIcon2 = (new ImageIcon(getClass().getResource("/icons/main2.png"))).getImage();
     ArrayList<Image> windowIcons = new ArrayList<Image>();
     windowIcons.add(windowIcon1);
     windowIcons.add(windowIcon2);
     setIconImages(windowIcons);
   }
   private GridBagConstraints c; private JComboBox<String> dropdownGroupList; private String[] groupList;
   private void initGroupList() {
     this.groupList = new String[165];
     for (int i = 0; i < 165; i++)
       this.groupList[i] = "Enemy Group " + i; 
   }
 }


