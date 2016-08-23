package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstanceFrac32SChart;
import components.piccolo.displays.PScopeComponent;

public class PDisplayInstanceViewFrac32SChart extends PDisplayInstanceViewFrac32 {

    DisplayInstanceFrac32SChart displayInstance;
    private PScopeComponent scope;

    public PDisplayInstanceViewFrac32SChart(DisplayInstanceFrac32SChart displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        scope = new PScopeComponent(-64.0, 64.0);
        scope.setValue(0);
        addChild(scope);
    }

    @Override
    public void updateV() {
        scope.setValue(displayInstance.getValueRef().getDouble());
    }
}
