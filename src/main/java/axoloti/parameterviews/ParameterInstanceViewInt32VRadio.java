/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.parameters.ParameterInstanceInt32VRadio;
import axoloti.parameters.ParameterInt32VRadio;
import components.AssignMidiCCMenuItems;
import components.control.VRadioComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

/**
 *
 * @author nicolas
 */
public class ParameterInstanceViewInt32VRadio extends ParameterInstanceViewInt32 {

    ParameterInstanceInt32VRadio parameterInstance;

    ParameterInstanceViewInt32VRadio(ParameterInstanceInt32VRadio parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }
    
    @Override
    public void updateV() {
        ctrl.setValue(parameterInstance.getValue().getInt());
    }
    
    
    @Override
    public VRadioComponent CreateControl() {
        return new VRadioComponent(0, ((ParameterInt32VRadio) parameterInstance.getParameter()).MaxValue.getInt());
    }

    @Override
    public VRadioComponent getControlComponent() {
        return (VRadioComponent) ctrl;
    }

    @Override
    public void populatePopup(JPopupMenu m) {
        super.populatePopup(m);
        JMenu m1 = new JMenu("Midi CC");
        // AssignMidiCCMenuItems, does stuff in ctor
        AssignMidiCCMenuItems assignMidiCCMenuItems = new AssignMidiCCMenuItems(this, m1);
        m.add(m1);
    }
}