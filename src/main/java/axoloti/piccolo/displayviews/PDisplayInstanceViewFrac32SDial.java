package axoloti.piccolo.displayviews;

import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.piccolo.components.displays.PDispComponent;
import java.beans.PropertyChangeEvent;

import axoloti.displays.DisplayInstance;
import axoloti.displays.DisplayInstanceController;

public class PDisplayInstanceViewFrac32SDial extends PDisplayInstanceViewFrac32 {

    private PDispComponent dial;
    private IAxoObjectInstanceView axoObjectInstanceView;

    public PDisplayInstanceViewFrac32SDial(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
	this.axoObjectInstanceView = axoObjectInstanceView;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        dial = new PDispComponent(0.0, -64.0, 64.0, axoObjectInstanceView);
        addChild(dial);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (DisplayInstance.DISP_VALUE.is(evt)) {
            dial.setValue((Double) evt.getNewValue());
        }
    }
}
