/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.parameterviews;

import axoloti.Modulation;
import axoloti.Preset;
import axoloti.datatypes.ValueFrac32;
import axoloti.parameters.ParameterInstanceFrac32;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author nicolas
 */
public abstract class ParameterInstanceFrac32View extends ParameterInstanceView {
    ParameterInstanceFrac32 parameterInstance;

    ParameterInstanceFrac32View(ParameterInstanceFrac32 parameterInstance) {
        super(parameterInstance);
        this.parameterInstance = parameterInstance;
    }
    
    @Override
    public void PostConstructor() {
        super.PostConstructor();
        if (parameterInstance.getModulators() != null) {
            List<Modulation> modulators = parameterInstance.getModulators();
            for (Modulation m : modulators) {
                System.out.println("mod amount " + m.getValue().getDouble());
                m.PostConstructor(parameterInstance);
            }
        }
    }
    
    @Override
    public void populatePopup(JPopupMenu m) {
        super.populatePopup(m);
        JMenuItem m_default = new JMenuItem("Reset to default value");
        m_default.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parameterInstance.applyDefaultValue();
                getControlComponent().setValue(parameterInstance.getValue().getDouble());
                handleAdjustment();
            }
        });
        m.add(m_default);
    }
    
    @Override
    public boolean handleAdjustment() {
        Preset p = parameterInstance.GetPreset(presetEditActive);
        if (p != null) {
            p.value = new ValueFrac32(getControlComponent().getValue());
        } else {
            if (parameterInstance.getValue().getDouble() != getControlComponent().getValue()) {
                parameterInstance.getValue().setDouble(getControlComponent().getValue());
                parameterInstance.setNeedsTransmit(true);
                UpdateUnit();
            } else {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void CopyValueFrom(ParameterInstanceView p) {
        if (p instanceof ParameterInstanceFrac32View) {
            parameterInstance.CopyValueFrom(((ParameterInstanceFrac32View) p).parameterInstance);
        }
    }
    
    public void updateModulation(int index, double amount) {
        parameterInstance.updateModulation(index, amount);
    }
}