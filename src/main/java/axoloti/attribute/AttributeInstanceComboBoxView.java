/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attribute;

import axoloti.object.AxoObjectInstance;
import axoloti.object.AxoObjectInstanceView;
import axoloti.utils.Constants;
import components.DropDownComponent;
import java.util.logging.Level;

/**
 *
 * @author nicolas
 */
public class AttributeInstanceComboBoxView extends AttributeInstanceStringView {
    DropDownComponent comboBox;
    AttributeInstanceComboBox attributeInstance;

    public AttributeInstanceComboBoxView(AttributeInstanceComboBox attributeInstance, AxoObjectInstanceView axoObjectView) {
        super(attributeInstance, axoObjectView);
        this.attributeInstance = attributeInstance;
    }
        
    @Override
    public void PostConstructor() {
        super.PostConstructor();
//        final DefaultComboBoxModel model = new DefaultComboBoxModel(((AxoAttributeComboBox) attr).getMenuEntries().toArray());
        comboBox = new DropDownComponent(attributeInstance.getDefinition().getMenuEntries(), attributeInstance);
        comboBox.setFont(Constants.FONT);
        setString(attributeInstance.selection);
        comboBox.addItemListener(new DropDownComponent.DDCListener() {
            @Override
            public void SelectionChanged() {
                attributeInstance.selection = (String) comboBox.getSelectedItem();
            }
        });
        this.add(comboBox);
    }
    
    @Override
    public String getString() {
        return comboBox.getSelectedItem();
    }
    
    @Override
    public void setString(String selection) {
        attributeInstance.setString(selection);
        
        if (comboBox == null) {
            return;
        }
        if (comboBox.getItemCount() == 0) {
            return;
        }
        if (selection == null) {
            attributeInstance.selection = (String) comboBox.getItemAt(0);
        }
        comboBox.setSelectedItem(attributeInstance.selection);
        attributeInstance.setSelectedIndex(comboBox.getSelectedIndex());
        if (attributeInstance.selection.equals((String) comboBox.getSelectedItem())) {
            return;
        }
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (attributeInstance.selection.equals(comboBox.getItemAt(i))) {
                attributeInstance.selection = comboBox.getItemAt(i);
                return;
            }
        }
        java.util.logging.Logger.getLogger(AxoObjectInstance.class.getName()).log(Level.SEVERE, "Error: object \"{0}\" attribute \"{1}\", value \"{2}\" unmatched", new Object[]{GetObjectInstance().getInstanceName(), GetDefinition().getName(), selection});
    }

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
}