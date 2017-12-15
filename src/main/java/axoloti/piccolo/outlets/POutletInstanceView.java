package axoloti.piccolo.outlets;

import axoloti.INetView;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.outlets.IOutletInstanceView;
import axoloti.outlets.OutletInstance;
import axoloti.outlets.OutletInstanceController;
import axoloti.outlets.OutletInstancePopupMenu;
import axoloti.piccolo.iolet.PIoletAbstract;
import components.piccolo.PJackOutputComponent;
import components.piccolo.PLabelComponent;
import components.piccolo.PSignalMetaDataIcon;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class POutletInstanceView extends PIoletAbstract implements IOutletInstanceView {

    OutletInstanceController controller;
    PLabelComponent label = new PLabelComponent("");

    public POutletInstanceView(OutletInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(axoObjectInstanceView);
	this.controller = controller;
    }

    private final PBasicInputEventHandler toolTipEventListener = new PBasicInputEventHandler() {
        @Override
        public void mouseEntered(PInputEvent e) {
            if (e.getInputManager().getMouseFocus() == null) {
                axoObjectInstanceView.getCanvas().setToolTipText(getModel().getModel().getDescription());
            }
        }

        @Override
        public void mouseExited(PInputEvent e) {
            if (e.getInputManager().getMouseFocus() == null) {
                axoObjectInstanceView.getCanvas().setToolTipText(null);
            }
        }
    };

    @Override
    public void PostConstructor() {
        setLayout(new BoxLayout(getProxyComponent(), BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(32767, 14));

        addToSwingProxy(Box.createHorizontalGlue());

        if (axoObjectInstanceView.getModel().getType().getOutlets().size() > 1) {
	    label.setText(getModel().getModel().getName());
            addChild(label);
            addToSwingProxy(Box.createHorizontalStrut(2));
        }
        PSignalMetaDataIcon foo = new PSignalMetaDataIcon(getModel().getModel().GetSignalMetaData(), axoObjectInstanceView);
        addChild(foo);

        jack = new PJackOutputComponent(this);
        ((PJackOutputComponent) jack).setForeground(getModel().getModel().getDatatype().GetColor());
        addChild(jack);

        addInputEventListener(getInputEventHandler());
        addInputEventListener(toolTipEventListener);
    }

    @Override
    public OutletInstance getModel() {
	return controller.getModel();
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
    public JPopupMenu getPopup() {
        return new OutletInstancePopupMenu(getController());
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
	if (OutletInstance.NAME.is(evt)) {
	    label.setText((String) evt.getNewValue());
	} else if (OutletInstance.DESCRIPTION.is(evt)) {
	    axoObjectInstanceView.getCanvas().setToolTipText((String) evt.getNewValue());
	}
    }

    @Override
    public OutletInstanceController getController() {
        return controller;
    }

    @Override
    public void dispose() {
    }
}
