package axoloti.piccolo.displayviews;

import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.piccolo.components.displays.PLedstripComponent;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import axoloti.displays.DisplayInstance;
import axoloti.displays.DisplayInstanceController;

public class PDisplayInstanceViewInt32Bar32 extends PDisplayInstanceViewInt32 {

    private PLedstripComponent readout;
    private IAxoObjectInstanceView axoObjectInstanceView;

    public PDisplayInstanceViewInt32Bar32(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
	this.axoObjectInstanceView = axoObjectInstanceView;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        readout = new PLedstripComponent(0, 32, axoObjectInstanceView);
        addChild(readout);
        readout.setSize(new Dimension(roundUp(readout.getHeight()), 80));
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (DisplayInstance.DISP_VALUE.is(evt)) {
            int i = (Integer) evt.getNewValue();
            if ((i >= 0) && (i < 32)) {
                readout.setValue(1 << i);
            } else {
                readout.setValue(0);
            }
        }
    }
}
