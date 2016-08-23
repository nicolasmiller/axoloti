package axoloti.piccolo.inlets;

import axoloti.inlets.IInletInstanceView;
import axoloti.inlets.InletInstanceZombie;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import axoloti.piccolo.objectviews.PAxoObjectInstanceViewAbstract;
import components.piccolo.PJackInputComponent;
import components.piccolo.PLabelComponent;

public class PInletInstanceZombieView extends PInletInstanceView implements IInletInstanceView {

    InletInstanceZombie inletInstanceZombie;

    public PInletInstanceZombieView(InletInstanceZombie inletInstanceZombie, PAxoObjectInstanceViewAbstract o) {
        super(inletInstanceZombie, o);
        this.inletInstanceZombie = inletInstanceZombie;
    }

    @Override
    public void PostConstructor() {
        setLayout(HORIZONTAL_CENTERED);
        addChild(new PLabelComponent(inletInstanceZombie.inletname));
        jack = new PJackInputComponent(this);
        jack.setForeground(inletInstanceZombie.getDataType().GetColor());

        addChild(jack);
        this.addInputEventListener(getInputEventHandler());
    }

}
