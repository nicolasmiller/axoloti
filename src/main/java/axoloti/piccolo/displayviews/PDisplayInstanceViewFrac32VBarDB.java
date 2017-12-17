package axoloti.piccolo.displayviews;

import java.beans.PropertyChangeEvent;

import axoloti.displays.DisplayInstance;
import axoloti.displays.DisplayInstanceController;
import axoloti.objectviews.IAxoObjectInstanceView;

import components.piccolo.displays.PVBarComponentDB;

public class PDisplayInstanceViewFrac32VBarDB extends PDisplayInstanceViewFrac32 {

    private IAxoObjectInstanceView axoObjectInstanceView;
    public PDisplayInstanceViewFrac32VBarDB(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
	this.axoObjectInstanceView = axoObjectInstanceView;
    }

    private PVBarComponentDB vbar;

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        vbar = new PVBarComponentDB(-200, -60, 10, axoObjectInstanceView);
        vbar.setValue(0);
        addChild(vbar);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (DisplayInstance.DISP_VALUE.is(evt)) {
            vbar.setValue((Double) evt.getNewValue());
        }
    }
}
