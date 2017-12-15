package axoloti.piccolo.parameterviews;

import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.parameters.ParameterInstanceController;
import axoloti.parameters.ParameterInstanceInt32BoxSmall;
import components.piccolo.control.PNumberBoxComponent;

public class PParameterInstanceViewInt32BoxSmall extends PParameterInstanceViewInt32Box {

    public PParameterInstanceViewInt32BoxSmall(ParameterInstanceController controller,
            IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    @Override
    public ParameterInstanceInt32BoxSmall getModel() {
        return (ParameterInstanceInt32BoxSmall) super.getModel();
    }

    @Override
    public PNumberBoxComponent CreateControl() {
        return new PNumberBoxComponent(0.0, getModel().getMinValue(),
                getModel().getMaxValue(), 1.0, 12, 12, axoObjectInstanceView);
    }
}
