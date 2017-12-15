package axoloti.piccolo.attributeviews;

import axoloti.abstractui.PatchView;
import axoloti.patch.object.attribute.AttributeInstance;
import axoloti.patch.object.attribute.AttributeInstanceController;
import axoloti.abstractui.IAttributeInstanceView;
import axoloti.abstractui.IAxoObjectInstanceView;
import axoloti.piccolo.PatchPNode;
import axoloti.piccolo.components.PLabelComponent;
import java.beans.PropertyChangeEvent;
import javax.swing.BoxLayout;

import static java.awt.Component.LEFT_ALIGNMENT;

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

    public void PostConstructor() {
        setLayout(new BoxLayout(getProxyComponent(), BoxLayout.LINE_AXIS));
        setPickable(false);
        label = new PLabelComponent(getModel().getModel().getName());
        addChild(label);
        setSize(getPreferredSize());
        String description = getModel().getModel().getDescription();
        if (description != null) {
            setToolTipText(description);
        }
        setAlignmentX(LEFT_ALIGNMENT);
    }

    @Override
    public AttributeInstance getModel() {
        return getController().getModel();
    }

    @Override
    public String getName() {
        return super.getName();
    }

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
