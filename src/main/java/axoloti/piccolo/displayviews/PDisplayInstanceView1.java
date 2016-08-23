package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstance1;

public abstract class PDisplayInstanceView1 extends PDisplayInstanceView {

    DisplayInstance1 displayInstance;

    PDisplayInstanceView1(DisplayInstance1 displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }
}
