package axoloti.piccolo.attributeviews;

import axoloti.PatchViewPiccolo;
import axoloti.attribute.AttributeInstance;
import axoloti.attributeviews.IAttributeInstanceView;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import axoloti.piccolo.PatchPNode;
import axoloti.piccolo.objectviews.PAxoObjectInstanceView;
import components.piccolo.PLabelComponent;

public abstract class PAttributeInstanceView extends PatchPNode implements IAttributeInstanceView {

    PAxoObjectInstanceView axoObjectInstanceView;
    PatchViewPiccolo patchView;

    AttributeInstance attributeInstance;

    PAttributeInstanceView(AttributeInstance attributeInstance, PAxoObjectInstanceView axoObjectInstanceView) {
        this.attributeInstance = attributeInstance;
        this.axoObjectInstanceView = axoObjectInstanceView;
    }

    @Override
    public abstract void Lock();

    @Override
    public abstract void UnLock();

    @Override
    public void Close() {
    }

    @Override
    public void PostConstructor() {
        this.setLayout(HORIZONTAL_CENTERED);
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
    public PatchViewPiccolo getPatchView() {
        return axoObjectInstanceView.getPatchView();
    }

    @Override
    public AttributeInstance getAttributeInstance() {
        return this.attributeInstance;
    }
}
