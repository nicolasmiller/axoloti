package axoloti.piccolo.outlets;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;

import axoloti.abstractui.IOutletInstanceView;
import axoloti.patch.object.outlet.OutletInstanceController;
import axoloti.piccolo.components.PJackOutputComponent;
import axoloti.piccolo.components.PLabelComponent;
import axoloti.piccolo.objectviews.PAxoObjectInstanceViewAbstract;

public class POutletInstanceZombieView extends POutletInstanceView implements IOutletInstanceView {

    public POutletInstanceZombieView(OutletInstanceController controller, PAxoObjectInstanceViewAbstract o) {
        super(controller, o);
    }

    @Override
    public void PostConstructor() {
        setLayout(new BoxLayout(getProxyComponent(), BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(32767, 14));

        addToSwingProxy(Box.createHorizontalGlue());
        addChild(new PLabelComponent(getModel().getName()));
        addToSwingProxy(Box.createHorizontalStrut(2));
        jack = new PJackOutputComponent(this);
        jack.setForeground(getModel().getDataType().GetColor());
        addChild(jack);
        addInputEventListener(getInputEventHandler());
    }
}
