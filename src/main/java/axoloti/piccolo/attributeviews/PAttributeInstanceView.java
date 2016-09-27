package axoloti.piccolo.attributeviews;

import axoloti.PatchView;
import axoloti.attribute.AttributeInstance;
import axoloti.attributeviews.IAttributeInstanceView;
import axoloti.objectviews.IAxoObjectInstanceView;
import axoloti.piccolo.PatchPNode;
import components.piccolo.PLabelComponent;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_TOP;

public abstract class PAttributeInstanceView extends PatchPNode implements IAttributeInstanceView {

    IAxoObjectInstanceView axoObjectInstanceView;

    AttributeInstance attributeInstance;

    PAttributeInstanceView(AttributeInstance attributeInstance, IAxoObjectInstanceView axoObjectInstanceView) {
        super(axoObjectInstanceView.getPatchView());
        this.attributeInstance = attributeInstance;
        this.axoObjectInstanceView = axoObjectInstanceView;
    }

    @Override
    public abstract void Lock();

    @Override
    public abstract void UnLock();

    @Override
    public void PostConstructor() {
        this.setLayout(HORIZONTAL_TOP);
        setPickable(false);
        addChild(new PLabelComponent(attributeInstance.getDefinition().getName()));
        setBounds(this.getUnionOfChildrenBounds(null));
    }

    @Override
    public String getName() {
        if (attributeInstance != null) {
            return attributeInstance.getAttributeName();
        } else {
            return super.getName();
        }
    }

    @Override
    public PatchView getPatchView() {
        return axoObjectInstanceView.getPatchView();
    }

    @Override
    public AttributeInstance getAttributeInstance() {
        return this.attributeInstance;
    }
}
