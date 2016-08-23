package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstanceInt32;

public abstract class PDisplayInstanceViewInt32 extends PDisplayInstanceView1 {

    DisplayInstanceInt32 displayInstance;

    PDisplayInstanceViewInt32(DisplayInstanceInt32 displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }
}
