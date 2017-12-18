package axoloti.piccolo.parameterviews;

import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.datatypes.Value;
import axoloti.patch.object.parameter.ParameterInstanceController;
import axoloti.piccolo.components.control.PPulseButtonComponent;

public class PParameterInstanceViewBin1Momentary extends PParameterInstanceViewBin {

    public PParameterInstanceViewBin1Momentary(ParameterInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    @Override
    public void updateV() {
        ctrl.setValue(getModel().getValue());
    }

    @Override
    public void setValue(Value value) {
//        parameterInstance.setValue(value);
        updateV();
    }

    @Override
    public PPulseButtonComponent CreateControl() {
        return new PPulseButtonComponent(axoObjectInstanceView);
    }

    @Override
    public PPulseButtonComponent getControlComponent() {
        return (PPulseButtonComponent) ctrl;
    }
}
