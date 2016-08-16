/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.displayviews;

import axoloti.displays.DisplayInstanceFrac8U128VBar;
import components.VGraphComponent;
import java.nio.ByteBuffer;

/**
 *
 * @author nicolas
 */
public class DisplayInstanceViewFrac8U128VBar extends DisplayInstanceView {

    DisplayInstanceFrac8U128VBar displayInstance;
    private VGraphComponent vgraph;

    public DisplayInstanceViewFrac8U128VBar(DisplayInstanceFrac8U128VBar displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        vgraph = new VGraphComponent(displayInstance.getN(), 128, 0, 128);
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