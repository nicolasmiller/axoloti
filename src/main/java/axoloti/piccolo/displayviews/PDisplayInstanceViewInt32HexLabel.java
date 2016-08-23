package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstanceInt32HexLabel;
import components.piccolo.PLabelComponent;

public class PDisplayInstanceViewInt32HexLabel extends PDisplayInstanceViewInt32 {

    private DisplayInstanceInt32HexLabel displayInstance;
    private PLabelComponent readout;

    public PDisplayInstanceViewInt32HexLabel(DisplayInstanceInt32HexLabel displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        readout = new PLabelComponent("0xxxxxxxxx");
        addChild(readout);
    }

    @Override
    public void updateV() {
        readout.setText(String.format("0x%08X", displayInstance.getValueRef().getInt()));
    }
}
