/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.outlets;

import components.LabelComponent;
import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 *
 * @author nicolas
 */
public class OutletInstanceZombieView extends OutletInstanceView {
    
    public OutletInstanceZombieView(OutletInstanceZombie outletInstanceZombie) {
        super(outletInstanceZombie);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        jack = new components.JackOutputComponent(this);
        add(jack);
        add(Box.createHorizontalStrut(2));
        add(new LabelComponent(outletInstanceZombie.outletname));
        add(Box.createHorizontalGlue());
        setComponentPopupMenu(popup);
    }
}