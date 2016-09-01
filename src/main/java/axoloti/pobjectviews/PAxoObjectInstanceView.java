package axoloti.pobjectviews;

import axoloti.INetView;
import axoloti.Net;
import axoloti.PatchModel;
import axoloti.PatchViewProcessing;
import axoloti.Theme;
import axoloti.attribute.AttributeInstance;
import axoloti.attributedefinition.AxoAttribute;
import axoloti.attributeviews.IAttributeInstanceView;
import axoloti.displays.Display;
import axoloti.displays.DisplayInstance;
import axoloti.displayviews.IDisplayInstanceView;
import axoloti.inlets.IInletInstanceView;
import axoloti.inlets.Inlet;
import axoloti.inlets.InletInstance;
import axoloti.object.AxoObject;
import axoloti.object.AxoObjectAbstract;
import axoloti.object.AxoObjectInstance;
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.object.ObjectModifiedListener;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.outlets.IOutletInstanceView;
import axoloti.outlets.Outlet;
import axoloti.outlets.OutletInstance;
import axoloti.parameters.Parameter;
import axoloti.parameters.ParameterInstance;
import axoloti.parameterviews.IParameterInstanceView;
import axoloti.pinlets.PInletInstanceView;
import axoloti.poutlets.POutletInstanceView;
import axoloti.processing.PComponent;
import static axoloti.processing.PLayoutType.HORIZONTAL_CENTERED;
import static axoloti.processing.PLayoutType.VERTICAL_CENTERED;
import static axoloti.processing.PLayoutType.VERTICAL_LEFT;
import axoloti.processing.PatchPApplet;
import axoloti.utils.Constants;
import components.processing.PLabelComponent;
import components.processing.PPopupIcon;
import components.processing.PTextFieldComponent;
import java.awt.Dimension;
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
    private PComponent titleBar;
    private boolean Locked = false;
    PatchPApplet pApplet;

    public PComponent p_parameterViews;
    public PComponent p_displayViews;
    public PComponent p_ioletViews;
    public PComponent p_inletViews;
    public PComponent p_outletViews;

    private final ArrayList<IInletInstanceView> inletInstanceViews = new ArrayList<>();
    private final ArrayList<IOutletInstanceView> outletInstanceViews = new ArrayList<>();
    private final ArrayList<IParameterInstanceView> parameterInstanceViews = new ArrayList<>();

    public PAxoObjectInstanceView(AxoObjectInstanceAbstract model, PatchViewProcessing p) {
        super(p.getPatchPApplet());
        this.model = model;
        this.patchView = p;
        this.pApplet = p.getPatchPApplet();
    }

    private AxoObjectInstanceAbstract model;

    @Override
    public AxoObjectInstance getModel() {
        return (AxoObjectInstance) model;
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

    public static final int PADDING = 2;

    public static final int MIN_WIDTH = 60;
    public static final int MIN_HEIGHT = 40;

    @Override
    public void setup() {
        super.setup();
//        setBounds(getX(), getY(), getWidth(), 100);
    }

    public void updateObj1() {
        getType().addObjectModifiedListener(this);
    }

    @Override
    public void PostConstructor() {
        p_parameterViews = new PComponent(pApplet);
        p_displayViews = new PComponent(pApplet);
        p_ioletViews = new PComponent(pApplet);
        p_ioletViews.setLayout(HORIZONTAL_CENTERED);
        p_inletViews = new PComponent(pApplet);
        p_inletViews.setLayout(VERTICAL_LEFT);
        p_outletViews = new PComponent(pApplet);
        p_outletViews.setLayout(VERTICAL_LEFT);

        boolean isFucked = false;

        try {
            getModel();
        } catch (ClassCastException e) {
            isFucked = true;
        }

        ArrayList<ParameterInstance> pParameterInstances = new ArrayList<>();
        ArrayList<AttributeInstance> pAttributeInstances = new ArrayList<>();
        ArrayList<InletInstance> pInletInstances = new ArrayList<>();
        ArrayList<OutletInstance> pOutletInstances = new ArrayList<>();

        if (!isFucked) {
            updateObj1();
//            ArrayList<ParameterInstance> pParameterInstances = getModel().parameterInstances;
////            ArrayList<AttributeInstance> pAttributeInstances = getModel().attributeInstances;
////            ArrayList<InletInstance> pInletInstances = getModel().inletInstances;
////            ArrayList<OutletInstance> pOutletInstances = getModel().outletInstances;

            pParameterInstances = getModel().parameterInstances;
            pAttributeInstances = getModel().attributeInstances;
            pInletInstances = getModel().inletInstances;
            pOutletInstances = getModel().outletInstances;

            getModel().parameterInstances = new ArrayList<>();
            getModel().attributeInstances = new ArrayList<>();
            getModel().displayInstances = new ArrayList<>();
            getModel().inletInstances = new ArrayList<>();
            getModel().outletInstances = new ArrayList<>();
        }

        setBounds(model.getX(), model.getY(), MIN_WIDTH, MIN_HEIGHT);
        setBackground(Theme.getCurrentTheme().Object_Default_Background);
        this.setLayout(VERTICAL_LEFT);
        this.titleBar = new PComponent(this.pApplet);
        titleBar.setBackground(Theme.getCurrentTheme().Object_TitleBar_Background);
        titleBar.setLayout(HORIZONTAL_CENTERED);

        final PPopupIcon popupIcon = new PPopupIcon(pApplet);
// TODO
//        popupIcon.setPopupIconListener(new PopupIcon.PopupIconListener() {
//            @Override
//            public void ShowPopup() {
//                JPopupMenu popup = CreatePopupMenu();
//                popupIcon.add(popup);
//                popup.show(popupIcon,
//                        0, popupIcon.getHeight());
//            }
//        });

        titleBar.add(popupIcon);
//
        PLabelComponent objectLabel = new PLabelComponent(this.pApplet, model.typeName);
        objectLabel.setForeground(Theme.getCurrentTheme().Object_TitleBar_Foreground);
        objectLabel.setTextHeight(Constants.FONT_POINT_SIZE);
        titleBar.add(objectLabel);
        add(titleBar);

//TODO
//        String tooltiptxt = "<html>";
//        if ((getType().sDescription != null) && (!getType().sDescription.isEmpty())) {
//            tooltiptxt += getType().sDescription;
//        }
//        if ((getType().sAuthor != null) && (!getType().sAuthor.isEmpty())) {
//            tooltiptxt += "<p>Author: " + getType().sAuthor;
//        }
//        if ((getType().sLicense != null) && (!getType().sLicense.isEmpty())) {
//            tooltiptxt += "<p>License: " + getType().sLicense;
//        }
//        if ((getType().sPath != null) && (!getType().sPath.isEmpty())) {
//            tooltiptxt += "<p>Path: " + getType().sPath;
//        }
//        Titlebar.setToolTipText(tooltiptxt);
// TODO
//        InstanceLabel.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) {
//                    addInstanceNameEditor();
//                    e.consume();
//                }
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//            }
//        });
        InstanceLabel = new PLabelComponent(pApplet, model.getInstanceName());
        add(InstanceLabel);

        if (!isFucked) {
            p_ioletViews.setBackground(Theme.getCurrentTheme().Object_Default_Background);

            p_ioletViews.setLayout(HORIZONTAL_CENTERED);
            p_inletViews.setBackground(Theme.getCurrentTheme().Object_Default_Background);

            p_inletViews.setLayout(VERTICAL_CENTERED);
            p_outletViews.setBackground(Theme.getCurrentTheme().Object_Default_Background);

            p_outletViews.setLayout(VERTICAL_CENTERED);
            p_parameterViews.setBackground(Theme.getCurrentTheme().Object_Default_Background);
            if (getType().getRotatedParams()) {
                p_parameterViews.setLayout(HORIZONTAL_CENTERED);
            } else {
                p_parameterViews.setLayout(VERTICAL_CENTERED);
            }
            p_displayViews.setBackground(Theme.getCurrentTheme().Object_Default_Background);

            if (getType().getRotatedParams()) {
                p_displayViews.setLayout(HORIZONTAL_CENTERED);
            } else {
                p_displayViews.setLayout(VERTICAL_CENTERED);
            }

            for (Inlet inlet : getType().inlets) {
                InletInstance inletInstanceP = null;
                for (InletInstance inletInstance : pInletInstances) {
                    if (inletInstance.GetLabel().equals(inlet.getName())) {
                        inletInstanceP = inletInstance;
                    }
                }
                InletInstance inletInstance = new InletInstance(inlet, this.getObjectInstance());
                if (inletInstanceP != null) {
                    Net n = getPatchModel().GetNet(inletInstanceP);
                    if (n != null) {
                        n.connectInlet(inletInstance);
                    }
                }
                getModel().inletInstances.add(inletInstance);
                PInletInstanceView view = (PInletInstanceView) inletInstance.createView(this);
//            view.setAlignmentX(LEFT_ALIGNMENT);
//                p_inletViews.add(view);
                inletInstanceViews.add(view);
            }
            // disconnect stale inlets from nets
            for (InletInstance inletInstance : pInletInstances) {
                getPatchModel().disconnect(inletInstance);
            }

            for (Outlet o : getType().outlets) {
                OutletInstance outletInstanceP = null;
                for (OutletInstance outletInstance : pOutletInstances) {
                    if (outletInstance.GetLabel().equals(o.getName())) {
                        outletInstanceP = outletInstance;
                    }
                }
                OutletInstance outletInstance = new OutletInstance(o, this.getObjectInstance());
                if (outletInstanceP != null) {
                    Net n = getPatchModel().GetNet(outletInstanceP);
                    if (n != null) {
                        n.connectOutlet(outletInstance);
                    }
                }
                // need a view here
                getModel().outletInstances.add(outletInstance);
                POutletInstanceView view = (POutletInstanceView) outletInstance.createView(this);
                //          view.setAlignmentX(RIGHT_ALIGNMENT);
//                p_outletViews.add(view);
                outletInstanceViews.add(view);
            }
            // disconnect stale outlets from nets
            for (OutletInstance outletInstance : pOutletInstances) {
                getPatchModel().disconnect(outletInstance);
            }

            /*
         if (p_inlets.getComponents().length == 0){
         p_inlets.add(Box.createHorizontalGlue());
         }
         if (p_outlets.getComponents().length == 0){
         p_outlets.add(Box.createHorizontalGlue());
         }*/
            System.out.println(model.getInstanceName());
            System.out.println(p_inletViews.getChildren().size());
            p_ioletViews.add(p_inletViews);
            //    p_ioletViews.add(Box.createHorizontalGlue());
            p_ioletViews.add(p_outletViews);
            add(p_ioletViews);

            for (AxoAttribute p : getType().attributes) {
                AttributeInstance attributeInstanceP = null;
                for (AttributeInstance attributeInstance : pAttributeInstances) {
                    if (attributeInstance.getAttributeName().equals(p.getName())) {
                        attributeInstanceP = attributeInstance;
                    }
                }
//            AttributeInstance attributeInstance1 = p.CreateInstance(this.getObjectInstance(), attributeInstanceP);
//            PAttributeInstanceView attributeInstanceView = (PAttributeInstanceView) attributeInstance1.createView(this);
                //      attributeInstanceView.setAlignmentX(LEFT_ALIGNMENT);
//            add(attributeInstanceView);
//            getModel().attributeInstances.add(attributeInstance1);
            }

            for (Parameter p : getType().params) {
                ParameterInstance pin = p.CreateInstance(this.getObjectInstance());
                for (ParameterInstance pinp : pParameterInstances) {
                    if (pinp.getName().equals(pin.getName())) {
                        pin.CopyValueFrom(pinp);
                    }
                }
//            PParameterInstanceView view = (PParameterInstanceView) pin.createView(this);
//            view.PostConstructor();
//            view.setAlignmentX(RIGHT_ALIGNMENT);
                getModel().parameterInstances.add(pin);
            }

            for (Display p : getType().displays) {
                DisplayInstance pin = p.CreateInstance(this.getObjectInstance());
//            PDisplayInstanceView view = (PDisplayInstanceView) pin.createView(this);
                //          view.setAlignmentX(RIGHT_ALIGNMENT);
//            getModel().displayInstances.add(pin);
            }
//        p_displays.add(Box.createHorizontalGlue());
//        p_params.add(Box.createHorizontalGlue());
//        add(p_parameterViews);
//        add(p_displayViews);
//        p_parameterViews.setAlignmentX(LEFT_ALIGNMENT);
//        p_displayViews.setAlignmentX(LEFT_ALIGNMENT);

            getType().addObjectModifiedListener(this);

            //      synchronized (getTreeLock()) {
//            validateTree();
//        }
            resizeToGrid();
        }
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

    public void addInstanceNameEditor() {
    }

    public void moveToFront() {
    }

    @Override
    public AxoObjectInstance getObjectInstance() {
        return (AxoObjectInstance) model;
    }

    public void repaint() {
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
//        this.p_parameterViews.add((PParameterInstanceView) view);
//        this.parameterInstanceViews.add(view);
    }

    @Override
    public void addAttributeInstanceView(IAttributeInstanceView view) {
//        this.add((PAttributeInstanceView) view);
    }

    @Override
    public void addDisplayInstanceView(IDisplayInstanceView view) {
//        this.p_displayViews.add((PDisplayInstanceView) view);
    }

    @Override
    public void addOutletInstanceView(IOutletInstanceView view) {
        this.p_outletViews.add((POutletInstanceView) view);

    }

    @Override
    public void addInletInstanceView(IInletInstanceView view) {
        this.p_inletViews.add((PInletInstanceView) view);
    }
}
