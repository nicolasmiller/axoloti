package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstanceFrac32VBar;
import components.piccolo.displays.PVBarComponent;

public class PDisplayInstanceViewFrac32VBar extends PDisplayInstanceViewFrac32 {

    DisplayInstanceFrac32VBar displayInstance;
    private PVBarComponent vbar;

    public PDisplayInstanceViewFrac32VBar(DisplayInstanceFrac32VBar displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        vbar = new PVBarComponent(0, 0, 64);
        vbar.setValue(0);
        addChild(vbar);
    }

    @Override
    public void updateV() {
        vbar.setValue(displayInstance.getValueRef().getDouble());
    }
}
