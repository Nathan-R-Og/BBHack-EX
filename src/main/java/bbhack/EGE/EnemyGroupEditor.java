
package bbhack.EGE;

import bbhack.MainMenu;
import java.awt.Component;
import javax.swing.JFrame;

public class EnemyGroupEditor extends JFrame {
    
   public MainMenu main;
   public EnemyGroupEditor(MainMenu instance) {
     super("Battle Editor");
     this.main = instance;
     
     setSize(832, 512);
     setVisible(true);
     setLocationRelativeTo((Component)null);
     
  
   }
}
