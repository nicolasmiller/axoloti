/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.datatypes.Value;
import axoloti.parameters.ParameterInstanceBin1Momentary;
import components.control.PulseButtonComponent;

/**
 *
 * @author nicolas
 */
public class ParameterInstanceViewBin1Momentary extends ParameterInstanceViewInt32 {
    ParameterInstanceBin1Momentary parameterInstance;

    public ParameterInstanceViewBin1Momentary(ParameterInstanceBin1Momentary parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
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
    public PulseButtonComponent CreateControl() {
        return new PulseButtonComponent();
    }

    @Override
    public PulseButtonComponent getControlComponent() {
        return (PulseButtonComponent) ctrl;
    }
}
