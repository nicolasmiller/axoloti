package axoloti.piccolo.displayviews;

import java.beans.PropertyChangeEvent;

import axoloti.displays.DisplayInstance;
import axoloti.displays.DisplayInstanceController;
import axoloti.objectviews.IAxoObjectInstanceView;

import components.piccolo.displays.PScopeComponent;

public class PDisplayInstanceViewFrac32SChart extends PDisplayInstanceViewFrac32 {

    private PScopeComponent scope;
    private IAxoObjectInstanceView axoObjectInstanceView;

    public PDisplayInstanceViewFrac32SChart(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
	this.axoObjectInstanceView = axoObjectInstanceView;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        scope = new PScopeComponent(-64.0, 64.0, axoObjectInstanceView);
        scope.setValue(0);
        addChild(scope);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (DisplayInstance.DISP_VALUE.is(evt)) {
            scope.setValue((Double) evt.getNewValue());
        }
    }

}
