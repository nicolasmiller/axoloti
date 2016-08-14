/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attribute;

import axoloti.object.AxoObjectInstanceView;
import components.control.ACtrlEvent;
import components.control.ACtrlListener;
import components.control.NumberBoxComponent;

/**
 *
 * @author nicolas
 */
public class AttributeInstanceSpinnerView extends AttributeInstanceIntView {

    AttributeInstanceSpinner attributeInstance;
    NumberBoxComponent spinner;

    public AttributeInstanceSpinnerView(AttributeInstanceSpinner attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        int value = attributeInstance.getValue();

        if (value < attributeInstance.attr.getMinValue()) {
            attributeInstance.setValue(attributeInstance.attr.getMinValue());
        }
        if (value > attributeInstance.attr.getMaxValue()) {
            value = attributeInstance.attr.getMaxValue();
        }
        spinner = new NumberBoxComponent(value, attributeInstance.attr.getMinValue(), attributeInstance.attr.getMaxValue(), 1.0);
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
