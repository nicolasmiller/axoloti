package axoloti.piccolo.parameterviews;

import java.beans.PropertyChangeEvent;

import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.object.parameter.ParameterInt32;
import axoloti.object.parameter.ParameterInt32HRadio;
import axoloti.patch.object.parameter.ParameterInstanceController;
import axoloti.piccolo.components.control.PHRadioComponent;

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

    // TODO wtf?
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
