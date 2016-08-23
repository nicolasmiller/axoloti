package axoloti.piccolo.attributeviews;

import axoloti.attribute.AttributeInstanceInt32;
import axoloti.piccolo.objectviews.PAxoObjectInstanceView;
import components.piccolo.PLabelComponent;
import java.awt.Dimension;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PAttributeInstanceViewInt32 extends PAttributeInstanceViewInt {

    AttributeInstanceInt32 attributeInstance;
    JSlider slider;
    PLabelComponent vlabel;

    public PAttributeInstanceViewInt32(AttributeInstanceInt32 attributeInstance, PAxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        slider = new JSlider();
        Dimension d = slider.getSize();
        d.width = 128;
        d.height = 22;
        if (attributeInstance.getValue() < attributeInstance.getDefinition().getMinValue()) {
            attributeInstance.setValue(attributeInstance.getDefinition().getMinValue());
        }
        if (attributeInstance.getValue() > attributeInstance.getDefinition().getMaxValue()) {
            attributeInstance.setValue(attributeInstance.getDefinition().getMaxValue());
        }
        slider.setMinimum(attributeInstance.getDefinition().getMinValue());
        slider.setMaximum(attributeInstance.getDefinition().getMaxValue());
        slider.setValue(attributeInstance.getValue());
        slider.setMaximumSize(d);
        slider.setMinimumSize(d);
        slider.setPreferredSize(d);
        slider.setSize(d);

// TODO Slider implementation
//        add(slider);
        vlabel = new PLabelComponent("       " + attributeInstance.getValue());
        addChild(vlabel);
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
