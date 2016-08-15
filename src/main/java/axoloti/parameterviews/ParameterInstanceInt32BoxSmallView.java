/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.parameters.ParameterInstanceInt32BoxSmall;
import components.control.NumberBoxComponent;

/**
 *
 * @author nicolas
 */
public class ParameterInstanceInt32BoxSmallView extends ParameterInstanceInt32BoxView {

    ParameterInstanceInt32BoxSmall parameterInstance;

    ParameterInstanceInt32BoxSmallView(ParameterInstanceInt32BoxSmall parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }
    
    @Override
    public NumberBoxComponent CreateControl() {
        return new NumberBoxComponent(0.0, parameterInstance.getMin(), parameterInstance.getMax(), 1.0, 12, 12);
    }
}
