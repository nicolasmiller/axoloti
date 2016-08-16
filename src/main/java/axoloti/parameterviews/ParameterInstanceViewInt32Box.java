/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.parameters.ParameterInstanceInt32Box;
import components.control.NumberBoxComponent;

/**
 *
 * @author nicolas
 */
public class ParameterInstanceViewInt32Box extends ParameterInstanceViewInt32 {

    ParameterInstanceInt32Box parameterInstance;

    ParameterInstanceViewInt32Box(ParameterInstanceInt32Box parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }

    @Override
    public void updateV() {
        ctrl.setValue(parameterInstance.getValue().getInt());
    }
    
    @Override
    public NumberBoxComponent CreateControl() {
        NumberBoxComponent n = new NumberBoxComponent(0.0, parameterInstance.getMin(), parameterInstance.getMax(), 1.0);
        return n;
    }

    @Override
    public NumberBoxComponent getControlComponent() {
        return (NumberBoxComponent) ctrl;
    }
}
