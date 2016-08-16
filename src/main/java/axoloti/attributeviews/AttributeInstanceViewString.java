/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attributeviews;

import axoloti.attribute.AttributeInstanceString;
import axoloti.objectviews.AxoObjectInstanceView;

/**
 *
 * @author nicolas
 */
public abstract class AttributeInstanceViewString extends AttributeInstanceView {
    AttributeInstanceString attributeInstance;

    public AttributeInstanceViewString(AttributeInstanceString attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;

    }
    
    public abstract String getString();

    public abstract void setString(String s);
}
