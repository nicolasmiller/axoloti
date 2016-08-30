package axoloti.pattributeviews;

import axoloti.atom.PAtomInstanceView;
import axoloti.attributeviews.IAttributeInstanceView;
import processing.core.PApplet;

public abstract class PAttributeInstanceView extends PAtomInstanceView implements IAttributeInstanceView {

    public PAttributeInstanceView(PApplet p) {
        super(p);
    }

}
