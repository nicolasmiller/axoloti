package axoloti.piccolo.parameterviews;

import axoloti.datatypes.Value;
import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.patch.object.parameter.ParameterInstanceBin32;
import axoloti.piccolo.components.control.PCheckboxComponent;
import axoloti.parameters.ParameterInstanceController;

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
