package axoloti.piccolo.patch.object;

import axoloti.patch.object.AxoObjectInstancePatcherObject;
import axoloti.patch.object.ObjectInstanceController;
import axoloti.piccolo.components.control.PButtonComponent;
import axoloti.piccolo.patch.PatchViewPiccolo;
import axoloti.swingui.objecteditor.AxoObjectEditor;
import static java.awt.Component.LEFT_ALIGNMENT;
import static java.awt.Component.TOP_ALIGNMENT;
import javax.swing.SwingUtilities;

public class PAxoObjectInstanceViewPatcherObject extends PAxoObjectInstanceView {

    PButtonComponent BtnEdit;
    AxoObjectEditor aoe;

    public PAxoObjectInstanceViewPatcherObject(ObjectInstanceController controller, PatchViewPiccolo p) {
        super(controller, p);
        initComponents();
    }

    @Override
    public AxoObjectInstancePatcherObject getModel() {
        return (AxoObjectInstancePatcherObject) controller.getModel();
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        BtnEdit = new PButtonComponent("edit", this);
        BtnEdit.setAlignmentX(LEFT_ALIGNMENT);
        BtnEdit.setAlignmentY(TOP_ALIGNMENT);
        BtnEdit.addActListener(new PButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                edit();
            }
        });

        addChild(BtnEdit);
        resizeToGrid();
    }

    @Override
    public void OpenEditor() {
        edit();
    }

    public void edit() {
        if (aoe == null) {
            aoe = new AxoObjectEditor(getModel().getAxoObject().getControllerFromModel());
        } else {
            aoe.updateReferenceXML();
        }
        // TODO: Review, invokeLater should not be needed:
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                aoe.setState(java.awt.Frame.NORMAL);
                aoe.setVisible(true);
            }
        });
    }

    public boolean isEditorOpen() {
        return aoe != null && aoe.isVisible();
    }

    @Override
    public void dispose() {
        if (aoe != null) {
            aoe.close();
            aoe = null;
        }
    }
}
