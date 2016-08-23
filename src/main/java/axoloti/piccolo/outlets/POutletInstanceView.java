package axoloti.piccolo.outlets;

import axoloti.INetView;
import axoloti.outlets.IOutletInstanceView;
import axoloti.outlets.OutletInstance;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import axoloti.piccolo.iolet.PIoletAbstract;
import axoloti.piccolo.objectviews.PAxoObjectInstanceView;
import components.piccolo.PJackOutputComponent;
import components.piccolo.PLabelComponent;
import components.piccolo.PSignalMetaDataIcon;

public class POutletInstanceView extends PIoletAbstract implements IOutletInstanceView {

    //   PiccoloInletInstancePopupMenu popup;
    OutletInstance outletInstance;

    public POutletInstanceView(OutletInstance outletInstance, PAxoObjectInstanceView axoObj) {
        super();
        this.outletInstance = outletInstance;
        this.axoObj = axoObj;

//        popup = new PInletInstancePopupMenu(axoObj.getPApplet(), this);
    }

    @Override
    public void PostConstructor() {
        setLayout(HORIZONTAL_CENTERED);
//        setBackground(Theme.getCurrentTheme().Object_Default_Background);

        if (axoObj.getObjectInstance().getType().GetOutlets().size() > 1) {
            addChild(new PLabelComponent(outletInstance.getOutlet().getName()));
//            add(new PLabelComponent(p, outletInstance.getOutlet().getName()));
        }
        addChild(new PSignalMetaDataIcon(outletInstance.getOutlet().GetSignalMetaData()));
        jack = new PJackOutputComponent(this);
        ((PJackOutputComponent) jack).setForeground(outletInstance.getOutlet().getDatatype().GetColor());
        addChild(jack);

        this.setBounds(0, 0, getChildrenWidth(), getChildrenHeight());

// TODO tooltips
//        setToolTipText(outletInstance.getOutlet().getDescription());
        this.addInputEventListener(inputEventListener);
    }

    @Override
    public OutletInstance getOutletInstance() {
        return outletInstance;
    }

    public void setHighlighted(boolean highlighted) {
// TODO cursor handling
//        if ((getRootPane() == null
//                || getRootPane().getCursor() != MainFrame.transparentCursor)
        if (axoObj != null
                && axoObj.getPatchView() != null) {
            INetView netView = axoObj.getPatchView().GetNetView((IOutletInstanceView) this);
            if (netView != null
                    && netView.getSelected() != highlighted) {
                netView.setSelected(highlighted);
            }
        }
    }

    public void disconnect() {
        getPatchView().getPatchController().disconnect(this);
    }

    public void deleteNet() {
        getPatchView().getPatchController().deleteNet(this);
    }
}
