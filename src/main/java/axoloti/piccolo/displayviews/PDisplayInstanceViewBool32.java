package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstanceBool32;
import components.piccolo.displays.PLedstripComponent;

public class PDisplayInstanceViewBool32 extends PDisplayInstanceViewInt32 {

    DisplayInstanceBool32 displayInstance;
    private PLedstripComponent readout;

    public PDisplayInstanceViewBool32(DisplayInstanceBool32 displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        readout = new PLedstripComponent(0, 1);
        addChild(readout);
    }

    @Override
    public void updateV() {
        readout.setValue(displayInstance.getValueRef().getInt() > 0 ? 1 : 0);
    }
}
