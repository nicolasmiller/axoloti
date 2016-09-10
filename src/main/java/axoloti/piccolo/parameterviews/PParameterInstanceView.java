package axoloti.piccolo.parameterviews;

import axoloti.Preset;
import axoloti.datatypes.Value;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.parameters.ParameterInstance;
import axoloti.parameterviews.IParameterInstanceView;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import static axoloti.piccolo.PNodeLayout.VERTICAL_CENTERED;
import axoloti.piccolo.PatchPNode;
import components.control.ACtrlEvent;
import components.control.ACtrlListener;
import components.piccolo.PAssignMidiCCComponent;
import components.piccolo.PLabelComponent;
import components.piccolo.control.PCtrlComponentAbstract;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public abstract class PParameterInstanceView extends PatchPNode implements ActionListener, IParameterInstanceView {

    ParameterInstance parameterInstance;
    PLabelComponent valuelbl = new PLabelComponent("123456789");
    PCtrlComponentAbstract ctrl;

    PAssignMidiCCComponent midiAssign;

    IAxoObjectInstanceView axoObjInstanceView;

    Color backgroundColor;

    PParameterInstanceView(ParameterInstance parameterInstance, IAxoObjectInstanceView axoObjectInstanceView) {
        super();
        this.parameterInstance = parameterInstance;
        this.axoObjInstanceView = axoObjectInstanceView;
    }

    @Override
    public void PostConstructor() {
        this.setPickable(false);
        this.setLayout(HORIZONTAL_CENTERED);
        this.removeAllChildren();

        PatchPNode lbls = null;
        if ((((parameterInstance.getParameter().noLabel == null)
                || (parameterInstance.getParameter().noLabel == false)))
                && (parameterInstance.getConvs() != null)) {
            lbls = new PatchPNode();
            lbls.setLayout(VERTICAL_CENTERED);
            this.addChild(lbls);
        }

        if ((parameterInstance.getParameter().noLabel == null) || (parameterInstance.getParameter().noLabel == false)) {
            if (lbls != null) {
                lbls.addChild(new PLabelComponent(parameterInstance.getParameter().name));
            } else {
                addChild(new PLabelComponent(parameterInstance.getParameter().name));
            }
        }

        if (parameterInstance.getConvs() != null) {
            if (lbls != null) {
                lbls.addChild(valuelbl);
            } else {
                addChild(valuelbl);
            }
            valuelbl.setBounds(0, 0, 50, 10);
            valuelbl.addInputEventListener(new PBasicInputEventHandler() {
                @Override
                public void mouseClicked(PInputEvent e) {
                    parameterInstance.setSelectedConv(parameterInstance.getSelectedConv() + 1);
                    if (parameterInstance.getSelectedConv() >= parameterInstance.getConvs().length) {
                        parameterInstance.setSelectedConv(0);
                    }
                    UpdateUnit();

                }
            });
            UpdateUnit();
        }

        ctrl = CreateControl();
        if (parameterInstance.getParameter().description != null) {
            ctrl.setToolTipText(parameterInstance.getParameter().description);
        } else {
            ctrl.setToolTipText(parameterInstance.getParameter().name);
        }
        addChild(getControlComponent());
        getControlComponent().addInputEventListener(popupMouseListener);
        getControlComponent().addACtrlListener(new ACtrlListener() {
            @Override
            public void ACtrlAdjusted(ACtrlEvent e) {
                boolean changed = handleAdjustment();
            }

            @Override
            public void ACtrlAdjustmentBegin(ACtrlEvent e) {
                valueBeforeAdjustment = getControlComponent().getValue();
                //System.out.println("begin "+value_before);
            }

            @Override
            public void ACtrlAdjustmentFinished(ACtrlEvent e) {
                if ((valueBeforeAdjustment != getControlComponent().getValue())
                        && (axoObjInstanceView != null)
                        && (axoObjInstanceView.getPatchModel() != null)) {
                    //System.out.println("finished" +getControlComponent().getValue());
                    axoObjInstanceView.getPatchModel().SetDirty();
                }
            }
        });
        updateV();
        parameterInstance.setMidiCC(parameterInstance.getMidiCC());
    }

    double valueBeforeAdjustment;

    public void doPopup(PInputEvent e) {
        JPopupMenu m = new JPopupMenu();
        populatePopup(m);
        m.show(getCanvas(), (int) getBounds().x, (int) (getBounds().y + getHeight()));
    }

    public void populatePopup(JPopupMenu m) {
        final JCheckBoxMenuItem m_onParent = new JCheckBoxMenuItem("parameter on parent");
        m_onParent.setSelected(parameterInstance.isOnParent());
        m.add(m_onParent);
        m_onParent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                parameterInstance.setOnParent(m_onParent.isSelected());
                repaint();
            }
        });

        JMenu m_preset = new JMenu("Preset");
        // AssignPresetMenuItems, does stuff in ctor
        // TODO
//        AssignPresetMenuItems assignPresetMenuItems = new AssignPresetMenuItems(this, m_preset);
        m.add(m_preset);
    }

    /**
     *
     * @return control component
     */
    abstract public PCtrlComponentAbstract getControlComponent();

    abstract public boolean handleAdjustment();

    public abstract PCtrlComponentAbstract CreateControl();

    PBasicInputEventHandler popupMouseListener = new PBasicInputEventHandler() {
        @Override
        public void mousePressed(PInputEvent e) {
            if (e.isPopupTrigger()) {
                doPopup(e);
                e.setHandled(true);
            }
        }

        @Override
        public void mouseReleased(PInputEvent e) {
            if (e.isPopupTrigger()) {
                doPopup(e);
                e.setHandled(true);
            }
        }
    };

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.startsWith("CC")) {
            int i = Integer.parseInt(s.substring(2));
            if (i != parameterInstance.getMidiCC()) {
                SetMidiCC(i);
                axoObjInstanceView.getPatchModel().SetDirty();
            }
        } else if (s.equals("none")) {
            if (-1 != parameterInstance.getMidiCC()) {
                SetMidiCC(-1);
                axoObjInstanceView.getPatchModel().SetDirty();
            }
        }
    }

    @Override
    public String getName() {
        if (parameterInstance != null) {
            return parameterInstance.getName();
        } else {
            return super.getName();
        }
    }

    void UpdateUnit() {
        if (parameterInstance.getConvs() != null) {
            valuelbl.setText(parameterInstance.getConvs()[parameterInstance.getSelectedConv()].ToReal(parameterInstance.getValue()));
        }
    }

    public void updateV() {
        UpdateUnit();
    }

    public void SetMidiCC(Integer cc) {
        parameterInstance.setMidiCC(cc);
        if ((cc != null) && (cc >= 0)) {
            if (midiAssign != null) {
                midiAssign.setCC(cc);
            }
        } else if (midiAssign != null) {
            midiAssign.setCC(-1);
        }
    }

    public void SetValueRaw(int v) {
        parameterInstance.SetValueRaw(v);
        updateV();
    }

    public abstract void ShowPreset(int i);

    public boolean isOnParent() {
        return parameterInstance.isOnParent();
    }

    public int presetEditActive = 0;

    public void IncludeInPreset() {
        if (presetEditActive > 0) {
            Preset p = parameterInstance.GetPreset(presetEditActive);
            if (p != null) {
                return;
            }
            if (parameterInstance.getPresets() == null) {
                parameterInstance.setPresets(new ArrayList<Preset>());
            }
            p = new Preset(presetEditActive, parameterInstance.getValue());
            parameterInstance.getPresets().add(p);
        }
        ShowPreset(presetEditActive);
    }

    public void ExcludeFromPreset() {
        if (presetEditActive > 0) {
            Preset p = parameterInstance.GetPreset(presetEditActive);
            if (p != null) {
                parameterInstance.getPresets().remove(p);
                if (parameterInstance.getPresets().isEmpty()) {
                    parameterInstance.setPresets(null);
                }
            }
        }
        ShowPreset(presetEditActive);
    }

    public void CopyValueFrom(PParameterInstanceView p) {
        parameterInstance.CopyValueFrom(p.parameterInstance);
    }

    public void setValue(Value value) {
        parameterInstance.setValue(value);
        updateV();
    }

    public ParameterInstance getParameterInstance() {
        return parameterInstance;
    }

    public Preset AddPreset(int index, Value value) {
        return parameterInstance.AddPreset(index, value);
    }

    public void RemovePreset(int index) {
        parameterInstance.RemovePreset(index);
    }

    public Component getCanvas() {
        return this.axoObjInstanceView.getCanvas();
    }

    public void setBackground(Color backgroundColor) {
        this.setPaint(backgroundColor);
    }
}
