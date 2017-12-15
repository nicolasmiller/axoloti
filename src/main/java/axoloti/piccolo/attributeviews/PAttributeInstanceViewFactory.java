package axoloti.piccolo.attributeviews;

import axoloti.attribute.AttributeInstance;
import axoloti.attribute.AttributeInstanceComboBox;
import axoloti.attribute.AttributeInstanceController;
import axoloti.attribute.AttributeInstanceInt32;
import axoloti.attribute.AttributeInstanceObjRef;
import axoloti.attribute.AttributeInstanceSDFile;
import axoloti.attribute.AttributeInstanceSpinner;
import axoloti.attribute.AttributeInstanceTablename;
import axoloti.attribute.AttributeInstanceTextEditor;
import axoloti.objectviews.AxoObjectInstanceView;
import axoloti.objectviews.IAxoObjectInstanceView;

public class PAttributeInstanceViewFactory {

    public static PAttributeInstanceView createView(AttributeInstanceController controller, IAxoObjectInstanceView obj) {
        AttributeInstance model = controller.getModel();
        PAttributeInstanceView view;
        if (model instanceof AttributeInstanceComboBox) {
            view = new PAttributeInstanceViewComboBox(controller, obj);
        } else if (model instanceof AttributeInstanceInt32) {
            view = new PAttributeInstanceViewInt32(controller, obj);
        } else if (model instanceof AttributeInstanceObjRef) {
            view = new PAttributeInstanceViewObjRef(controller, obj);
        } else if (model instanceof AttributeInstanceSDFile) {
            view = new PAttributeInstanceViewSDFile(controller, obj);
        } else if (model instanceof AttributeInstanceSpinner) {
            view = new PAttributeInstanceViewSpinner(controller, obj);
        } else if (model instanceof AttributeInstanceTablename) {
            view = new PAttributeInstanceViewTablename(controller, obj);
        } else if (model instanceof AttributeInstanceTextEditor) {
            view = new PAttributeInstanceViewTextEditor(controller, obj);
        } else {
            view = null;
            throw new Error("unkonwn attribute type");
        }
        /*
         // these have different constructors... FIXME
         } else if (model instanceof AttributeInstanceWavefile) {
         return new AttributeInstanceWavefile((AttributeInstanceWavefile)model, obj);
         }
         */
        view.PostConstructor();
        controller.addView(view);
        return view;
    }
}
