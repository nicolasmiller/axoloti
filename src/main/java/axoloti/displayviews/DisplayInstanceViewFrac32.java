/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.displayviews;

import axoloti.displays.DisplayInstanceFrac32;

/**
 *
 * @author nicolas
 */
public abstract class DisplayInstanceViewFrac32 extends DisplayInstanceView1 {

    DisplayInstanceFrac32 displayInstance;

    DisplayInstanceViewFrac32(DisplayInstanceFrac32 displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }
}
