package axoloti.piccolo.inlets;

import axoloti.INetView;
import axoloti.inlets.IInletInstanceView;
import axoloti.inlets.InletInstance;
import axoloti.inlets.InletInstancePopupMenu;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.piccolo.iolet.PIoletAbstract;
import components.piccolo.PJackInputComponent;
import components.piccolo.PLabelComponent;
import components.piccolo.PSignalMetaDataIcon;
import javax.swing.JPopupMenu;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_TOP;

public class PInletInstanceView extends PIoletAbstract implements IInletInstanceView {

    InletInstancePopupMenu popup;
    InletInstance inletInstance;

    public PInletInstanceView(InletInstance inletInstance, IAxoObjectInstanceView axoObjectInstanceView) {
        super(axoObjectInstanceView);
        this.inletInstance = inletInstance;

        popup = new InletInstancePopupMenu(this);
    }

    private final PBasicInputEventHandler toolTipEventListener = new PBasicInputEventHandler() {
        @Override
        public void mouseEntered(PInputEvent e) {
            axoObjectInstanceView.getCanvas().setToolTipText(inletInstance.getInlet().getDescription());
        }

        @Override
        public void mouseExited(PInputEvent e) {
            axoObjectInstanceView.getCanvas().setToolTipText(null);
        }
    };

    @Override
    public void PostConstructor() {
        setLayout(HORIZONTAL_TOP);

        jack = new PJackInputComponent(this);
        ((PJackInputComponent) jack).setForeground(inletInstance.getInlet().getDatatype().GetColor());

        addChild(jack);
        addChild(new PSignalMetaDataIcon(inletInstance.getInlet().GetSignalMetaData(), axoObjectInstanceView));

        if (axoObjectInstanceView.getObjectInstance().getType().GetInlets().size() > 1) {
            addChild(new PLabelComponent(inletInstance.getInlet().getName()));
        }

        setBounds(0, 0, getChildrenWidth(), getChildrenHeight());

        addInputEventListener(getInputEventHandler());
        addInputEventListener(toolTipEventListener);
    }

    @Override
    public InletInstance getInletInstance() {
        return inletInstance;
    }

    @Override
    public void disconnect() {
        getPatchView().getPatchController().disconnect(this);
    }

    @Override
    public void deleteNet() {
        getPatchView().getPatchController().deleteNet(this);
    }

    public void setHighlighted(boolean highlighted) {
        if (axoObjectInstanceView != null
                && axoObjectInstanceView.getPatchView() != null) {
            INetView netView = axoObjectInstanceView.getPatchView().GetNetView((IInletInstanceView) this);
            if (netView != null
                    && netView.getSelected() != highlighted) {
                netView.setSelected(highlighted);
            }
        }
    }

    @Override
    public JPopupMenu getPopup() {
        return popup;
    }
}
