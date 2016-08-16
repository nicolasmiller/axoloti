/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.parameters.ParameterInstanceInt32HRadio;
import axoloti.parameters.ParameterInt32HRadio;
import components.AssignMidiCCMenuItems;
import components.control.HRadioComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

/**
 *
 * @author nicolas
 */
public class ParameterInstanceViewInt32HRadio extends ParameterInstanceViewInt32 {

    ParameterInstanceInt32HRadio parameterInstance;

    public ParameterInstanceViewInt32HRadio(ParameterInstanceInt32HRadio parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }

    @Override
    public void updateV() {
        ctrl.setValue(parameterInstance.getValue().getInt());
    }

    @Override
    public HRadioComponent CreateControl() {
        return new HRadioComponent(0, ((ParameterInt32HRadio) parameterInstance.getParameter()).MaxValue.getInt());
    }

    @Override
    public HRadioComponent getControlComponent() {
        return (HRadioComponent) ctrl;
    }

    @Override
    public void populatePopup(JPopupMenu m) {
        super.populatePopup(m);
        JMenu m1 = new JMenu("Midi CC");
        new AssignMidiCCMenuItems(this, m1);
        m.add(m1);
    }
}
