/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.datatypes.Value;
import axoloti.parameters.ParameterInstanceBin16;
import components.control.CheckboxComponent;

/**
 *
 * @author nicolas
 */
public class ParameterInstanceViewBin16 extends ParameterInstanceViewInt32 {
    ParameterInstanceBin16 parameterInstance;

    ParameterInstanceViewBin16(ParameterInstanceBin16 parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }
    
    @Override
    public CheckboxComponent CreateControl() {
        return new CheckboxComponent(0, 16);
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