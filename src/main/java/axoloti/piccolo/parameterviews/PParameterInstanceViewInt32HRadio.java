package axoloti.piccolo.parameterviews;

import java.beans.PropertyChangeEvent;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.parameters.ParameterInstanceController;
import axoloti.parameters.ParameterInt32;
import axoloti.parameters.ParameterInt32HRadio;

import components.piccolo.PAssignMidiCCMenuItems;
import components.piccolo.control.PHRadioComponent;

public class PParameterInstanceViewInt32HRadio extends PParameterInstanceViewInt32 {

    public PParameterInstanceViewInt32HRadio(ParameterInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    @Override
    public PHRadioComponent CreateControl() {
        return new PHRadioComponent(0, ((ParameterInt32HRadio) getModel().getModel()).getMaxValue(), axoObjectInstanceView);
    }

    @Override
    public PHRadioComponent getControlComponent() {
        return (PHRadioComponent) ctrl;
    }

    // @Override
    // public void populatePopup(JPopupMenu m) {
    //     super.populatePopup(m);
    //     JMenu m1 = new JMenu("Midi CC");
    //     new PAssignMidiCCMenuItems(this, m1);
    //     m.add(m1);
    // }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (ParameterInt32.VALUE_MAX.is(evt)) {
            getControlComponent().setMax((Integer) evt.getNewValue());
        }
    }
}
