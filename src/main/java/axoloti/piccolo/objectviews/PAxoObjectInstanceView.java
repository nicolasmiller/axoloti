package axoloti.piccolo.objectviews;

import axoloti.MainFrame;
import axoloti.Net;
import axoloti.PatchViewPiccolo;
import axoloti.PatchViewSwing;
import axoloti.Theme;
import axoloti.attribute.AttributeInstance;
import axoloti.attributedefinition.AxoAttribute;
import axoloti.attributeviews.AttributeInstanceView;
import axoloti.attributeviews.IAttributeInstanceView;
import axoloti.displays.Display;
import axoloti.displays.DisplayInstance;
import axoloti.displayviews.IDisplayInstanceView;
import axoloti.inlets.IInletInstanceView;
import axoloti.inlets.Inlet;
import axoloti.inlets.InletInstance;
import axoloti.object.AxoObject;
import axoloti.object.AxoObjectFromPatch;
import axoloti.object.AxoObjectInstance;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.outlets.IOutletInstanceView;
import axoloti.outlets.Outlet;
import axoloti.outlets.OutletInstance;
import axoloti.parameters.Parameter;
import axoloti.parameters.ParameterInstance;
import axoloti.parameterviews.IParameterInstanceView;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import static axoloti.piccolo.PNodeLayout.VERTICAL_CENTERED;
import axoloti.piccolo.PUtils;
import axoloti.piccolo.PatchPNode;
import axoloti.piccolo.attributeviews.PAttributeInstanceView;
import axoloti.piccolo.displayviews.PDisplayInstanceView;
import axoloti.piccolo.inlets.PInletInstanceView;
import axoloti.piccolo.outlets.POutletInstanceView;
import axoloti.piccolo.parameterviews.PParameterInstanceView;
import components.piccolo.PLabelComponent;
import components.piccolo.PPopupIcon;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class PAxoObjectInstanceView extends PAxoObjectInstanceViewAbstract implements IAxoObjectInstanceView {

    public static final int MIN_HEIGHT = 40;
    public static final int MIN_WIDTH = 80;

    private AxoObjectInstance model;

    PLabelComponent IndexLabel;

    public PatchPNode p_parameterViews;
    public PatchPNode p_displayViews;
    public PatchPNode p_ioletViews;
    public PatchPNode p_inletViews;
    public PatchPNode p_outletViews;
    boolean deferredObjTypeUpdate = false;

    private final ArrayList<IInletInstanceView> inletInstanceViews = new ArrayList<>();
    private final ArrayList<IOutletInstanceView> outletInstanceViews = new ArrayList<>();
    private final ArrayList<IParameterInstanceView> parameterInstanceViews = new ArrayList<>();

    public PAxoObjectInstanceView(AxoObjectInstance model, PatchViewPiccolo patchView) {
        super(model, patchView);
        this.model = model;
    }

    public AxoObject getType() {
        return model.getType();
    }

    public static final int PADDING = 1;

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        setPaint(Theme.getCurrentTheme().Object_Default_Background);
        model.updateObj1();

        setLocation(model.getX(), model.getY());
        setLayout(VERTICAL_CENTERED);
        setDrawBorder(true);

        p_parameterViews = new PatchPNode(patchView);
        p_parameterViews.setPickable(false);
        p_parameterViews.setLayout(VERTICAL_CENTERED);
        p_displayViews = new PatchPNode(patchView);
        p_displayViews.setPickable(false);
        p_displayViews.setLayout(VERTICAL_CENTERED);
        p_ioletViews = new PatchPNode(patchView);
        p_ioletViews.setLayout(HORIZONTAL_CENTERED);
        p_ioletViews.setPickable(false);
        p_inletViews = new PatchPNode(patchView);
        p_inletViews.setPickable(false);
        p_inletViews.setLayout(VERTICAL_CENTERED);
        p_outletViews = new PatchPNode(patchView);
        p_outletViews.setLayout(VERTICAL_CENTERED);
        p_outletViews.setPickable(false);

        ArrayList<ParameterInstance> pParameterInstances = getModel().getParameterInstances();
        ArrayList<AttributeInstance> pAttributeInstances = getModel().getAttributeInstances();
        ArrayList<InletInstance> pInletInstances = getModel().getInletInstances();
        ArrayList<OutletInstance> pOutletInstances = getModel().getOutletInstances();

        getModel().setParameterInstances(new ArrayList<>());
        getModel().setAttributeInstances(new ArrayList<>());
        getModel().setDisplayInstances(new ArrayList<>());
        getModel().setInletInstances(new ArrayList<>());
        getModel().setOutletInstances(new ArrayList<>());

        final PPopupIcon popupIcon = new PPopupIcon(this);

        popupIcon.setPopupIconListener(new PPopupIcon.PopupIconListener() {
            @Override
            public void ShowPopup(PInputEvent e) {
                JPopupMenu popup = CreatePopupMenu();
                Point popupLocation = PUtils.getPopupLocation(e);
                popup.show(getCanvas(), popupLocation.x, popupLocation.y);
            }
        });
        titleBar.addChild(popupIcon);
        titleBar.addChild(new PLabelComponent(model.typeName));

        titleBar.setBounds(0, 0, titleBar.getChildrenWidth(), titleBar.getChildrenHeight());

        addChild(titleBar);
        InstanceLabel = new PLabelComponent(model.getInstanceName());
        addChild(InstanceLabel);

        ////TODO
////        String tooltiptxt = "<html>";
////        if ((getType().sDescription != null) && (!getType().sDescription.isEmpty())) {
////            tooltiptxt += getType().sDescription;
////        }
////        if ((getType().sAuthor != null) && (!getType().sAuthor.isEmpty())) {
////            tooltiptxt += "<p>Author: " + getType().sAuthor;
////        }
////        if ((getType().sLicense != null) && (!getType().sLicense.isEmpty())) {
////            tooltiptxt += "<p>License: " + getType().sLicense;
////        }
////        if ((getType().sPath != null) && (!getType().sPath.isEmpty())) {
////            tooltiptxt += "<p>Path: " + getType().sPath;
////        }
////        Titlebar.setToolTipText(tooltiptxt);
        InstanceLabel.setPickable(true);
        InstanceLabel.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent e) {
                if (e.getClickCount() == 2) {
                    addInstanceNameEditor();
                    e.setHandled(true);
                }
            }
        });

        p_parameterViews = new PatchPNode(patchView);
        p_parameterViews.setPickable(false);
        if (getType().getRotatedParams()) {
            p_parameterViews.setLayout(HORIZONTAL_CENTERED);
        } else {
            p_parameterViews.setLayout(VERTICAL_CENTERED);
        }

        p_displayViews = new PatchPNode(patchView);
        p_displayViews.setPickable(false);
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
            getModel().getInletInstances().add(inletInstance);
            PInletInstanceView view = (PInletInstanceView) inletInstance.createView(this);
            inletInstanceViews.add(view);
        }

        // disconnect stale inlets from nets
        for (InletInstance inletInstance : pInletInstances) {
            getPatchModel().disconnect(inletInstance, false);
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
            getModel().getOutletInstances().add(outletInstance);
            POutletInstanceView view = (POutletInstanceView) outletInstance.createView(this);
            outletInstanceViews.add(view);
        }

        // disconnect stale outlets from nets
        for (OutletInstance outletInstance : pOutletInstances) {
            getPatchModel().disconnect(outletInstance, false);
        }

        p_ioletViews.addChild(p_inletViews);
        p_ioletViews.addChild(p_outletViews);
        addChild(p_ioletViews);

        for (AxoAttribute p : getType().attributes) {
            AttributeInstance attributeInstanceP = null;
            for (AttributeInstance attributeInstance : pAttributeInstances) {
                if (attributeInstance.getAttributeName().equals(p.getName())) {
                    attributeInstanceP = attributeInstance;
                }
            }
            AttributeInstance attributeInstance1 = p.CreateInstance(this.getObjectInstance(), attributeInstanceP);
            PAttributeInstanceView attributeInstanceView = (PAttributeInstanceView) attributeInstance1.createView(this);
            //      attributeInstanceView.setAlignmentX(LEFT_ALIGNMENT);
            addChild(attributeInstanceView);
            getModel().getAttributeInstances().add(attributeInstance1);
        }

        for (Parameter p : getType().params) {
            ParameterInstance pin = p.CreateInstance(this.getObjectInstance());
            for (ParameterInstance pinp : pParameterInstances) {
                if (pinp.getName().equals(pin.getName())) {
                    pin.CopyValueFrom(pinp);
                }
            }
            PParameterInstanceView view = (PParameterInstanceView) pin.createView(this);
            getModel().getParameterInstances().add(pin);
        }

        for (Display p : getType().displays) {
            DisplayInstance pin = p.CreateInstance(this.getObjectInstance());
            PDisplayInstanceView view = (PDisplayInstanceView) pin.createView(this);
            getModel().getDisplayInstances().add(pin);
        }

        addChild(p_parameterViews);
        addChild(p_displayViews);

        getType().addObjectModifiedListener(model);

        resizeToGrid();

        p_inletViews.setBounds(
                0, 0, p_inletViews.getChildrenWidth(), p_inletViews.getChildrenHeight());
        p_outletViews.setBounds(
                0, 0, p_outletViews.getChildrenWidth(), p_outletViews.getChildrenHeight());
        p_ioletViews.setBounds(
                0, 0, p_ioletViews.getChildrenWidth(), p_ioletViews.getChildrenHeight());
        p_displayViews.setBounds(
                0, 0, p_displayViews.getChildrenWidth(), p_displayViews.getChildrenHeight());
        p_parameterViews.setBounds(
                0, 0, p_parameterViews.getChildrenWidth(), p_parameterViews.getChildrenHeight());

        this.setBounds(
                0, 0, getChildrenWidth(), getChildrenHeight());
        translate(model.getX(), model.getY());
    }

    @Override
    JPopupMenu CreatePopupMenu() {
        JPopupMenu popup = super.CreatePopupMenu();
        JMenuItem popm_edit = new JMenuItem("edit object definition");
        popm_edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                OpenEditor();
            }
        });
        popup.add(popm_edit);
        JMenuItem popm_editInstanceName = new JMenuItem("edit instance name");
        popm_editInstanceName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addInstanceNameEditor();
            }
        });
        popup.add(popm_editInstanceName);
        JMenuItem popm_substitute = new JMenuItem("replace");
        popm_substitute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ((PatchViewPiccolo) getPatchView()).ShowClassSelector(PAxoObjectInstanceView.this.getLocation(), null, PAxoObjectInstanceView.this, null);
            }
        });
        popup.add(popm_substitute);
        if (getType().GetHelpPatchFile() != null) {
            JMenuItem popm_help = new JMenuItem("help");
            popm_help.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    PatchViewSwing.OpenPatch(getType().GetHelpPatchFile());
                }
            });
            popup.add(popm_help);
        }
        if (MainFrame.prefs.getExpertMode()) {
            JMenuItem popm_adapt = new JMenuItem("adapt homonym");
            popm_adapt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    model.PromoteToOverloadedObj();
                }
            });
            popup.add(popm_adapt);
        }

        if (model.getType() instanceof AxoObjectFromPatch) {
            JMenuItem popm_embed = new JMenuItem("embed as patch/patcher");
            popm_embed.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (!getPatchView().isLocked()) {
                        model.ConvertToPatchPatcher();
                    }
                }
            });
            popup.add(popm_embed);
        } else if (!(this instanceof PAxoObjectInstanceViewPatcherObject)) {
            JMenuItem popm_embed = new JMenuItem("embed as patch/object");
            popm_embed.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (!getPatchView().isLocked()) {
                        model.ConvertToEmbeddedObj();
                    }
                }
            });
            popup.add(popm_embed);
        }
        return popup;
    }

    public void refreshIndex() {
        if (getPatchView() != null && IndexLabel != null) {
            IndexLabel.setText(" " + this.getPatchView().getObjectInstanceViews().indexOf(this));
        }
    }

    public void OpenEditor() {
        getType().OpenEditor(model.editorBounds, model.editorActiveTabIndex);
    }

    public ArrayList<AttributeInstanceView> attributeInstanceViews = new ArrayList<AttributeInstanceView>();

    @Override
    public void Lock() {
        super.Lock();
        for (AttributeInstanceView a : attributeInstanceViews) {
            a.Lock();
        }
    }

    @Override
    public void Unlock() {
        super.Unlock();
        for (AttributeInstanceView a : attributeInstanceViews) {
            a.UnLock();
        }
        if (deferredObjTypeUpdate) {
            model.updateObj();
            deferredObjTypeUpdate = false;
        }
    }

    @Override
    public AxoObjectInstance getObjectInstance() {
        return this.model;
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
    public void addParameterInstanceView(IParameterInstanceView view) {
        this.p_parameterViews.addChild((PParameterInstanceView) view);
        this.parameterInstanceViews.add(view);
    }

    @Override
    public void addAttributeInstanceView(IAttributeInstanceView view) {
        this.addChild((PAttributeInstanceView) view);
    }

    @Override
    public void addDisplayInstanceView(IDisplayInstanceView view) {
        this.p_displayViews.addChild((PDisplayInstanceView) view);
    }

    @Override
    public void addOutletInstanceView(IOutletInstanceView view) {
        this.p_outletViews.addChild((POutletInstanceView) view);
    }

    @Override
    public void addInletInstanceView(IInletInstanceView view) {
        this.p_inletViews.addChild((PInletInstanceView) view);
    }
}
