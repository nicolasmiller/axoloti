package axoloti.outlets;

import axoloti.objectviews.IAxoObjectInstanceView;
import java.awt.Point;
import javax.swing.JPopupMenu;

public interface IOutletInstanceView {
     public void PostConstructor();
     public JPopupMenu getPopup();
     public OutletInstance getOutletInstance();
     public void setHighlighted(boolean highlighted);
     public void disconnect();
     public void deleteNet();
     public Point getJackLocInCanvas();
     public IAxoObjectInstanceView getObjectInstanceView();
}