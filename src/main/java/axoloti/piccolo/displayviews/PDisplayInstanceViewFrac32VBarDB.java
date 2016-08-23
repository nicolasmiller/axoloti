package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstanceFrac32VBarDB;
import components.piccolo.displays.PVBarComponentDB;

public class PDisplayInstanceViewFrac32VBarDB extends PDisplayInstanceViewFrac32 {

    DisplayInstanceFrac32VBarDB displayInstance;

    public PDisplayInstanceViewFrac32VBarDB(DisplayInstanceFrac32VBarDB displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }

    private PVBarComponentDB vbar;

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        vbar = new PVBarComponentDB(-200, -60, 10);
        vbar.setValue(0);
        addChild(vbar);
    }

    @Override
    public void updateV() {
        vbar.setValue(displayInstance.getValueRef().getDouble());
    }
}
