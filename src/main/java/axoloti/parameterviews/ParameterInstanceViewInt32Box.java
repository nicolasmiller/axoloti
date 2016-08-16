/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.parameters.ParameterInstanceFrac32UMap;
import axoloti.parameters.ParameterInstanceInt32Box;
import components.control.NumberBoxComponent;

/**
 *
 * @author nicolas
 */
public class ParameterInstanceViewInt32Box extends ParameterInstanceViewInt32 {

    public ParameterInstanceViewInt32Box(ParameterInstanceInt32Box parameterInstance) {
        super(parameterInstance);
    }
    
   @Override
    public ParameterInstanceInt32Box getParameterInstance() {
        return (ParameterInstanceInt32Box) this.parameterInstance;
    }

    @Override
    public void updateV() {
        ctrl.setValue(getParameterInstance().getValue().getInt());
    }
    
    @Override
    public NumberBoxComponent CreateControl() {
        NumberBoxComponent n = new NumberBoxComponent(0.0, getParameterInstance().getMin(), getParameterInstance().getMax(), 1.0);
        return n;
    }

    @Override
    public NumberBoxComponent getControlComponent() {
        return (NumberBoxComponent) ctrl;
    }
}
