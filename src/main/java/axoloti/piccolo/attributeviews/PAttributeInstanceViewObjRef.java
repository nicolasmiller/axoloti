package axoloti.piccolo.attributeviews;

import java.awt.Dimension;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

import axoloti.attribute.AttributeInstanceController;
import axoloti.attribute.AttributeInstanceObjRef;
import axoloti.objectviews.IAxoObjectInstanceView;

import components.piccolo.PTextFieldComponent;

public class PAttributeInstanceViewObjRef extends PAttributeInstanceViewString {

    PTextFieldComponent TFObjName;

    public PAttributeInstanceViewObjRef(AttributeInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    @Override
    public AttributeInstanceObjRef getModel() {
        return (AttributeInstanceObjRef) super.getModel();
    }

    public void PostConstructor() {
        super.PostConstructor();
        TFObjName = new PTextFieldComponent(getModel().getValue());
        Dimension d = TFObjName.getSize();
        d.width = 92;
        d.height = 22;
        TFObjName.setMaximumSize(d);
        TFObjName.setMinimumSize(d);
        TFObjName.setPreferredSize(d);
        TFObjName.setSize(d);
        addChild(TFObjName);
        TFObjName.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent e) {
                TFObjName.grabFocus();
            }

            @Override
            public void keyPressed(PInputEvent ke) {
                TFObjName.transferFocus(ke, patchView);
            }

            @Override
            public void keyboardFocusGained(PInputEvent e) {
                //attributeInstance.setValueBeforeAdjustment(TFObjName.getText());
            }

            @Override
            public void keyboardFocusLost(PInputEvent e) {
                //if (!TFObjName.getText().equals(attributeInstance.getValueBeforeAdjustment())) {
                //    attributeInstance.getObjectInstance().getPatchModel().setDirty();
                //}
            }
        });

        TFObjName.getDocument().addDocumentListener(new DocumentListener() {

            void update() {
                getController().setModelUndoableProperty(AttributeInstanceObjRef.ATTR_VALUE,TFObjName.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });
    }

    @Override
    public void Lock() {
        if (TFObjName != null) {
            TFObjName.setEnabled(false);
        }
    }

    @Override
    public void UnLock() {
        if (TFObjName != null) {
            TFObjName.setEnabled(true);
        }
    }

    @Override
    public void setString(String objName) {
        if (TFObjName != null) {
            if (!TFObjName.getText().equals(objName)) {
                TFObjName.setText(objName);
            }
        }
    }
}
