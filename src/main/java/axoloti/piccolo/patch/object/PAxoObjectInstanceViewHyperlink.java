package axoloti.piccolo.patch.object;

import axoloti.patch.object.AxoObjectInstanceHyperlink;
import axoloti.patch.object.ObjectInstanceController;
import axoloti.piccolo.components.PLabelComponent;
import axoloti.piccolo.components.control.PPulseButtonComponent;
import axoloti.piccolo.patch.PatchViewPiccolo;
import static axoloti.swingui.components.control.ACtrlComponent.PROP_VALUE;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

public class PAxoObjectInstanceViewHyperlink extends PAxoObjectInstanceViewAbstract {
    private PPulseButtonComponent button;

    public PAxoObjectInstanceViewHyperlink(ObjectInstanceController controller, PatchViewPiccolo p) {
        super(controller, p);
        initComponents();
    }

    @Override
    public AxoObjectInstanceHyperlink getModel() {
        return (AxoObjectInstanceHyperlink) super.getModel();
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        setLayout(new BoxLayout(getProxyComponent(), BoxLayout.LINE_AXIS));

        setDrawBorder(true);

        button = new PPulseButtonComponent(this);
        button.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(PROP_VALUE)) {
                        if (evt.getNewValue().equals(1.0)) {
                            getModel().Launch();
                        }
                    }
                }
            });
        addChild(button);
        addToSwingProxy(Box.createHorizontalStrut(5));
        instanceLabel = new PLabelComponent(getModel().getInstanceName());
        instanceLabel.setAlignmentX(LEFT_ALIGNMENT);

        addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent e) {
                if (e.getClickCount() == 2) {
                    addInstanceNameEditor();
                }
            }
        });

        addChild(instanceLabel);

        resizeToGrid();
        setVisible(true);
    }

    @Override
    protected void handleInstanceNameEditorAction() {
        super.handleInstanceNameEditorAction();
        repaint();
    }

    @Override
    public void showInstanceName(String s) {
        super.showInstanceName(s);
        resizeToGrid();
    }
}
