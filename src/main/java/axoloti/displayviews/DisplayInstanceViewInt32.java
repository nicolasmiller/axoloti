/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.displayviews;

import axoloti.displays.DisplayInstanceInt32;

/**
 *
 * @author nicolas
 */
public abstract class DisplayInstanceViewInt32 extends DisplayInstanceView1 {
    DisplayInstanceInt32 displayInstance;
    
    DisplayInstanceViewInt32(DisplayInstanceInt32 displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }
}