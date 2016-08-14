/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axoloti.attribute;

import axoloti.TextEditor;
import axoloti.object.AxoObjectInstanceView;
import components.ButtonComponent;
import javax.swing.JLabel;

/**
 *
 * @author nicolas
 */
public class AttributeInstanceTextEditorView extends AttributeInstanceStringView {

    AttributeInstanceTextEditor attributeInstance;
    ButtonComponent bEdit;
    JLabel vlabel;
    TextEditor editor;

    public AttributeInstanceTextEditorView(AttributeInstanceTextEditor attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        bEdit = new ButtonComponent("Edit");
        add(bEdit);
        bEdit.addActListener(new ButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                if (editor == null) {
                    editor = new TextEditor(attributeInstance.sRef, getPatchView().getPatchController().getPatchFrame());
                    editor.setTitle(attributeInstance.getObjectInstance().getInstanceName() + "/" + attributeInstance.attr.getName());
                }
                editor.setState(java.awt.Frame.NORMAL);
                editor.setVisible(true);

            }
        });
    }

    @Override
    public void Lock() {
        if (bEdit != null) {
            bEdit.setEnabled(false);
        }
    }

    @Override
    public void UnLock() {
        if (bEdit != null) {
            bEdit.setEnabled(true);
        }
    }

    @Override
    public String getString() {
        return attributeInstance.getString();
    }

    @Override
    public void setString(String sText) {
        attributeInstance.setString(sText);
        if (editor != null) {
            editor.SetText(sText);
        }
    }
}
