package axoloti.piccolo.inlets;

import axoloti.INetView;
import axoloti.inlets.IInletInstanceView;
import axoloti.inlets.InletInstance;
import axoloti.inlets.InletInstancePopupMenu;
import axoloti.objectviews.IAxoObjectInstanceView;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import axoloti.piccolo.iolet.PIoletAbstract;
import components.piccolo.PJackInputComponent;
import components.piccolo.PLabelComponent;
import components.piccolo.PSignalMetaDataIcon;
import javax.swing.JPopupMenu;

public class PInletInstanceView extends PIoletAbstract implements IInletInstanceView {

    InletInstancePopupMenu popup;
    InletInstance inletInstance;

    public PInletInstanceView(InletInstance inletInstance, IAxoObjectInstanceView axoObjectInstanceView) {
        super(axoObjectInstanceView);
        this.inletInstance = inletInstance;

        popup = new InletInstancePopupMenu(this);
    }

    @Override
    public void PostConstructor() {
        this.setLayout(HORIZONTAL_CENTERED);

//        setBackground(Theme.getCurrentTheme().Object_Default_Background);
        addChild(new PSignalMetaDataIcon(inletInstance.getInlet().GetSignalMetaData(), axoObjectInstanceView));
        jack = new PJackInputComponent(this);

//        jack.setForeground(inletInstance.getInlet().getDatatype().GetColor());
        ((PJackInputComponent) jack).setForeground(inletInstance.getInlet().getDatatype().GetColor());

        addChild(jack);
        if (axoObjectInstanceView.getObjectInstance().getType().GetInlets().size() > 1) {
            addChild(new PLabelComponent(inletInstance.getInlet().getName()));
        }

        this.setBounds(0, 0, getChildrenWidth(), getChildrenHeight());

// TODO tooltips
//        setToolTipText(outletInstance.getOutlet().getDescription());
        this.addInputEventListener(getInputEventHandler());
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
