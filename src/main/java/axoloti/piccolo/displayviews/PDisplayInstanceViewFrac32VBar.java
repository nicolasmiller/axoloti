package axoloti.piccolo.displayviews;

import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.piccolo.components.displays.PVBarComponent;
import java.beans.PropertyChangeEvent;

import axoloti.displays.DisplayInstance;
import axoloti.displays.DisplayInstanceController;

public class PDisplayInstanceViewFrac32VBar extends PDisplayInstanceViewFrac32 {

    private PVBarComponent vbar;
    private IAxoObjectInstanceView axoObjectInstanceView;

    public PDisplayInstanceViewFrac32VBar(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
	this.axoObjectInstanceView = axoObjectInstanceView;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        vbar = new PVBarComponent(0, 0, 64, axoObjectInstanceView);
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
