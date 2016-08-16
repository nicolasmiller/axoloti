/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.datatypes.Value;
import axoloti.parameters.ParameterInstanceBin32;
import components.control.CheckboxComponent;

/**
 *
 * @author nicolas
 */
public class ParameterInstanceViewBin32 extends ParameterInstanceViewInt32 {
    ParameterInstanceBin32 parameterInstance;

    ParameterInstanceViewBin32(ParameterInstanceBin32 parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }
    
    @Override
    public CheckboxComponent CreateControl() {
        return new CheckboxComponent(0, 32);
    }
    
    @Override
    public void updateV() {
        ctrl.setValue(parameterInstance.getValue().getInt());
    }

    @Override
    public void setValue(Value value) {
        parameterInstance.setValue(value);
        updateV();
    }

    @Override
    public CheckboxComponent getControlComponent() {
        return (CheckboxComponent) ctrl;
    }
}
