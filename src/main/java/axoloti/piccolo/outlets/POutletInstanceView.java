package axoloti.piccolo.outlets;

import axoloti.INetView;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.outlets.IOutletInstanceView;
import axoloti.outlets.OutletInstance;
import axoloti.outlets.OutletInstancePopupMenu;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import axoloti.piccolo.iolet.PIoletAbstract;
import components.piccolo.PJackOutputComponent;
import components.piccolo.PLabelComponent;
import components.piccolo.PSignalMetaDataIcon;
import javax.swing.JPopupMenu;

public class POutletInstanceView extends PIoletAbstract implements IOutletInstanceView {

    OutletInstancePopupMenu popup;
    OutletInstance outletInstance;

    public POutletInstanceView(OutletInstance outletInstance, IAxoObjectInstanceView axoObjectInstanceView) {
        super(axoObjectInstanceView);
        this.outletInstance = outletInstance;

        popup = new OutletInstancePopupMenu(this);
    }

    @Override
    public void PostConstructor() {
        setLayout(HORIZONTAL_CENTERED);
//        setBackground(Theme.getCurrentTheme().Object_Default_Background);

        if (axoObjectInstanceView.getObjectInstance().getType().GetOutlets().size() > 1) {
            addChild(new PLabelComponent(outletInstance.getOutlet().getName()));
//            add(new PLabelComponent(p, outletInstance.getOutlet().getName()));
        }
        addChild(new PSignalMetaDataIcon(outletInstance.getOutlet().GetSignalMetaData(), axoObjectInstanceView));
        jack = new PJackOutputComponent(this);
        ((PJackOutputComponent) jack).setForeground(outletInstance.getOutlet().getDatatype().GetColor());
        addChild(jack);

        this.setBounds(0, 0, getChildrenWidth(), getChildrenHeight());

// TODO tooltips
//        setToolTipText(outletInstance.getOutlet().getDescription());
        this.addInputEventListener(getInputEventHandler());
    }

    @Override
    public OutletInstance getOutletInstance() {
        return outletInstance;
    }

    @Override
    public void setHighlighted(boolean highlighted) {
// TODO cursor handling
//        if ((getRootPane() == null
//                || getRootPane().getCursor() != MainFrame.transparentCursor)
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
