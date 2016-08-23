package axoloti.piccolo.displayviews;

import axoloti.displays.DisplayInstanceVScale;
import components.piccolo.displays.PVValueLabelsComponent;

public class PDisplayInstanceViewVScale extends PDisplayInstanceView {

    DisplayInstanceVScale displayInstance;
    private PVValueLabelsComponent vlabels;

    public PDisplayInstanceViewVScale(DisplayInstanceVScale displayInstance) {
        super(displayInstance);
        this.displayInstance = displayInstance;
    }

    @Override
    public void updateV() {
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        vlabels = new PVValueLabelsComponent(-60, 10, 10);
        addChild(vlabels);
    }
}
