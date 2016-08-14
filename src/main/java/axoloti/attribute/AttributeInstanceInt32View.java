/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attribute;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nicolas
 */
public class AttributeInstanceInt32View extends AttributeInstanceIntView {

    AttributeInstanceInt32 attributeInstance;
    JSlider slider;
    JLabel vlabel;

    AttributeInstanceInt32View(AttributeInstanceInt32 attributeInstance) {
        super(attributeInstance);
        this.attributeInstance = attributeInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        slider = new JSlider();
        Dimension d = slider.getSize();
        d.width = 128;
        d.height = 22;
        if (attributeInstance.getValue() < attributeInstance.attr.getMinValue()) {
            attributeInstance.setValue(attributeInstance.attr.getMinValue());
        }
        if (attributeInstance.getValue() > attributeInstance.attr.getMaxValue()) {
            attributeInstance.setValue(attributeInstance.attr.getMaxValue());
        }
        slider.setMinimum(attributeInstance.attr.getMinValue());
        slider.setMaximum(attributeInstance.attr.getMaxValue());
        slider.setValue(attributeInstance.getValue());
        slider.setMaximumSize(d);
        slider.setMinimumSize(d);
        slider.setPreferredSize(d);
        slider.setSize(d);
        add(slider);
        vlabel = new JLabel();
        vlabel.setText("       " + attributeInstance.getValue());
        add(vlabel);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                attributeInstance.setValue(slider.getValue());
                vlabel.setText("" + attributeInstance.getValue());
            }
        });
    }

    @Override
    public void Lock() {
        if (slider != null) {
            slider.setEnabled(false);
        }
    }

    @Override
    public void UnLock() {
        if (slider != null) {
            slider.setEnabled(true);
        }
    }
}
