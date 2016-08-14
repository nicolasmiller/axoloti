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
public abstract class AttributeInstanceIntView extends AttributeInstanceView {

    AttributeInstanceInt attributeInstance;

    AttributeInstanceIntView(AttributeInstanceInt attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;

    }
}
