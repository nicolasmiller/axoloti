/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.inlets;

import components.LabelComponent;
import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 *
 * @author nicolas
 */
public class InletInstanceZombieView extends InletInstanceView {
    
    public InletInstanceZombieView(InletInstanceZombie inletInstanceZombie) {
        super(inletInstanceZombie);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        jack = new components.JackInputComponent(this);
        add(jack);
        add(Box.createHorizontalStrut(2));
        add(new LabelComponent(inletInstanceZombie.inletname));
        add(Box.createHorizontalGlue());
        setComponentPopupMenu(popup);
    }
}
