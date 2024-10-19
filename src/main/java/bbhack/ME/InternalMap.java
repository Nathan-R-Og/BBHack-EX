 package bbhack.ME;
 
 import java.awt.event.AdjustmentEvent;
 import java.awt.event.AdjustmentListener;
 import java.awt.event.MouseWheelEvent;
 import java.awt.event.MouseWheelListener;
 import javax.swing.JInternalFrame;
 import javax.swing.JScrollBar;
 
 
 
 
 public class InternalMap
   extends JInternalFrame
 {
   private static final long serialVersionUID = 1L;
   private PanelMap panelMap;
   public JScrollBar scrollH;
   public JScrollBar scrollV;
   
   public InternalMap(PanelMap panelInstance) {
     this.panelMap = panelInstance;
     
     this.scrollH = new JScrollBar(0, 0, 15, 0, 270);
     this.scrollV = new JScrollBar(1, 0, 15, 0, 270);
     
     add(this.scrollH, "South");
     add(this.scrollV, "East");
     
     this.scrollH.addAdjustmentListener(
         new AdjustmentListener() {
           public void adjustmentValueChanged(AdjustmentEvent event) {
             InternalMap.this.panelMap.scroll(event.getValue(), InternalMap.this.panelMap.scrollVLast);
           }
         });
 
     
     this.scrollV.addAdjustmentListener(
         new AdjustmentListener() {
           public void adjustmentValueChanged(AdjustmentEvent event) {
             InternalMap.this.panelMap.scroll(InternalMap.this.panelMap.scrollHLast, event.getValue());
           }
         });
 
     
     addMouseWheelListener(
         new MouseWheelListener()
         {
           public void mouseWheelMoved(MouseWheelEvent event) {
             if (InternalMap.this.getWidth() - event.getX() < InternalMap.this.getHeight() - event.getY()) {
               if (event.getWheelRotation() > 0) {
                 if (InternalMap.this.scrollV.getValue() < InternalMap.this.scrollV.getMaximum() - 15) {
                   InternalMap.this.scrollV.setValue(InternalMap.this.scrollV.getValue() + 1);
                   InternalMap.this.panelMap.viewY = InternalMap.this.scrollV.getValue();
                   InternalMap.this.panelMap.repaint();
                 } 
               } else if (event.getWheelRotation() < 0 && 
                 InternalMap.this.scrollV.getValue() > 0) {
                 InternalMap.this.scrollV.setValue(InternalMap.this.scrollV.getValue() - 1);
                 InternalMap.this.panelMap.viewY = InternalMap.this.scrollV.getValue();
                 InternalMap.this.panelMap.repaint();
               }
             
             }
             else if (event.getWheelRotation() > 0) {
               if (InternalMap.this.scrollH.getValue() < InternalMap.this.scrollH.getMaximum() - 15) {
                 InternalMap.this.scrollH.setValue(InternalMap.this.scrollH.getValue() + 1);
                 InternalMap.this.panelMap.viewX = InternalMap.this.scrollH.getValue();
                 InternalMap.this.panelMap.repaint();
               } 
             } else if (event.getWheelRotation() < 0 && 
               InternalMap.this.scrollH.getValue() > 0) {
               InternalMap.this.scrollH.setValue(InternalMap.this.scrollH.getValue() - 1);
               InternalMap.this.panelMap.viewX = InternalMap.this.scrollH.getValue();
               InternalMap.this.panelMap.repaint();
             } 
           }
         });
   }
 }


