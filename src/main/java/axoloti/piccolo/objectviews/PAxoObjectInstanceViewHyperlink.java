package axoloti.piccolo.objectviews;

import axoloti.PatchViewPiccolo;
import axoloti.object.AxoObjectInstanceHyperlink;
import components.piccolo.PLabelComponent;
import components.piccolo.control.PCtrlEvent;
import components.piccolo.control.PCtrlListener;
import components.piccolo.control.PPulseButtonComponent;
import static java.awt.Component.LEFT_ALIGNMENT;
import javax.swing.BoxLayout;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import axoloti.object.ObjectInstanceController;

public class PAxoObjectInstanceViewHyperlink extends PAxoObjectInstanceViewAbstract {
    private PPulseButtonComponent button;

    public PAxoObjectInstanceViewHyperlink(ObjectInstanceController controller, PatchViewPiccolo p) {
        super(controller, p);
    }

    @Override
    public AxoObjectInstanceHyperlink getModel() {
        return (AxoObjectInstanceHyperlink) super.getModel();
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        setLayout(new BoxLayout(getProxyComponent(), BoxLayout.LINE_AXIS));

        setDrawBorder(true);

        button = new PPulseButtonComponent(this);
        button.addPCtrlListener(new PCtrlListener() {
            @Override
            public void PCtrlAdjusted(PCtrlEvent e) {
                if (e.getValue() == 1.0) {
                    getModel().Launch();
                }
            }

            @Override
            public void PCtrlAdjustmentBegin(PCtrlEvent e) {
            }

            @Override
            public void PCtrlAdjustmentFinished(PCtrlEvent e) {
            }
        });
        addChild(button);
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
        translate(getModel().getX(), getModel().getY());
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
