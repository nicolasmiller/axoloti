/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.Preset;
import axoloti.Theme;
import axoloti.parameters.ParameterInstanceFrac32UMapVSlider;
import components.control.VSliderComponent;

/**
 *
 * @author nicolas
 */
public class ParameterInstanceFrac32UMapVSliderView extends ParameterInstanceFrac32UView {

    ParameterInstanceFrac32UMapVSlider parameterInstance;

    ParameterInstanceFrac32UMapVSliderView(ParameterInstanceFrac32UMapVSlider parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
    }

    @Override
    public void updateV() {
        if (ctrl != null) {
            ctrl.setValue(parameterInstance.getValue().getDouble());
        }
    }

    /*
     *  Preset logic
     */
    @Override
    public void ShowPreset(int i) {
        this.presetEditActive = i;
        if (i > 0) {
            Preset p = parameterInstance.GetPreset(presetEditActive);
            if (p != null) {
                setBackground(Theme.getCurrentTheme().Paramete_Preset_Highlight);
                ctrl.setValue(p.value.getDouble());
            } else {
                setBackground(Theme.getCurrentTheme().Parameter_Default_Background);
                ctrl.setValue(parameterInstance.getValue().getDouble());
            }
        } else {
            setBackground(Theme.getCurrentTheme().Parameter_Default_Background);
            ctrl.setValue(parameterInstance.getValue().getDouble());
        }
        if ((parameterInstance.getPresets() != null) && (!parameterInstance.getPresets().isEmpty())) {
//            lblPreset.setVisible(true);
        } else {
//            lblPreset.setVisible(false);
        }
    }

    @Override
    public VSliderComponent CreateControl() {
        return new VSliderComponent(0.0, 0.0, 64, 0.5);
    }

    @Override
    public VSliderComponent getControlComponent() {
        return (VSliderComponent) ctrl;
    }
}