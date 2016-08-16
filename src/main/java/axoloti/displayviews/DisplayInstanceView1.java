/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.displayviews;

import axoloti.displays.DisplayInstance1;

/**
 *
 * @author nicolas
 */
public abstract class DisplayInstanceView1 extends DisplayInstanceView {
    DisplayInstance1 displayInstance;
    
    DisplayInstanceView1(DisplayInstance1 displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }
}