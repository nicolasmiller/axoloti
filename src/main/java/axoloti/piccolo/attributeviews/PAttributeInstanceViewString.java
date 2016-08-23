package axoloti.piccolo.attributeviews;

import axoloti.attribute.AttributeInstanceString;
import axoloti.piccolo.objectviews.PAxoObjectInstanceView;

public abstract class PAttributeInstanceViewString extends PAttributeInstanceView {

    AttributeInstanceString attributeInstance;

    public PAttributeInstanceViewString(AttributeInstanceString attributeInstance, PAxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;

    }

    public abstract String getString();

    public abstract void setString(String s);
}
