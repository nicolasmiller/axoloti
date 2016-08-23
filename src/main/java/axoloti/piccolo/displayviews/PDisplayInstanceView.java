package axoloti.piccolo.displayviews;

import axoloti.ModelChangedListener;
import axoloti.displays.DisplayInstance;
import axoloti.displayviews.IDisplayInstanceView;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import axoloti.piccolo.PatchPNode;
import components.piccolo.PLabelComponent;

public abstract class PDisplayInstanceView extends PatchPNode implements ModelChangedListener, IDisplayInstanceView {

    DisplayInstance displayInstance;

    PDisplayInstanceView(DisplayInstance displayInstance) {
        this.displayInstance = displayInstance;
    }

    @Override
    public void PostConstructor() {
        setLayout(HORIZONTAL_CENTERED);
        setPickable(false);
        if ((displayInstance.getDefinition().noLabel == null) || (displayInstance.getDefinition().noLabel == false)) {
            addChild(new PLabelComponent(displayInstance.getDefinition().getName()));
        }
        setBounds(this.getUnionOfChildrenBounds(null));
    }

    @Override
    public abstract void updateV();

    @Override
    public void modelChanged() {
        updateV();
    }
}
