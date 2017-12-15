package axoloti.piccolo.inlets;

import axoloti.abstractui.INetView;
import axoloti.abstractui.IIoletInstanceView;
import axoloti.patch.object.inlet.InletInstance;
import axoloti.patch.object.iolet.IoletInstanceController;
import axoloti.swingui.patch.object.iolet.IoletInstancePopupMenu;
import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.piccolo.iolet.PIoletAbstract;
import axoloti.piccolo.components.PJackInputComponent;
import axoloti.piccolo.components.PLabelComponent;
import axoloti.piccolo.components.PSignalMetaDataIcon;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

import static java.awt.Component.LEFT_ALIGNMENT;

public class PInletInstanceView extends PIoletAbstract implements IIoletInstanceView {
    IoletInstanceController controller;
    IoletInstancePopupMenu popup;
    PLabelComponent label = new PLabelComponent("");

    public PInletInstanceView(IoletInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(axoObjectInstanceView);
	this.controller = controller;

        popup = new IoletInstancePopupMenu(getController());
    }

    @Override
    public InletInstance getModel() {
        return (InletInstance) controller.getModel();
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

        jack = new PJackInputComponent(this);
        ((PJackInputComponent) jack).setForeground(getModel().getModel().getDatatype().GetColor());

        addChild(jack);
        addChild(new PSignalMetaDataIcon(getModel().getModel().GetSignalMetaData(), axoObjectInstanceView));

        if (axoObjectInstanceView.getModel().getType().getInlets().size() > 1) {
            addToSwingProxy(Box.createHorizontalStrut(3));
	    label.setText(getModel().getModel().getName());
            addChild(label);
        }
        addToSwingProxy(Box.createHorizontalGlue());

        addInputEventListener(getInputEventHandler());
        addInputEventListener(toolTipEventListener);
        setAlignmentX(LEFT_ALIGNMENT);
    }

    public void setHighlighted(boolean highlighted) {
        if (axoObjectInstanceView != null
                && axoObjectInstanceView.getPatchView() != null) {
            INetView netView = axoObjectInstanceView.getPatchView().GetNetView(this);
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

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (InletInstance.NAME.is(evt)) {
	    label.setText((String) evt.getNewValue());
        } else if (InletInstance.DESCRIPTION.is(evt)) {
	    axoObjectInstanceView.getCanvas().setToolTipText((String) evt.getNewValue());
        }
    }

    @Override
    public IoletInstanceController getController() {
        return controller;
    }

    @Override
    public void dispose() {
    }
}
