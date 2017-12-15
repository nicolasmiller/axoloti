package axoloti.piccolo.inlets;

import axoloti.objectviews.AxoObjectInstanceViewAbstract;
import axoloti.inlets.InletInstanceController;
import axoloti.inlets.IInletInstanceView;
import axoloti.objectviews.IAxoObjectInstanceView;

public class PInletInstanceViewFactory {

    public static IInletInstanceView createView(InletInstanceController controller, IAxoObjectInstanceView obj) {
        IInletInstanceView view = new PInletInstanceView(controller, obj);
        view.PostConstructor();
        controller.addView(view);
        return view;
    }
}
