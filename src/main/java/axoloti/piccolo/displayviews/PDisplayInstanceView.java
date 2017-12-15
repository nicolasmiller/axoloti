package axoloti.piccolo.displayviews;

import axoloti.patch.object.display.DisplayInstance;
import axoloti.patch.object.display.DisplayInstanceController;
import axoloti.abstractui.IDisplayInstanceView;
import axoloti.mvc.AbstractController;
import axoloti.mvc.IView;
import axoloti.piccolo.PatchPNode;
import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.piccolo.components.PLabelComponent;
import java.beans.PropertyChangeEvent;
import javax.swing.BoxLayout;

import static java.awt.Component.RIGHT_ALIGNMENT;

public abstract class PDisplayInstanceView extends PatchPNode implements IDisplayInstanceView {
    DisplayInstanceController controller;
    PLabelComponent label;

    PDisplayInstanceView(DisplayInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(axoObjectInstanceView.getPatchView());
	this.controller = controller;
    }

    DisplayInstance getModel() {
        return getController().getModel();
    }

    public void PostConstructor() {
        setLayout(new BoxLayout(getProxyComponent(), BoxLayout.LINE_AXIS));
        setPickable(false);
	label = new PLabelComponent("");
        addChild(label);
        setSize(getPreferredSize());
        setAlignmentX(RIGHT_ALIGNMENT);
    }

    @Override
    public DisplayInstanceController getController() {
        return controller;
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (DisplayInstance.NAME.is(evt)) {
            label.setText((String) evt.getNewValue());
        } else if (DisplayInstance.NOLABEL.is(evt)) {
            Boolean b = (Boolean) evt.getNewValue();
            if (b == null) {
                b = false;
            }
            label.setVisible(!b);
        } else if (DisplayInstance.DESCRIPTION.is(evt)) {
	    // TODO wtf
//            setToolTipText((String) evt.getNewValue());
        }
    }

    @Override
    public void dispose() {
    }
}
