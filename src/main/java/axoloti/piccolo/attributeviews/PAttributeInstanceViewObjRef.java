package axoloti.piccolo.attributeviews;

import axoloti.attribute.AttributeInstanceObjRef;
import axoloti.piccolo.objectviews.PAxoObjectInstanceView;
import components.piccolo.PTextFieldComponent;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class PAttributeInstanceViewObjRef extends PAttributeInstanceViewString {

    AttributeInstanceObjRef attributeInstance;

    PTextFieldComponent TFObjName;

    public PAttributeInstanceViewObjRef(AttributeInstanceObjRef attributeInstance, PAxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;
    }

    public void PostConstructor() {
        super.PostConstructor();
        TFObjName = new PTextFieldComponent(attributeInstance.getString());
        Dimension d = new Dimension(92, 22);
        TFObjName.setBounds(0, 0, d.width, d.height);
        addChild(TFObjName);
        TFObjName.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void keyTyped(PInputEvent ke) {
                if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    // TODO
//                    transferFocus();
                }
            }

            @Override
            public void keyboardFocusGained(PInputEvent e) {
                attributeInstance.setValueBeforeAdjustment(TFObjName.getText());
            }

            @Override
            public void keyboardFocusLost(PInputEvent e) {
                if (!TFObjName.getText().equals(attributeInstance.getValueBeforeAdjustment())) {
                    attributeInstance.getObjectInstance().getPatchModel().SetDirty();
                }
            }
        });

        TFObjName.getDocument().addDocumentListener(new DocumentListener() {

            void update() {
                attributeInstance.setString(TFObjName.getText());
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
    public String getString() {
        return attributeInstance.getString();
    }

    @Override
    public void setString(String objName) {
        attributeInstance.setString(objName);
        if (TFObjName != null) {
            TFObjName.setText(objName);
        }
    }
}
