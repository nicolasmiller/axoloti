package axoloti.piccolo.parameterviews;

import axoloti.datatypes.Value;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.parameters.ParameterInstanceController;
import axoloti.parameters.ParameterInstanceBin32;
import components.piccolo.control.PCheckboxComponent;

public class PParameterInstanceViewBin32 extends PParameterInstanceViewInt32 {

    public PParameterInstanceViewBin32(ParameterInstanceController controller,
            IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    @Override
    public PCheckboxComponent CreateControl() {
        return new PCheckboxComponent(0, 32, axoObjectInstanceView);
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
    public PCheckboxComponent getControlComponent() {
        return (PCheckboxComponent) ctrl;
    }
}
