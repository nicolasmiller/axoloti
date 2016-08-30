package axoloti.pinlets;

import axoloti.processing.PComponent;
import processing.core.PApplet;

public class PInletInstancePopupMenu extends PComponent {

    PInletInstanceView view;

    public PInletInstancePopupMenu(PApplet p, PInletInstanceView v) {
        super(p);
        this.view = v;
    }
}
