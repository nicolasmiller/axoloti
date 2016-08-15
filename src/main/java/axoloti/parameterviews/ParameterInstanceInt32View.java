/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.Preset;
import axoloti.Theme;
import axoloti.datatypes.Value;
import axoloti.datatypes.ValueInt32;
import axoloti.parameters.ParameterInstanceInt32;

/**
 *
 * @author nicolas
 */
public abstract class ParameterInstanceInt32View extends ParameterInstanceView {

    ParameterInstanceInt32 parameterInstance;

    ParameterInstanceInt32View(ParameterInstanceInt32 parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }

    @Override
    public void setValue(Value value) {
        parameterInstance.setValue(value);
        updateV();
    }

    @Override
    public void ShowPreset(int i) {
        presetEditActive = i;
        if (i > 0) {
            Preset p = parameterInstance.GetPreset(presetEditActive);
            if (p != null) {
                setBackground(Theme.getCurrentTheme().Paramete_Preset_Highlight);
                getControlComponent().setValue(p.value.getDouble());
            } else {
                setBackground(Theme.getCurrentTheme().Parameter_Default_Background);
                getControlComponent().setValue(parameterInstance.getValue().getDouble());
            }
        } else {
            setBackground(Theme.getCurrentTheme().Parameter_Default_Background);
            getControlComponent().setValue(parameterInstance.getValue().getDouble());
        }
    }
    
    @Override
    public boolean handleAdjustment() {
        Preset p = parameterInstance.GetPreset(presetEditActive);
        if (p != null) {
            p.value = new ValueInt32((int) getControlComponent().getValue());
        } else {
            if (parameterInstance.getValue().getInt() != (int) getControlComponent().getValue()) {
                parameterInstance.getValue().setInt((int) getControlComponent().getValue());
                parameterInstance.setNeedsTransmit(true);
                UpdateUnit();
            }
            else {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void setOnParent(Boolean b) {
        super.setOnParent(b);
        if ((b != null) && b) {
            setForeground(Theme.getCurrentTheme().Parameter_On_Parent_Highlight);
        } else {
            setForeground(Theme.getCurrentTheme().Parameter_Default_Foreground);
        }
    }
    
    @Override
    public void CopyValueFrom(ParameterInstanceView p) {
        if (p instanceof ParameterInstanceInt32View) {
            parameterInstance.CopyValueFrom(((ParameterInstanceInt32View) p).parameterInstance);
        }
    }
}
