package axoloti.piccolo.parameterviews;

import java.beans.PropertyChangeEvent;

import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.parameters.ParameterInstanceController;
import axoloti.parameters.ParameterInstanceInt32Box;
import axoloti.parameters.ParameterInt32;

import components.piccolo.control.PNumberBoxComponent;

public class PParameterInstanceViewInt32Box extends PParameterInstanceViewInt32 {

    public PParameterInstanceViewInt32Box(ParameterInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    @Override
    public ParameterInstanceInt32Box getModel() {
        return (ParameterInstanceInt32Box) super.getModel();
    }

    @Override
    public PNumberBoxComponent CreateControl() {
        PNumberBoxComponent n = new PNumberBoxComponent(
            0.0, getModel().getMinValue(),
            getModel().getMaxValue(), 1.0, axoObjectInstanceView);
        return n;
    }

    @Override
    public PNumberBoxComponent getControlComponent() {
        return (PNumberBoxComponent) ctrl;
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (ParameterInt32.VALUE_MIN.is(evt)) {
            getControlComponent().setMin((Integer) evt.getNewValue());
        } else if (ParameterInt32.VALUE_MAX.is(evt)) {
            getControlComponent().setMax((Integer) evt.getNewValue());
        }
    }
}
