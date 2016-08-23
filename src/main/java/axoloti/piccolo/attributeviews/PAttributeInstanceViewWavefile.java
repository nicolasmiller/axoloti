package axoloti.piccolo.attributeviews;

import axoloti.attribute.AttributeInstanceWavefile;
import axoloti.objectviews.IAxoObjectInstanceView;
import components.piccolo.PTextFieldComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class PAttributeInstanceViewWavefile extends PAttributeInstanceView {

    AttributeInstanceWavefile attributeInstance;
    PTextFieldComponent TFwaveFilename;

    public PAttributeInstanceViewWavefile(AttributeInstanceWavefile attributeInstance, IAxoObjectInstanceView axoObjectInstanceView) {
        super(attributeInstance, axoObjectInstanceView);
        this.attributeInstance = attributeInstance;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        TFwaveFilename = new PTextFieldComponent(attributeInstance.getWaveFilename());
        TFwaveFilename.setBounds(0, 0, 128, 22);
        addChild(TFwaveFilename);
        TFwaveFilename.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void keyTyped(PInputEvent ke) {
                TFwaveFilename.transferFocus(ke, getPatchView());
            }

            @Override
            public void keyPressed(PInputEvent ke) {
                repaint();
            }

            @Override
            public void keyboardFocusGained(PInputEvent e) {
                attributeInstance.setValueBeforeAdjustment(TFwaveFilename.getText());
            }

            @Override
            public void keyboardFocusLost(PInputEvent e) {
                if (!TFwaveFilename.getText().equals(attributeInstance.getValueBeforeAdjustment())) {
                    attributeInstance.getObjectInstance().getPatchModel().SetDirty();
                }
            }
        });

        TFwaveFilename.getDocument().addDocumentListener(new DocumentListener() {

            void update() {
                attributeInstance.setWaveFilename(TFwaveFilename.getText());
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
        if (TFwaveFilename != null) {
            TFwaveFilename.setEnabled(false);
        }
    }

    @Override
    public void UnLock() {
        if (TFwaveFilename != null) {
            TFwaveFilename.setEnabled(true);
        }
    }
}
