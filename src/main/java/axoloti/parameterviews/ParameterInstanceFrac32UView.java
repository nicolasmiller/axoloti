/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.parameters.ParameterInstanceFrac32U;

/**
 *
 * @author nicolas
 */
public abstract class ParameterInstanceFrac32UView extends ParameterInstanceFrac32View {
    ParameterInstanceFrac32U parameterInstance;

    ParameterInstanceFrac32UView(ParameterInstanceFrac32U parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }
}
