package axoloti.objectviews;

import axoloti.PatchViewSwing;
import axoloti.PatchView;
import axoloti.PatchViewPiccolo;
import axoloti.object.AxoObjectInstance;
import axoloti.object.AxoObjectInstanceComment;
import axoloti.object.AxoObjectInstanceHyperlink;
import axoloti.object.AxoObjectInstancePatcher;
import axoloti.object.AxoObjectInstancePatcherObject;
import axoloti.object.AxoObjectInstanceZombie;
import axoloti.object.IAxoObjectInstance;
import axoloti.object.ObjectInstanceController;

import axoloti.piccolo.objectviews.*;

/**
 *
 * @author jtaelman
 */
public class AxoObjectInstanceViewFactory {

    public static IAxoObjectInstanceView createView(ObjectInstanceController controller, PatchView pv) {
	if(pv instanceof PatchViewSwing) {
	    return createView(controller, (PatchViewSwing) pv);
	}
	return createView(controller, (PatchViewPiccolo) pv);
    }

    public static IAxoObjectInstanceView createView(ObjectInstanceController controller, PatchViewSwing pv) {
        IAxoObjectInstance model = controller.getModel();
        AxoObjectInstanceViewAbstract view = null;
        if (model instanceof AxoObjectInstanceComment) {
            view = new AxoObjectInstanceViewComment(controller, pv);
        } else if (model instanceof AxoObjectInstanceHyperlink) {
            view = new AxoObjectInstanceViewHyperlink(controller, pv);
        } else if (model instanceof AxoObjectInstanceZombie) {
            view = new AxoObjectInstanceViewZombie(controller, pv);
        } else if (model instanceof AxoObjectInstancePatcherObject) {
            view = new AxoObjectInstanceViewPatcherObject(controller, pv);
        } else if (model instanceof AxoObjectInstancePatcher) {
            view = new AxoObjectInstanceViewPatcher(controller, pv);
        } else if (model instanceof AxoObjectInstance) {
            view = new AxoObjectInstanceView(controller, pv);
        } else {
            throw new Error("unknown object type");
        }
        view.PostConstructor();
        controller.addView(view);
        return view;
    }

    public static IAxoObjectInstanceView createView(ObjectInstanceController controller, PatchViewPiccolo pv) {
        IAxoObjectInstance model = controller.getModel();
        PAxoObjectInstanceViewAbstract view = null;
        if (model instanceof AxoObjectInstanceComment) {
            view = new PAxoObjectInstanceViewComment(controller, pv);
        } else if (model instanceof AxoObjectInstanceHyperlink) {
            view = new PAxoObjectInstanceViewHyperlink(controller, pv);
        } else if (model instanceof AxoObjectInstanceZombie) {
            view = new PAxoObjectInstanceViewZombie(controller, pv);
        } else if (model instanceof AxoObjectInstancePatcherObject) {
            view = new PAxoObjectInstanceViewPatcherObject(controller, pv);
        } else if (model instanceof AxoObjectInstancePatcher) {
            view = new PAxoObjectInstanceViewPatcher(controller, pv);
        } else if (model instanceof AxoObjectInstance) {
            view = new PAxoObjectInstanceView(controller, pv);
        } else {
            throw new Error("unknown object type");
        }
        view.PostConstructor();
        controller.addView(view);
        return view;
    }
}
