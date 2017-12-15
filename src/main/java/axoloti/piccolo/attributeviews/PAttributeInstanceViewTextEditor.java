package axoloti.piccolo.attributeviews;

import javax.swing.SwingUtilities;

import axoloti.DocumentWindow;
import axoloti.TextEditor;
import axoloti.attribute.AttributeInstanceController;
import axoloti.attribute.AttributeInstanceTextEditor;
import axoloti.objectviews.IAxoObjectInstanceView;

import components.piccolo.control.PButtonComponent;

public class PAttributeInstanceViewTextEditor extends PAttributeInstanceViewString {

    PButtonComponent bEdit;

    public PAttributeInstanceViewTextEditor(AttributeInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    @Override
    public AttributeInstanceTextEditor getModel() {
        return (AttributeInstanceTextEditor) super.getModel();
    }

    void showEditor() {
        if (getModel().editor == null) {
            DocumentWindow dw = (DocumentWindow) SwingUtilities.getWindowAncestor(this.getProxyComponent());
            getModel().editor = new TextEditor(AttributeInstanceTextEditor.ATTR_VALUE, getController(), dw);
            getModel().editor.setTitle(getController().getParent().getModel().getInstanceName() + "/" + getModel().getModel().getName());
        }
        getModel().editor.setState(java.awt.Frame.NORMAL);
        getModel().editor.setVisible(true);
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        bEdit = new PButtonComponent("Edit", axoObjectInstanceView);
        addChild(bEdit);
        bEdit.addActListener(new PButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                showEditor();
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
    public void setString(String sText) {
        getModel().setValue(sText);
        if (getModel().editor != null) {
            getModel().editor.SetText(sText);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
