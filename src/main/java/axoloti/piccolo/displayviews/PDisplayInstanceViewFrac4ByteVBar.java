package axoloti.piccolo.displayviews;

import java.beans.PropertyChangeEvent;

import axoloti.displays.DisplayInstance;
import axoloti.displays.DisplayInstanceController;
import axoloti.objectviews.IAxoObjectInstanceView;

import components.piccolo.displays.PVLineComponent;

public class PDisplayInstanceViewFrac4ByteVBar extends PDisplayInstanceViewFrac32 {

    private PVLineComponent vbar[];
    private IAxoObjectInstanceView axoObjectInstanceView;

    public PDisplayInstanceViewFrac4ByteVBar(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
	this.axoObjectInstanceView = axoObjectInstanceView;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        vbar = new PVLineComponent[4];
        for (int i = 0; i < 4; i++) {
            vbar[i] = new PVLineComponent(0, -64, 64, axoObjectInstanceView);
            vbar[i].setValue(0);
            addChild(vbar[i]);
        }
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (DisplayInstance.DISP_VALUE.is(evt)) {
            int raw = (Integer) evt.getNewValue();
            vbar[0].setValue((byte) ((raw & 0x000000FF)));
            vbar[1].setValue((byte) ((raw & 0x0000FF00) >> 8));
            vbar[2].setValue((byte) ((raw & 0x00FF0000) >> 16));
            vbar[3].setValue((byte) ((raw & 0xFF000000) >> 24));
        }
    }
}
