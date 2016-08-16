package axoloti.displayviews;

import axoloti.displays.DisplayInstanceFrac8S128VBar;
import components.VGraphComponent;
import java.nio.ByteBuffer;

public class DisplayInstanceViewFrac8S128VBar extends DisplayInstanceView {

    DisplayInstanceFrac8S128VBar displayInstance;
    private VGraphComponent vgraph;

    DisplayInstanceViewFrac8S128VBar(DisplayInstanceFrac8S128VBar displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }
    
    @Override
    public void PostConstructor() {
        super.PostConstructor();
        vgraph = new VGraphComponent(displayInstance.getN(), 128, -64, 64);
        add(vgraph);
    }
    
    @Override
    public void updateV() {

    }
    
    public void ProcessByteBuffer(ByteBuffer bb) {
        displayInstance.ProcessByteBuffer(bb);
        vgraph.setValue(displayInstance.getIDst());
    }
}