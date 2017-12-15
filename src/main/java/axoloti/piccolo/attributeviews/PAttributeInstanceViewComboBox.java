package axoloti.piccolo.attributeviews;

import java.beans.PropertyChangeEvent;
import java.util.List;

import axoloti.attribute.AttributeInstanceComboBox;
import axoloti.attribute.AttributeInstanceController;
import axoloti.attributedefinition.AxoAttributeComboBox;
import axoloti.objectviews.IAxoObjectInstanceView;

import components.piccolo.control.PDropDownComponent;

public class PAttributeInstanceViewComboBox extends PAttributeInstanceViewString {

    PDropDownComponent comboBox;

    public PAttributeInstanceViewComboBox(AttributeInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    @Override
    public AttributeInstanceComboBox getModel() {
        return (AttributeInstanceComboBox) super.getModel();
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        comboBox = new PDropDownComponent(getModel().getModel().getMenuEntries(), getModel(), axoObjectInstanceView);
        setString(getModel().getValue());
        comboBox.addItemListener(new PDropDownComponent.DDCListener() {
            @Override
            public void SelectionChanged() {
                getController().addMetaUndo("edit attribute " + getModel().getName());
                getController().setModelUndoableProperty(AttributeInstanceComboBox.ATTR_VALUE,comboBox.getSelectedItem());

            }
        });
        addChild(comboBox);
    }

    // @Override
    // public void setString(String selection) {
    //     // TODO: use MVC pattern
    //     /*
    //     attributeInstance.setValue(selection);

    //     if (comboBox == null) {
    //         return;
    //     }
    //     if (comboBox.getItemCount() == 0) {
    //         return;
    //     }
    //     if (selection == null) {
    //         attributeInstance.setValue(comboBox.getItemAt(0));
    //     }
    //     comboBox.setSelectedItem(attributeInstance.getValue());
    //     attributeInstance.setSelectedIndex(comboBox.getSelectedIndex());
    //     if (attributeInstance.getValue().equals(comboBox.getSelectedItem())) {
    //         return;
    //     }
    //     for (int i = 0; i < comboBox.getItemCount(); i++) {
    //         if (attributeInstance.getValue().equals(comboBox.getItemAt(i))) {
    //             attributeInstance.setValue(comboBox.getItemAt(i));
    //             return;
    //         }
    //     }
    //     java.util.logging.Logger.getLogger(AxoObjectInstance.class.getName()).log(Level.SEVERE, "Error: object \"{0}\" attribute \"{1}\", value \"{2}\" unmatched", new Object[]{attributeInstance.getObjectInstance().getInstanceName(), attributeInstance.getModel().getName(), selection});
    //     */
    // }

    @Override
    public void Lock() {
        if (comboBox != null) {
            comboBox.setEnabled(false);
        }
    }

    @Override
    public void UnLock() {
        if (comboBox != null) {
            comboBox.setEnabled(true);
        }
    }

    @Override
    public void setString(String s) {
        AttributeInstanceComboBox aic = (AttributeInstanceComboBox) getController().getModel();
        int index = aic.getIndex(s);
        if (aic.getModel().getMenuEntries().size() > 0) {
            comboBox.setSelectedItem(aic.getModel().getMenuEntries().get(index));
        }
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (AxoAttributeComboBox.ATOM_MENUENTRIES.is(evt)) {
            comboBox.setItems((List<String>) evt.getNewValue());
        }
    }
}
