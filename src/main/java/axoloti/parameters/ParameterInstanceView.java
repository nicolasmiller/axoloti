/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameters;

import axoloti.Preset;
import axoloti.object.AxoObjectInstanceView;
import components.AssignMidiCCComponent;
import components.AssignPresetMenuItems;
import components.LabelComponent;
import components.control.ACtrlComponent;
import components.control.ACtrlEvent;
import components.control.ACtrlListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputAdapter;

public abstract class ParameterInstanceView extends JPanel implements ActionListener {

    ParameterInstance parameterInstance;
    LabelComponent valuelbl = new LabelComponent("123456789");
    ACtrlComponent ctrl;

    AssignMidiCCComponent midiAssign;

    AxoObjectInstanceView axoObjInstanceView;

    ParameterInstanceView(ParameterInstance parameterInstance) {
        this.parameterInstance = parameterInstance;
    }

    public void PostConstructor() {
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel lbls = null;
        if ((((parameterInstance.parameter.noLabel == null) || (parameterInstance.parameter.noLabel == false))) && (parameterInstance.convs != null)) {
            lbls = new JPanel();
            lbls.setLayout(new BoxLayout(lbls, BoxLayout.Y_AXIS));
            this.add(lbls);
        }

        if ((parameterInstance.parameter.noLabel == null) || (parameterInstance.parameter.noLabel == false)) {
            if (lbls != null) {
                lbls.add(new LabelComponent(parameterInstance.parameter.name));
            } else {
                add(new LabelComponent(parameterInstance.parameter.name));
            }
        }
        if (parameterInstance.convs != null) {
            if (lbls != null) {
                lbls.add(valuelbl);
            } else {
                add(valuelbl);
            }
            Dimension d = new Dimension(50, 10);
            valuelbl.setMinimumSize(d);
            valuelbl.setPreferredSize(d);
            valuelbl.setSize(d);
            valuelbl.setMaximumSize(d);
            valuelbl.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    parameterInstance.selectedConv = parameterInstance.selectedConv + 1;
                    if (parameterInstance.selectedConv >= parameterInstance.convs.length) {
                        parameterInstance.selectedConv = 0;
                    }
                    UpdateUnit();
                }
            });
            UpdateUnit();
        }

        ctrl = CreateControl();
        if (parameterInstance.parameter.description != null) {
            ctrl.setToolTipText(parameterInstance.parameter.description);
        } else {
            ctrl.setToolTipText(parameterInstance.parameter.name);
        }
        add(getControlComponent());
        getControlComponent().addMouseListener(popupMouseListener);
        getControlComponent().addACtrlListener(new ACtrlListener() {
            @Override
            public void ACtrlAdjusted(ACtrlEvent e) {
                boolean changed = handleAdjustment();
                if (axoObjInstanceView != null && changed) {
                    if (axoObjInstanceView.getPatchModel() != null) {
                        axoObjInstanceView.getPatchModel().SetDirty();
                    }
                }
            }
        });
        updateV();
        SetMidiCC(parameterInstance.MidiCC);
    }

    public void doPopup(MouseEvent e) {
        JPopupMenu m = new JPopupMenu();
        populatePopup(m);
        m.show(this, 0, getHeight());
    }

    public void populatePopup(JPopupMenu m) {
        final JCheckBoxMenuItem m_onParent = new JCheckBoxMenuItem("parameter on parent");
        m_onParent.setSelected(parameterInstance.getOnParent());
        m.add(m_onParent);
        m_onParent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                parameterInstance.setOnParent(m_onParent.isSelected());
            }
        });

        JMenu m_preset = new JMenu("Preset");
        // AssignPresetMenuItems, does stuff in ctor
        AssignPresetMenuItems assignPresetMenuItems = new AssignPresetMenuItems(parameterInstance, m_preset);
        m.add(m_preset);
    }

    /**
     *
     * @return control component
     */
    abstract public ACtrlComponent getControlComponent();

    abstract public boolean handleAdjustment();

    public abstract ACtrlComponent CreateControl();

    MouseListener popupMouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                doPopup(e);
                e.consume();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                doPopup(e);
                e.consume();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    };

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.startsWith("CC")) {
            int i = Integer.parseInt(s.substring(2));
            SetMidiCC(i);
        } else if (s.equals("none")) {
            SetMidiCC(-1);
        }
    }

    @Override
    public String getName() {
        return parameterInstance.name;
    }

    void UpdateUnit() {
        if (parameterInstance.convs != null) {
            valuelbl.setText(parameterInstance.convs[parameterInstance.selectedConv].ToReal(parameterInstance.getValue()));
        }
    }

    public void updateV() {
        UpdateUnit();
    }

    void SetMidiCC(Integer cc) {
        parameterInstance.SetMidiCC(cc);
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
        if (parameterInstance.getOnParent() == null) {
            return false;
        } else {
            return parameterInstance.getOnParent();
        }
    }

    public void setOnParent(Boolean b) {
        if (b == null) {
            return;
        }
        if (isOnParent() == b) {
            return;
        }
        if (b) {
            parameterInstance.setOnParent(true);
        } else {
            parameterInstance.setOnParent(null);
        }
    }
    
    public int presetEditActive = 0;

    
    public void IncludeInPreset() {
        if (presetEditActive > 0) {
            Preset p = parameterInstance.GetPreset(presetEditActive);
            if (p != null) {
                return;
            }
            if (parameterInstance.presets == null) {
                parameterInstance.presets = new ArrayList<Preset>();
            }
            p = new Preset(presetEditActive, parameterInstance.getValue());
            parameterInstance.presets.add(p);
        }
        ShowPreset(presetEditActive);
    }

    public void ExcludeFromPreset() {
        if (presetEditActive > 0) {
            Preset p = parameterInstance.GetPreset(presetEditActive);
            if (p != null) {
                parameterInstance.presets.remove(p);
                if (parameterInstance.presets.isEmpty()) {
                    parameterInstance.presets = null;
                }
            }
        }
        ShowPreset(presetEditActive);
    }
}