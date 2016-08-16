/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.objectviews;

import axoloti.MainFrame;
import axoloti.Net;
import axoloti.PatchView;
import axoloti.Theme;
import axoloti.attribute.AttributeInstance;
import axoloti.attributeviews.AttributeInstanceView;
import axoloti.attributedefinition.AxoAttribute;
import axoloti.displays.Display;
import axoloti.displays.DisplayInstance;
import axoloti.displayviews.DisplayInstanceView;
import axoloti.inlets.Inlet;
import axoloti.inlets.InletInstance;
import axoloti.inlets.InletInstanceView;
import axoloti.object.AxoObject;
import axoloti.object.AxoObjectFromPatch;
import axoloti.object.AxoObjectInstance;
import axoloti.object.AxoObjectInstanceAbstract;
import axoloti.object.ObjectModifiedListener;
import axoloti.outlets.Outlet;
import axoloti.outlets.OutletInstance;
import axoloti.outlets.OutletInstanceView;
import axoloti.parameters.Parameter;
import axoloti.parameters.ParameterInstance;
import axoloti.parameterviews.ParameterInstanceView;
import components.LabelComponent;
import components.PopupIcon;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author nicolas
 */
public class AxoObjectInstanceView extends AxoObjectInstanceViewAbstract implements ObjectModifiedListener {

    private AxoObjectInstance model;
    LabelComponent IndexLabel;
    public JPanel p_parameterViews;
    public JPanel p_displayViews;
    public JPanel p_inletViews;
    public JPanel p_outletViews;
    boolean deferredObjTypeUpdate = false;

    public AxoObjectInstanceView(AxoObjectInstanceAbstract model) {
        super(model);
        this.model = (AxoObjectInstance) model;
    }

    public AxoObject getType() {
        return model.getType();
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        model.updateObj1();

        ArrayList<ParameterInstanceView> pParameterInstanceViews = new ArrayList<ParameterInstanceView>();
        for (ParameterInstance parameterInstance : model.parameterInstances) {
            pParameterInstanceViews.add(parameterInstance.CreateView(this));
        }

        ArrayList<AttributeInstanceView> pAttributeInstanceViews = new ArrayList<AttributeInstanceView>();
        for (AttributeInstance attributeInstance : model.attributeInstances) {
            pAttributeInstanceViews.add(attributeInstance.CreateView(this));
        }

        ArrayList<InletInstanceView> pInletInstanceViews = new ArrayList<InletInstanceView>();
        for (InletInstance inletInstance : model.inletInstances) {
            pInletInstanceViews.add(inletInstance.CreateView(this));
        }

        ArrayList<OutletInstanceView> pOutletInstanceViews = new ArrayList<OutletInstanceView>();
        for (OutletInstance outletInstance : model.outletInstances) {
            pOutletInstanceViews.add(outletInstance.CreateView(this));
        }

        model.parameterInstances = new ArrayList<ParameterInstance>();
        model.attributeInstances = new ArrayList<AttributeInstance>();
        model.displayInstances = new ArrayList<DisplayInstance>();
        model.inletInstances = new ArrayList<InletInstance>();
        model.outletInstances = new ArrayList<OutletInstance>();

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        final PopupIcon popupIcon = new PopupIcon();
        popupIcon.setPopupIconListener(new PopupIcon.PopupIconListener() {
            @Override
            public void ShowPopup() {
                if (popup.getParent() == null) {
                    popupIcon.add(popup);
                }
                popup.show(popupIcon,
                        0, popupIcon.getHeight());
            }
        });
        Titlebar.add(popupIcon);

        LabelComponent idlbl = new LabelComponent(model.typeName);
        idlbl.setAlignmentX(LEFT_ALIGNMENT);
        idlbl.setForeground(Theme.getCurrentTheme().Object_TitleBar_Foreground);
        Titlebar.add(idlbl);

        String tooltiptxt = "<html>";
        if ((getType().sDescription != null) && (!getType().sDescription.isEmpty())) {
            tooltiptxt += getType().sDescription;
        }
        if ((getType().sAuthor != null) && (!getType().sAuthor.isEmpty())) {
            tooltiptxt += "<p>Author: " + getType().sAuthor;
        }
        if ((getType().sLicense != null) && (!getType().sLicense.isEmpty())) {
            tooltiptxt += "<p>License: " + getType().sLicense;
        }
        if ((getType().sPath != null) && (!getType().sPath.isEmpty())) {
            tooltiptxt += "<p>Path: " + getType().sPath;
        }
        Titlebar.setToolTipText(tooltiptxt);
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
                AxoObjectInstanceView.this.getPatchView().ShowClassSelector(AxoObjectInstanceView.this.getLocation(), AxoObjectInstanceView.this, null);
            }
        });
        popup.add(popm_substitute);
        if (getType().GetHelpPatchFile() != null) {
            JMenuItem popm_help = new JMenuItem("help");
            popm_help.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    PatchView.OpenPatch(getType().GetHelpPatchFile());
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
                        AxoObjectInstanceView.this.updateObj();
                    }
                }
            });
            popup.add(popm_embed);
        } else if (!(this instanceof AxoObjectInstanceViewPatcherObject)) {
            JMenuItem popm_embed = new JMenuItem("embed as patch/object");
            popm_embed.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (!getPatchView().isLocked()) {
                        model.ConvertToEmbeddedObj();
                        AxoObjectInstanceView.this.updateObj();
                    }
                }
            });
            popup.add(popm_embed);
        }

        /*
         h.add(Box.createHorizontalStrut(3));
         h.add(Box.createHorizontalGlue());
         h.add(new JSeparator(SwingConstants.VERTICAL));*/
//        IndexLabel.setSize(IndexLabel.getMinimumSize());
        IndexLabel = new LabelComponent("");
        model.refreshIndex();
        //h.add(IndexLabel);
        //IndexLabel.setAlignmentX(RIGHT_ALIGNMENT);
        Titlebar.setAlignmentX(LEFT_ALIGNMENT);
        add(Titlebar);
        Titlebar.doLayout();
        InstanceLabel = new LabelComponent(model.getInstanceName());
        InstanceLabel.setAlignmentX(LEFT_ALIGNMENT);
        InstanceLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addInstanceNameEditor();
                    e.consume();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                AxoObjectInstanceView.this.handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                AxoObjectInstanceView.this.handleMouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        InstanceLabel.addMouseMotionListener(mml);
        add(InstanceLabel);

        JPanel p_iolets = new JPanel();
        p_iolets.setBackground(Theme.getCurrentTheme().Object_Default_Background);

        p_iolets.setLayout(new BoxLayout(p_iolets, BoxLayout.LINE_AXIS));
        p_iolets.setAlignmentX(LEFT_ALIGNMENT);
        p_iolets.setAlignmentY(TOP_ALIGNMENT);
        p_inletViews = new JPanel();
        p_inletViews.setBackground(Theme.getCurrentTheme().Object_Default_Background);

        p_inletViews.setLayout(new BoxLayout(p_inletViews, BoxLayout.PAGE_AXIS));
        p_inletViews.setAlignmentX(LEFT_ALIGNMENT);
        p_inletViews.setAlignmentY(TOP_ALIGNMENT);
        p_outletViews = new JPanel();
        p_outletViews.setBackground(Theme.getCurrentTheme().Object_Default_Background);

        p_outletViews.setLayout(new BoxLayout(p_outletViews, BoxLayout.PAGE_AXIS));
        p_outletViews.setAlignmentX(RIGHT_ALIGNMENT);
        p_outletViews.setAlignmentY(TOP_ALIGNMENT);
        p_parameterViews = new JPanel();
        p_parameterViews.setBackground(Theme.getCurrentTheme().Object_Default_Background);
        if (getType().getRotatedParams()) {
            p_parameterViews.setLayout(new BoxLayout(p_parameterViews, BoxLayout.LINE_AXIS));
        } else {
            p_parameterViews.setLayout(new BoxLayout(p_parameterViews, BoxLayout.PAGE_AXIS));
        }
        p_displayViews = new JPanel();
        p_displayViews.setBackground(Theme.getCurrentTheme().Object_Default_Background);

        if (getType().getRotatedParams()) {
            p_displayViews.setLayout(new BoxLayout(p_displayViews, BoxLayout.LINE_AXIS));
        } else {
            p_displayViews.setLayout(new BoxLayout(p_displayViews, BoxLayout.PAGE_AXIS));
        }
        p_displayViews.add(Box.createHorizontalGlue());
        p_parameterViews.add(Box.createHorizontalGlue());

        for (Inlet inlet : getType().inlets) {
            InletInstance inletInstanceP = null;
            for (InletInstanceView inletInstanceView : pInletInstanceViews) {
                if (inletInstanceView.getInletInstance().GetLabel().equals(inlet.getName())) {
                    inletInstanceP = inletInstanceView.getInletInstance();
                }
            }
            InletInstance inletInstance = new InletInstance(inlet, this.getObjectInstance());
            if (inletInstanceP != null) {
                Net n = getPatchModel().GetNet(inletInstanceP);
                if (n != null) {
                    n.connectInlet(inletInstance);
                }
            }
            model.inletInstances.add(inletInstance);
            InletInstanceView view = inletInstance.CreateView(this);
            view.setAlignmentX(LEFT_ALIGNMENT);
            p_inletViews.add(view);
        }
        // disconnect stale inlets from nets
        for (InletInstanceView inletInstanceView : pInletInstanceViews) {
            getPatchModel().disconnect(inletInstanceView.getInletInstance());
        }

        for (Outlet o : getType().outlets) {
            OutletInstance outletInstanceP = null;
            for (OutletInstanceView outletInstanceView : pOutletInstanceViews) {
                if (outletInstanceView.getOutletInstance().GetLabel().equals(o.getName())) {
                    outletInstanceP = outletInstanceView.getOutletInstance();
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
            model.outletInstances.add(outletInstance);
            OutletInstanceView view = outletInstance.CreateView(this);
            view.setAlignmentX(RIGHT_ALIGNMENT);
            p_outletViews.add(view);
        }
        // disconnect stale outlets from nets
        for (OutletInstanceView outletInstanceView : pOutletInstanceViews) {
            getPatchModel().disconnect(outletInstanceView.getOutletInstance());
        }

        /*
         if (p_inlets.getComponents().length == 0){
         p_inlets.add(Box.createHorizontalGlue());
         }
         if (p_outlets.getComponents().length == 0){
         p_outlets.add(Box.createHorizontalGlue());
         }*/
        p_iolets.add(p_inletViews);
        p_iolets.add(Box.createHorizontalGlue());
        p_iolets.add(p_outletViews);
        add(p_iolets);

        for (AxoAttribute p : getType().attributes) {
            AttributeInstance attributeInstance = null;
            for (AttributeInstanceView attributeInstanceView : pAttributeInstanceViews) {

                if (attributeInstanceView.getName().equals(p.getName())) {
                    attributeInstance = attributeInstanceView.getAttributeInstance();
                }
            }
            AttributeInstance attributeInstance1 = p.CreateInstance(this.getObjectInstance(), attributeInstance);
            AttributeInstanceView attributeInstanceView = attributeInstance1.CreateView(this);
            attributeInstanceView.setAlignmentX(LEFT_ALIGNMENT);
            add(attributeInstanceView);
            attributeInstanceView.doLayout();
            model.attributeInstances.add(attributeInstance1);
        }

        for (Parameter p : getType().params) {
            ParameterInstance pin = p.CreateInstance(this.getObjectInstance());
            for (ParameterInstanceView pinp : pParameterInstanceViews) {
                if (pinp.getName().equals(pin.getName())) {
                    pin.CopyValueFrom(pinp.getParameterInstance());
                }
            }
            ParameterInstanceView view = pin.CreateView(this);
            view.PostConstructor();
            view.setAlignmentX(RIGHT_ALIGNMENT);
            view.doLayout();
            model.parameterInstances.add(pin);
        }

        for (Display p : getType().displays) {
            DisplayInstance pin = p.CreateInstance(this.getObjectInstance());
            DisplayInstanceView view = pin.CreateView(this);
            view.setAlignmentX(RIGHT_ALIGNMENT);
            view.doLayout();
            model.displayInstances.add(pin);
        }
//        p_displays.add(Box.createHorizontalGlue());
//        p_params.add(Box.createHorizontalGlue());
        add(p_parameterViews);
        add(p_displayViews);
        p_parameterViews.setAlignmentX(LEFT_ALIGNMENT);
        p_displayViews.setAlignmentX(LEFT_ALIGNMENT);

        model.getType().addObjectModifiedListener(this);

        resizeToGrid();
    }

    public void refreshIndex() {
        if (getPatchView() != null) {
            IndexLabel.setText(" " + this.getPatchView().getObjectInstanceViews().indexOf(this));
        }
    }

    Rectangle editorBounds;
    Integer editorActiveTabIndex;

    public void OpenEditor() {
        getType().OpenEditor(editorBounds, editorActiveTabIndex);
    }

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

    public ArrayList<AttributeInstanceView> attributeInstanceViews;

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
            updateObj();
            deferredObjTypeUpdate = false;
        }
    }

    @Override
    public AxoObjectInstance getObjectInstance() {
        return this.model;
    }
}
