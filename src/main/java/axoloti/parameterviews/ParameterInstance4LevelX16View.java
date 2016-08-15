/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.parameters.ParameterInstance4LevelX16;
import components.control.Checkbox4StatesComponent;

/**
 *
 * @author nicolas
 */
public class ParameterInstance4LevelX16View extends ParameterInstanceInt32View {
    ParameterInstance4LevelX16 parameterInstance;

    ParameterInstance4LevelX16View(ParameterInstance4LevelX16 parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }   
    
    @Override
    public Checkbox4StatesComponent CreateControl() {
        return new Checkbox4StatesComponent(0, 16);
    }
    
    @Override
    public Checkbox4StatesComponent getControlComponent() {
        return (Checkbox4StatesComponent) ctrl;
    }
    
    @Override
    public void ShowPreset(int i) {
    }

    @Override
    public void updateV() {
        ctrl.setValue(parameterInstance.getValue().getInt());
    }
}