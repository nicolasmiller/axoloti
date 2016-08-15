/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.Preset;
import axoloti.Theme;
import axoloti.datatypes.Value;
import axoloti.parameters.ParameterFrac32;
import axoloti.parameters.ParameterInstanceFrac32UMap;
import components.AssignMidiCCComponent;
import components.AssignMidiCCMenuItems;
import components.AssignModulatorComponent;
import components.AssignModulatorMenuItems;
import components.AssignPresetComponent;
import components.control.DialComponent;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author nicolas
 */
public class ParameterInstanceFrac32UMapView extends ParameterInstanceFrac32UView {

    ParameterInstanceFrac32UMap parameterInstance;

    AssignModulatorComponent modulationAssign;
    AssignPresetComponent presetAssign;

    ParameterInstanceFrac32UMapView(ParameterInstanceFrac32UMap parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }

    @Override
    public DialComponent CreateControl() {
        return new DialComponent(0.0, parameterInstance.getMin(), parameterInstance.getMax(), parameterInstance.getTick());
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        JPanel btns = new JPanel();
        btns.setBackground(Theme.getCurrentTheme().Object_Default_Background);
        btns.setLayout(new BoxLayout(btns, BoxLayout.PAGE_AXIS));

        //lblCC = new LabelComponent("C");
        //btns.add(lblCC);
        midiAssign = new AssignMidiCCComponent(this);
        btns.add(midiAssign);
        modulationAssign = new AssignModulatorComponent(this);
        btns.add(modulationAssign);
        presetAssign = new AssignPresetComponent(this);
        btns.add(presetAssign);
        add(btns);

//        setComponentPopupMenu(new ParameterInstanceUInt7MapPopupMenu3(this));
        addMouseListener(popupMouseListener);
        updateV();
    }

    @Override
    public void setOnParent(Boolean b) {
        super.setOnParent(b);
        if ((b != null) && b) {
            setForeground(Theme.getCurrentTheme().Parameter_On_Parent_Highlight);
        } else {
            setForeground(Theme.getCurrentTheme().Parameter_Default_Foreground);
        }
    }

    @Override
    public void updateV() {
        super.updateV();
        if (ctrl != null) {
            ctrl.setValue(parameterInstance.getValue().getDouble());
        }
    }

    /*
     *  Preset logic
     */
    @Override
    public void ShowPreset(int i) {
        this.presetEditActive = i;
        if (i > 0) {
            Preset p = parameterInstance.GetPreset(presetEditActive);
            if (p != null) {
                setBackground(Theme.getCurrentTheme().Paramete_Preset_Highlight);
                ctrl.setValue(p.value.getDouble());
            } else {
                setBackground(Theme.getCurrentTheme().Parameter_Default_Background);
                ctrl.setValue(parameterInstance.getValue().getDouble());
            }
        } else {
            setBackground(Theme.getCurrentTheme().Parameter_Default_Background);
            ctrl.setValue(parameterInstance.getValue().getDouble());
        }
        presetAssign.repaint();
        /*
         if ((presets != null) && (!presets.isEmpty())) {            
         lblPreset.setVisible(true);
         } else {
         lblPreset.setVisible(false);
         }
         */
    }

    @Override
    public void populatePopup(JPopupMenu m) {
        super.populatePopup(m);
        JMenu m1 = new JMenu("Midi CC");
        new AssignMidiCCMenuItems(this, m1);
        m.add(m1);
        JMenu m2 = new JMenu("Modulation");
        new AssignModulatorMenuItems((ParameterInstanceFrac32UMap<ParameterFrac32>) parameterInstance, m2);
        m.add(m2);
    }

    @Override
    public DialComponent getControlComponent() {
        return (DialComponent) ctrl;
    }
    
    @Override
    public void updateModulation(int index, double amount) {
        parameterInstance.updateModulation(index, amount);
        if (modulationAssign != null) {
            modulationAssign.repaint();
        }
    }
    
    @Override
    public Preset AddPreset(int index, Value value) {
        Preset p = parameterInstance.AddPreset(index, value);
        presetAssign.repaint();
        return p;
    }

    @Override
    public void RemovePreset(int index) {
        parameterInstance.RemovePreset(index);
        presetAssign.repaint();
    }
}