/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.pparameterviews;

import axoloti.parameterviews.IParameterInstanceView;
import axoloti.processing.PComponent;
import java.awt.event.ActionListener;
import processing.core.PApplet;

public abstract class PParameterInstanceView extends PComponent implements ActionListener, IParameterInstanceView {

    public PParameterInstanceView(PApplet p) {
        super(p);
    }
}
