package axoloti.pobjectviews;

import axoloti.INetView;
import axoloti.PatchModel;
import axoloti.PatchViewProcessing;
import axoloti.attributeviews.IAttributeInstanceView;
import axoloti.displayviews.IDisplayInstanceView;
import axoloti.inlets.IInletInstanceView;
import axoloti.object.AxoObject;
import axoloti.object.AxoObjectAbstract;
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.object.ObjectModifiedListener;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.outlets.IOutletInstanceView;
import axoloti.parameterviews.IParameterInstanceView;
import axoloti.processing.PComponent;
import axoloti.utils.Constants;
import components.processing.PLabelComponent;
import components.processing.PTextFieldComponent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PAxoObjectInstanceView extends PComponent implements IAxoObjectInstanceView, ObjectModifiedListener {
    protected String InstanceName;
    protected boolean selected = false;
    PTextFieldComponent InstanceNameTF;
    PLabelComponent InstanceLabel;
    PatchViewProcessing patchView;
    private boolean Locked = false;

    private final ArrayList<IInletInstanceView> inletInstanceViews = new ArrayList<>();
    private final ArrayList<IOutletInstanceView> outletInstanceViews = new ArrayList<>();
    private final ArrayList<IParameterInstanceView> parameterInstanceViews = new ArrayList<>();
    
    public PAxoObjectInstanceView(AxoObjectInstanceAbstract model, PatchViewProcessing p) {
        super(p.getPatchPApplet());
        this.model = model;
        this.patchView = p;
    }
    
    private AxoObjectInstanceAbstract model;

    @Override
    public AxoObjectInstanceAbstract getModel() { 
        return model;
    }

    @Override
    public void Lock() {
        Locked = true;
    }

    @Override
    public void Unlock() {
        Locked = false;
    }

    @Override
    public boolean isLocked() {
        return Locked;
    }

    @Override
    public void PostConstructor() {
        setLocation(model.getX(), model.getY());
        setSize(new Dimension(60, 40));
    }

    @Override    
    public PatchViewProcessing getPatchView() { 
        return patchView;
    }
    
    @Override
    public PatchModel getPatchModel() {
        return patchView.getPatchController().patchModel;
    }

    @Override
    public ArrayList<IInletInstanceView> getInletInstanceViews() {
        return inletInstanceViews;
    }
    
    @Override
    public ArrayList<IOutletInstanceView> getOutletInstanceViews() {
        return outletInstanceViews;
    }
    
    @Override
    public ArrayList<IParameterInstanceView> getParameterInstanceViews() {
        return parameterInstanceViews;
    }
    
    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);

        model.setX(x);
        model.setY(y);
        if (getPatchView() != null) {
            repaint();
            for (IInletInstanceView i : getInletInstanceViews()) {
                INetView n = getPatchView().GetNetView(i);
                if (n != null) {
                    n.updateBounds();
                    n.repaint();
                }
            }
            for (IOutletInstanceView i : getOutletInstanceViews()) {
                INetView n = getPatchView().GetNetView(i);
                if (n != null) {
                    n.updateBounds();
                    n.repaint();
                }
            }
        }
    }
    
    static int borderSelected = 0;
    static int borderUnselected = 1;
    
    private void setBorder(int borderType) {
        
    }
    
    @Override
    public void setSelected(boolean Selected) {
        if (this.selected != Selected) {
            if (Selected) {
                setBorder(borderSelected);
            } else {
                setBorder(borderUnselected);
            }
            repaint();
        }
        this.selected = Selected;
    }

    @Override
    public Boolean isSelected() {
        return this.selected;
    }
    
    @Override
    public void SetLocation(int x1, int y1) {
        super.setLocation(x1, y1);
        model.setLocation(x1, y1);
        if (getPatchView() != null) {
            for (INetView n : getPatchView().getNetViews()) {
                n.updateBounds();
            }
        }
    }
    
    @Override
    public void resizeToGrid() {
        Dimension d = getPreferredSize();
        d.width = ((d.width + Constants.X_GRID - 1) / Constants.X_GRID) * Constants.X_GRID;
        d.height = ((d.height + Constants.Y_GRID - 1) / Constants.Y_GRID) * Constants.Y_GRID;
        setSize(d);
    }
    
    public void addInstanceNameEditor() {}
    
    public void moveToFront() {}

    @Override    
    public AxoObjectInstanceAbstract getObjectInstance() {
        return model;
    }
    public Rectangle getBounds() {
        return null;
    }
    public Point getLocation() {
        return null;
    }
    public void repaint() {
    }
    public Dimension getPreferredSize() {
        return null;
    }
    
    public int getX() {
        return 0;
    }
    
    public int getY() {
        return 0;
    }
    
    public Dimension getSize() {
        return null;
    }
    
    public void Close() {
        AxoObjectAbstract t = model.getType();
        if (t != null) {
            t.removeObjectModifiedListener(this);
        }
    }
    
    
    public void updateObj() {
        model.getType().addObjectModifiedListener(this);
        getPatchModel().ChangeObjectInstanceType(this.getObjectInstance(), this.getObjectInstance().getType());
        getPatchModel().cleanUpIntermediateChangeStates(3);
    }
    
    public void setInstanceName(String InstanceName) {
            if (this.InstanceName != null && this.InstanceName.equals(InstanceName)) {
            return;
        }

        if (getPatchModel() != null) {
            AxoObjectInstanceAbstract o1 = getPatchModel().GetObjectInstance(InstanceName);
            if ((o1 != null) && (o1 != this.getObjectInstance())) {
                Logger.getLogger(AxoObjectInstanceAbstract.class.getName()).log(Level.SEVERE, "Object name {0} already exists!", InstanceName);
                repaint();
                return;
            }
        }
        this.InstanceName = InstanceName;
        if (InstanceLabel != null) {
            InstanceLabel.setText(InstanceName);
        }
    }
    
    boolean deferredObjTypeUpdate = false;
    Rectangle editorBounds;
    Integer editorActiveTabIndex;

    @Override
    public void ObjectModified(Object src) {
        if (getPatchView() != null) {
            if (!getPatchView().isLocked()) {
                updateObj();
            } else {
                deferredObjTypeUpdate = true;
            }
        }

        try {
            AxoObject o = (AxoObject) src;
            if (o.getEditor() != null && o.getEditor().getBounds() != null) {
                editorBounds = o.getEditor().getBounds();
                editorActiveTabIndex = o.getEditor().getActiveTabIndex();
                this.getType().setEditorBounds(editorBounds);
                this.getType().setEditorActiveTabIndex(editorActiveTabIndex);
            }
        } catch (ClassCastException ex) {
        }
    }
    
    public AxoObject getType() {
        return (AxoObject) model.getType();
    }
    
    @Override
    public void addParameterInstanceView(IParameterInstanceView view) {
        this.parameterInstanceViews.add(view);
    }
    
    @Override
    public void addAttributeInstanceView(IAttributeInstanceView view) {
        // TODO
    }
    
    @Override
    public void addDisplayInstanceView(IDisplayInstanceView view) {
        
    }
    
    @Override
    public void addOutletInstanceView(IOutletInstanceView view) {
        
    }
    
    @Override
    public void addInletInstanceView(IInletInstanceView view) {
        
    }   
}
