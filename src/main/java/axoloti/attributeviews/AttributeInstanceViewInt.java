/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attributeviews;

import axoloti.attribute.AttributeInstanceInt;
import axoloti.objectviews.AxoObjectInstanceView;

/**
 *
 * @author nicolas
 */
public abstract class AttributeInstanceViewInt extends AttributeInstanceView {

    AttributeInstanceInt attributeInstance;

    AttributeInstanceViewInt(AttributeInstanceInt attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;

    }
}
