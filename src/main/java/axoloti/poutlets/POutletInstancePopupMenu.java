package axoloti.poutlets;

import axoloti.processing.PComponent;
import processing.core.PApplet;

public class POutletInstancePopupMenu extends PComponent {

    POutletInstanceView view;

    public POutletInstancePopupMenu(PApplet p, POutletInstanceView v) {
        super(p);
        this.view = v;
    }
}
