package axoloti.piccolo.outlets;

import axoloti.INetView;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.outlets.IOutletInstanceView;
import axoloti.outlets.OutletInstance;
import axoloti.outlets.OutletInstancePopupMenu;
import axoloti.piccolo.iolet.PIoletAbstract;
import components.piccolo.PJackOutputComponent;
import components.piccolo.PLabelComponent;
import components.piccolo.PSignalMetaDataIcon;
import javax.swing.Box;
import javax.swing.JPopupMenu;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class POutletInstanceView extends PIoletAbstract implements IOutletInstanceView {

    OutletInstancePopupMenu popup;
    OutletInstance outletInstance;

    public POutletInstanceView(OutletInstance outletInstance, IAxoObjectInstanceView axoObjectInstanceView) {
        super(axoObjectInstanceView);
        this.outletInstance = outletInstance;

        popup = new OutletInstancePopupMenu(this);
    }

    private final PBasicInputEventHandler toolTipEventListener = new PBasicInputEventHandler() {
        @Override
        public void mouseEntered(PInputEvent e) {
            axoObjectInstanceView.getCanvas().setToolTipText(outletInstance.getOutlet().getDescription());
        }

        @Override
        public void mouseExited(PInputEvent e) {
            axoObjectInstanceView.getCanvas().setToolTipText(null);
        }
    };

    @Override
    public void PostConstructor() {
        getContainer().add(Box.createHorizontalGlue());
        if (axoObjectInstanceView.getObjectInstance().getType().GetOutlets().size() > 1) {
            addChild(new PLabelComponent(outletInstance.getOutlet().getName()), Anchor.EAST);
            getContainer().add(Box.createHorizontalStrut(2));
        }
        addChild(new PSignalMetaDataIcon(outletInstance.getOutlet().GetSignalMetaData(), axoObjectInstanceView), Anchor.EAST);
        jack = new PJackOutputComponent(this);
        ((PJackOutputComponent) jack).setForeground(outletInstance.getOutlet().getDatatype().GetColor());
        addChild(jack, Anchor.EAST);

        setBounds(0, 0, getContainer().getWidth(), getContainer().getHeight());
        addInputEventListener(getInputEventHandler());
        addInputEventListener(toolTipEventListener);
    }

    @Override
    public OutletInstance getOutletInstance() {
        return outletInstance;
    }

    @Override
    public void setHighlighted(boolean highlighted) {
        if (axoObjectInstanceView != null
                && axoObjectInstanceView.getPatchView() != null) {
            INetView netView = axoObjectInstanceView.getPatchView().GetNetView((IOutletInstanceView) this);
            if (netView != null
                    && netView.getSelected() != highlighted) {
                netView.setSelected(highlighted);
            }
        }
    }

    @Override
    public void disconnect() {
        getPatchView().getPatchController().disconnect(this);
    }

    @Override
    public void deleteNet() {
        getPatchView().getPatchController().deleteNet(this);
    }

    @Override
    public JPopupMenu getPopup() {
        return popup;
    }
}
