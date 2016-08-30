package axoloti.pdisplayviews;

import axoloti.ModelChangedListener;
import axoloti.displayviews.IDisplayInstanceView;
import axoloti.processing.PComponent;
import processing.core.PApplet;

public abstract class PDisplayInstanceView extends PComponent implements ModelChangedListener, IDisplayInstanceView {

    public PDisplayInstanceView(PApplet p) {
        super(p);
    }
}
