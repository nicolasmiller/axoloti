/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.object;

import axoloti.MainFrame;
import axoloti.Net;
import axoloti.PatchView;
import axoloti.Theme;
import axoloti.attribute.AttributeInstance;
import axoloti.attribute.AttributeInstanceView;
import axoloti.attributedefinition.AxoAttribute;
import axoloti.displays.Display;
import axoloti.displays.DisplayInstance;
import axoloti.inlets.Inlet;
import axoloti.inlets.InletInstance;
import axoloti.outlets.Outlet;
import axoloti.outlets.OutletInstance;
import axoloti.parameters.Parameter;
import axoloti.parameters.ParameterInstance;
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
public class AxoObjectInstanceView extends AxoObjectInstanceAbstractView implements ObjectModifiedListener {
    private AxoObjectInstance model;
    LabelComponent IndexLabel;
    public JPanel p_params;
    public JPanel p_displays;
    public JPanel p_inlets;
    public JPanel p_outlets;
    boolean deferredObjTypeUpdate = false;

    
    public AxoObjectInstanceView(AxoObjectInstanceAbstract model) {
        super(model);
    }
    
    public AxoObject getType() {
        return model.getType();
    }
    
    @Override
    public void PostConstructor() {
        super.PostConstructor();
        model.updateObj1();
        ArrayList<ParameterInstance> pParameterInstances = model.parameterInstances;
        ArrayList<AttributeInstance> pAttributeInstances = model.attributeInstances;
        ArrayList<InletInstance> pInletInstances = model.inletInstances;
        ArrayList<OutletInstance> pOutletInstances = model.outletInstances;
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

        if (model.type instanceof AxoObjectFromPatch) {
            JMenuItem popm_embed = new JMenuItem("embed as patch/patcher");
            popm_embed.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    model.ConvertToPatchPatcher();
                }
            });
            popup.add(popm_embed);
        } else if (!(this instanceof AxoObjectInstancePatcherObjectView)) {
            JMenuItem popm_embed = new JMenuItem("embed as patch/object");
            popm_embed.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    model.ConvertToEmbeddedObj();
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
        p_inlets = new JPanel();
        p_inlets.setBackground(Theme.getCurrentTheme().Object_Default_Background);

        p_inlets.setLayout(new BoxLayout(p_inlets, BoxLayout.PAGE_AXIS));
        p_inlets.setAlignmentX(LEFT_ALIGNMENT);
        p_inlets.setAlignmentY(TOP_ALIGNMENT);
        p_outlets = new JPanel();
        p_outlets.setBackground(Theme.getCurrentTheme().Object_Default_Background);

        p_outlets.setLayout(new BoxLayout(p_outlets, BoxLayout.PAGE_AXIS));
        p_outlets.setAlignmentX(RIGHT_ALIGNMENT);
        p_outlets.setAlignmentY(TOP_ALIGNMENT);
        p_params = new JPanel();
        p_params.setBackground(Theme.getCurrentTheme().Object_Default_Background);
        if (getType().getRotatedParams()) {
            p_params.setLayout(new BoxLayout(p_params, BoxLayout.LINE_AXIS));
        } else {
            p_params.setLayout(new BoxLayout(p_params, BoxLayout.PAGE_AXIS));
        }
        p_displays = new JPanel();
        p_displays.setBackground(Theme.getCurrentTheme().Object_Default_Background);

        if (getType().getRotatedParams()) {
            p_displays.setLayout(new BoxLayout(p_displays, BoxLayout.LINE_AXIS));
        } else {
            p_displays.setLayout(new BoxLayout(p_displays, BoxLayout.PAGE_AXIS));
        }
        p_displays.add(Box.createHorizontalGlue());
        p_params.add(Box.createHorizontalGlue());

        for (Inlet inl : getType().inlets) {
            InletInstance inlinp = null;
            for (InletInstance inlin1 : pInletInstances) {
                if (inlin1.GetLabel().equals(inl.getName())) {
                    inlinp = inlin1;
                }
            }
            InletInstance inlin = new InletInstance(inl, this);
            if (inlinp != null) {
                Net n = getPatchModel().GetNet(inlinp);
                if (n != null) {
                    n.connectInlet(inlin);
                }
            }
            inletInstances.add(inlin);
            inlin.setAlignmentX(LEFT_ALIGNMENT);
            p_inlets.add(inlin);
        }
        // disconnect stale inlets from nets
        for (InletInstance inlin1 : pInletInstances) {
            getPatchModel().disconnect(inlin1);
        }

        for (Outlet o : getType().outlets) {
            OutletInstance oinp = null;
            for (OutletInstance oinp1 : pOutletInstances) {
                if (oinp1.GetLabel().equals(o.getName())) {
                    oinp = oinp1;
                }
            }
            OutletInstance oin = new OutletInstance(o, this);
            if (oinp != null) {
                Net n = getPatchModel().GetNet(oinp);
                if (n != null) {
                    n.connectOutlet(oin);
                }
            }
            outletInstances.add(oin);
            oin.setAlignmentX(RIGHT_ALIGNMENT);
            p_outlets.add(oin);
        }
        // disconnect stale outlets from nets
        for (OutletInstance oinp1 : pOutletInstances) {
            getPatchModel().disconnect(oinp1);
        }

        /*
         if (p_inlets.getComponents().length == 0){
         p_inlets.add(Box.createHorizontalGlue());
         }
         if (p_outlets.getComponents().length == 0){
         p_outlets.add(Box.createHorizontalGlue());
         }*/
        p_iolets.add(p_inlets);
        p_iolets.add(Box.createHorizontalGlue());
        p_iolets.add(p_outlets);
        add(p_iolets);

        for (AxoAttribute p : getType().attributes) {
            AttributeInstance attrp1 = null;
            for (AttributeInstance attrp : pAttributeInstances) {
                if (attrp.getName().equals(p.getName())) {
                    attrp1 = attrp;
                }
            }
            AttributeInstance attri = p.CreateInstance(this.getObjectInstance(), attrp1);
            // need to make a view here
            attri.setAlignmentX(LEFT_ALIGNMENT);
            add(attri);
            attri.doLayout();
            model.attributeInstances.add(attri);
        }

        for (Parameter p : getType().params) {
            ParameterInstance pin = p.CreateInstance(this);
            for (ParameterInstance pinp : pParameterInstances) {
                if (pinp.getName().equals(pin.getName())) {
                    pin.CopyValueFrom(pinp);
                }
            }
            pin.PostConstructor();
            pin.setAlignmentX(RIGHT_ALIGNMENT);
            pin.doLayout();
            model.parameterInstances.add(pin);
        }

        for (Display p : getType().displays) {
            DisplayInstance pin = p.CreateInstance(this);
            pin.setAlignmentX(RIGHT_ALIGNMENT);
            pin.doLayout();
            model.displayInstances.add(pin);
        }
//        p_displays.add(Box.createHorizontalGlue());
//        p_params.add(Box.createHorizontalGlue());
        add(p_params);
        add(p_displays);
        p_params.setAlignmentX(LEFT_ALIGNMENT);
        p_displays.setAlignmentX(LEFT_ALIGNMENT);

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
            if (!getPatchView().IsLocked()) {
                updateObj();
            } else {
                deferredObjTypeUpdate = true;
            }
        }

        try {
            AxoObject o = (AxoObject) src;
            if (o.editor != null && o.editor.getBounds() != null) {
                editorBounds = o.editor.getBounds();
                editorActiveTabIndex = o.editor.getActiveTabIndex();
                this.getType().editorBounds = editorBounds;
                this.getType().editorActiveTabIndex = editorActiveTabIndex;
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
    
    public AxoObjectInstance getObjectInstance() {
        return this.model;
    }
}