package axoloti.piccolo.objectviews;

import static java.awt.Component.CENTER_ALIGNMENT;

import java.beans.PropertyChangeEvent;

import javax.swing.BoxLayout;

import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

import axoloti.patch.PatchViewPiccolo;
import axoloti.patch.object.AxoObjectInstanceComment;
import axoloti.patch.object.ObjectInstanceController;
import axoloti.piccolo.components.PLabelComponent;

public class PAxoObjectInstanceViewComment extends PAxoObjectInstanceViewAbstract {

    public PAxoObjectInstanceViewComment(ObjectInstanceController controller, PatchViewPiccolo p) {
        super(controller, p);
    }

    @Override
    public AxoObjectInstanceComment getModel() {
        return (AxoObjectInstanceComment) super.getModel();
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();
        setDrawBorder(true);

        setLayout(new BoxLayout(getProxyComponent(), BoxLayout.LINE_AXIS));

        instanceLabel = new PLabelComponent(getModel().getCommentText());
        instanceLabel.setAlignmentX(CENTER_ALIGNMENT);

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
    public void showInstanceName(String s) {
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        super.modelPropertyChange(evt);
        if (AxoObjectInstanceComment.COMMENT.is(evt)) {
            instanceLabel.setText((String) evt.getNewValue());
        }
    }
}
