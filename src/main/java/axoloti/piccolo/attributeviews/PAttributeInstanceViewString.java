package axoloti.piccolo.attributeviews;

import java.beans.PropertyChangeEvent;

import axoloti.attribute.AttributeInstanceController;
import axoloti.attribute.AttributeInstanceString;
import axoloti.objectviews.IAxoObjectInstanceView;

public abstract class PAttributeInstanceViewString extends PAttributeInstanceView {

    public PAttributeInstanceViewString(AttributeInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    public abstract void setString(String s);

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (AttributeInstanceString.ATTR_VALUE.is(evt)) {
            String newValue = (String) evt.getNewValue();
            setString(newValue);
        }
    }
}
