package axoloti.piccolo.inlets;

import axoloti.INetView;
import axoloti.inlets.IInletInstanceView;
import axoloti.inlets.InletInstance;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import axoloti.piccolo.iolet.PIoletAbstract;
import axoloti.piccolo.objectviews.PAxoObjectInstanceView;
import components.piccolo.PJackInputComponent;
import components.piccolo.PLabelComponent;
import components.piccolo.PSignalMetaDataIcon;

public class PInletInstanceView extends PIoletAbstract implements IInletInstanceView {

    //   PiccoloInletInstancePopupMenu popup;
    InletInstance inletInstance;

    public PInletInstanceView(InletInstance inletInstance, PAxoObjectInstanceView axoObj) {
        super();
        this.inletInstance = inletInstance;
        this.axoObj = axoObj;

//        popup = new PInletInstancePopupMenu(axoObj.getPApplet(), this);
    }

    @Override
    public void PostConstructor() {
        this.setLayout(HORIZONTAL_CENTERED);

//        setBackground(Theme.getCurrentTheme().Object_Default_Background);
        addChild(new PSignalMetaDataIcon(inletInstance.getInlet().GetSignalMetaData()));
        jack = new PJackInputComponent(this);

//        jack.setForeground(inletInstance.getInlet().getDatatype().GetColor());
        ((PJackInputComponent) jack).setForeground(inletInstance.getInlet().getDatatype().GetColor());

        addChild(jack);
        if (axoObj.getObjectInstance().getType().GetInlets().size() > 1) {
            addChild(new PLabelComponent(inletInstance.getInlet().getName()));
        }

        this.setBounds(0, 0, getChildrenWidth(), getChildrenHeight());

// TODO tooltips
//        setToolTipText(outletInstance.getOutlet().getDescription());
        this.addInputEventListener(inputEventListener);
    }

    @Override
    public InletInstance getInletInstance() {
        return inletInstance;
    }

    public void disconnect() {
        getPatchView().getPatchController().disconnect(this);
    }

    public void deleteNet() {
        getPatchView().getPatchController().deleteNet(this);
    }

    public void setHighlighted(boolean highlighted) {
//        if ((getRootPane() == null
//                || getRootPane().getCursor() != MainFrame.transparentCursor)

        if (axoObj != null
                && axoObj.getPatchView() != null) {
            INetView netView = axoObj.getPatchView().GetNetView((IInletInstanceView) this);
            if (netView != null
                    && netView.getSelected() != highlighted) {
                netView.setSelected(highlighted);
            }
        }
    }
}
