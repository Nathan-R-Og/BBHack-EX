 package bbhack.CE;
 
 import java.awt.event.AdjustmentEvent;
 import java.awt.event.AdjustmentListener;
 import java.awt.event.MouseWheelEvent;
 import java.awt.event.MouseWheelListener;
 import javax.swing.JInternalFrame;
 import javax.swing.JScrollBar;
 
 
 
 
 public class InternalChunkSelectCE
   extends JInternalFrame
 {
   private static final long serialVersionUID = 1L;
   private PanelChunkSelectCE panelChunkSelect;
   public JScrollBar scroll;
   
   public InternalChunkSelectCE(PanelChunkSelectCE panelInstance) {
     this.panelChunkSelect = panelInstance;
     
     this.scroll = new JScrollBar(1, 0, 15, 0, 255);
     add(this.scroll, "East");
     
     this.scroll.addAdjustmentListener(
         new AdjustmentListener() {
           public void adjustmentValueChanged(AdjustmentEvent event) {
             InternalChunkSelectCE.this.panelChunkSelect.viewY = event.getValue();
             InternalChunkSelectCE.this.panelChunkSelect.repaint();
           }
         });
 
     
     addMouseWheelListener(
         new MouseWheelListener() {
           public void mouseWheelMoved(MouseWheelEvent event) {
             if (event.getWheelRotation() > 0) {
               if (InternalChunkSelectCE.this.scroll.getValue() < InternalChunkSelectCE.this.scroll.getMaximum() - 15) {
                 InternalChunkSelectCE.this.scroll.setValue(InternalChunkSelectCE.this.scroll.getValue() + 1);
                 InternalChunkSelectCE.this.panelChunkSelect.viewY = InternalChunkSelectCE.this.scroll.getValue();
                 InternalChunkSelectCE.this.panelChunkSelect.repaint();
               } 
             } else if (event.getWheelRotation() < 0 && 
               InternalChunkSelectCE.this.scroll.getValue() > 0) {
               InternalChunkSelectCE.this.scroll.setValue(InternalChunkSelectCE.this.scroll.getValue() - 1);
               InternalChunkSelectCE.this.panelChunkSelect.viewY = InternalChunkSelectCE.this.scroll.getValue();
               InternalChunkSelectCE.this.panelChunkSelect.repaint();
             } 
           }
         });
   }
 }


