package axoloti.piccolo.parameterviews;

import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.object.parameter.ParameterInt32VRadio;
import axoloti.piccolo.components.PAssignMidiCCMenuItems;
import axoloti.piccolo.components.control.PVRadioComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import java.beans.PropertyChangeEvent;

import axoloti.parameters.ParameterInstanceController;
import axoloti.parameters.ParameterInt32;

public class PParameterInstanceViewInt32VRadio extends PParameterInstanceViewInt32 {

    public PParameterInstanceViewInt32VRadio(ParameterInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    @Override
    public PVRadioComponent CreateControl() {
        return new PVRadioComponent(0, ((ParameterInt32VRadio) getModel().getModel()).getMaxValue(), axoObjectInstanceView);
    }

    @Override
    public PVRadioComponent getControlComponent() {
        return (PVRadioComponent) ctrl;
    }

    @Override
    public void populatePopup(JPopupMenu m) {
        super.populatePopup(m);
        JMenu m1 = new JMenu("Midi CC");
        // AssignMidiCCMenuItems, does stuff in ctor
        PAssignMidiCCMenuItems assignMidiCCMenuItems = new PAssignMidiCCMenuItems(this, m1);
        m.add(m1);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (ParameterInt32.VALUE_MAX.is(evt)) {
            getControlComponent().setMax((Integer) evt.getNewValue());
        }
    }
}
