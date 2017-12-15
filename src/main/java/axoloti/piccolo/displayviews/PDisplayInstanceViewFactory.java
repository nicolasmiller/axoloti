package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstance;
import axoloti.displays.DisplayInstanceBool32;
import axoloti.displays.DisplayInstanceController;
import axoloti.displays.DisplayInstanceFrac32SChart;
import axoloti.displays.DisplayInstanceFrac32SDial;
import axoloti.displays.DisplayInstanceFrac32UChart;
import axoloti.displays.DisplayInstanceFrac32UDial;
import axoloti.displays.DisplayInstanceFrac32VBar;
import axoloti.displays.DisplayInstanceFrac32VBarDB;
import axoloti.displays.DisplayInstanceFrac32VU;
import axoloti.displays.DisplayInstanceFrac4ByteVBar;
import axoloti.displays.DisplayInstanceFrac4UByteVBar;
import axoloti.displays.DisplayInstanceFrac4UByteVBarDB;
import axoloti.displays.DisplayInstanceFrac8S128VBar;
import axoloti.displays.DisplayInstanceFrac8U128VBar;
import axoloti.displays.DisplayInstanceInt32Bar16;
import axoloti.displays.DisplayInstanceInt32Bar32;
import axoloti.displays.DisplayInstanceInt32HexLabel;
import axoloti.displays.DisplayInstanceInt32Label;
import axoloti.displays.DisplayInstanceNoteLabel;
import axoloti.displays.DisplayInstanceVScale;
import axoloti.objectviews.IAxoObjectInstanceView;

public class PDisplayInstanceViewFactory {

    public static PDisplayInstanceView createView(DisplayInstanceController controller, IAxoObjectInstanceView obj) {
        DisplayInstance model = controller.getModel();
        PDisplayInstanceView view;
        if (model instanceof DisplayInstanceBool32) {
            view = new PDisplayInstanceViewBool32(controller, obj);
        } else if (model instanceof DisplayInstanceFrac32SChart) {
            view = new PDisplayInstanceViewFrac32SChart(controller, obj);
        } else if (model instanceof DisplayInstanceFrac32SDial) {
            view = new PDisplayInstanceViewFrac32SDial(controller, obj);
        } else if (model instanceof DisplayInstanceFrac32UChart) {
            view = new PDisplayInstanceViewFrac32UChart(controller, obj);
        } else if (model instanceof DisplayInstanceFrac32UDial) {
            view = new PDisplayInstanceViewFrac32UDial(controller, obj);
        } else if (model instanceof DisplayInstanceFrac32VBar) {
            view = new PDisplayInstanceViewFrac32VBar(controller, obj);
        } else if (model instanceof DisplayInstanceFrac32VBarDB) {
            view = new PDisplayInstanceViewFrac32VBarDB(controller, obj);
        } else if (model instanceof DisplayInstanceFrac32VU) {
            view = new PDisplayInstanceViewFrac32VU(controller, obj);
        } else if (model instanceof DisplayInstanceFrac4ByteVBar) {
            view = new PDisplayInstanceViewFrac4ByteVBar(controller, obj);
        } else if (model instanceof DisplayInstanceFrac4UByteVBar) {
            view = new PDisplayInstanceViewFrac4UByteVBar(controller, obj);
        } else if (model instanceof DisplayInstanceFrac4UByteVBarDB) {
            view = new PDisplayInstanceViewFrac4UByteVBarDB(controller, obj);
        } else if (model instanceof DisplayInstanceFrac8S128VBar) {
            view = new PDisplayInstanceViewFrac8S128VBar(controller, obj);
        } else if (model instanceof DisplayInstanceFrac8U128VBar) {
            view = new PDisplayInstanceViewFrac8U128VBar(controller, obj);
        } else if (model instanceof DisplayInstanceInt32Bar16) {
            view = new PDisplayInstanceViewInt32Bar16(controller, obj);
        } else if (model instanceof DisplayInstanceInt32Bar32) {
            view = new PDisplayInstanceViewInt32Bar32(controller, obj);
        } else if (model instanceof DisplayInstanceInt32HexLabel) {
            view = new PDisplayInstanceViewInt32HexLabel(controller, obj);
        } else if (model instanceof DisplayInstanceInt32Label) {
            view = new PDisplayInstanceViewInt32Label(controller, obj);
        } else if (model instanceof DisplayInstanceNoteLabel) {
            view = new PDisplayInstanceViewNoteLabel(controller, obj);
        } else if (model instanceof DisplayInstanceVScale) {
            view = new PDisplayInstanceViewVScale(controller, obj);
        } else {
            view = null;
            throw new Error("unkown Display type");
        }
        view.PostConstructor();
        controller.addView(view);
        return view;
    }
}
