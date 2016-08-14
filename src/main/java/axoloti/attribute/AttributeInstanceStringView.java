/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attribute;

import axoloti.object.AxoObjectInstanceView;

/**
 *
 * @author nicolas
 */
public abstract class AttributeInstanceStringView extends AttributeInstanceView {
    AttributeInstanceString attributeInstance;

    public AttributeInstanceStringView(AttributeInstanceString attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;

    }
    
    public abstract String getString();

    public abstract void setString(String s);
}
