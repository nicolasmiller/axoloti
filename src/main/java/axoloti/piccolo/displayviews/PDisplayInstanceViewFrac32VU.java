package axoloti.piccolo.displayviews;

import java.beans.PropertyChangeEvent;

import axoloti.displays.DisplayInstance;
import axoloti.displays.DisplayInstanceController;
import axoloti.objectviews.IAxoObjectInstanceView;

import components.piccolo.displays.PVUComponent;

public class PDisplayInstanceViewFrac32VU extends PDisplayInstanceViewFrac32 {
    private IAxoObjectInstanceView axoObjectInstanceView;

    public PDisplayInstanceViewFrac32VU(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
	this.axoObjectInstanceView = axoObjectInstanceView;
    }

    private PVUComponent vu;

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        vu = new PVUComponent(axoObjectInstanceView);
        vu.setValue(0);
        addChild(vu);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (DisplayInstance.DISP_VALUE.is(evt)) {
            vu.setValue((Double) evt.getNewValue());
        }
    }
}
