/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attributeviews;

import axoloti.attribute.AttributeInstanceSpinner;
import axoloti.attributeviews.AttributeInstanceViewInt;
import axoloti.objectviews.AxoObjectInstanceView;
import components.control.ACtrlEvent;
import components.control.ACtrlListener;
import components.control.NumberBoxComponent;

/**
 *
 * @author nicolas
 */
public class AttributeInstanceViewSpinner extends AttributeInstanceViewInt {

    AttributeInstanceSpinner attributeInstance;
    NumberBoxComponent spinner;

    public AttributeInstanceViewSpinner(AttributeInstanceSpinner attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        int value = attributeInstance.getValue();

        if (value < attributeInstance.getDefinition().getMinValue()) {
            attributeInstance.setValue(attributeInstance.getDefinition().getMinValue());
        }
        if (value > attributeInstance.getDefinition().getMaxValue()) {
            value = attributeInstance.getDefinition().getMaxValue();
        }
        spinner = new NumberBoxComponent(value, attributeInstance.getDefinition().getMinValue(), attributeInstance.getDefinition().getMaxValue(), 1.0);
        spinner.setParentAxoObjectInstanceView(axoObjectInstanceView);
        add(spinner);
        spinner.addACtrlListener(new ACtrlListener() {
            @Override
            public void ACtrlAdjusted(ACtrlEvent e) {
                attributeInstance.setValue((int) spinner.getValue());
            }
        });
    }

    @Override
    public void Lock() {
        if (spinner != null) {
            spinner.setEnabled(false);
        }
    }

    @Override
    public void UnLock() {
        if (spinner != null) {
            spinner.setEnabled(true);
        }
    }
}
