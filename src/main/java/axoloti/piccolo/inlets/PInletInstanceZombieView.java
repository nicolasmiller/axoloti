package axoloti.piccolo.inlets;

import axoloti.inlets.IInletInstanceView;
import axoloti.inlets.InletInstanceZombie;
import axoloti.piccolo.objectviews.PAxoObjectInstanceViewAbstract;
import components.piccolo.PJackInputComponent;
import components.piccolo.PLabelComponent;
import javax.swing.BoxLayout;

public class PInletInstanceZombieView extends PInletInstanceView implements IInletInstanceView {

    InletInstanceZombie inletInstanceZombie;

    public PInletInstanceZombieView(InletInstanceZombie inletInstanceZombie, PAxoObjectInstanceViewAbstract o) {
        super(inletInstanceZombie, o);
        this.inletInstanceZombie = inletInstanceZombie;
    }

    @Override
    public void PostConstructor() {
        setLayout(new BoxLayout(this.getProxyComponent(), BoxLayout.LINE_AXIS));

        addChild(new PLabelComponent(inletInstanceZombie.inletname));
        jack = new PJackInputComponent(this);
        jack.setForeground(inletInstanceZombie.getDataType().GetColor());

        addChild(jack);
        this.addInputEventListener(getInputEventHandler());
    }

}
