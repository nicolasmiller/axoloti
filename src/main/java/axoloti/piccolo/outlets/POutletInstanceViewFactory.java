package axoloti.piccolo.outlets;

import axoloti.objectviews.AxoObjectInstanceViewAbstract;
import axoloti.outlets.OutletInstanceController;
import axoloti.outlets.IOutletInstanceView;
import axoloti.objectviews.IAxoObjectInstanceView;

public class POutletInstanceViewFactory {

    public static IOutletInstanceView createView(OutletInstanceController controller, IAxoObjectInstanceView obj) {
        IOutletInstanceView view = new POutletInstanceView(controller, obj);
        view.PostConstructor();
        controller.addView(view);
        return view;
    }

}
