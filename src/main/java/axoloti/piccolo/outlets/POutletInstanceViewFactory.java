package axoloti.piccolo.outlets;

import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.abstractui.IOutletInstanceView;
import axoloti.patch.object.outlet.OutletInstanceController;

public class POutletInstanceViewFactory {

    public static IOutletInstanceView createView(OutletInstanceController controller, IAxoObjectInstanceView obj) {
        IOutletInstanceView view = new POutletInstanceView(controller, obj);
        view.PostConstructor();
        controller.addView(view);
        return view;
    }

}
