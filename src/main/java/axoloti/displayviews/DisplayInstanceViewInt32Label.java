/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.displayviews;

import axoloti.displays.DisplayInstanceInt32Label;
import components.LabelComponent;

/**
 *
 * @author nicolas
 */
public class DisplayInstanceViewInt32Label extends DisplayInstanceViewInt32 {
    private DisplayInstanceInt32Label displayInstance;
    private LabelComponent readout;

    DisplayInstanceViewInt32Label(DisplayInstanceInt32Label displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }
    
    @Override
    public void PostConstructor() {
        super.PostConstructor();

        readout = new LabelComponent("xxxxxx");
        add(readout);
        readout.setSize(80, 18);
    }

    @Override
    public void updateV() {
        readout.setText(Integer.toString(displayInstance.getValueRef().getInt()));
    }
}