package axoloti.piccolo.displayviews;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import axoloti.displays.DisplayInstance;
import axoloti.displays.DisplayInstanceController;
import axoloti.objectviews.IAxoObjectInstanceView;

import components.piccolo.PLabelComponent;

public class PDisplayInstanceViewInt32HexLabel extends PDisplayInstanceViewInt32 {

    private PLabelComponent readout;

    public PDisplayInstanceViewInt32HexLabel(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        readout = new PLabelComponent("0xxxxxxxxx");
        addChild(readout);
        readout.setSize(new Dimension(80, 18));
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (DisplayInstance.DISP_VALUE.is(evt)) {
            int i = (Integer) evt.getNewValue();
            readout.setText(String.format("0x%08X", i));
        }
    }
}
