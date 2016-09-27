package axoloti.piccolo.outlets;

import axoloti.outlets.IOutletInstanceView;
import axoloti.outlets.OutletInstanceZombie;
import axoloti.piccolo.objectviews.PAxoObjectInstanceViewAbstract;
import components.piccolo.PJackOutputComponent;
import components.piccolo.PLabelComponent;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_TOP;

public class POutletInstanceZombieView extends POutletInstanceView implements IOutletInstanceView {

    OutletInstanceZombie outletInstanceZombie;

    public POutletInstanceZombieView(OutletInstanceZombie outletInstanceZombie, PAxoObjectInstanceViewAbstract o) {
        super(outletInstanceZombie, o);
        this.outletInstanceZombie = outletInstanceZombie;
    }

    @Override
    public void PostConstructor() {
        setLayout(HORIZONTAL_TOP);
        addChild(new PLabelComponent(outletInstanceZombie.outletname));
        jack = new PJackOutputComponent(this);
        jack.setForeground(outletInstanceZombie.getDataType().GetColor());

        addChild(jack);
        this.addInputEventListener(getInputEventHandler());
    }
}
