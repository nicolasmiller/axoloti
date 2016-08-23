package axoloti.piccolo.objectviews;

import axoloti.PatchViewPiccolo;
import axoloti.object.AxoObjectInstancePatcherObject;
import axoloti.object.AxoObjectPatcherObject;
import axoloti.objecteditor.AxoObjectEditor;
import components.piccolo.control.PButtonComponent;
import javax.swing.SwingUtilities;

public class PAxoObjectInstanceViewPatcherObject extends PAxoObjectInstanceView {

    AxoObjectEditor aoe;

    AxoObjectInstancePatcherObject model;
    PButtonComponent BtnEdit;

    public PAxoObjectInstanceViewPatcherObject(AxoObjectInstancePatcherObject model, PatchViewPiccolo p) {
        super(model, p);
        this.model = model;
    }

    @Override
    public void PostConstructor() {
        super.PostConstructor();

        //updateObj();
        BtnEdit = new PButtonComponent("edit");
//        BtnEdit.setAlignmentX(LEFT_ALIGNMENT);
//        BtnEdit.setAlignmentY(TOP_ALIGNMENT);
        BtnEdit.addActListener(new PButtonComponent.ActListener() {
            @Override
            public void OnPushed() {
                edit();
            }
        });
        addChild(BtnEdit);
        this.setBounds(0, 0, getChildrenWidth(), getChildrenHeight());
        translate(model.getX(), model.getY());
        resizeToGrid();
    }

    @Override
    public void updateObj() {
        if (model.getAxoObject() != null) {
            model.getAxoObject().id = "patch/object";
            model.setType(model.getAxoObject());
            PostConstructor();
        }

        //TODO is this needed?
//        validate();
    }

    public void OpenEditor() {
        edit();
    }

    public void edit() {
        if (model.getAxoObject() == null) {
            model.setAxoObject(new AxoObjectPatcherObject());
//            ao.id = "id";
            model.getAxoObject().sDescription = "";
        }
        if (aoe == null) {
            aoe = new AxoObjectEditor(model.getAxoObject());
        } else {
            aoe.updateReferenceXML();
        }
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
    public void Close() {
        super.Close();
        if (aoe != null) {
            aoe.Close();
        }
    }
}
