package axoloti.piccolo.attributeviews;

import java.awt.Dimension;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

import axoloti.attribute.AttributeInstanceController;
import axoloti.attribute.AttributeInstanceWavefile;
import axoloti.objectviews.IAxoObjectInstanceView;

import components.piccolo.PTextFieldComponent;

public class PAttributeInstanceViewWavefile extends PAttributeInstanceView {

    PTextFieldComponent TFwaveFilename;

    public PAttributeInstanceViewWavefile(AttributeInstanceController controller, IAxoObjectInstanceView axoObjectInstanceView) {
        super(controller, axoObjectInstanceView);
    }

    @Override
    public AttributeInstanceWavefile getModel() {
        return (AttributeInstanceWavefile) super.getModel();
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        TFwaveFilename = new PTextFieldComponent(getModel().getWaveFilename());
        Dimension d = TFwaveFilename.getSize();
        d.width = 128;
        d.height = 22;
        TFwaveFilename.setMaximumSize(d);
        TFwaveFilename.setMinimumSize(d);
        TFwaveFilename.setPreferredSize(d);
        TFwaveFilename.setSize(d);
        addChild(TFwaveFilename);
        TFwaveFilename.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void keyTyped(PInputEvent ke) {
               }

            @Override
            public void keyPressed(PInputEvent ke) {
                repaint();
            }

            @Override
            public void keyboardFocusGained(PInputEvent e) {
            }

            @Override
            public void keyboardFocusLost(PInputEvent e) {
            }
        });

        TFwaveFilename.getDocument().addDocumentListener(new DocumentListener() {

            void update() {
                getModel().setWaveFilename(TFwaveFilename.getText());
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
