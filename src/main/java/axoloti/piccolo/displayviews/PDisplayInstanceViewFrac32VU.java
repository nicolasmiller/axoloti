package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstanceFrac32VU;
import components.piccolo.displays.PVUComponent;

public class PDisplayInstanceViewFrac32VU extends PDisplayInstanceViewFrac32 {

    DisplayInstanceFrac32VU displayInstance;

    public PDisplayInstanceViewFrac32VU(DisplayInstanceFrac32VU displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }

    private PVUComponent vu;

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        vu = new PVUComponent();
        vu.setValue(0);
        addChild(vu);
    }

    @Override
    public void updateV() {
        vu.setValue(displayInstance.getValueRef().getDouble());
    }
}
