package axoloti.outlets;

import axoloti.objectviews.IAxoObjectInstanceView;
import java.awt.Point;

public interface IOutletInstanceView {

    public void PostConstructor();

    public OutletInstance getOutletInstance();

    public void setHighlighted(boolean highlighted);

    public void disconnect();

    public void deleteNet();

    public Point getJackLocInCanvas();

    public IAxoObjectInstanceView getObjectInstanceView();
}
