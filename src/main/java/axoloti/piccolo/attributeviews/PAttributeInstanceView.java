package axoloti.piccolo.attributeviews;

import axoloti.PatchView;
import axoloti.attribute.AttributeInstance;
import axoloti.attribute.AttributeInstanceController;
import axoloti.attributeviews.IAttributeInstanceView;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.piccolo.PatchPNode;
import components.piccolo.PLabelComponent;
import java.beans.PropertyChangeEvent;
import javax.swing.BoxLayout;

public abstract class PAttributeInstanceView extends PatchPNode implements IAttributeInstanceView {

    IAxoObjectInstanceView axoObjectInstanceView;

    final AttributeInstanceController controller;

    PLabelComponent label;

    PAttributeInstanceView(AttributeInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(axoObjectInstanceView.getPatchView());
        this.controller = controller;
        this.axoObjectInstanceView = axoObjectInstanceView;
    }

    @Override
    public abstract void Lock();

    @Override
    public abstract void UnLock();

    void PostConstructor() {
        setLayout(new BoxLayout(getProxyComponent(), BoxLayout.LINE_AXIS));
        setPickable(false);
        label = new PLabelComponent(getModel().getModel().getName());
        addChild(label);
        setSize(getPreferredSize());
        String description = getModel().getModel().getDescription();
        if (description != null) {
            setToolTipText(description);
        }
    }

    @Override
    public AttributeInstance getModel() {
        return getController().getModel();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    // public PatchView getPatchView() {
    //     return axoObjectInstanceView.getPatchView();
    // }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (AttributeInstance.NAME.is(evt)) {
            label.setText((String) evt.getNewValue());
        } else if (AttributeInstance.DESCRIPTION.is(evt)) {
            setToolTipText((String) evt.getNewValue());
        }
    }

    @Override
    public AttributeInstanceController getController() {
        return controller;
    }

    @Override
    public void dispose() {
    }
}
