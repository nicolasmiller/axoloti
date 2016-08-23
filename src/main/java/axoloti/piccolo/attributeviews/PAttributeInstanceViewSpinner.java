package axoloti.piccolo.attributeviews;

import axoloti.attribute.AttributeInstanceSpinner;
import axoloti.piccolo.objectviews.PAxoObjectInstanceView;
import components.control.ACtrlEvent;
import components.control.ACtrlListener;
import components.piccolo.control.PNumberBoxComponent;

public class PAttributeInstanceViewSpinner extends PAttributeInstanceViewInt {

    AttributeInstanceSpinner attributeInstance;
    PNumberBoxComponent spinner;

    public PAttributeInstanceViewSpinner(AttributeInstanceSpinner attributeInstance, PAxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        int value = attributeInstance.getValue();

        if (value < attributeInstance.getDefinition().getMinValue()) {
            attributeInstance.setValue(attributeInstance.getDefinition().getMinValue());
        }
        if (value > attributeInstance.getDefinition().getMaxValue()) {
            attributeInstance.setValue(attributeInstance.getDefinition().getMaxValue());
        }
        spinner = new PNumberBoxComponent(value, attributeInstance.getDefinition().getMinValue(), attributeInstance.getDefinition().getMaxValue(), 1.0);
        spinner.setParentAxoObjectInstanceView(axoObjectInstanceView);
        addChild(spinner);
        spinner.addACtrlListener(new ACtrlListener() {
            @Override
            public void ACtrlAdjusted(ACtrlEvent e) {
                attributeInstance.setValue((int) spinner.getValue());
            }

            @Override
            public void ACtrlAdjustmentBegin(ACtrlEvent e) {
                attributeInstance.setValueBeforeAdjustment(attributeInstance.getValue());
            }

            @Override
            public void ACtrlAdjustmentFinished(ACtrlEvent e) {
                if (attributeInstance.getValue() != attributeInstance.getValueBeforeAdjustment()) {
                    attributeInstance.getObjectInstance().getPatchModel().SetDirty();
                }
            }
        });
    }

    @Override
    public void Lock() {
        if (spinner != null) {
            spinner.setEnabled(false);
        }
    }

    @Override
    public void UnLock() {
        if (spinner != null) {
            spinner.setEnabled(true);
        }
    }
}
