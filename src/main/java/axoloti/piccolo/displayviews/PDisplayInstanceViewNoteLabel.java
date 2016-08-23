package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstanceNoteLabel;
import components.piccolo.PLabelComponent;

public class PDisplayInstanceViewNoteLabel extends PDisplayInstanceViewFrac32 {

    DisplayInstanceNoteLabel displayInstance;

    public PDisplayInstanceViewNoteLabel(DisplayInstanceNoteLabel displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }

    private PLabelComponent readout;

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        readout = new PLabelComponent("xxxxx");
        addChild(readout);
    }

    @Override
    public void updateV() {
        readout.setText(displayInstance.getConv().ToReal(displayInstance.getValueRef()));
    }
}
