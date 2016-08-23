package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstanceInt32Label;
import axoloti.objectviews.IAxoObjectInstanceView;
import components.piccolo.PLabelComponent;

public class PDisplayInstanceViewInt32Label extends PDisplayInstanceViewInt32 {

    private DisplayInstanceInt32Label displayInstance;
    private PLabelComponent readout;

    public PDisplayInstanceViewInt32Label(DisplayInstanceInt32Label displayInstance, IAxoObjectInstanceView axoObjectInstanceView) {
        super(displayInstance, axoObjectInstanceView);
        this.displayInstance = displayInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        readout = new PLabelComponent("xxxxxx");
        addChild(readout);
    }

    @Override
    public void updateV() {
        readout.setText(Integer.toString(displayInstance.getValueRef().getInt()));
    }
}
