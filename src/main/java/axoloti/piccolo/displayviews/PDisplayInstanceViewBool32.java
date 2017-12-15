package axoloti.piccolo.displayviews;

import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.piccolo.components.displays.PLedstripComponent;
import java.beans.PropertyChangeEvent;

import axoloti.displays.DisplayInstance;
import axoloti.displays.DisplayInstanceController;

public class PDisplayInstanceViewBool32 extends PDisplayInstanceViewInt32 {

    private PLedstripComponent readout;
    private IAxoObjectInstanceView axoObjectInstanceView;

    public PDisplayInstanceViewBool32(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
	this.axoObjectInstanceView = axoObjectInstanceView;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        readout = new PLedstripComponent(0, 1, axoObjectInstanceView);
        addChild(readout);
        readout.setSize(new Dimension(roundUp(readout.getHeight()), 80));
        setSize(getPreferredSize());
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (DisplayInstance.DISP_VALUE.is(evt)) {
            int i = (Integer) evt.getNewValue();
            readout.setValue(i > 0 ? 1 : 0);
        }
    }
}
