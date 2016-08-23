package axoloti.piccolo.attributeviews;

import axoloti.attribute.AttributeInstanceInt;
import axoloti.piccolo.objectviews.PAxoObjectInstanceView;

public abstract class PAttributeInstanceViewInt extends PAttributeInstanceView {

    AttributeInstanceInt attributeInstance;

    PAttributeInstanceViewInt(AttributeInstanceInt attributeInstance, PAxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;

    }
}
