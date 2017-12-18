package axoloti.piccolo.inlets;

import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.abstractui.IInletInstanceView;
import axoloti.patch.object.inlet.InletInstanceController;

public class PInletInstanceViewFactory {

    public static IInletInstanceView createView(InletInstanceController controller, IAxoObjectInstanceView obj) {
        IInletInstanceView view = new PInletInstanceView(controller, obj);
        view.PostConstructor();
        controller.addView(view);
        return view;
    }
}
