package axoloti.objectviews;

import axoloti.PatchModel;
import axoloti.PatchView;
import axoloti.attributeviews.IAttributeInstanceView;
import axoloti.displayviews.IDisplayInstanceView;
import axoloti.inlets.IInletInstanceView;
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.outlets.IOutletInstanceView;
import axoloti.outlets.OutletInstanceView;
import axoloti.parameterviews.IParameterInstanceView;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public interface IAxoObjectInstanceView {
    public AxoObjectInstanceAbstract getModel();
    public void Lock();
    public void Unlock();
    public boolean isLocked();
    public void PostConstructor();
    public PatchView getPatchView();
    public PatchModel getPatchModel();

    public ArrayList<IInletInstanceView> getInletInstanceViews();
    public ArrayList<IOutletInstanceView> getOutletInstanceViews();
    public ArrayList<IParameterInstanceView> getParameterInstanceViews();
    
    public void setLocation(int x, int y);
    public void addInstanceNameEditor();
    public void setInstanceName(String InstanceName);
    public void setSelected(boolean Selected);
    public Boolean isSelected();
    public void SetLocation(int x1, int y1);
    public void moveToFront();
    public void resizeToGrid();
    public void Close();
    public void updateObj();
    public void ObjectModified(Object src);
    public AxoObjectInstanceAbstract getObjectInstance();
    public Rectangle getBounds();
    public Point getLocation();
    public void repaint();
    public Dimension getPreferredSize();
    public int getX();
    public int getY();
    public Dimension getSize();
    public void addParameterInstanceView(IParameterInstanceView view);
    public void addAttributeInstanceView(IAttributeInstanceView view);
    public void addDisplayInstanceView(IDisplayInstanceView view);
    public void addOutletInstanceView(IOutletInstanceView view);
    public void addInletInstanceView(IInletInstanceView view);

}