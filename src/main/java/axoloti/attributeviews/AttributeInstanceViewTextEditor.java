package axoloti.attributeviews;

import axoloti.TextEditor;
import axoloti.attribute.AttributeInstanceTextEditor;
import axoloti.objectviews.AxoObjectInstanceView;
import components.ButtonComponent;
import javax.swing.JLabel;

public class AttributeInstanceViewTextEditor extends AttributeInstanceViewString {

    AttributeInstanceTextEditor attributeInstance;
    ButtonComponent bEdit;
    JLabel vlabel;
    TextEditor editor;

    public AttributeInstanceViewTextEditor(AttributeInstanceTextEditor attributeInstance, AxoObjectInstanceView axoObjectInstanceView) {
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
                    editor = new TextEditor(attributeInstance.getStringRef(), getPatchView().getPatchController().getPatchFrame());
                    editor.setTitle(attributeInstance.getObjectInstance().getInstanceName() + "/" + attributeInstance.getDefinition().getName());
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
