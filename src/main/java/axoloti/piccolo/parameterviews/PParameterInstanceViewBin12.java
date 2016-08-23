package axoloti.piccolo.parameterviews;

import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.parameters.ParameterInstanceBin12;
import components.piccolo.control.PCheckboxComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

public class PParameterInstanceViewBin12 extends PParameterInstanceViewInt32 {

    public PParameterInstanceViewBin12(ParameterInstanceBin12 parameterInstance, IAxoObjectInstanceView axoObjectInstanceView) {
        super(parameterInstance, axoObjectInstanceView);
    }

    @Override
    public PCheckboxComponent CreateControl() {
        return new PCheckboxComponent(0, 12);
    }

    @Override
    public void ShowPreset(int i) {
    }

    @Override
    public void updateV() {
        ctrl.setValue(parameterInstance.getValue().getInt());
    }

    @Override
    public PCheckboxComponent getControlComponent() {
        return (PCheckboxComponent) ctrl;
    }

    @Override
    public void populatePopup(JPopupMenu m) {
        super.populatePopup(m);
        JMenu m1 = new JMenu("Midi CC");
        // assignMidiCCMenuItems, does stuff in ctor
        // TODO
//        AssignMidiCCMenuItems assignMidiCCMenuItems = new AssignMidiCCMenuItems(this, m1);
        m.add(m1);
    }
}
