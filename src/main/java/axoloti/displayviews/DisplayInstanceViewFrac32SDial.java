/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.displayviews;

import axoloti.displays.DisplayInstanceFrac32SDial;
import components.displays.DispComponent;

/**
 *
 * @author nicolas
 */
public class DisplayInstanceViewFrac32SDial extends DisplayInstanceViewFrac32 {

    private DispComponent dial;

    DisplayInstanceFrac32SDial displayInstance;

    DisplayInstanceViewFrac32SDial(DisplayInstanceFrac32SDial displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        dial = new DispComponent(0.0, -64.0, 64.0);
        add(dial);
    }

    @Override
    public void updateV() {
        dial.setValue(displayInstance.getValueRef().getDouble());
    }
}
