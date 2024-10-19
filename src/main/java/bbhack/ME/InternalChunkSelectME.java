 package bbhack.ME;
 
 import java.awt.event.AdjustmentEvent;
 import java.awt.event.AdjustmentListener;
 import java.awt.event.MouseWheelEvent;
 import java.awt.event.MouseWheelListener;
 import javax.swing.JInternalFrame;
 import javax.swing.JScrollBar;
 
 
 
 
 public class InternalChunkSelectME
   extends JInternalFrame
 {
   private static final long serialVersionUID = 1L;
   private PanelChunkSelectME panelChunkSelect;
   public JScrollBar scroll;
   
   public InternalChunkSelectME(PanelChunkSelectME panelInstance) {
     this.panelChunkSelect = panelInstance;
     
     this.scroll = new JScrollBar(1, 0, 15, 0, 255);
     add(this.scroll, "East");
     
     this.scroll.addAdjustmentListener(
         new AdjustmentListener() {
           public void adjustmentValueChanged(AdjustmentEvent event) {
             InternalChunkSelectME.this.panelChunkSelect.viewY = event.getValue();
             InternalChunkSelectME.this.panelChunkSelect.repaint();
           }
         });
 
     
     addMouseWheelListener(
         new MouseWheelListener() {
           public void mouseWheelMoved(MouseWheelEvent event) {
             if (event.getWheelRotation() > 0) {
               if (InternalChunkSelectME.this.scroll.getValue() < InternalChunkSelectME.this.scroll.getMaximum() - 15) {
                 InternalChunkSelectME.this.scroll.setValue(InternalChunkSelectME.this.scroll.getValue() + 1);
                 InternalChunkSelectME.this.panelChunkSelect.viewY = InternalChunkSelectME.this.scroll.getValue();
                 InternalChunkSelectME.this.panelChunkSelect.repaint();
               } 
             } else if (event.getWheelRotation() < 0 && 
               InternalChunkSelectME.this.scroll.getValue() > 0) {
               InternalChunkSelectME.this.scroll.setValue(InternalChunkSelectME.this.scroll.getValue() - 1);
               InternalChunkSelectME.this.panelChunkSelect.viewY = InternalChunkSelectME.this.scroll.getValue();
               InternalChunkSelectME.this.panelChunkSelect.repaint();
             } 
           }
         });
   }
 }


