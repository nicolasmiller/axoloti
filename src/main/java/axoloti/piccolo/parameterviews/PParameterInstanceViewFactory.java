package axoloti.piccolo.parameterviews;

import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.parameters.ParameterInstance;
import axoloti.parameters.ParameterInstance4LevelX16;
import axoloti.parameters.ParameterInstanceBin1;
import axoloti.parameters.ParameterInstanceBin12;
import axoloti.parameters.ParameterInstanceBin16;
import axoloti.parameters.ParameterInstanceBin1Momentary;
import axoloti.parameters.ParameterInstanceBin32;
import axoloti.parameters.ParameterInstanceController;
import axoloti.parameters.ParameterInstanceFrac32SMap;
import axoloti.parameters.ParameterInstanceFrac32SMapVSlider;
import axoloti.parameters.ParameterInstanceFrac32UMap;
import axoloti.parameters.ParameterInstanceFrac32UMapVSlider;
import axoloti.parameters.ParameterInstanceInt32Box;
import axoloti.parameters.ParameterInstanceInt32BoxSmall;
import axoloti.parameters.ParameterInstanceInt32HRadio;
import axoloti.parameters.ParameterInstanceInt32VRadio;
import axoloti.objectviews.IAxoObjectInstanceView;

public class PParameterInstanceViewFactory {
    public static PParameterInstanceView createView(ParameterInstanceController controller, IAxoObjectInstanceView obj) {
        ParameterInstance model = controller.getModel();
        PParameterInstanceView view;
        if (model instanceof ParameterInstance4LevelX16) {
            view = new PParameterInstanceView4LevelX16(controller, obj);
        } else if (model instanceof ParameterInstanceBin1) {
            view = new PParameterInstanceViewBin1(controller, obj);
        } else if (model instanceof ParameterInstanceBin12) {
            view = new PParameterInstanceViewBin12(controller, obj);
        } else if (model instanceof ParameterInstanceBin16) {
            view = new PParameterInstanceViewBin16(controller, obj);
        } else if (model instanceof ParameterInstanceBin1Momentary) {
            view = new PParameterInstanceViewBin1Momentary(controller, obj);
        } else if (model instanceof ParameterInstanceBin32) {
            view = new PParameterInstanceViewBin32(controller, obj);
        } else if (model instanceof ParameterInstanceFrac32SMap) {
            view = new PParameterInstanceViewFrac32SMap(controller, obj);
        } else if (model instanceof ParameterInstanceFrac32SMapVSlider) {
            view = new PParameterInstanceViewFrac32SMapVSlider(controller, obj);
        } else if (model instanceof ParameterInstanceFrac32UMap) {
            view = new PParameterInstanceViewFrac32UMap(controller, obj);
        } else if (model instanceof ParameterInstanceFrac32UMapVSlider) {
            view = new PParameterInstanceViewFrac32UMapVSlider(controller, obj);
        } else if (model instanceof ParameterInstanceInt32Box) {
            view = new PParameterInstanceViewInt32Box(controller, obj);
        } else if (model instanceof ParameterInstanceInt32BoxSmall) {
            view = new PParameterInstanceViewInt32BoxSmall(controller, obj);
        } else if (model instanceof ParameterInstanceInt32HRadio) {
            view = new PParameterInstanceViewInt32HRadio(controller, obj);
        } else if (model instanceof ParameterInstanceInt32VRadio) {
            view = new PParameterInstanceViewInt32VRadio(controller, obj);
        } else {
            view = null;
            throw new Error("unknown parameter type");
        }
        view.PostConstructor();
        controller.addView(view);
        return view;
    }
}
