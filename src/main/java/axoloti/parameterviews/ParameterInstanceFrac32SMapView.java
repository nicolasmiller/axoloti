/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.parameters.ParameterInstanceFrac32SMap;

/**
 *
 * @author nicolas
 */
public class ParameterInstanceFrac32SMapView extends ParameterInstanceFrac32UMapView {
    ParameterInstanceFrac32SMap parameterInstance;

    ParameterInstanceFrac32SMapView(ParameterInstanceFrac32SMap parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }
}
