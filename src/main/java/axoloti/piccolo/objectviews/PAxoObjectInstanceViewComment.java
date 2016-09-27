package axoloti.piccolo.objectviews;

import axoloti.PatchViewPiccolo;
import axoloti.object.AxoObjectInstanceComment;
import components.piccolo.PLabelComponent;
import static java.awt.Component.CENTER_ALIGNMENT;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import static axoloti.piccolo.PNodeLayout.HORIZONTAL_TOP;

public class PAxoObjectInstanceViewComment extends PAxoObjectInstanceViewAbstract {

    AxoObjectInstanceComment model;

    public PAxoObjectInstanceViewComment(AxoObjectInstanceComment model, PatchViewPiccolo p) {
        super(model, p);
        this.model = model;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        setDrawBorder(true);

        if (InstanceName != null) {
            model.setCommentText(InstanceName);
            InstanceName = null;
        }
        setLayout(HORIZONTAL_TOP);
        instanceLabel = new PLabelComponent(model.getCommentText());
        instanceLabel.setHorizontalAlignment(CENTER_ALIGNMENT);
        addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent e) {
                if (e.getClickCount() == 2) {
                    addInstanceNameEditor();
                }
            }
        });

        addChild(instanceLabel);

        setBounds(
                0, 0, getChildrenWidth(), getChildrenHeight());
        translate(model.getX(), model.getY());

        resizeToGrid();
    }

    @Override
    protected void handleInstanceNameEditorAction() {
        super.handleInstanceNameEditorAction();

        setBounds(0, 0,
                getChildrenWidth(), getChildrenHeight());
        repaint();
    }

    @Override
    public void setInstanceName(String s) {
        model.setCommentText(s);
        if (instanceLabel != null) {
            instanceLabel.setText(model.getCommentText());
        }
        if (getParent() != null) {
            getParent().repaint();
        }
        resizeToGrid();
    }
}
