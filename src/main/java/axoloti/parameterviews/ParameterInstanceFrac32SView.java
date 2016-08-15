/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.parameters.ParameterInstanceFrac32S;

/**
 *
 * @author nicolas
 */
public abstract class ParameterInstanceFrac32SView  extends ParameterInstanceFrac32View {
    ParameterInstanceFrac32S parameterInstance;

    ParameterInstanceFrac32SView(ParameterInstanceFrac32S parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }
}
