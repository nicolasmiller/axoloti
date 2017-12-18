package axoloti.piccolo.objectviews;

import static java.awt.Component.LEFT_ALIGNMENT;
import static java.awt.Component.RIGHT_ALIGNMENT;
import static java.awt.Component.TOP_ALIGNMENT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

import axoloti.abstractui.IAttributeInstanceView;
import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.abstractui.IDisplayInstanceView;
import axoloti.abstractui.IInletInstanceView;
import axoloti.abstractui.IOutletInstanceView;
import axoloti.abstractui.IParameterInstanceView;
import axoloti.mvc.AbstractController;
import axoloti.mvc.array.ArrayView;
import axoloti.object.AxoObjectFromPatch;
import axoloti.object.IAxoObject;
import axoloti.patch.PatchViewPiccolo;
import axoloti.patch.object.AxoObjectInstance;
import axoloti.patch.object.ObjectInstanceController;
import axoloti.patch.object.attribute.AttributeInstanceController;
import axoloti.patch.object.display.DisplayInstanceController;
import axoloti.patch.object.inlet.InletInstance;
import axoloti.patch.object.inlet.InletInstanceController;
import axoloti.patch.object.outlet.OutletInstance;
import axoloti.patch.object.outlet.OutletInstanceController;
import axoloti.patch.object.parameter.ParameterInstanceController;
import axoloti.piccolo.PatchPNode;
import axoloti.piccolo.attributeviews.PAttributeInstanceView;
import axoloti.piccolo.attributeviews.PAttributeInstanceViewFactory;
import axoloti.piccolo.components.PLabelComponent;
import axoloti.piccolo.displayviews.PDisplayInstanceView;
import axoloti.piccolo.displayviews.PDisplayInstanceViewFactory;
import axoloti.piccolo.inlets.PInletInstanceView;
import axoloti.piccolo.inlets.PInletInstanceViewFactory;
import axoloti.piccolo.outlets.POutletInstanceView;
import axoloti.piccolo.outlets.POutletInstanceViewFactory;
import axoloti.piccolo.parameterviews.PParameterInstanceView;
import axoloti.piccolo.parameterviews.PParameterInstanceViewFactory;
import axoloti.preferences.Preferences;
import axoloti.preferences.Theme;
import axoloti.swingui.patch.PatchViewSwing;

public class PAxoObjectInstanceView extends PAxoObjectInstanceViewAbstract implements IAxoObjectInstanceView {

    public static final int MIN_HEIGHT = 40;
    public static final int MIN_WIDTH = 80;

    PLabelComponent IndexLabel;

    public PatchPNode p_attributeViews;
    public PatchPNode p_parameterViews;
    public PatchPNode p_displayViews;
    public PatchPNode p_ioletViews;
    public PatchPNode p_inletViews;
    public PatchPNode p_outletViews;
    boolean deferredObjTypeUpdate = false;

    //private ArrayView<IInletInstanceView> inletInstanceViews;
    //private ArrayView<IOutletInstanceView> outletInstanceViews;
    //private ArrayView<IParameterInstanceView> parameterInstanceViews;

    String tooltipText = "<html>";

    public PAxoObjectInstanceView(ObjectInstanceController controller, PatchViewPiccolo patchView) {
        super(controller, patchView);
    }

    @Override
    public AxoObjectInstance getModel() {
        return (AxoObjectInstance) super.getModel();
    }

    public IAxoObject getType() {
        return getModel().getType();
    }

    List<IInletInstanceView> inletInstanceViews;
    List<IOutletInstanceView> outletInstanceViews;
    List<IAttributeInstanceView> attributeInstanceViews;
    List<IParameterInstanceView> parameterInstanceViews;
    List<IDisplayInstanceView> displayInstanceViews;

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        setPaint(Theme.getCurrentTheme().Object_Default_Background);
        //model.updateObj1();

        setLocation(getModel().getX(), getModel().getY());
        setDrawBorder(true);

        p_parameterViews = new PatchPNode(patchView);
        p_parameterViews.setLayout(new BoxLayout(p_parameterViews.getProxyComponent(),
                getType().getRotatedParams() ? BoxLayout.LINE_AXIS : BoxLayout.PAGE_AXIS));
        p_parameterViews.setPickable(false);
        p_displayViews = new PatchPNode(patchView);
        p_displayViews.setLayout(new BoxLayout(p_displayViews.getProxyComponent(),
                getType().getRotatedParams() ? BoxLayout.LINE_AXIS : BoxLayout.PAGE_AXIS));
        p_displayViews.setPickable(false);
        p_ioletViews = new PatchPNode(patchView);
        p_ioletViews.setLayout(new BoxLayout(p_ioletViews.getProxyComponent(), BoxLayout.LINE_AXIS));
        p_ioletViews.setAlignmentX(LEFT_ALIGNMENT);
        p_ioletViews.setAlignmentY(TOP_ALIGNMENT);
        p_ioletViews.setPickable(false);

        p_inletViews = new PatchPNode(patchView);
        p_inletViews.setLayout(new BoxLayout(
                p_inletViews.getProxyComponent(),
                BoxLayout.PAGE_AXIS));
        p_inletViews.setAlignmentX(LEFT_ALIGNMENT);
        p_inletViews.setAlignmentY(TOP_ALIGNMENT);
        p_inletViews.setPickable(false);

	p_attributeViews = new PatchPNode(patchView);
        p_attributeViews.setLayout(new BoxLayout(
				   p_attributeViews.getProxyComponent(),
				   BoxLayout.PAGE_AXIS));
        p_attributeViews.setAlignmentX(LEFT_ALIGNMENT);
        p_attributeViews.setPickable(false);

        p_outletViews = new PatchPNode(patchView);
        p_outletViews.setLayout(new BoxLayout(p_outletViews.getProxyComponent(), BoxLayout.PAGE_AXIS));
        p_outletViews.setPickable(false);
        p_outletViews.setAlignmentX(RIGHT_ALIGNMENT);
        p_outletViews.setAlignmentY(TOP_ALIGNMENT);

        //ArrayModel<ParameterInstance> pParameterInstances = getModel().getParameterInstances();
        //ArrayModel<AttributeInstance> pAttributeInstances = getModel().getAttributeInstances();
        Collection<InletInstance> pInletInstances = getModel().getInletInstances();
        Collection<OutletInstance> pOutletInstances = getModel().getOutletInstances();

//        getModel().setParameterInstances(new ArrayModel<>());
//        getModel().setAttributeInstances(new ArrayModel<>());
//        getModel().setDisplayInstances(new ArrayModel<>());
//        getModel().setInletInstances(new ArrayModel<>());
//        getModel().setOutletInstances(new ArrayModel<>());

        setLayout(new BoxLayout(getProxyComponent(), BoxLayout.PAGE_AXIS));

        titleBar.addChild(popupIcon);

        PLabelComponent titleBarLabel = new PLabelComponent(getModel().getTypeName());
        titleBarLabel.setAlignmentX(LEFT_ALIGNMENT);
        titleBarLabel.setPickable(false);

        titleBar.addChild(titleBarLabel);
        titleBar.setAlignmentX(LEFT_ALIGNMENT);
        titleBar.setMinimumSize(TITLEBAR_MINIMUM_SIZE);
        titleBar.setMaximumSize(TITLEBAR_MAXIMUM_SIZE);
        titleBar.setPickable(false);

        addChild(titleBar);

        instanceLabel = new PLabelComponent(getModel().getInstanceName());
        instanceLabel.setAlignmentX(LEFT_ALIGNMENT);

        addChild(instanceLabel);

        initializeTooltipText();

        instanceLabel.setPickable(true);
        instanceLabel.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent e) {
                if (e.getClickCount() == 2) {
                    addInstanceNameEditor();
                    e.setHandled(true);
                }
            }
        });

        p_parameterViews.setPickable(false);
        p_parameterViews.setAlignmentX(LEFT_ALIGNMENT);

        p_displayViews.setPickable(false);
        p_displayViews.setAlignmentX(LEFT_ALIGNMENT);

        p_displayViews.addToSwingProxy(Box.createHorizontalGlue());
        p_parameterViews.addToSwingProxy(Box.createHorizontalGlue());

//        for (Inlet inlet : getType().getInlets()) {
//            InletInstance inletInstanceP = null;
//            for (InletInstance inletInstance : pInletInstances) {
//                if (inletInstance.GetLabel().equals(inlet.getName())) {
//                    inletInstanceP = inletInstance;
//                }
//            }
//            InletInstance inletInstance = new InletInstance(inlet, getModel());
//            if (inletInstanceP != null) {
//                Net n = getPatchModel().GetNet(inletInstanceP);
//                if (n != null) {
//                    n.connectInlet(inletInstance);
//                }
//            }
//            getModel().getInletInstances().add(inletInstance);
            // TODO: PICCOLO view factory
            PInletInstanceView view = null; // (PInletInstanceView) inletInstance.createView(this);
//            view.setAlignmentX(LEFT_ALIGNMENT);
            //inletInstanceViews.add(view);
//        }

        // disconnect stale inlets from nets
//        for (InletInstance inletInstance : pInletInstances) {
//            getPatchModel().disconnect(inletInstance);
//        }

//        for (Outlet o : getType().getOutlets()) {
//            OutletInstance outletInstanceP = null;
//            for (OutletInstance outletInstance : pOutletInstances) {
//                if (outletInstance.GetLabel().equals(o.getName())) {
//                    outletInstanceP = outletInstance;
//                }
//            }
//            OutletInstance outletInstance = new OutletInstance(o, getModel());
//            if (outletInstanceP != null) {
//                Net n = getPatchModel().GetNet(outletInstanceP);
//                if (n != null) {
//                    n.connectOutlet(outletInstance);
//                }
//            }
//            getModel().getOutletInstances().add(outletInstance);
//            POutletInstanceView view = null;
            // TODO: PICCOLO view factory
            // ... = (POutletInstanceView) outletInstance.createView(this);
//            view.setAlignmentX(RIGHT_ALIGNMENT);
            //outletInstanceViews.add(view);
//        }

        // disconnect stale outlets from nets
//        for (OutletInstance outletInstance : pOutletInstances) {
//            getPatchModel().disconnect(outletInstance);
//        }


/*
        for (AxoAttribute p : getType().attributes) {
            AttributeInstance attributeInstanceP = null;
            for (AttributeInstance attributeInstance : pAttributeInstances) {
                if (attributeInstance.getName().equals(p.getName())) {
                    attributeInstanceP = attributeInstance;
                }
            }
//            AttributeInstance attributeInstance1 = p.CreateInstance(getModel(), attributeInstanceP);
            PAttributeInstanceView attributeInstanceView = null;
            // TODO: implement PICCOLO view factory
            // ... = (PAttributeInstanceView) attributeInstance1.createView(this);
            attributeInstanceView.setAlignmentX(LEFT_ALIGNMENT);
            addChild(attributeInstanceView);
//            getModel().getAttributeInstances().add(attributeInstance1);
        }

        for (Parameter p : getType().params) {
            ParameterInstance pin = p.CreateInstance(getModel());
            for (ParameterInstance pinp : pParameterInstances) {
                if (pinp.getName().equals(pin.getName())) {
                    pin.CopyValueFrom(pinp);
                }
            }
            PParameterInstanceView view = null;
            // TODO: implement PICCOLO view factory
            // ... = (PParameterInstanceView) pin.createView(this);
            view.setAlignmentX(RIGHT_ALIGNMENT);
            getModel().getParameterInstances().add(pin);
        }
*/
//        for (Display p : getType().displays) {
//            DisplayInstance pin = p.CreateInstance(getModel());
//            PDisplayInstanceView view = null;
            // TODO: implement PICCOLO view factory
            // ... = (PDisplayInstanceView) pin.createView(this);
//            view.setAlignmentX(RIGHT_ALIGNMENT);
//            getModel().getDisplayInstances().add(pin);
//        }

	p_ioletViews.addChild(p_inletViews);
        p_ioletViews.addToSwingProxy(Box.createHorizontalGlue());
        p_ioletViews.addChild(p_outletViews);
        addChild(p_ioletViews);
	addChild(p_attributeViews);
        addChild(p_parameterViews);
        addChild(p_displayViews);
        addToSwingProxy(Box.createVerticalGlue());

        addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mousePressed(PInputEvent e) {
                if (e.isPopupTrigger() && !overPickableChild(e)) {
                    ShowPopup(e);
                }
            }

            @Override
            public void mouseClicked(PInputEvent e) {
                if (e.getPickedNode() instanceof PAxoObjectInstanceView) {
                    showReplaceClassSelector(e);
                }
            }

            @Override
            public void mouseEntered(PInputEvent e) {
                if (e.getInputManager().getMouseFocus() == null && !overPickableChild(e)) {
                    getCanvas().setToolTipText(tooltipText);
                }
            }

            @Override
            public void mouseExited(PInputEvent e) {
                if (e.getInputManager().getMouseFocus() == null) {
                    getCanvas().setToolTipText(null);
                }
            }
        });

        validate();
        resizeToGrid();
        translate(getModel().getX(), getModel().getY());
    }

    private void showReplaceClassSelector(PInputEvent e) {
        if (e.isLeftMouseButton() && e.getClickCount() == 2) {
            ((PatchViewPiccolo) getPatchView()).ShowClassSelector(PAxoObjectInstanceView.this.getLocation(), null, PAxoObjectInstanceView.this, null);
        }
    }

    private void initializeTooltipText() {
        if ((getType().getDescription() != null) && (!getType().getDescription().isEmpty())) {
            tooltipText += getType().getDescription();
        }
        if ((getType().getAuthor() != null) && (!getType().getAuthor().isEmpty())) {
            tooltipText += "<p>Author: " + getType().getAuthor();
        }
        if ((getType().getLicense() != null) && (!getType().getLicense().isEmpty())) {
            tooltipText += "<p>License: " + getType().getLicense();
        }
        if ((getType().getPath() != null) && (!getType().getPath().isEmpty())) {
            tooltipText += "<p>Path: " + getType().getPath();
        }
    }

    ArrayView<IInletInstanceView> inletInstanceViewSync = new ArrayView<IInletInstanceView>() {
	    @Override
	    public IInletInstanceView viewFactory(AbstractController ctrl) {
		return PInletInstanceViewFactory.createView((InletInstanceController) ctrl, PAxoObjectInstanceView.this);
	    }

	    @Override
	    public void updateUI(List<IInletInstanceView> views) {
		p_inletViews.removeAllChildren();
		for (IInletInstanceView c : views) {
		    p_inletViews.addChild((PatchPNode) c);
		}
		resizeToGrid();
	    }

	    @Override
	    public void removeView(IInletInstanceView view) {
	    }
	};

    ArrayView<IOutletInstanceView> outletInstanceViewSync = new ArrayView<IOutletInstanceView>() {
	    @Override
	    public IOutletInstanceView viewFactory(AbstractController ctrl) {
		return POutletInstanceViewFactory.createView((OutletInstanceController) ctrl, PAxoObjectInstanceView.this);
	    }

	    @Override
	    public void updateUI(List<IOutletInstanceView> views) {
		p_outletViews.removeAllChildren();
		for (IOutletInstanceView c : views) {
		    p_outletViews.addChild((PatchPNode) c);
		}
		resizeToGrid();
	    }

	    @Override
	    public void removeView(IOutletInstanceView view) {
	    }
	};

    ArrayView<IAttributeInstanceView> attributeInstanceViewSync = new ArrayView<IAttributeInstanceView>() {
            @Override
            public IAttributeInstanceView viewFactory(AbstractController ctrl) {
                return PAttributeInstanceViewFactory.createView((AttributeInstanceController) ctrl, PAxoObjectInstanceView.this);
            }

            @Override
            public void updateUI(List<IAttributeInstanceView> views) {
                p_attributeViews.removeAllChildren();
                for (IAttributeInstanceView c : views) {
                    p_attributeViews.addChild((PatchPNode) c);
                }
                resizeToGrid();
            }

            @Override
            public void removeView(IAttributeInstanceView view) {
            }
        };

    ArrayView<IParameterInstanceView> parameterInstanceViewSync = new ArrayView<IParameterInstanceView>() {
            @Override
            public IParameterInstanceView viewFactory(AbstractController ctrl) {
                return PParameterInstanceViewFactory.createView((ParameterInstanceController) ctrl, PAxoObjectInstanceView.this);
            }

            @Override
            public void updateUI(List<IParameterInstanceView> views) {
                p_parameterViews.removeAllChildren();
                for (IParameterInstanceView c : views) {
                    p_parameterViews.addChild((PatchPNode) c);
                }
                resizeToGrid();
            }

            @Override
            public void removeView(IParameterInstanceView view) {
            }
        };

    ArrayView<IDisplayInstanceView> displayInstanceViewSync = new ArrayView<IDisplayInstanceView>() {
            @Override
            public IDisplayInstanceView viewFactory(AbstractController ctrl) {
                return PDisplayInstanceViewFactory.createView((DisplayInstanceController) ctrl, PAxoObjectInstanceView.this);
            }

            @Override
            public void updateUI(List<IDisplayInstanceView> views) {
                p_displayViews.removeAllChildren();
                for (IDisplayInstanceView c : views) {
                    p_displayViews.addChild((PatchPNode) c);
                }
                resizeToGrid();
            }

            @Override
            public void removeView(IDisplayInstanceView view) {
            }
        };

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (AxoObjectInstance.OBJ_INLET_INSTANCES.is(evt)) {
            inletInstanceViews = inletInstanceViewSync.Sync(inletInstanceViews, getController().inletInstanceControllers);
        } else if (AxoObjectInstance.OBJ_OUTLET_INSTANCES.is(evt)) {
            outletInstanceViews = outletInstanceViewSync.Sync(outletInstanceViews, getController().outletInstanceControllers);
        } else if (AxoObjectInstance.OBJ_ATTRIBUTE_INSTANCES.is(evt)) {
            attributeInstanceViews = attributeInstanceViewSync.Sync(attributeInstanceViews, getController().attributeInstanceControllers);
        } else if (AxoObjectInstance.OBJ_PARAMETER_INSTANCES.is(evt)) {
            parameterInstanceViews = parameterInstanceViewSync.Sync(parameterInstanceViews, getController().parameterInstanceControllers);
        } else if (AxoObjectInstance.OBJ_DISPLAY_INSTANCES.is(evt)) {
            displayInstanceViews = displayInstanceViewSync.Sync(displayInstanceViews, getController().displayInstanceControllers);
        }
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
        if (Preferences.getPreferences().getExpertMode()) {
            JMenuItem popm_adapt = new JMenuItem("adapt homonym");
            popm_adapt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    getController().getParent().PromoteToOverloadedObj(getModel());
                }
            });
            popup.add(popm_adapt);
        }

        if (getModel().getType() instanceof AxoObjectFromPatch) {
            JMenuItem popm_embed = new JMenuItem("embed as patch/patcher");
            popm_embed.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (!getPatchView().isLocked()) {
                        //model.ConvertToPatchPatcher();
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
                        //getController().ConvertToEmbeddedObj();
                    }
                }
            });
            popup.add(popm_embed);
        }
        return popup;
    }

    public void refreshIndex() {
        if (getPatchView() != null && IndexLabel != null) {
//            IndexLabel.setText(" " + getPatchView().getObjectInstanceViews().getSubViews().indexOf(this));
        }
    }

    public void OpenEditor() {
	getType().OpenEditor(getModel().editorBounds, getModel().editorActiveTabIndex);
    }

    @Override
    public void Lock() {
        super.Lock();
        for (IAttributeInstanceView a : attributeInstanceViews) {
            a.Lock();
        }
    }

    @Override
    public void Unlock() {
        super.Unlock();
        for (IAttributeInstanceView a : attributeInstanceViews) {
            a.UnLock();
        }
        if (deferredObjTypeUpdate) {
            //model.updateObj();
            deferredObjTypeUpdate = false;
        }
    }

    @Override
    public List<IInletInstanceView> getInletInstanceViews() {
        return inletInstanceViews;
    }

    @Override
    public List<IOutletInstanceView> getOutletInstanceViews() {
        return outletInstanceViews;
    }

    @Override
    public List<IParameterInstanceView> getParameterInstanceViews() {
        return parameterInstanceViews;
    }

    @Override
    public void addParameterInstanceView(IParameterInstanceView view) {
        p_parameterViews.addChild((PParameterInstanceView) view);
    }

    @Override
    public void addAttributeInstanceView(IAttributeInstanceView view) {
        addChild((PAttributeInstanceView) view);
    }

    @Override
    public void addDisplayInstanceView(IDisplayInstanceView view) {
        p_displayViews.addChild((PDisplayInstanceView) view);
    }

    @Override
    public void addOutletInstanceView(IOutletInstanceView view) {
        p_outletViews.addChild((POutletInstanceView) view);
    }

    @Override
    public void addInletInstanceView(IInletInstanceView view) {
        p_inletViews.addChild((PInletInstanceView) view);
    }
}
