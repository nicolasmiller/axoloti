package axoloti.piccolo.objectviews;

import axoloti.PatchViewPiccolo;
import axoloti.object.AxoObjectInstanceHyperlink;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_CENTERED;
import components.control.ACtrlEvent;
import components.control.ACtrlListener;
import components.piccolo.PLabelComponent;
import components.piccolo.control.PPulseButtonComponent;

public class PAxoObjectInstanceViewHyperlink extends PAxoObjectInstanceViewAbstract {

    AxoObjectInstanceHyperlink model;
    private PPulseButtonComponent button;

    public PAxoObjectInstanceViewHyperlink(AxoObjectInstanceHyperlink model, PatchViewPiccolo p) {
        super(model, p);
        this.model = model;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        setDrawBorder(true);

        setLayout(HORIZONTAL_CENTERED);
        button = new PPulseButtonComponent();
        button.addACtrlListener(new ACtrlListener() {
            @Override
            public void ACtrlAdjusted(ACtrlEvent e) {
                if (e.getValue() == 1.0) {
                    model.Launch();
                }
            }

            @Override
            public void ACtrlAdjustmentBegin(ACtrlEvent e) {
            }

            @Override
            public void ACtrlAdjustmentFinished(ACtrlEvent e) {
            }
        });
        addChild(button);
        InstanceLabel = new PLabelComponent(model.getInstanceName());
//        InstanceLabel.setAlignmentX(LEFT_ALIGNMENT);
// TODO
//        InstanceLabel.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) {
//                    addInstanceNameEditor();
//                    e.consume();
//                }
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                AxoObjectInstanceViewHyperlink.this.mousePressed(e);
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                AxoObjectInstanceViewHyperlink.this.mouseReleased(e);
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//            }
//        });
//        InstanceLabel.addMouseMotionListener(this);
        addChild(InstanceLabel);
        setBounds(0, 0, getChildrenWidth(), getChildrenHeight());
        translate(model.getX(), model.getY());

        resizeToGrid();
    }

    @Override
    public void setInstanceName(String s) {
        super.setInstanceName(s);
        resizeToGrid();
    }
}
