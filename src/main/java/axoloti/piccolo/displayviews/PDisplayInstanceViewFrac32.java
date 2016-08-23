package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstanceFrac32;

public abstract class PDisplayInstanceViewFrac32 extends PDisplayInstanceView1 {

    DisplayInstanceFrac32 displayInstance;

    PDisplayInstanceViewFrac32(DisplayInstanceFrac32 displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }
}
