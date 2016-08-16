/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.displayviews;

import axoloti.displays.DisplayInstanceFrac32VBar;
import components.displays.VBarComponent;

/**
 *
 * @author nicolas
 */
public class DisplayInstanceViewFrac32VBar extends DisplayInstanceViewFrac32 {
    DisplayInstanceFrac32VBar displayInstance;
    private VBarComponent vbar;

    public DisplayInstanceViewFrac32VBar(DisplayInstanceFrac32VBar displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }
    
    @Override
    public void PostConstructor() {
        super.PostConstructor();

        vbar = new VBarComponent(0, 0, 64);
        vbar.setValue(0);
        add(vbar);
    }

    @Override
    public void updateV() {
        vbar.setValue(displayInstance.getValueRef().getDouble());
    }
}