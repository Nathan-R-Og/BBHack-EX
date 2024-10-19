 package bbhack;
 
 import javax.swing.UIManager;
 import javax.swing.UnsupportedLookAndFeelException;
 
 class BBHack {
   public static void main(String[] args) {
     try {
       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
     } catch (ClassNotFoundException e) {
       e.printStackTrace();
     } catch (InstantiationException e) {
       e.printStackTrace();
     } catch (IllegalAccessException e) {
       e.printStackTrace();
     } catch (UnsupportedLookAndFeelException e) {
       e.printStackTrace();
     } 
     
     UIManager.put("DesktopPaneUI", "javax.swing.plaf.basic.BasicDesktopPaneUI");
     
     MainMenu gui = new MainMenu();
     gui.setDefaultCloseOperation(3);
     gui.setSize(256, 512);
     gui.setVisible(true);
     gui.setLocationRelativeTo(null);
   }
 }


