/**
 * Copyright (C) 2013, 2014 Johannes Taelman
 *
 * This file is part of Axoloti.
 *
 * Axoloti is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Axoloti is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Axoloti. If not, see <http://www.gnu.org/licenses/>.
 */
package components;

import axoloti.PatchModel;
import axoloti.Preset;
import axoloti.datatypes.ValueFrac32;
import axoloti.datatypes.ValueInt32;
import axoloti.parameterviews.ParameterInstanceView;
import components.control.ACtrlComponent;
import components.control.ACtrlEvent;
import components.control.ACtrlListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author Johannes Taelman
 */
public class AssignPresetPanel extends JPanel {

    final ParameterInstanceView parameterInstanceView;
    final ArrayList<ACtrlComponent> ctrls;

    public AssignPresetPanel(ParameterInstanceView parameterInstanceView) {
        this.parameterInstanceView = parameterInstanceView;
        int n = parameterInstanceView.getParameterInstance().getObjectInstance().getPatchModel().getSettings().GetNPresets();
        ctrls = new ArrayList<ACtrlComponent>(n);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        for (int i = 0; i < n; i++) {
            c.gridx = 0;
            c.gridy = i;
            JCheckBox cb = new JCheckBox("Preset " + (i + 1));
            cb.addActionListener(cbActionListener);
            add(cb, c);
            c.gridx = 1;
            ACtrlComponent ctrl = parameterInstanceView.CreateControl();
            ctrls.add(ctrl);
            ctrl.addACtrlListener(ctrlListener);
            Preset p = parameterInstanceView.getParameterInstance().GetPreset(i + 1);
            if (p != null) {
                cb.setSelected(true);
                ctrl.setValue(p.value.getDouble());
            } else {
                ctrl.setEnabled(false);
                ctrl.setValue(parameterInstanceView.getParameterInstance().getValue().getDouble());
            }
            add(ctrl, c);
        }
    }

    ActionListener cbActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            String[] s = e.getActionCommand().split(" ");
            int i = Integer.parseInt(s[1]) - 1;
            if (((JCheckBox) e.getSource()).isSelected()) {
                parameterInstanceView.AddPreset(i + 1, parameterInstanceView.getParameterInstance().getValue());
                ctrls.get(i).setEnabled(true);
                ctrls.get(i).setValue(parameterInstanceView.getParameterInstance().GetPreset(i + 1).value.getDouble()); // TBC!!!
            } else {
                ctrls.get(i).setEnabled(false);
                parameterInstanceView.RemovePreset(i + 1);
            }
            PatchModel patchModel = parameterInstanceView.getParameterInstance().getObjectInstance().getPatchModel();
            if (patchModel != null) {
                patchModel.SetDirty();
            }
            patchModel.presetUpdatePending = true;
        }

    };

    double valueBeforeAdjustment;

    ACtrlListener ctrlListener = new ACtrlListener() {

        @Override
        public void ACtrlAdjusted(ACtrlEvent e) {
            int i = ctrls.indexOf(e.getSource());
            if (i >= 0) {
                if (ctrls.get(i).isEnabled()) {
                    if (parameterInstanceView.getParameterInstance().getValue() instanceof ValueInt32) {
                        parameterInstanceView.getParameterInstance().AddPreset(i + 1, new ValueInt32((int) ctrls.get(i).getValue()));
                    } else if (parameterInstanceView.getParameterInstance().getValue() instanceof ValueFrac32) {
                        parameterInstanceView.AddPreset(i + 1, new ValueFrac32(ctrls.get(i).getValue()));
                    }
                }
            }
            parameterInstanceView.getParameterInstance().getObjectInstance().getPatchModel().presetUpdatePending = true;
        }

        @Override
        public void ACtrlAdjustmentBegin(ACtrlEvent e) {
            int i = ctrls.indexOf(e.getSource());
            if (i >= 0) {
                valueBeforeAdjustment = ctrls.get(i).getValue();
            }
        }

        @Override
        public void ACtrlAdjustmentFinished(ACtrlEvent e) {
            int i = ctrls.indexOf(e.getSource());
            if (i >= 0) {
                if (valueBeforeAdjustment != ctrls.get(i).getValue()) {
                    PatchModel patchModel = parameterInstanceView.getParameterInstance().getObjectInstance().getPatchModel();
                    if (patchModel != null) {
                        patchModel.SetDirty();
                    }
                }
            }
        }
    };
}
